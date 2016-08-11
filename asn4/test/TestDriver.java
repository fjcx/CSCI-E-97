package cscie97.asn4.test;

import java.io.FileNotFoundException;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.AuthenticationImportException;
import cscie97.asn4.ecommerce.authentication.AuthenticationImporter;
import cscie97.asn4.ecommerce.authentication.AuthenticationService;
import cscie97.asn4.ecommerce.authentication.AuthenticationServiceImpl;
import cscie97.asn4.ecommerce.authentication.EntitlementChildException;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;
import cscie97.asn4.ecommerce.authentication.InvalidReferenceException;
import cscie97.asn4.ecommerce.collection.Collectable;
import cscie97.asn4.ecommerce.collection.CollectableChildException;
import cscie97.asn4.ecommerce.collection.CollectableIterator;
import cscie97.asn4.ecommerce.collection.CollectableValidationException;
import cscie97.asn4.ecommerce.collection.CollectionImportException;
import cscie97.asn4.ecommerce.collection.CollectionImporter;
import cscie97.asn4.ecommerce.collection.CollectionService;
import cscie97.asn4.ecommerce.collection.CollectionServiceImpl;
import cscie97.asn4.ecommerce.collection.DynamicCollection;
import cscie97.asn4.ecommerce.collection.ProductProxy;
import cscie97.asn4.ecommerce.collection.StaticCollection;
import cscie97.asn4.ecommerce.product.ImportException;
import cscie97.asn4.ecommerce.product.Importer;

/**
 * Test class for Authentication Service system.
 * The main() method accepts 5 parameters, a file with authentication about users, a country info input file, 
 * a device info input file, a product info file and collection info file.
 * The class loads authentication info about the users, loads product info into the Product Catalog, then uses 
 * Collection Service to populate collections, and tests Authentication Service by calling restricted methods.
 * 
 * @author Frank O'Connor
 *
 */
public class TestDriver {

	/**
	 * Accepts 5 parameters, a file with authentication about users, a country info input file, a device info input file, 
	 * a product info file and collection info file.
	 * The class loads authentication info about the users, loads product info into the Product Catalog, 
	 * then uses Collection Service to populate collections, and tests Authentication Service by calling restricted methods.
	 * Correct Usage: java ­cp . cscie97.asn4.test.TestDriver authInfoFileName countryInfoFileName deviceInfoFileName productInfoFileName collectionsFileName
	 * @param args[0] the authInfo input file name
	 * @param args[1] the country input file name
	 * @param args[2] the device input file name
	 * @param args[3] the product info input file name
	 * @param args[4] the collection info file name
	 * 
	 */
	public static void main(String[] args) {
		
		if(args.length != 5 ){
			System.out.println("Incorrect arguments passed./nCorrect Usage: java -cp . cscie97.asn4.test.TestDriver "
					+ "authInfoFileName countryInfoFileName deviceInfoFileName productInfoFileName collectionsFileName");
		} else {
			String authFilename = args[0];
			String countryFilename = args[1];
			String deviceFilename = args[2];
			String productFilename = args[3];
			String collectionsFileName = args[4];
			
			// placeholder guid String
			UUID adminGuid = null;
			
			try {
				AuthenticationService authService = AuthenticationServiceImpl.getInstance();
				adminGuid = authService.login("sysAdmin", "rOotAcCeSsPwd");
				
				// calling importer to parse the csv for auth info files
				AuthenticationImporter csvAuthImporter = new AuthenticationImporter();
				// Adding auth info via Importer.
				csvAuthImporter.importAuthenticationCsvFile(adminGuid, authFilename);
				
				// log out root user
				authService.logout("sysAdmin");
				
				// log in as product_admin user
				adminGuid = authService.login("sam", "secret");
				
				// calling importer to parse the csv for product info files
				Importer csvImporter = new Importer();
				// Adding valid countries via Importer.
				csvImporter.importFileData(adminGuid, countryFilename, "country");
				
				// log out as sam
				authService.logout("sam");
				
				try {
					// try login with incorrect id 
					adminGuid = authService.login("sam2", "secret4");
				} catch (AccessDeniedException ade1) {
					System.out.println("The specified username/password are incorrect, please try again.");
					// log in with sam's other (correct) credentials
					adminGuid = authService.login("sam2", "secret2");
				}
				
				// Adding valid devices via Importer.
				csvImporter.importFileData(adminGuid, deviceFilename, "device");
				
				// log in as product_dev
				adminGuid = authService.login("joe", "1234");
				
				// Adding new Products via Importer.
				csvImporter.importFileData(adminGuid, productFilename, "product");
				
				// log in as collection_admin
				adminGuid = authService.login("lucy", "4567");
				
				CollectionImporter csvCollectionImporter = new CollectionImporter();
				// calling collection importer to parse the csv files
				csvCollectionImporter.importCollectionCsvFile(adminGuid, collectionsFileName);
				
				// Display inventory
				authService.showInventory();
				
				// log out as sam
				authService.logout("sam2");
				authService.logout("joe");
				authService.logout("lucy");
				
			} catch (ImportException ie) {
				// Exception occurred while importing ProductCatalog
				System.out.println(ie.getMessage());
			} catch (AuthenticationImportException aie) {
				// Exception occurred while importing Collections
				if(aie.getCause() instanceof FileNotFoundException){
					System.out.println("Error: Could not find the specified file, \""+aie.getFilename()+"\"\nPlease check filepath is correct.");
				}else{
					System.out.println("An error occured while importing from the csv file : " + aie.getFilename());
					System.out.println("The cause was: " + aie.getReason());
					System.out.println("This occured on line "+ aie.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + aie.getLineWhereFailed()+"\"");
				}
			} catch (CollectionImportException cie) {
				// Exception occurred while importing Collections
				if(cie.getCause() instanceof FileNotFoundException){
					System.out.println("Error: Could not find the specified file, \""+cie.getFilename()+"\"\nPlease check filepath is correct.");
				}else{
					System.out.println("An error occured while importing from the csv file : " + cie.getFilename());
					System.out.println("The cause was: " + cie.getReason());
					System.out.println("This occured on line "+ cie.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + cie.getLineWhereFailed()+"\"");
				}
			} catch (CollectableChildException cce) {
				// Exception occurred while trying to add/remove children from a collection
				System.out.println("An error occured while trying to add/remove children from a collection");
				System.out.println("The cause was: " + cce.getReason());
				// case where we we processing commands from a csv file
				if(cce.getFilename()!=null && !cce.getFilename().equals("")){
					System.out.println("An error occured while processing commands from the csv file : " + cce.getFilename());
					System.out.println("This occured on line "+ cce.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + cce.getLineWhereFailed()+"\"");
				}
			} catch (CollectableValidationException cve) {
				// Exception occurred while trying to add/remove children from a collection
				System.out.println("An error occured due to an invalid action while modifying a Collectable object");
				System.out.println("The cause was: " + cve.getReason());
				// case where we we processing commands from a csv file
				if(cve.getFilename()!=null && !cve.getFilename().equals("")){
					System.out.println("An error occured while processing commands from the csv file : " + cve.getFilename());
					System.out.println("This occured on line "+ cve.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + cve.getLineWhereFailed()+"\"");
				}
			} catch (EntitlementChildException ece) {
				// Exception occurred while trying to add/remove children from an Entitlement
				System.out.println("An error occured while trying to add/remove children from an entitlement");
				System.out.println("The cause was: " + ece.getReason());
				// case where we we processing commands from a csv file
				if(ece.getFilename()!=null && !ece.getFilename().equals("")){
					System.out.println("An error occured while processing commands from the csv file : " + ece.getFilename());
					System.out.println("This occured on line "+ ece.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + ece.getLineWhereFailed()+"\"");
				}
			} catch (AccessDeniedException ade) {
				// User does not have correct access
				System.out.println("The authentication details supplied are not valid");
				System.out.println("The cause was: " + ade.getReason());
				// case where we we processing commands from a csv file
				if(ade.getFilename()!=null && !ade.getFilename().equals("")){
					System.out.println("An error occured while processing commands from the csv file : " + ade.getFilename());
					System.out.println("This occured on line "+ ade.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + ade.getLineWhereFailed()+"\"");
				}
			} catch (InvalidReferenceException ire) {
				// Exception occurred while trying to reference an object that is not there
				System.out.println("An error occured while trying to create/reference an object with a specified id");
				System.out.println("The cause was: " + ire.getReason());
				// case where we we processing commands from a csv file
				if(ire.getFilename()!=null && !ire.getFilename().equals("")){
					System.out.println("An error occured while processing commands from the csv file : " + ire.getFilename());
					System.out.println("This occured on line "+ ire.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + ire.getLineWhereFailed()+"\"");
				}
			} catch (InvalidAccessTokenException iate) {
				// Access token is no longer valid
				System.out.println("AccessToken with specified id is no longer active.");
				System.out.println("The cause was: " + iate.getReason());
				// case where we we processing commands from a csv file
				if(iate.getFilename()!=null && !iate.getFilename().equals("")){
					System.out.println("An error occured while processing commands from the csv file : " + iate.getFilename());
					System.out.println("This occured on line "+ iate.getLineIndexWhereFailed() +" of the csv file");
					System.out.println("This failed line reads as follows: \"" + iate.getLineWhereFailed()+"\"");
				}
			}
		}
	}

}
