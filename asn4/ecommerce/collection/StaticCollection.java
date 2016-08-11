package cscie97.asn4.ecommerce.collection;

import java.util.List;

/**
 * The StaticCollection class inherits from the generalized ProductCollection class. 
 * It represents a static collection of Collectable objects within the CollectionService. 
 * Static collections refer to a specific collection of products and child collections.
 * 
 * @author Frank O'Connor
 *
 */
public class StaticCollection extends ProductCollection{
	
	/**
	 * Constructor for StaticCollection
	 * @param collectionId
	 * @param collectionName
	 * @param collectionDescription
	 */
	public StaticCollection(String collectionId, String collectionName, String collectionDescription) {
		super(collectionId, collectionName, collectionDescription);
	}
		
	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#getChildren()
	 * Returns the children of the StaticCollection
	 */
	@Override
	public List<Collectable> getChildren() {
		return this.childCollections;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#addChild(cscie97.asn3.ecommerce.collection.Collectable)
	 * Adds a child to the StaticCollection, if a child does not already exist with the same id.
	 * An Exception is thrown if existing child with Id is found
	 */
	@Override
	public void addChild(Collectable collectable) throws CollectableChildException {
		// check if any child in the direct children has the same id as the new child 
		for (Collectable existingChild : this.getChildren()) {
			if(existingChild.getCollectionId().equals(collectable.getCollectionId())){
				throw new CollectableChildException("Child of Collection already exists with Id", "", 0, "", new Exception());
			}
		}		
		
		// can add any Collectable to StaticCollection
		childCollections.add(collectable);
	}
	
}
