package cscie97.asn4.ecommerce.authentication;

import java.util.List;
import java.util.Stack;

/**
 * The Entitlement class is an abstract class from which specific concrete iterators inherit.  
 * An iterator allows the user to traverse through all the Entitlement items in the 
 * AuthenticationService in a depth first search pattern. 
 * 
 * @author Frank O'Connor
 *
 */
public abstract class EntitlementIterator { 	// maybe  implements Iterator
	
	// First item the iterator was pointing to
	private Entitlement firstItem;
	// Current item the iterator is pointing to
	private Entitlement currentItem;
	// List of nodes already visited by the iterator
	protected List<Entitlement> visitedList;
	// Stack of the Entitlement objects the iterator can move to next
	protected Stack<Entitlement> frontier;
	// Record of the Node we began iterating at
	private Role startingNode;
	
	/**
	 * Moves the iterator pointer to the first item in the collection of entitlements, and returns that item.
	 * @return first item from the iterator
	 */
	public abstract Entitlement reset();
	
	/**
	 * Moves the iterator pointer to the next item in the collection of entitlements, and returns that item.
	 * @return next item in the entitlement tree.
	 */
	public abstract Entitlement next();
	
	/**
	 * Returns the Entitlement item which the iterator is currently pointing to, this may be 
	 * a Role or a Permission.
	 * @return current item the iterator is pointing to
	 */
	public abstract Entitlement getCurrent();
	
	/**
	 * Returns true/false based on whether an entitlement has any items left to iterate over.
	 * @return boolean whether an entitlement had more items to iterate over.
	 */
	public abstract boolean hasNext();
	
	/**
	 * @return list of items previously visited by iterator.
	 */
	public List<Entitlement> getVisitedList() {
		return visitedList;
	}

	/**
	 * Sets list of items previously visited by iterator.
	 * @param visitedList
	 */
	public void setVisitedList(List<Entitlement> visitedList) {
		this.visitedList = visitedList;
	}

	/**
	 * @return current item the iterator points to.
	 */
	protected Entitlement getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the current item the iterator points to.
	 * @param currentItem
	 */
	protected void setCurrentItem(Entitlement currentItem) {
		this.currentItem = currentItem;
	}

	/**
	 * @return node we began iterating from.
	 */
	protected Role getStartingNode() {
		return startingNode;
	}

	/**
	 * Sets the node we began iterating from.
	 * @param startRole
	 */
	protected void setStartingNode(Role startRole) {
		this.startingNode = startRole;
	}

	/**
	 * @return first Item in the Stack at instantiation of the iterator.
	 */
	protected Entitlement getFirstItem() {
		return firstItem;
	}

	/**
	 * Record first Item in the Stack at instantiation of the iterator.
	 * @param firstItem
	 */
	protected void setFirstItem(Entitlement firstItem) {
		this.firstItem = firstItem;
	}
	
	
}
