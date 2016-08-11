package cscie97.asn4.ecommerce.product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The Product class is an abstract class for the content types in ProductCatalog
 * The Product API Service is responsible for management of the Product inventory. The Product
 * API supports management of inventory, as well as ability to search, sort, and page through the
 * contents of the product catalog.
 * 
 * @author Frank O'Connor
 *
 */
public abstract class Product {
	
	private String productId;
	private String productName;
	private String author;
	private List<Device> devices;
	private Set<String> categories;
	private String description;
	private int rating;
	private float price;	// in BitCoins
	private List <Country> countries;
	private List <String> languages;
	private String imageUrl;
	
	/**
	 * Constructor for Product abstract class
	 * @param adminGuid authGuid for validating user
	 * @param productId id of the product
	 * @param productName name of the product
	 * @param author creator of the product
	 * @param devices compatible devices with the product
	 * @param categories product can be described by
	 * @param description comment about product
	 * @param rating of the product
	 * @param price bitCoin price of the product
	 * @param countries valid export countries
	 * @param languages supported by the product
	 * @param imageUrl link to image
	 * @throws ImportException 
	 */
	public Product(UUID adminGuid, String productId, String productName, String author,
			List<Device> devices, Set<String> categories, String description,
			int rating, float price, List<Country> countries,
			List<String> languages, String imageUrl) throws ImportException {
		super();
		this.productId = productId;
		this.productName = productName;
		this.author = author;
		this.devices = devices;
		this.categories = categories;
		this.description = description;
		this.rating = rating;
		this.price = price;
		this.countries = countries;
		this.languages = languages;
		this.imageUrl = imageUrl;
		
		if(!isValid()){
			throw new ImportException("Invalid Product params", "", 0, "", new Exception());
		}
	}

	/**
	 * Validator for constructing a Product Object.
	 * @return boolean isValid 
	 */
	private boolean isValid(){
		// validating input values for the product
		if(!hasText(productId)){
			return false;
		}
	
		if(!hasText(productName)){
			return false;
		}
		
		if(!hasText(author)){
			return false;
		}
		
		if(!hasText(description)){
			return false;
		}
		
		if(rating < 0 || rating> 5){
			return false;
		}
		
		if(price < 0){
			return false;
		}
		
		if(countries.size() < 1){
			return false;
		}
		
		if(devices.size() < 1){
			return false;
		}
		
		if(languages.size() < 1){
			return false;
		}else{
			for (String language : languages) {
				if(language.length() != 5){
					return false;
				}
			}
		}
		
		if(!hasText(imageUrl)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * @return
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param name
	 */
	public void setProductName(String name) {
		this.productName = name;
	}

	/**
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return
	 */
	public List<Device> getDevices() {
		return devices;
	}

	/**
	 * @param devices
	 */
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	/**
	 * @return
	 */
	public Set<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return
	 */
	public List<Country> getCountries() {
		return countries;
	}

	/**
	 * @param countries
	 */
	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	/**
	 * @return
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * @param languages
	 */
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	/**
	 * @return
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String toString(){
		String outputStr = "{productId: "+
		productId +"\nproductName: "+
		productName +"\nauthor: " +
		author +"\ndescription: " +
		description +"\nrating: " +
		rating +"\nprice: " +
		price +"\nimageUrl: " +
		imageUrl+"\ndevices: ";
		
		for (Device device : devices) {
			outputStr+= device.getDeviceName()+ ",";
		}
		outputStr+="\ncategories: ";
		for (String cat : categories) {
			outputStr+= cat+ ",";
		}
		outputStr+="\nlanguages: ";
		for (String lang : languages) {
			outputStr+= lang+ ",";
		}
		outputStr+="\ncountries: ";
		for (Country country : countries) {
			outputStr+= country.getCountryId()+ ",";
		}
		outputStr+="}";
		return outputStr;
		
	}
	
	// private utility method
	private boolean hasText(String text) {
        return (text != null && !text.isEmpty() && !text.trim().isEmpty());
    }

}
