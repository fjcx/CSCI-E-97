package cscie97.asn4.ecommerce.authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * The Permission class inherits from the abstract Entitlement class. It represents 
 * a leaf node within the composite pattern structure of the Entitlement. It does not 
 * have any Entitlement children. The class represents whether a User has permission to 
 * perform the specified job function or method call within the mobile application system. 
 * @author Frank O'Connor
 *
 */
public class Permission extends Entitlement {

	/**
	 * Constructor of the Permission class
	 * @param permissionId id of the permission
	 * @param permissionName name of the permission
	 * @param permissionDesc description of the permission
	 */
	public Permission(String permissionId, String permissionName, String permissionDesc) {
		super(permissionId, permissionName, permissionDesc);
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.Entitlement#addChild(cscie97.asn4.ecommerce.authentication.Entitlement)
	 */
	@Override
	public void addChild(Entitlement collectable) throws EntitlementChildException {
		throw new EntitlementChildException("Cannot create child of Permission", "", 0, "", new Exception());
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.Entitlement#getChildren()
	 */
	@Override
	public List<Entitlement> getChildren() {
		// Just return an empty list, do not throw exception
		List<Entitlement> emptyList = new ArrayList<Entitlement>();
		return emptyList;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.Entitlement#createIterator()
	 */
	@Override
	public EntitlementIterator createIterator() {
		// no iterator returned, as we have no children !!
		return null;
	}

}
