package cscie97.asn4.ecommerce.product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The QueryProcessor class is responsible for reading queries from csv input files with the expected format.
 * Each line of a csv file is parsed as a query.
 * 
 * @author Frank O'Connor
 *
 */
public class QueryProcessor {
	
	/** 
	 * Constructor class for QueryProcessor.
	 */
	public QueryProcessor(){
		// constructor
	}
	
	/**
	 * Method for processing queries against the ProductCatalog.
	 *  Checks for valid input file name. 
	 * @param filename the name of the input file.
	 * @return HashMap<String, List<Product>> Map of results with query as key, and list of Products as values.
	 * @throws QueryProcessorException if there is an error accessing or processing the query File.
	 */
	public HashMap<String, List<Product>> processQueries(String filename) throws QueryProcessorException {
		HashMap<String, List<Product>> queriesResults = new HashMap<String, List<Product>>();
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
	    			// converting all input to lower case for case insensitivity
	    			line = line.toLowerCase();
	    			// splitting the lines on a comma delimiter, but ignoring escaped commas (\,)
	    			String[] splitString = line.split("(?<!\\\\),");
	    			
	    			List <Product> queryResult = new ArrayList<Product>();
	    			// process a single query
	    			queryResult = processQuery(splitString);
	    			// Results list for a single query
	    			queriesResults.put(line, queryResult);
	    		}
	    		
	    	}
		} catch (FileNotFoundException fnfe) {
			// catching the FileNotFoundException and throwing our custom Exception, which includes useful info
			throw new QueryProcessorException("File not found!", line, lineNum, filename, fnfe);
		} catch (QueryProcessorException qpe) {
			// catching QueryProcessorException due to invalid line input and re-throwing
			qpe.setLineWhereFailed(line);
			qpe.setLineIndexWhereFailed(lineNum);
			qpe.setFilename(filename);
			throw qpe;
		} catch (Exception e) {
			// catching the Exception and throwing our custom Exception, which includes useful info
			throw new QueryProcessorException(e.getMessage(), line, lineNum, filename, e);
		} finally {
			// ensuring the closure the underlying stream
			if(lineScanner != null){
				lineScanner.close();
			}
		}
		return queriesResults;
	}
	
	/**
	 * Private method to process valid query line and against the ProductCatalog 
	 * and return the matching results.
	 * @param splitString the parsed line from the csv file.
	 * @return list of results that match the query.
	 * @throws QueryProcessorException if a processing problem is encountered.
	 */
	private List<Product> processQuery(String[] splitString) throws QueryProcessorException {
		List <Product> queryResult = new ArrayList<Product>();
		// expecting at 8 variables, but last may be blank, so checking for 7 here
		if(splitString.length>6){
			
			// splitting categories on pipe delimiter
			Set<String> categorySet = new HashSet<String>();
			if(hasText(splitString[0])){
				String[] splitCategories = splitString[0].trim().split("\\|");
				for (String category : splitCategories) {
					categorySet.add(category);
				}
			}
			
			// removing escape characters from textSearch
			String textSearch = splitString[1].trim().replaceAll("\\\\", "");
			
			// parsing rating from input
			// defaults to 0
			int minimumRating = 0;
			if(hasText(splitString[2])){
				try{
    				int rating = Integer.parseInt(splitString[2].trim());
    				if(rating>=0 && rating<=5){
    					minimumRating=rating;
    				}else{
    					throw new QueryProcessorException("Error in parsing input line: invalid rating", "", 0, "", new Exception());
    				}
				} catch(NumberFormatException e){
					throw new QueryProcessorException("Error in parsing input line: NumberFormatException on rating", "", 0, "", new Exception());
				}
			}
			
			// parsing price from input
			// defaults to 0/free
			float maxPrice = 0;
			if(hasText(splitString[3])){
				try{
    				float price = Float.parseFloat(splitString[3].trim());
    				if(price>=0){
    					maxPrice=price;
    				}else{
    					throw new QueryProcessorException("Error in parsing input line: invalid rating", "", 0, "", new Exception());
    				}
				} catch(NumberFormatException e){
					throw new QueryProcessorException("Error in parsing input line: NumberFormatException on rating", "", 0, "", new Exception());
				}
			}
			
			// splitting export language on pipe delimiter
			Set<String> languageSet  = new HashSet<String>();
			if(hasText(splitString[4])){
				String[] splitLanguages = splitString[4].trim().split("\\|");
				for (String language : splitLanguages) {
					languageSet.add(language);
				}
			}

			// parsing query params
			String countryCode = splitString[5].trim();
			String deviceId = splitString[6].trim();
			
			// splitting content types on pipe delimiter
			String[] splitContentTypes = {};
			Set<String> contentTypeSet = new HashSet<String>();	
			// validating 7th parm exists here
			if(splitString.length>7 && hasText(splitString[7])){
				splitContentTypes = splitString[7].trim().split("\\|");
				for (String contentType : splitContentTypes) {
					contentTypeSet.add(contentType);
				}
			}
			
			// calling singleton instance of productCatalog
			ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
			// querying Products
			queryResult = pCatalog.queryProducts(categorySet, textSearch, minimumRating, maxPrice, languageSet, countryCode, deviceId, contentTypeSet);
		}else {
			// more than 3 split strings, indicates an invalid input line.
			// throwing ImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			throw new QueryProcessorException("Error in parsing input line", "", 0, "", new Exception());
		}
		
		// returning query results
		return queryResult;
	}
	
	// private utility method
	private boolean hasText(String text) {
        return (text != null && !text.isEmpty() && !text.trim().isEmpty());
    }
	
}
