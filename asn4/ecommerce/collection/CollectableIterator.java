package cscie97.asn4.ecommerce.collection;

import java.util.List;
import java.util.Stack;

/**
 * The Collectable class is an abstract class from which specific concrete iterators inherit.  
 * An iterator allows the user to traverse through all the Collectable items in the 
 * CollectionService in a depth first search pattern. 
 * 
 * @author Frank O'Connor
 *
 */
public abstract class CollectableIterator { 	// maybe  implements Iterator
	
	// First item the iterator was pointing to
	private Collectable firstItem;
	// Current item the iterator is pointing to
	private Collectable currentItem;
	// List of nodes already visited by the iterator
	protected List<Collectable> visitedList;
	// Stack of the Collectable objects the iterator can move to next
	protected Stack<Collectable> frontier;
	// Record of the Node we began iterating at
	private ProductCollection startingNode;
	
	/**
	 * Moves the iterator pointer to the first item in the collection, and returns that item.
	 * @return first item from the iterator
	 */
	public abstract Collectable reset();
	
	/**
	 * Moves the iterator pointer to the next item in the collection, and returns that item.
	 * @return next item in the collection tree.
	 */
	public abstract Collectable next();
	
	/**
	 * Returns the Colelctable item which the iterator is currently pointing to, this may be 
	 * a ProductCollection or a ProductProxy.
	 * @return current item the iterator is pointing to
	 */
	public abstract Collectable getCurrent();
	
	/**
	 * Returns true/false based on whether a collection has any items left to iterate over.
	 * @return boolean whether a collection had more items to iterate over.
	 */
	public abstract boolean hasNext();
	
	/**
	 * @return list of items previously visited by iterator.
	 */
	public List<Collectable> getVisitedList() {
		return visitedList;
	}

	/**
	 * Sets list of items previously visited by iterator.
	 * @param visitedList
	 */
	public void setVisitedList(List<Collectable> visitedList) {
		this.visitedList = visitedList;
	}

	/**
	 * @return current item the iterator points to.
	 */
	protected Collectable getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item the iterator points to.
	 * @param currentItem
	 */
	protected void setCurrentItem(Collectable currentItem) {
		this.currentItem = currentItem;
	}

	/**
	 * @return node we began iterating from.
	 */
	protected ProductCollection getStartingNode() {
		return startingNode;
	}

	/**
	 * Sets the node we began iterating from.
	 * @param pCollection
	 */
	protected void setStartingNode(ProductCollection pCollection) {
		this.startingNode = pCollection;
	}

	/**
	 * @return first Item in the Stack at instantiation of the iterator.
	 */
	protected Collectable getFirstItem() {
		return firstItem;
	}

	/**
	 * Record first Item in the Stack at instantiation of the iterator.
	 * @param firstItem
	 */
	protected void setFirstItem(Collectable firstItem) {
		this.firstItem = firstItem;
	}
	
	
}
