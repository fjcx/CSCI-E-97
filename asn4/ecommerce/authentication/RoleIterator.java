package cscie97.asn4.ecommerce.authentication;

import java.util.ArrayList;
import java.util.Stack;

/**
 * The RoleIterator inherits from EntitlementeIterator and is a class 
 * for iterating through the items of type Role. It generalizes the iteration
 * functionality for iterating over Roles and Permissions.
 * Entitlements are traversed in a depth first search manner.
 * 
 * @author Frank O'Connor
 *
 */
public class RoleIterator extends EntitlementIterator {
	/**
	 * Constructor for RoleIterator
	 * @param pRole the parent Role whose children we iterated over.
	 */
	public RoleIterator (Role pRole){
		// initialize the frontier and place expanded nodes (children) within
		this.frontier = new Stack<Entitlement>();
		for (Entitlement childEntitlement : pRole.getChildren()) {
			this.frontier.push(childEntitlement);
		}
		// keep record of original Stack for ability to reset using reset()
		this.setStartingNode(pRole);
		
		if(this.frontier.size()>0){
			this.setFirstItem(this.frontier.peek());
		}
		
		this.visitedList = new ArrayList<Entitlement>();
	}


	@Override
	public Entitlement reset() {
		// reset the iterator to the original set
		this.visitedList = new ArrayList<Entitlement>();
		
		// re-set the frontier and begin again from our startingNode
		this.frontier = new Stack<Entitlement>();
		for (Entitlement childEntitlement : this.getStartingNode().getChildren()) {
			this.frontier.push(childEntitlement);
		}
		
		this.setFirstItem(this.frontier.peek());
		return this.getFirstItem();
	}

	@Override
	public Entitlement next() {
		Entitlement nextItem = null;
		if(hasNext()){
			// remove from nextItem from the Stack
			nextItem = this.frontier.pop();
			// prevent infinite cycles by checking vistedList
			if(!this.visitedList.contains(nextItem)){	
				this.setCurrentItem(nextItem);
				this.visitedList.add(nextItem);
				// expand the node's children, (if it has any) and add them to the frontier
				// for iterating over next
				for (Entitlement childEntitlement : nextItem.getChildren()) {
					this.frontier.push(childEntitlement);
				}
			}else{
				// if we previously visited nextItem, then try the one after that in the frontier.
				nextItem = next();	
			}
		}
		return nextItem;
	}


	@Override
	public Entitlement getCurrent() {
		return getCurrentItem();
	}


	@Override
	public boolean hasNext() {
		return (!this.frontier.isEmpty());
	}
}
