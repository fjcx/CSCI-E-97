package cscie97.asn4.ecommerce.product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;

/**
 * The Importer class is responsible for reading csv input files with the expected format.
 * Importer class can import countries, devices and products into the ProductCatalog.
 * Each line of a csv file is parsed as either a country, device or product respectively.
 * 
 * @author Frank O'Connor
 *
 */
public class Importer {
	
	/** 
	 * Constructor class for Importer.
	 */
	public Importer(){
		// constructor
	}
	
	/**
	 * Method for importing countries, devices and products into
	 * the ProductCatalog. Checks for valid input file name. 
	 * @param adminGuid the authGuid passed to validate user.
	 * @param filename the name of the input file.
	 * @param inputType the type of data in the file to be processed. Expecting "country", "device" or "product"
	 * @throws ImportException if there is an error accessing or processing the input File.
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void importFileData(UUID adminGuid, String filename, String inputType) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
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
	    			
	    			// checking for inputType and importing individual objects depending on type
	    			if(inputType.equals("country")){
	    				importCountry(adminGuid, splitString);
	    			} else if(inputType.equals("device")){
	    				importDevice(adminGuid, splitString);
	    			} else if(inputType.equals("product")){
	    				importProduct(adminGuid,splitString);
	    			} else{
	    				throw new ImportException("Invalid contentType specified", line, lineNum, filename, new Exception());
	    			}
	    		}
	    		
	    	}
		} catch (FileNotFoundException fnfe) {
			// catching the FileNotFoundException and throwing our custom Exception, which includes useful info
			throw new ImportException("File not found!", line, lineNum, filename, fnfe);
		} catch (ImportException ie) {
			// catching ImportException due to invalid line input and re-throwing
			ie.setLineWhereFailed(line);
			ie.setLineIndexWhereFailed(lineNum);
			ie.setFilename(filename);
			throw ie;
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
		} finally {
			// ensuring the closure the underlying stream
			if(lineScanner != null){
				lineScanner.close();
			}
		}
	}

	
	/**
	 * Private method to process valid parsed line and import an individual country into 
	 * the ProductCatalog.
	 * @param adminGuid the authGuid passed to validate user.
	 * @param splitString the parsed line from the csv file.
	 * @throws ImportException if a parsing problem is encountered
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	private void importCountry(UUID adminGuid, String[] splitString) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
		// when line is parsed we should only have 3 variables,
		// if we more/less the line is not in the correct format
		if(splitString.length==3){
			
			// expects string with either 'open' or 'closed'
			boolean isExportOpen = false;
			if(splitString[2].trim().equals("open")){
				isExportOpen = true;
			}else if (splitString[2].trim().equals("closed")){
				isExportOpen = false;
			}else{
				// if not "open" or "closed" it is incorrect
				throw new ImportException("Unexpected string, should be 'open'/'closed'", "", 0, "", new Exception());
			}
			
			// removing any escape characters from the country name
			String countryName = splitString[1].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of ProductCatalog
			ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
			// adding country to ProductCatalog country list
			pCatalog.addCountry(adminGuid, splitString[0].trim(), countryName, isExportOpen);
			
		}else {
			// more than 3 split strings, indicates an invalid input line.
			// throwing ImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			// note file and line info set in caller/catcher
			throw new ImportException("Error in parsing input line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Private method to process valid parsed line and import an individual device into 
	 * the ProductCatalog.
	 * @param adminGuid the authGuid passed to validate user.
	 * @param splitString the parsed line from the csv file.
	 * @throws ImportException if a parsing problem is encountered
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	private void importDevice(UUID adminGuid, String[] splitString) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
		// when line is parsed we should only have 3 variables,
		// if we more/less the line is not in the correct format
		if(splitString.length==3){
				
			// removing any escape characters from the device name
			String deviceName = splitString[1].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of ProductCatalog
			ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
			// adding device to ProductCatalog device list
			pCatalog.addDevice(adminGuid, splitString[0].trim(), deviceName, splitString[2].trim());	    				
		}else {
			// more than 3 split strings, indicates an invalid input line.
			// throwing ImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			// note file and line info set in caller/catcher
			throw new ImportException("Error in parsing input line", "", 0, "", new Exception());
		}
		
	}
	
	/**
	 * Private method to process valid parsed line and import an individual product into 
	 * the ProductCatalog.
	 * @param adminGuid the authGuid passed to validate user.
	 * @param splitString the parsed line from the csv file.
	 * @throws ImportException if a parsing problem is encountered
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	private void importProduct(UUID adminGuid, String[] splitString) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
		// when line is parsed we should only have 12 variables, or 13 as application
		// if we more/less the line is not in the correct format
		if(splitString.length==12 || (splitString.length==13 && splitString[0].trim().equals("application"))){
			
			// calling singleton instance of ProductCatalog
			ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
			
			// parsing params for product
			String contentType = splitString[0].trim();					
			String productId = splitString[1].trim();
			String productName = splitString[2].trim().replaceAll("\\\\", "");;			
			String productDescription = splitString[3].trim().replaceAll("\\\\", "");;			
			String author = splitString[4].trim().replaceAll("\\\\", "");;			
			String imageUrl = splitString[11].trim();
			
			// parsing rating from input
			// defaults to 0
			int rating = 0;
			try{
				rating = Integer.parseInt(splitString[5].trim());
				if(rating<0 || rating>5){
					throw new ImportException("Error in parsing input line: invalid rating", "", 0, "", new Exception());
				}
			} catch(NumberFormatException e){
				throw new ImportException("Error in parsing input line: NumberFormatException in rating", "", 0, "", new Exception());
			}
			
			// parsing price from input
			// defaults to 0/free
			float price = 0;
			try{
				price = Float.parseFloat(splitString[9].trim());
				// free or more, assuming negative price is incorrect input
				if(price < 0){
					throw new ImportException("Error in parsing input line: invalid price", "", 0, "", new Exception());
				}
			} catch(NumberFormatException e){
				throw new ImportException("Error in parsing input line: NumberFormatException in price", "", 0, "", new Exception());
			}
			
			// splitting categories on pipe delimiter
			String[] splitCategories = splitString[6].trim().split("\\|");
			Set<String> categories = new HashSet<String>();
			for (String category : splitCategories) {
				categories.add(category);
			}
			
			// splitting export countries on pipe delimiter
			String[] splitExportCountries = splitString[7].trim().split("\\|");
			List<Country> countries = new ArrayList<Country>();
			for (String countryId : splitExportCountries) {
				// we check if the input country is a valid country existing in the ProductCatalog list of countries
				Country countryInfo = pCatalog.getValidCountry(countryId);
				if(countryInfo != null){
					countries.add(countryInfo);
				}else{
					throw new ImportException("Error in parsing input line: invalid countryId", "", 0, "", new Exception());
				}
			}
			
			// splitting export devices on pipe delimiter
			String[] splitDevices = splitString[8].trim().split("\\|");
			List<Device> devices = new ArrayList<Device>();
			for (String deviceId : splitDevices) {
				// we check if the input country is a valid device existing in the ProductCatalog list of devices
				Device deviceInfo = pCatalog.getValidDevice(deviceId);
				if(deviceInfo != null){
					devices.add(deviceInfo);
				}else{
					throw new ImportException("Error in parsing input line: invalid deviceId", "", 0, "", new Exception());
				}
			}
			
			// splitting export language on pipe delimiter
			String[] splitSupportedLanguages = splitString[10].trim().split("\\|");
			List<String> languages = new ArrayList<String>();
			for (String language : splitSupportedLanguages) {
				languages.add(language);
			}			
			
			Product product = null;
			if(contentType.equals("application")){	// we are validating in the ConStructors for correct input !!!!!!!!
				float appSize = 0;
				try{
					appSize = Float.parseFloat(splitString[12].trim());
					// application size must be non-negative
					if(appSize < 0){
						throw new ImportException("Error in parsing input line: invalid appSize", "", 0, "", new Exception());
					}
				} catch(NumberFormatException  e){
					throw new ImportException("Error in parsing input line: NumberFormatException in appSize", "", 0, "", new Exception());
				}
				
				// creating new Application, validation also occurs in the Product constructor
				product = new Application(adminGuid, productId, productName, author, devices, categories, productDescription,
						rating, price, countries, languages, imageUrl, appSize);
			}else if (contentType.equals("ringtone")){
				// creating new RingTone, validation also occurs in the Product constructor
				product = new RingTone(adminGuid, productId, productName, author, devices, categories, productDescription,
						rating, price, countries, languages, imageUrl);
			}else if (contentType.equals("wallpaper")){
				// creating new Wallpaper, validation also occurs in the Product constructor
				product = new Wallpaper(adminGuid, productId, productName, author, devices, categories, productDescription,
						rating, price, countries, languages, imageUrl);
			}else{
				throw new ImportException("Error in parsing input line: invalid contentType", "", 0, "", new Exception());
			}
			
			if (product!=null){
				// add product to ProductCatalog
				pCatalog.addProduct(adminGuid, product);
			}
		}else {
			// more than 13 split strings, indicates an invalid input line.
			// throwing ImportException when we encounter an invalid line. Invalid line
			// info is added to Exception and passed back to user
			throw new ImportException("Error in parsing input line", "", 0, "", new Exception());
		}
	}
	


}
