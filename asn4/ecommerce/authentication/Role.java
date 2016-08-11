package cscie97.asn4.ecommerce.authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * The Role class inherits from the abstract Entitlement class. It represents a 
 * collection of Permissions within the AuthenticationService which may represent 
 * a certain job role, or simply as a way of grouping certain Permissions and other Roles.
 * 
 * @author Frank O'Connor
 *
 */
public class Role extends Entitlement {
	
	// Entitlement children of the Role object
	private List<Entitlement> childEntitlements;

	/**
	 * Constructor of the Role class
	 * @param roleId id of the role
	 * @param roleName name of the role
	 * @param roleDesc description of the role
	 */
	public Role(String roleId, String roleName, String roleDesc) {
		super(roleId, roleName, roleDesc);
		this.childEntitlements = new ArrayList<Entitlement>();
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.Entitlement#addChild(cscie97.asn4.ecommerce.authentication.Entitlement)
	 */
	@Override
	public void addChild(Entitlement entitlement) throws EntitlementChildException {
		// check if any child in the direct children has the same id as the new child 
		for (Entitlement existingChild : this.getChildren()) {
			if(existingChild.getEntitlementId().equals(entitlement.getEntitlementId())){
				throw new EntitlementChildException("Child of Entitlement already exists with Id", "", 0, "", new Exception());
			}
		}		
		
		childEntitlements.add(entitlement);
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.Entitlement#getChildren()
	 */
	@Override
	public List<Entitlement> getChildren() {
		return this.childEntitlements;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.Entitlement#createIterator()
	 */
	@Override
	public EntitlementIterator createIterator() {
		return new RoleIterator(this);
	}
}
