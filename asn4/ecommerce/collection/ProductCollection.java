package cscie97.asn4.ecommerce.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The ProductCollection is an abstract class which inherits from the abstract Collectable class. 
 * It generalizes the functionality of a collection within the CollectionService.
 * 
 * @author Frank O'Connor
 *
 */
public abstract class ProductCollection extends Collectable{
	
	protected List<Collectable> childCollections;

	/**
	 * Constructor for ProductCollection
	 * @param collectionId
	 * @param collectionName
	 * @param collectionDescription
	 */
	protected ProductCollection(String collectionId, String collectionName, String collectionDescription) {
		super(collectionId, collectionName, collectionDescription);
		this.childCollections = new ArrayList<Collectable>();
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#removeChild(java.lang.String)
	 */
	@Override
	public Collectable removeChild(String collectionId) {
		// we remove  the child from the graph by removing the reference to it
		// the collection still exists within the collections map for further reassignment
		Iterator<Collectable> iter = this.childCollections.iterator();
		Collectable removed = null;
		while (removed ==null && iter.hasNext()) {
			Collectable child = iter.next();
	        if (child.getCollectionId().equals(collectionId)){
	        	removed = child;
	        	iter.remove();
	        }
	    }

		// returns copy of removedChild if it existed, (like a pop really) otherwise return null
		return removed;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#createIterator()
	 */
	@Override
	public CollectableIterator createIterator() {
		return new ProductCollectionIterator(this);
	}
	
}
