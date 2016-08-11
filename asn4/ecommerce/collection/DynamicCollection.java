package cscie97.asn4.ecommerce.collection;

import java.util.ArrayList;
import java.util.List;

import cscie97.asn4.ecommerce.product.Product;
import cscie97.asn4.ecommerce.product.ProductCatalog;
import cscie97.asn4.ecommerce.product.ProductCatalogImpl;

/**
 * The DynamicCollection class inherits from the generalized ProductCollection class. It represents a dynamic 
 * collection within the CollectionService. A Dynamic Collection is grouping of products represented by specific 
 * search criteria. This criteria is used to call the ProductCatlog API and the returned matching List of Product 
 * objects represent this group. A Dynamic Collection also allows other collections as children.
 * 
 * @author Frank O'Connor
 *
 */
public class DynamicCollection extends ProductCollection {
	
	// Criteria defining Dynamic Collection
	private SearchCriteria searchTerms;
	
	/**
	 * Constructor for Dynamic Collection
	 * @param collectionId
	 * @param collectionName
	 * @param collectionDescription
	 */
	public DynamicCollection(String collectionId, String collectionName, String collectionDescription) {
		super(collectionId, collectionName, collectionDescription);
		this.searchTerms = null;
	}
	
	/**
	 * Overloaded constructor for Dynamic Collection, allowing setting of criteria at instantiation.
	 * @param collectionId
	 * @param collectionName
	 * @param collectionDescription
	 * @param searchTerms
	 */
	public DynamicCollection(String collectionId, String collectionName, String collectionDescription, SearchCriteria searchTerms) {
		super(collectionId, collectionName, collectionDescription);
		this.searchTerms = searchTerms;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#addChild(cscie97.asn3.ecommerce.collection.Collectable)
	 * Adding Child collection to DynamicCollection. If passed Collectable object is not a collection then an 
	 * CollectableChildException is thrown. 
	 */
	@Override
	public void addChild(Collectable collectable) throws CollectableChildException {
		// Validate we do not add a child with an id already existing in the collection
		List<Collectable> dChildren= this.getChildren();
		
		for (Collectable existingChild : dChildren) {
			if(existingChild.getCollectionId().equals(collectable.getCollectionId())){
				throw new CollectableChildException("Child of Collection already exists with Id", "", 0, "", new Exception());
			}
		}
		
		// Dynamic Collection only allows addition of static collections (not products)
		if(collectable instanceof ProductCollection){
			childCollections.add(collectable);				
		} else {
			throw new CollectableChildException("Dynamic Collection may only have ProductCollection as static child", "", 0, "", new Exception());
		}
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#getChildren()
	 * Returns children of DynamicCollection. Includes products matching Criteria as ProductProxy objects, and 
	 * any child collections belonging to the DynamicCollection.
	 */
	@Override
	public List<Collectable> getChildren() {
		List<Collectable> childList = new ArrayList<Collectable>();
		childList.addAll(getProducts());
		childList.addAll(this.childCollections);
		return childList;
	}
	
	/**
	 * This function calls the ProductCatalog API and passes the productCriteria. The resultant 
	 * matching products returned from the ProductCatalog API represent the products in this DynamicCollection. 
	 * Note: Products are returned as ProductProxy objects to aid in iteration over the collection.
	 * @return List<Collectable> of ProductProxy objects matching criteria of DynamicCollection.
	 */
	public List<Collectable> getProducts() {
		List<Collectable> productList = new ArrayList<Collectable>();
		
		// calling singleton instance of productCatalog API
		ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
		// querying Products
		List<Product> queryResult = new ArrayList<Product>();
		if(this.searchTerms!=null){
			queryResult = pCatalog.queryProducts(this.searchTerms.getCategories(), this.searchTerms.getTextSearch(), this.searchTerms.getMinimumRating(),
					this.searchTerms.getMaxPrice(), this.searchTerms.getLanguages(), this.searchTerms.getCountryCode(), this.searchTerms.getDeviceId(),
					this.searchTerms.getContentTypes());
		}
		
		// Here ProductProxy objects are returned instead of products doing this to allows for iteration of dynamic product children
		// as all returned children are Collectable
		for (Product product : queryResult) {
			product.getProductId();
			ProductProxy productProxy = new ProductProxy(product.getProductId(), product.getProductName(), product.getDescription());
			productList.add(productProxy);
		}
			
		return productList;
	}
	
	/**
	 * Get the criteria of the DynamicCollection
	 * @return criteria of the collection.
	 */
	public SearchCriteria getSearchTerms() {
		return searchTerms;
	}

	/**
	 * Set the criteria of the DynamicCollection
	 * @param searchTerms defines criteria of the DynamicCollection
	 */
	public void setSearchTerms(SearchCriteria searchTerms) {
		this.searchTerms = searchTerms;
	}
}
