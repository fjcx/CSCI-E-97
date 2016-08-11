package cscie97.asn4.ecommerce.collection;

import java.util.List;

/**
 * The Collectable class is an abstract class which abstracts the functionality of all the objects 
 * comprising a collection that inherit from it. This abstract class defines the main functions 
 * for the objects in a collection. This functionality includes adding a child collection, removing 
 * a child collection, returning all the children of the collection and returning an iterator to walk 
 * through all the items within the collection.
 * 
 * @author Frank O'Connor
 *
 */
public abstract class Collectable {
	
	private String collectionId;
	private String collectionName;
	private String collectionDescription;
	
	/**
	 * Constructor of abstract Collectable class
	 * @param collectionId id for collection
	 * @param collectionName name for collection
	 * @param collectionDescription description for collection
	 */
	protected Collectable(String collectionId, String collectionName, String collectionDescription) {
		this.collectionId = collectionId;
		this.collectionName = collectionName;
		this.collectionDescription = collectionDescription;
	}
	
	/**
	 * Adds the passed parameter collection as a child to the Collectable object.
	 * 
	 * Does not allow child to be added to ProductProxy. 
	 * Does not allow ProductProxy to be added as child to DynamicCollection. 
	 * 
	 * @param collectable Object to be set as child of Collection
	 * @throws CollectableChildException if trying to add ProductProxy child to DynamicCollection, or trying to add child to ProductProxy 
	 */
	public abstract void addChild(Collectable collectable) throws CollectableChildException;
	
	/**
	 * From the Collectable object a child is removed which has the collectionId matching the passed parameter. 
	 * @param collectionId of the child to be removed
	 * @return the removed Child if it exists, else this is null
	 * @throws CollectableChildException if attempting to remove a child from ProductProxy.
	 */
	public abstract Collectable removeChild(String collectionId) throws CollectableChildException;
	
	/**
	 * Returns a List of all the children that belong to the Collectable object.
	 * @return list of children of the Collection
	 */
	public abstract List<Collectable> getChildren();
	
	/**
	 * Returns an iterator of type CollectableIterator, which allows the traversal of the child objects 
	 * within the collection in a depth first search manner.
	 * @return CollectableIterator
	 */
	public abstract CollectableIterator createIterator();
	
	/**
	 * Get Id for Collectable object
	 * @return collectionId
	 */
	public String getCollectionId() {
		return collectionId;
	}
	
	/**
	 * Set Id for Collectable object
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	
	/**
	 * Get name for Collectable object
	 * @return collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}
	
	/**
	 * Set name for Collectable object
	 * @param collectionName
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
	/**
	 * Get description for Collectable object
	 * @return collectionDescription
	 */
	public String getCollectionDescription() {
		return collectionDescription;
	}
	
	/**
	 * Set description for Collectable object
	 * @param collectionDescription
	 */
	public void setCollectionDescription(String collectionDescription) {
		this.collectionDescription = collectionDescription;
	}
	
}
