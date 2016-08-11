package cscie97.asn4.ecommerce.product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;

/**
 * The Product Catalog stores information about all valid export countries, devices. Information about Products for sale
 * and meta data about the products
 * 
 * @author Frank O'Connor
 *
 */
public interface ProductCatalog {
	
	/**
	 * Adds a country to the Product Catalog list of valid countries.
	 * @param adminGuid guid to validate user
	 * @param countryId 
	 * @param countryName
	 * @param isExportOpen
	 * @throws ImportException
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void addCountry(UUID adminGuid, String countryId, String countryName, boolean isExportOpen) throws ImportException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Adds a device to the Product Catalog list of valid devices.
	 * @param authGuid guid to validate user
	 * @param deviceId
	 * @param deviceName
	 * @param manufacturer
	 * @throws ImportException if validation fails on Device
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void addDevice(UUID authGuid, String deviceId, String deviceName, String manufacturer) throws ImportException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Adds a product to the Product Catalog if none already exists with it's id.
	 * @param product a product object
	 * @throws ImportException if validation fails on Product
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void addProduct(UUID authGuid, Product product) throws ImportException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Process query on ProductCatalog, tries to match criteria to exist Products in Catalog.
	 * @param categorySet query categories to match
	 * @param textSearch query text to find
	 * @param minimumRating query rating minimum
	 * @param maxPrice query price maximum
	 * @param languageSet query languages to match
	 * @param countryCode query country to match
	 * @param deviceId query device to match
	 * @param contentTypeSet query contentType to match
	 * @return
	 */
	public List<Product> queryProducts(Set<String> categorySet, String textSearch, int minimumRating, float maxPrice, Set<String> languageSet, String countryCode, String deviceId, Set<String> contentTypeSet);
	
	/**
	 * Returns info about a valid country if it exists
	 * @param countryId to search for
	 * @return country object
	 */
	public Country getValidCountry(String countryId);
	
	/**
	 * Returns info about a valid device if it exists
	 * @param deviceId to search for
	 * @return device object
	 */
	public Device getValidDevice(String deviceId);
	
	/**
	 * Returns product by Id
	 * @param deviceId to search for
	 * @return device object
	 */
	public Product getProductById(String productId);

}
