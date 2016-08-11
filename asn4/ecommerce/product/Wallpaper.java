package cscie97.asn4.ecommerce.product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The Wallpaper class is a Content Type and extends from Product Object
 * 
 * @author Frank O'Connor
 *
 */
public class Wallpaper extends Product{

	/**
	 * Wallpaper for Product abstract class
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
	public Wallpaper(UUID adminGuid, String productId, String productName, String author,
			List<Device> devices, Set<String> categories, String description,
			int rating, float price, List<Country> countries,
			List<String> languages, String imageUrl) throws ImportException {
		super(adminGuid, productId, productName, author, devices, categories, description, rating,
				price, countries, languages, imageUrl);
	}

}
