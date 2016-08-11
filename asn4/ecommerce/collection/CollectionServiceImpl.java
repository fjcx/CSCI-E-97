package cscie97.asn4.ecommerce.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.AuthenticationService;
import cscie97.asn4.ecommerce.authentication.AuthenticationServiceImpl;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;
import cscie97.asn4.ecommerce.product.Product;
import cscie97.asn4.ecommerce.product.ProductCatalog;
import cscie97.asn4.ecommerce.product.ProductCatalogImpl;

/**
 * Implementation class of the CollectionService API.
 * 
 * @author Frank O'Connor
 *
 */
public class CollectionServiceImpl implements CollectionService {
	
	// single instance of CollectionService
	private static CollectionService instance = null;
	
	// Map containing all collections
	private Map<String, Collectable> collectionMap;
	private AuthenticationService authService;
	
	/**
	 * Constructor for CollectionServiceImpl.
	 * CollectionServiceImpl is a singleton, hence the constructor is private
	 * which prevents instantiation from classes other than getInstance
	 */
	private CollectionServiceImpl() {
		this.collectionMap = new HashMap<String, Collectable>();
		// root collection of collections graph
		this.collectionMap.put("root_collection", new StaticCollection("root_collection", "root_collection", "top_level_collection"));
		this.authService = AuthenticationServiceImpl.getInstance();
	}
	
	/**
	 * Return instance of ProductCatalog, or creates one if not already created
	 * using synchronized to ensure thread safety
	 * @return singleton instance of ProductCatalog.
	 */
	public static synchronized CollectionService getInstance(){
		if (instance == null) {
            instance = new CollectionServiceImpl();
	    }
	    return instance;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectionService#createCollection(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createCollection(UUID authGuid, String collectionType, String collectionId, String collectionName, String collectionDesc) throws CollectableChildException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException {
		if(authService.validateAccessToken(authGuid, "create_collection")){
			// creating node under root collection by default
			Collectable newCollection;
	
			if(collectionType.toLowerCase().equals("static")){
				newCollection = new StaticCollection(collectionId, collectionName, collectionDesc);
			}else if(collectionType.toLowerCase().equals("dynamic")){
				newCollection = new DynamicCollection(collectionId, collectionName, collectionDesc);
			}else{
				throw new CollectableValidationException("Incorrect collectionType specified", "", 0, "", new Exception());
			}
			
			this.collectionMap.put(collectionId, newCollection);
			// add reference of new collection to root_collection
			this.collectionMap.get("root_collection").addChild(this.collectionMap.get(collectionId));	// should we check if this exists
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectionService#addContent(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addContent(UUID authGuid, String parentCollectionId, String contentType, String childContentId) throws CollectableValidationException, CollectableChildException, AccessDeniedException, InvalidAccessTokenException {		
		if(authService.validateAccessToken(authGuid, "add_content")){
			// addContent moves a collection to under a new parent collection
			// or in the case of a Product if a product exists then a ProductProxy class is generated under the parent
			Collectable toAdd = null;
			String cleanParentId = parentCollectionId.trim().toLowerCase();
			String cleanChildId = childContentId.trim().toLowerCase();
	
			// Note: not allowing addition of product to root_collection
			if(contentType.toLowerCase().equals("product") && !cleanParentId.toLowerCase().equals("root_collection")) {
				// Going to Product API to see if product exists
				ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
				Product product = pCatalog.getProductById(cleanChildId);
				// add product to collection, if one exists with the same productId as in the Product Catalog
				if(product != null){
					toAdd = new ProductProxy(cleanChildId, product.getProductName(), product.getDescription());
				} else {
					throw new CollectableValidationException("Product with passed Id does not exist", "", 0, "", new Exception());
				}
		
			} else if(contentType.toLowerCase().equals("collection")){
				// if node exists at root level, remove it from here, before adding as a child elsewhere.
				// this deals with the case where all collections are added to root_collection by default
				// pop child from rootCollection to add elsewhere
				
				// removeChild returns null if it did not exist
				this.collectionMap.get("root_collection").removeChild(cleanChildId);	
				
				// if collection already exists then get reference, and add it elsewhere in graph
				toAdd = this.collectionMap.get(cleanChildId);
				
				if(toAdd == null){
					throw new CollectableValidationException("No existing collection with specified Id", "", 0, "", new Exception());
				}
				
			}else{
				throw new CollectableValidationException("Incorrect contentType specified", "", 0, "", new Exception());
			}
					
			// find reference to parent and add child
			if(this.collectionMap.get(cleanParentId) != null){
				if(this.collectionMap.get(cleanParentId) instanceof StaticCollection){
					this.collectionMap.get(cleanParentId).addChild(toAdd);					
				}else if(this.collectionMap.get(cleanParentId) instanceof DynamicCollection){
					if(toAdd instanceof ProductCollection){
						this.collectionMap.get(cleanParentId).addChild(toAdd);
					}else{
						throw new CollectableValidationException("Can only add Collection as child to Dynamic Collection", "", 0, "", new Exception());
					}				
				}else {
					throw new CollectableValidationException("Parent Collection is not a Static/Dynamic Collection", "", 0, "", new Exception());
				}
			}else {
				throw new CollectableValidationException("ParentCollection with Id does not exist", "", 0, "", new Exception());
			}		
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectionService#searchCollection(java.lang.String)
	 */
	@Override
	public List<ProductCollection> searchCollection(String searchText) {
		List<ProductCollection> resultList = new ArrayList<ProductCollection>();
		boolean addAll = false;
		String cleanSearchString = searchText.trim().toLowerCase();
		// Returns only matching ProductCollections, not ProductProxies
		if(cleanSearchString.trim().toLowerCase().equals("")){
			addAll = true;
		}
		
		// iterator starting from root_collection by passing null
		CollectableIterator rootIter = createIterator(null);
		while (rootIter.hasNext()) {
			Collectable cItem = (Collectable) rootIter.next();
			if(cItem instanceof ProductCollection){
				// compare against name and description of collectables
				if(addAll || cItem.getCollectionName().contains(searchText) || cItem.getCollectionDescription().contains(searchText)){
					resultList.add((ProductCollection) cItem);
				}
			}
			
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectionService#createIterator(cscie97.asn3.ecommerce.collection.Collectable)
	 */
	@Override
	public CollectableIterator createIterator(String collectionId) {	// should this be an id really ????
		CollectableIterator collectionIter = null;
		
		if(collectionId==null){
			// start from top level (root_collection) of graph if null is passed
			collectionIter = this.collectionMap.get("root_collection").createIterator();
		}else{
			// find specified collection, start from there
			if(this.collectionMap.get(collectionId)!=null){
				collectionIter = this.collectionMap.get(collectionId.trim().toLowerCase()).createIterator();
			}
		}
		
		return collectionIter;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectionService#setDynamicCollectionCriteria(java.lang.String, java.lang.String, cscie97.asn3.ecommerce.collection.SearchCriteria)
	 */
	@Override
	public void setDynamicCollectionCriteria(UUID authId, String collectionId, SearchCriteria searchTerms) throws CollectableValidationException, AccessDeniedException, InvalidAccessTokenException {
		if(authService.validateAccessToken(authId, "add_content")){
			// perform some validation on criteria
			if(searchTerms.getMinimumRating() < 0 || searchTerms.getMinimumRating() > 5){
				throw new CollectableValidationException("Invalid Rating. Rating should be between 0 and 5.", "", 0, "", new Exception());
			}
			
			if(searchTerms.getMaxPrice() < 0){
				throw new CollectableValidationException("MaxPrice cannot be less than 0", "", 0, "", new Exception());
			}
			
			// get collection from map and set its criteria
			Collectable dCollection = this.collectionMap.get(collectionId.trim().toLowerCase());
			if(dCollection!=null && dCollection instanceof DynamicCollection){
				((DynamicCollection) dCollection).setSearchTerms(searchTerms);
			}else{
				throw new CollectableValidationException("Specified collection is not a DynamicCollection", "", 0, "", new Exception());
			}
		}
	}
	
}
