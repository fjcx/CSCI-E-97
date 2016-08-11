package cscie97.asn4.ecommerce.collection;

import java.util.HashSet;
import java.util.Set;

/**
 * The SearchCriteria class represents the criteria for a specific product search query. In the 
 * CollectionService each DynamicCollection will have a set of criteria to defines it’s products. 
 * Any of the properties in the SearchCriteria can be null, these will not be used for the search.
 * @author Frank O'Connor
 *
 */
public class SearchCriteria {
	
	private Set<String> categories;
	private String textSearch;
	private int minimumRating;
	private float maxPrice;
	private Set<String> languages;
	private String countryCode;
	private String deviceId;
	private Set<String> contentTypes;
	
	/**
	 * Constructor for SerachCriteria
	 * @param categories set of Strings listing the categories of the products to search for.
	 * @param textSearch text String to match to the Product name or description in a search.
	 * @param minimumRating of product to search for. (the rating range is between 0 and 5).
	 * @param maxPrice of product to search for.
	 * @param languages set of Strings listing the languages of the products to search for.
	 * @param countryCode a String of the countryCode of the products to search for.
	 * @param deviceId a String of the deviceId supported by the products to search for.
	 * @param contentTypes set of Strings listing the contentTypes of the products to search for.
	 */
	public SearchCriteria(Set<String> categories, String textSearch, int minimumRating, float maxPrice, Set<String> languages,
			String countryCode, String deviceId, Set<String> contentTypes) {
		this.categories = categories;
		this.textSearch = textSearch;
		this.minimumRating = minimumRating;
		this.maxPrice = maxPrice;
		this.languages = languages;
		this.countryCode = countryCode;
		this.deviceId = deviceId;
		this.contentTypes = contentTypes;
	}
	
	/**
	 * Constructor for SearchCriteria, where no params are passed
	 */
	public SearchCriteria() {	
		// setting default values for the Criteria if none are set
		this.categories = new HashSet<String>();
		this.textSearch = "";
		this.minimumRating = 0;
		this.maxPrice = 999999999;
		this.languages = new HashSet<String>();
		this.countryCode = "";
		this.deviceId = "";
		this.contentTypes = new HashSet<String>();
	}
	
	/**
	 * @return categories of the products to search for
	 */
	public Set<String> getCategories() {
		return categories;
	}
	
	/**
	 * Sets categories of the products to search for
	 * @param categories
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	
	/**
	 * @return text String used for matching to the Product name or description in a search
	 */
	public String getTextSearch() {
		return textSearch;
	}
	
	/**
	 * Sets text String to match to the Product name or description in a search
	 * @param textSearch
	 */
	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
	
	/**
	 * @return minimumRating of product to search for.
	 */
	public int getMinimumRating() {
		return minimumRating;
	}
	
	/**
	 * Sets minimumRating of product to search for.
	 * @param minimumRating
	 */
	public void setMinimumRating(int minimumRating) {
		this.minimumRating = minimumRating;
	}
	
	/**
	 * @return maxPrice of product to search for.
	 */
	public float getMaxPrice() {
		return maxPrice;
	}
	
	/**
	 * Sets maxPrice of product to search for.
	 * @param maxPrice
	 */
	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}
	
	/**
	 * @return languages of the products to search for
	 */
	public Set<String> getLanguages() {
		return languages;
	}
	
	/**
	 * Sets languages of the products to search for
	 * @param languages
	 */
	public void setLanguages(Set<String> languages) {
		this.languages = languages;
	}
	
	/**
	 * @return countryCode of the products to search for.
	 */
	public String getCountryCode() {
		return countryCode;
	}
	
	/**
	 * Sets countryCode of the products to search for.
	 * @param countryCode
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	/**
	 * @return deviceId supported by the products to search for.
	 */
	public String getDeviceId() {
		return deviceId;
	}
	
	/**
	 * deviceId supported by the products to search for.
	 * @param deviceId
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	/**
	 * @return contentTypes of the products to search for
	 */
	public Set<String> getContentTypes() {
		return contentTypes;
	}
	
	/**
	 * Sets contentTypes of the products to search for
	 * @param contentTypes
	 */
	public void setContentTypes(Set<String> contentTypes) {
		this.contentTypes = contentTypes;
	}

}
