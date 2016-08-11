package cscie97.asn4.ecommerce.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.AuthenticationService;
import cscie97.asn4.ecommerce.authentication.AuthenticationServiceImpl;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;

/**
 * Implementation class of the ProductCatalog API
 * 
 * @author Frank O'Connor
 *
 */
public class ProductCatalogImpl implements ProductCatalog {
	
	// single instance of ProductCatalog
	private static ProductCatalog instance = null;
	private List <Country> validCountries;
	private List <Device> validDevices;
	private Set <Product> products;
	private AuthenticationService authService;
	
	/**
	 * Constructor for ProductCatalogImpl.
	 * ProductCatalogImpl is a singleton, hence the constructor is private
	 * which prevents instantiation from classes other than getInstance
	 */
	private ProductCatalogImpl() {
		validCountries = new ArrayList<Country>();
		validDevices = new ArrayList<Device>();
		products = new HashSet<Product>();
		this.authService = AuthenticationServiceImpl.getInstance();
	}
	
	/**
	 * Return instance of ProductCatalog, or creates one if not already created
	 * using synchronized to ensure thread safety
	 * @return singleton instance of ProductCatalog.
	 */
	public static synchronized ProductCatalog getInstance(){
		if (instance == null) {
            instance = new ProductCatalogImpl();
	    }
	    return instance;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn2.ecommerce.product.ProductCatalog#queryProducts(java.util.Set, java.lang.String, int, float, java.util.Set, java.lang.String, java.lang.String, java.util.Set)
	 */
	public List<Product> queryProducts(Set<String> categorySet, String searchText, int minimumRating, float maxPrice, Set<String> languageSet,
			String countryCode, String deviceId, Set<String> contentTypeSet) {
		
		// list of all Products in the ProductCatalog
		Set<Product> completeProductSet = getProducts();
		// result list of query
		List<Product> resultProductList = new ArrayList<Product>();
		 
		// 
		for (Product product: completeProductSet) {
			boolean addProduct = true;
			// checking for matching categories
			if (categorySet.size() > 0) {
				HashSet<String> catIntersectionSet =  new HashSet<String>(categorySet);
				// getting intersection of query categories those of the products
				catIntersectionSet.retainAll(product.getCategories());
				if(catIntersectionSet.size()==0){
					// does not match query
					addProduct =  false;	
				}
			}
			
			// checking text search
			if (addProduct) {
				if (hasText(searchText)) {
					addProduct = ((product.getProductName().indexOf(searchText) >= 0)  || (product.getDescription().indexOf(searchText) >= 0));
				}
			}
			 
			// checking minimum rating
			if (addProduct) {
				addProduct = (product.getRating() >= minimumRating);
			}
			 
			// checking max price
			if (addProduct) {
				addProduct = (product.getPrice() <= maxPrice);
			}
			 
			// checking language list
			if (addProduct && languageSet.size() > 0) {
				HashSet<String> langIntersectionSet =  new HashSet<String>(languageSet);
				// getting intersection of query languages those of the products
				langIntersectionSet.retainAll(product.getLanguages());
				if(langIntersectionSet.size()==0){
					// does not match query
					addProduct =  false;	
				}
			}
			 
			// checking country code
			if (addProduct) {
				if (hasText(countryCode)) {
					addProduct = false;
					for (Country country : product.getCountries()) {
						if(country.getCountryId().equals(countryCode)){
							addProduct = true;
							break;
						}
					}
				}
			}
			 
			// checking device id
			if (addProduct) {
				if (hasText(deviceId)) {
					addProduct = false;
					for (Device device : product.getDevices()) {
						if(device.getDeviceId().equals(deviceId)){
							addProduct = true;
							break;
						}
					}
				}
			}
			 
			// check content type list
			if (addProduct && contentTypeSet.size() > 0) {
				
				addProduct = false;	// default to false
				if(product instanceof Application){
					if(contentTypeSet.contains("application")){
						addProduct = true;
					}
				} else if (product instanceof RingTone){
					if(contentTypeSet.contains("ringtone")){
						addProduct = true;
					}
				} else if (product instanceof Wallpaper){
					if(contentTypeSet.contains("wallpaper")){
						addProduct = true;
					}
				}				
			}
			 
			// finally, if all checks passed, add the product to the result set.
			if (addProduct) {
				resultProductList.add(product);
			}
		}		
		
		return resultProductList;
	}
	

	
	/* (non-Javadoc)
	 * @see cscie97.asn2.ecommerce.product.ProductCatalog#getValidCountry(java.lang.String)
	 */
	@Override
	public Country getValidCountry(String countryId){
		for(Country validCountry : getValidCountries()){
			if(validCountry.getCountryId().equals(countryId)){
				return validCountry;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn2.ecommerce.product.ProductCatalog#getValidDevice(java.lang.String)
	 */
	@Override
	public Device getValidDevice(String deviceId){		// maybe should be private
		for(Device validDevice : getValidDevices()){
			if(validDevice.getDeviceId().equals(deviceId)){
				return validDevice;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn2.ecommerce.product.ProductCatalog#addProduct(cscie97.asn2.ecommerce.product.Product)
	 */
	@Override
	public void addProduct(UUID authGuid, Product product) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
		if(authService.validateAccessToken(authGuid, "create_product")){
			// check uniqueness of productId
			for (Product existingProduct : getProducts()) {
				if(existingProduct.getProductId().equals(product.getProductId())){
					throw new ImportException("Existing ProductId", "", 0, "", new Exception());
				}
			}
			this.getProducts().add(product);
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn2.ecommerce.product.ProductCatalog#addCountry(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void addCountry(UUID authGuid, String countryId, String countryName, boolean isExportOpen) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
		if(authService.validateAccessToken(authGuid, "create_country")){
			// check uniqueness of countryId
			for (Country existingCountry : validCountries) {
				if(existingCountry.getCountryId().equals(countryId)){
					throw new ImportException("Existing CountryId", "", 0, "", new Exception());
				}
			}
			// validated in the constructor
			Country country = new Country(countryId, countryName, isExportOpen);
			this.validCountries.add(country);
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn2.ecommerce.product.ProductCatalog#addDevice(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addDevice(UUID authGuid, String deviceId, String deviceName, String manufacturer) throws ImportException, AccessDeniedException, InvalidAccessTokenException {
		if(authService.validateAccessToken(authGuid, "create_device")){
			// check uniqueness of deviceId
			for (Device existingDevice : validDevices) {
				if(existingDevice.getDeviceId().equals(deviceId)){
					throw new ImportException("Existing DeviceId", "", 0, "", new Exception());
				}
			}
	
			Device device = new Device(deviceId, deviceName, manufacturer);
			this.validDevices.add(device);
		}
	}
	
	public List<Country> getValidCountries() {
		return validCountries;
	}

	public void setValidCountries(List<Country> validCountries) {
		this.validCountries = validCountries;
	}

	public List<Device> getValidDevices() {
		return validDevices;
	}

	public void setValidDevices(List<Device> validDevices) {
		this.validDevices = validDevices;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	// private utility method
	private boolean hasText(String text) {
        return (text != null && !text.isEmpty() && !text.trim().isEmpty());
    }

	@Override
	public Product getProductById(String productId) {
		for (Product product : getProducts()) {
			if(product.getProductId().equals(productId)){
				return product;
			}
		}
		return null;
	}

}
