package cscie97.asn4.ecommerce.authentication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.UUID;

/**
 * The AuthenticationImporter class is responsible for reading csv input files 
 * to import data for the AuthenticationService.  The AuthenticationImporter class parses 
 * a csv file and uses the data it parses to define elements within the authentication architecture 
 * such as users, permissions, roles and services by calling functions in the AuthenticationService.
 * 
 * @author Frank O'Connor
 *
 */
public class AuthenticationImporter {
	
	/** 
	 * Constructor class for AuthenticationImporter.
	 */
	public AuthenticationImporter(){}
	
	/**
	 * Method for parsing csv file data. Uses the data parses it to define elements within the authentication architecture 
	 * such as users, permissions, roles and services by calling functions in the AuthenticationService.
	 * @param authId the access token id passed to validate user.
	 * @param filename the name of the input file.
	 * @throws AuthenticationImportException if there is an error accessing or processing the input File.
	 * @throws EntitlementChildException exception while interacting with child
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	public void importAuthenticationCsvFile(UUID authId, String filename) throws AuthenticationImportException, EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
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
	    				 if(splitString[0].equals("define_service")){
	    					 defineService(authId, splitString);
	    				 }else if(splitString[0].equals("define_permission")){
	    					 definePermission(authId, splitString);
	    				 }else if(splitString[0].equals("define_role")){
	    					 defineRole(authId, splitString);
	    				 }else if(splitString[0].equals("add_entitlement_to_role")){
	    					 addEntitlementToRole(authId, splitString);
	    				 }else if(splitString[0].equals("create_user")){
	    					 createUser(authId, splitString);
	    				 }else if(splitString[0].equals("add_credential")){
	    					 addCredential(authId, splitString);
	    				 }else if(splitString[0].equals("add_entitlement_to_user")){
	    					 addEntitlementToUser(authId, splitString);
	    				 }else{
	    					 throw new AuthenticationImportException("Invalid command specified", line, lineNum, filename, new Exception());
	    				 }
	    				
	    			}
	    		}
	    		
	    	}
		} catch (FileNotFoundException fnfe) {
			// catching the FileNotFoundException and throwing our custom Exception, which includes useful info
			throw new AuthenticationImportException("Filepath not found", line, lineNum, filename, fnfe);
		} catch (AuthenticationImportException ie) {
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
		}catch (EntitlementChildException ece) {
			ece.setLineWhereFailed(line);
			ece.setLineIndexWhereFailed(lineNum);
			ece.setFilename(filename);
			throw ece;
		}catch (InvalidReferenceException ece) {
			ece.setLineWhereFailed(line);
			ece.setLineIndexWhereFailed(lineNum);
			ece.setFilename(filename);
			throw ece;
		}finally {
			// ensuring the closure the underlying stream
			if(lineScanner != null){
				lineScanner.close();
			}
		}
	}

	/**
	 * Processes the 'define_collection' command from the importer.
	 * Validates input data and calls AuthenticationService createService method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void defineService(UUID authId, String[] splitString) throws AuthenticationImportException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 5 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==4){
					
			// removing any escape characters from inputs
			String serviceId = splitString[1].trim().replaceAll("\\\\", "");
			String serviceName = splitString[2].trim().replaceAll("\\\\", "");
			String serviceDesc = splitString[3].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.createService(authId, serviceId, serviceName, serviceDesc);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'define_permission' command from the importer.
	 * Validates input data and calls AuthenticationService createPermission method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws EntitlementChildException exception while interacting with child
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void definePermission(UUID authId, String[] splitString) throws AuthenticationImportException, EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 5 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==5){
					
			// removing any escape characters from inputs
			String serviceId = splitString[1].trim().replaceAll("\\\\", "");
			String permissionId = splitString[2].trim().replaceAll("\\\\", "");
			String permissionName = splitString[3].trim().replaceAll("\\\\", "");
			String permissionDesc = splitString[4].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.createPermission(authId, serviceId, permissionId, permissionName, permissionDesc);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'define_role' command from the importer.
	 * Validates input data and calls AuthenticationService createRole method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void defineRole(UUID authId, String[] splitString) throws AuthenticationImportException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 4 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==4){
					
			// removing any escape characters from inputs
			String roleId = splitString[1].trim().replaceAll("\\\\", "");
			String roleName = splitString[2].trim().replaceAll("\\\\", "");
			String roleDesc = splitString[3].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.createRole(authId, roleId, roleName, roleDesc);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'add_entitlement_to_role' command from the importer.
	 * Validates input data and calls AuthenticationService addEntitlementToRole method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws EntitlementChildException exception while interacting with child
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void addEntitlementToRole(UUID authId, String[] splitString) throws AuthenticationImportException, EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 3 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==3){
					
			// removing any escape characters from inputs
			String roleId = splitString[1].trim().replaceAll("\\\\", "");
			String entitleId = splitString[2].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.addEntitlementToRole(authId, roleId, entitleId);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'create_user' command from the importer.
	 * Validates input data and calls AuthenticationService createUser method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void createUser(UUID authId, String[] splitString) throws AuthenticationImportException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 3 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==3){
					
			// removing any escape characters from inputs
			String userId = splitString[1].trim().replaceAll("\\\\", "");
			String userName = splitString[2].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.createUser(authId, userId, userName);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'add_credential' command from the importer.
	 * Validates input data and calls AuthenticationService addCredentialToUser method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void addCredential(UUID authId, String[] splitString) throws AuthenticationImportException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 4 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==4){
					
			// removing any escape characters from inputs
			String userId = splitString[1].trim().replaceAll("\\\\", "");
			String loginName = splitString[2].trim().replaceAll("\\\\", "");
			String password = splitString[3].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.addCredentialToUser(authId, userId, loginName, password);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}
	
	/**
	 * Processes the 'add_entitlement_to_user' command from the importer.
	 * Validates input data and calls AuthenticationService addEntitlementToUser method
	 * @param authId used to validate the user
	 * @param splitString strings from parsed line of csv file 
	 * @throws AuthenticationImportException if invalid input if passed
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws InvalidAccessTokenException 
	 */
	private void addEntitlementToUser(UUID authId, String[] splitString) throws AuthenticationImportException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException{
		// when line is parsed we should only have 3 variables,
		// if we have more/less the line is not in the correct format
		if(splitString.length==3){
					
			// removing any escape characters from inputs
			String userId = splitString[1].trim().replaceAll("\\\\", "");
			String entitleId = splitString[2].trim().replaceAll("\\\\", "");
			
			// calling singleton instance of AuthenticationService
			AuthenticationService aService = AuthenticationServiceImpl.getInstance();

			aService.addEntitlementToUser(authId, userId, entitleId);
			
		}else {
			throw new AuthenticationImportException("Incorrect number of variables in parsed csv line", "", 0, "", new Exception());
		}
	}

}
