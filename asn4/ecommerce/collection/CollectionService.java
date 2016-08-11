package cscie97.asn4.ecommerce.collection;

import java.util.List;
import java.util.UUID;

import cscie97.asn4.ecommerce.authentication.AccessDeniedException;
import cscie97.asn4.ecommerce.authentication.InvalidAccessTokenException;

/**
 * This interface defines the publically available functions for the CollectionService. 
 * Functionality made available through the interface includes the abilities to create 
 * a new collection, add content to an existing collection, set the criteria of a 
 * DynamicCollection, search for a collection, and iterate through the collections.
 * 
 * @author Frank O'Connor
 *
 */
public interface CollectionService {
	

	/**
	 * Creates a new collection in the CollectionService. The collection by default is created at the top level of 
	 * the CollectionService.
	 * @param adminGuid used to validate the user
	 * @param collectionType defines what kind of collection to create (expecting 'static' or 'dynamic') 
	 * @param collectionId the id for the new collection
	 * @param collectionName the name for the new collection
	 * @param collectionDesc the description for the new collection
	 * @throws CollectableChildException if exception occurs while creating Collectable child
	 * @throws CollectableValidationException if invalid action performed while modifying a Collectable object
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void createCollection(UUID adminGuid, String collectionType, String collectionId, String collectionName, String collectionDesc) throws CollectableChildException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Adds content to a collection which is defined by the parameter collectionId. This content can be either 
	 * a new ProductProxy or a child Collection. 
	 * @param adminGuid used to validate the user
	 * @param parentCollectionId the parent node to which the child content is added
	 * @param contentType defines what kind of child is added/created (expecting 'product' or 'collection') 
	 * @param childContentId the id of the child node
	 * @throws CollectableChildException if exception occurs while creating Collectable child 
	 * @throws CollectableValidationException if invalid action performed while modifying a Collectable object
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void addContent(UUID adminGuid, String parentCollectionId, String contentType, String childContentId) throws CollectableChildException, CollectableValidationException, AccessDeniedException, InvalidAccessTokenException;
	

	/**
	 * Allows the user to search through the CollectionService. The search traverses through collections 
	 * in CollectionService and checks if for matching ProductCollections whose names or descriptions contains 
	 * the search String.
	 * @param searchText text to search for.
	 * @return list of matching collections
	 */
	public List<ProductCollection> searchCollection(String searchText);
	

	/**
	 * Returns a CollectableIterator which can be used to iterate over all items within that collection.
	 * @param collectionId the collectionId of the collection node to begin iterating from.
	 * @return CollectableIterator used for iterating through collections.
	 */
	public CollectableIterator createIterator(String collectionId);


	/**
	 * Sets the criteria in a DynamicCollection. The function first validates the criteria to ensure it is valid.
	 * @param adminGuid used to validate the user
	 * @param collectionId id of the DynamicCollection
	 * @param searchTerms the SearchCriteria for the DynamicCollection
	 * @throws CollectableValidationException if invalid action performed while modifying a Collectable object
	 * @throws AccessDeniedException 
	 * @throws InvalidAccessTokenException 
	 */
	public void setDynamicCollectionCriteria(UUID adminGuid, String collectionId, SearchCriteria searchTerms) throws CollectableValidationException, AccessDeniedException, InvalidAccessTokenException;
	
}
