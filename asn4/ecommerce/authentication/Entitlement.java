package cscie97.asn4.ecommerce.authentication;

import java.util.List;


/**
 * The Entitlement class is an abstract class which abstracts the functionality of all the 
 * Permission and Role objects that inherit from it. It implements the Composite pattern (along 
 * with Role and Permission). This abstract class defines the main functions for the objects 
 * Role and Permission. This functionality includes adding a child entitlement, removing a child 
 * entitlement, returning all the children of the entitlement and returning an iterator to walk 
 * through all the items within the entitlement.
 * 
 * @author Frank O'Connor
 *
 */
public abstract class Entitlement {
	
	private String entitlementId;
	private String entitlementName;
	private String entitlementDesc;
	
	/**
	 * Constructor for Entitlement class
	 * @param entitlementId id of the new Entitlement
	 * @param entitlementName name of the new Entitlement
	 * @param entitlementDesc description of the new Entitlement
	 */
	protected Entitlement(String entitlementId, String entitlementName, String entitlementDesc) {
		this.entitlementId = entitlementId;
		this.entitlementName = entitlementName;
		this.entitlementDesc = entitlementDesc;
	}
	
	/**
	 * Adds the parameter entitlement as a child to the Entitlement object. Throws a EntitlementChildException 
	 * if we encounter an issue while add the child.
	 * @param entitlement child object to be added to the entitlement
	 * @throws EntitlementChildException
	 */
	public abstract void addChild(Entitlement entitlement) throws EntitlementChildException;
	
	/**
	 * Returns a List of all the children that belong to the Entitlement object. 
	 * @return children of the entitlement
	 */
	public abstract List<Entitlement> getChildren();
	
	/**
	 * Returns a List of all the children that belong to the Entitlement object. 
	 * @return EntitlementIterator
	 */
	public abstract EntitlementIterator createIterator();

	/**
	 * Gets id of the entitlement
	 * @return entitlementId
	 */
	public String getEntitlementId() {
		return entitlementId;
	}

	/**
	 * Sets id of the entitlement
	 * @param entitlementId
	 */
	public void setEntitlementId(String entitlementId) {
		this.entitlementId = entitlementId;
	}

	/**
	 * Gets name of the entitlement
	 * @return entitlementName
	 */
	public String getEntitlementName() {
		return entitlementName;
	}

	/**
	 * Sets name of the entitlement
	 * @param entitlementName
	 */
	public void setEntitlementName(String entitlementName) {
		this.entitlementName = entitlementName;
	}

	/**
	 * Gets description of the entitlement
	 * @return entitlementDesc
	 */
	public String getEntitlementDesc() {
		return entitlementDesc;
	}

	/**
	 * Sets description of the entitlement
	 * @param entitlementDesc
	 */
	public void setEntitlementDesc(String entitlementDesc) {
		this.entitlementDesc = entitlementDesc;
	}
	
	/**
	 * Accepts an object of AuthVisitor.
	 * @param visitor
	 */
	public void acceptVisitor(AuthVisitor visitor){
		visitor.visit(this);
	}

}
