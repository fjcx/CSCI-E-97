package cscie97.asn4.ecommerce.collection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;

/**
 * The CollectionImporter class is responsible for reading csv input files to import 
 * data about collections. The CollectionImporter class parses a csv file and uses 
 * the data in it to define collections, add content to collections, set search criteria 
 * on dynamic collections, and search through collections by calling functions in the 
 * CollectionService.
 * 
 * @author Frank O'Connor
 *
 */
public class CollectionImporter {
	
	/** 
	 * Constructor class for CollectionImporter.
	 */
	public CollectionImporter(){}
	
	/**
	 * Method for parsing csv file data. Calls respective functions of CollectionService 
	 * to create new collection, add content to collection, define search criteria of 
	 * a dynamic collection, or search through collections in collection service..
	 * @param adminGuid the authGuid passed to validate user.
	 * @param filename the name of the input file.
	 * @throws CollectionImportException if there is an error accessing or processing the input File.
	 * @throws CollectableChildException if an issue occurs creating/removing child Collectables
	 * @throws CollectableValidationException 
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void importCollectionCsvFile(UUID adminGuid, String filename) throws CollectionImportException, CollectableChildException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException {
    	Scanner lineScanner = null;

    	String line = "";
    	int lineNum = 0;
    	
		try {
			// reading file line by line using Scanner
			lineScanner = new Scanner(new FileInputStream(filename));
				      
	    	while(lineScanner.hasNextLine()){
	    		line = lineScanner.nextLine().trim();
	    		// keeping track of the number of lines we have read
	    		lineNum += 1;
	    		if(line.equals("") || line.charAt(0)=='#'){
	    			// we are ignoring comments and blank lines and do not count these as parsing errors 
	    		} else {
	    			// dealing with case insensitivity
	    			line = line.toLowerCase();
	    			// splitting the lines on a comma delimiter, but ignoring escaped commas (\,)
	    			String[] splitString = line.split("(?<!\\\\),");	    			
	    				    			
	    			if(splitString.length>0){
	    				 if(splitString[0].equals("define_collection")){
	    					 defineCollection(adminGuid, splitString);
	    				 }else if(splitString[0].equals("add_collection_content")){
	    					 addCollectionContent(adminGuid, splitString);
	    				 }else if(splitString[0].equals("set_dynamic_criteria")){
	    					 setDynamicCriteria(adminGuid, splitString);
	    				 }else if(splitString[0].equals("search_collection")){
	    					 searchCollection(splitString);
	    				 }else{
	    					 throw new CollectionImportException("Invalid command specified", line, lineNum, filename, new Exception());
	    				 }
	    				
	    			}
	    		}
	    		
	    	}
		} catch (FileNotFoundException fnfe) {
			// catching the FileNotFoundException and throwing our custom Exception, which includes useful info
			throw new CollectionImportException("Filepath not found", line, lineNum, filename, fnfe);
		} catch (CollectionImportException ie) {
			// catching ImportException due to invalid line input and re-throwing
			ie.setLineWhereFailed(line);
			ie.setLineIndexWhereFailed(lineNum);
			ie.setFilename(filename);
			throw ie;
		} catch (CollectableChildException cce) {
			// adding file line info to custom exception
			cce.setLineWhereFailed(line);
			cce.setLineIndexWhereFailed(lineNum);
			cce.setFilename(filename);
			throw cce;
		} catch (CollectableValidationException cve) {
			// adding file line info to custom exception
			cve.setLineWhereFailed(line);
			cve.setLineIndexWhereFailed(lineNum);
			cve.setFilename(filename);
			throw cve;
		} catch (AccessDeniedException ade) {
			ade.setLineWhereFailed(line);
			ade.setLineIndexWhereFailed(lineNum);
			ade.setFilename(filename);
			throw ade;
		} catch (InvalidAccessTokenException iate) {
			iate.setLineWhereFailed(line);
			iate.setLineIndexWhereFailed(lineNum);
			iate.setFilename(filename);
			throw iate;
		}finally {
			// ensuring the closure the underlying stream
			if(lineScanner != null){
				lineScanner.close();
			}
		}
	}

	/**
	 * Processes the 'define_collection' command from the importer.
	 * Validates input data and calls CollectionService createCollection method
	 * @param adminGuid used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws CollectionImportException if invalid input if passed
	 * @throws CollectableChildException if exception occurs while creating Collectable child
	 * @throws CollectableValidationException if invalid action performed while modifying a Collectable object
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	private void defineCollection(UUID adminGuid, String[] splitString) throws CollectionImportException, CollectableChildException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException {
		// when line is parsed we should only have 5 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==5){
					
			// removing any escape characters from inputs
			String collectionType = splitString[1].trim().replaceAll("\\\\", "");
			String collectionId = splitString[2].trim().replaceAll("\\\\", "");
			String collectionName = splitString[3].trim().replaceAll("\\\\", "");
			String collectionDesc = splitString[4].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of CollectionService
			CollectionService cService = CollectionServiceImpl.getInstance();

			// NOTE:creating all newly defined collections at the root level of the graph,
			// these can then be moved later using add content to collections
			cService.createCollection(adminGuid, collectionType, collectionId, collectionName, collectionDesc);
			
		}else {
			// more than 5 split strings, indicates an invalid input line.
			// throwing CollectionImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			// note file and line info set in caller/catcher
			throw new CollectionImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'add_collection_content' command from the importer.
	 * Validates input data and calls CollectionService addContent method
	 * @param adminGuid used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws CollectionImportException if invalid input if passed
	 * @throws CollectableChildException if exception occurs while creating Collectable child
	 * @throws CollectableValidationException if invalid action performed while modifying a Collectable object
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	private void addCollectionContent(UUID adminGuid, String[] splitString) throws CollectionImportException, CollectableChildException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException {
		// when line is parsed we should only have 4 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==4){
					
			// removing any escape characters from inputs
			String parentCollectionId = splitString[1].trim().replaceAll("\\\\", "");
			String contentType = splitString[2].trim().replaceAll("\\\\", "");
			String childContentId = splitString[3].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of CollectionService
			CollectionService cService = CollectionServiceImpl.getInstance();
			
			cService.addContent(adminGuid, parentCollectionId, contentType, childContentId);
			
		}else {
			// more than 4 split strings, indicates an invalid input line.
			// throwing CollectionImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			// note file and line info set in caller/catcher
			throw new CollectionImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'search_collection' command from the importer.
	 * Validates input data and calls CollectionService searchCollection method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws CollectionImportException if invalid input if passed
	 */
	private void searchCollection(String[] splitString) throws CollectionImportException {
		// calling singleton instance of CollectionService
		CollectionService cService = CollectionServiceImpl.getInstance();
		//SearchCriteria searchTerms = new SearchCriteria();
		String searchText = "";
		if(splitString.length>1){
			// removing any escape characters from inputs
			searchText = splitString[1].trim().replaceAll("\\\\", "");	
		}
		
		List<ProductCollection> resultList = cService.searchCollection(searchText);
		
		if(searchText.equals("")){
			System.out.println("\nprocessing search command over: All Collections");
		}else{
			System.out.println("\nprocessing search command with SearchString: "+ searchText);
		}
		for (ProductCollection pCol : resultList) {
			System.out.println("Collection Id: "+pCol.getCollectionId());
			System.out.println("	Name: "+pCol.getCollectionName());
			System.out.println("	Description: "+pCol.getCollectionDescription());
			if(pCol instanceof DynamicCollection){
				System.out.println("	Type: DynamicCollection");
			}else{
				System.out.println("	Type: StaticCollection");
			}
			
			System.out.println("Direct Children:");
			for (Collectable child : pCol.getChildren()) {
				System.out.println("	Collectable Id: "+child.getCollectionId());
				System.out.println("		Name: "+child.getCollectionName());
				System.out.println("		Description: "+child.getCollectionDescription());
				if(child instanceof DynamicCollection){
					System.out.println("		Type: DynamicCollection");
				}else if(child instanceof StaticCollection){
					System.out.println("		Type: StaticCollection");
				}else {
					System.out.println("		Type: Product");
				}
			}
			
		}
	}
	
	/**
	 * Processes the 'set_dynamic_criteria' command from the importer.
	 * Validates input data and calls CollectionService setDynamicCollectionCriteria method
	 * @param adminGuid used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws CollectionImportException if invalid input if passed
	 * @throws CollectableValidationException if invalid action performed while modifying a Collectable object
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	private void setDynamicCriteria(UUID adminGuid, String[] splitString) throws CollectionImportException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException {
		// when line is parsed we should only have 4 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==9 || splitString.length==10){
			
			String collectionId = splitString[1].trim().replaceAll("\\\\", "");
			String textSearch = splitString[3].trim().replaceAll("\\\\", "");
			String countryCode = splitString[7].trim().replaceAll("\\\\", "");
			String deviceId = splitString[8].trim().replaceAll("\\\\", "");
			
			// parsing minimumRating from input
			// defaults to 0
			String ratingStr = splitString[4].trim();
			int minimumRating = 0;	// default minRating
			try{
				if(ratingStr!=null && !ratingStr.equals("")){
					minimumRating = Integer.parseInt(ratingStr);
					if(minimumRating < 0 || minimumRating > 5){
						throw new CollectionImportException("Invalid Rating. Rating should be between 0 and 5.", "", 0, "", new Exception());
					}
				}
			} catch(NumberFormatException nfe){
				throw new CollectionImportException("NumberFormatException while parsing rating", "", 0, "", nfe);
			}
			
			// parsing price from input
			// defaults to 0/free
			String priceStr = splitString[5].trim();
			float maxPrice = 999999999;	// default maxPrice
			try{
				if(priceStr!=null && !priceStr.equals("")){
					maxPrice = Float.parseFloat(priceStr);
					// free or more, assuming negative price is incorrect input
					if(maxPrice < 0){
						throw new CollectionImportException("MaxPrice cannot be less than 0", "", 0, "", new Exception());
					}
				}
			} catch(NumberFormatException nfe){
				throw new CollectionImportException("NumberFormatException while parsing maxPrice", "", 0, "", nfe);
			}

			// splitting categories on pipe delimiter
			String[] splitCategories = splitString[2].trim().split("\\|");
			Set<String> categories = new HashSet<String>();
			for (String category : splitCategories) {
				if(!category.toLowerCase().equals("")){
					categories.add(category);
				}
			}
			
			// splitting languages on pipe delimiter
			String[] splitLanguages = splitString[6].trim().split("\\|");
			Set<String> languages = new HashSet<String>();
			for (String language : splitLanguages) {
				if(!language.toLowerCase().equals("")){
					languages.add(language);
				}
			}
			
			Set<String> contentTypes = new HashSet<String>();
			if(splitString.length==10){
				// splitting contentTypes on pipe delimiter
				String[] splitContentTypes = splitString[9].trim().split("\\|");
				for (String contentType : splitContentTypes) {
					if(!contentType.toLowerCase().equals("")){
						contentTypes.add(contentType);
					}
				}
			}
			
			SearchCriteria searchTerms = new SearchCriteria(categories, textSearch, minimumRating, maxPrice, languages, countryCode, deviceId, contentTypes);
			// calling singleton instance of CollectionService
			CollectionService cService = CollectionServiceImpl.getInstance();

			
			cService.setDynamicCollectionCriteria(adminGuid, collectionId, searchTerms);
			
		}else {
			// more than 10 split strings, indicates an invalid input line.
			// throwing CollectionImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			// note file and line info set in caller/catcher
			throw new CollectionImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}

}
