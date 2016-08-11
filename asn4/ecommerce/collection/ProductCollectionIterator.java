package cscie97.asn4.ecommerce.collection;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The ProductCollectionIterator inherits from CollectiableIterator and is a class 
 * for iterating through the items of type ProductCollection. It generalizes the iteration
 * functionality for iterating over StaticCollections or DynamicCollections.
 * Collections are traversed in a depth first search manner.
 * 
 * @author Frank O'Connor
 *
 */
public class ProductCollectionIterator extends CollectableIterator {
	/**
	 * Constructor for ProductCollectionIterator
	 * @param pCollection the parent collection whose children we iterated over.
	 */
	public ProductCollectionIterator (ProductCollection pCollection){
		// initialize the frontier and place expanded nodes (children) within
		this.frontier = new Stack<Collectable>();
		for (Collectable childCollection : pCollection.getChildren()) {
			this.frontier.push(childCollection);
		}
		// keep record of original Stack for ability to reset using reset()
		this.setStartingNode(pCollection);
		
		this.setFirstItem(this.frontier.peek());
		this.visitedList = new ArrayList<Collectable>();
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectableIterator#first()
	 * Resets the Iterator to the first node it started at, and returns that node.
	 */
	@Override
	public Collectable reset() {
		// reset the iterator to the original set
		this.visitedList = new ArrayList<Collectable>();
		
		// re-set the frontier and begin again from our startingNode
		this.frontier = new Stack<Collectable>();
		for (Collectable childCollection : this.getStartingNode().getChildren()) {
			this.frontier.push(childCollection);
		}
		
		this.setFirstItem(this.frontier.peek());
		return this.getFirstItem();
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectableIterator#next()
	 * Moves the iterator pointer to the next item in the collection, and returns that item.
	 */
	@Override
	public Collectable next() {
		Collectable nextItem = null;
		if(hasNext()){
			// remove from nextItem from the Stack
			nextItem = this.frontier.pop();
			// prevent infinite cycles by checking vistedList
			if(!this.visitedList.contains(nextItem)){	
				this.setCurrentItem(nextItem);
				this.visitedList.add(nextItem);
				// expand the node's children, (if it has any) and add them to the frontier
				// for iterating over next
				// Note: We can safely iterate over DynamicCollections and StaticCollections, as getChildren always returns
				// a list of Collectables. In the case of StaticCollection they the static children from childCollections.
				// In the case of DynamicCollections the ProductCatalog API is called and the DynamicCollection getChildren 
				// returns its static collection children childCollections and a list of ProductProxys that match it's searchCriteria.
				for (Collectable childCollection : nextItem.getChildren()) {
					this.frontier.push(childCollection);
				}
			}else{
				// if we previously visited nextItem, then try the one after that in the frontier.
				nextItem = next();	
			}
		}
		return nextItem;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectableIterator#getCurrent()
	 * Gets currentItem the iterator is pointing to.
	 */
	@Override
	public Collectable getCurrent() {
		return getCurrentItem();
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.CollectableIterator#hasNext()
	 * Returns true/false based on whether collections has more nodes to iterate over.
	 */
	@Override
	public boolean hasNext() {
		return (!this.frontier.isEmpty());
	}
}
