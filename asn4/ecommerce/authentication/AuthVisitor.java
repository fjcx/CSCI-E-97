package cscie97.asn4.ecommerce.authentication;

/**
 * The AuthVisitor class is an interface which facilitates the use of the 
 * visitor pattern in the AuthenticationSystem. The visit method is overloaded 
 * and depending on what object is passed a different version is called.
 * 
 * @author Frank O'Connor
 *
 */
interface AuthVisitor {
	
	/**
	 * Allows visitation of an AuthenticationService object
	 * @param authService
	 */
	void visit(AuthenticationService authService);
	//void visit(Permission permission);
	//void visit(Role role);
	
	/**
	 * Allows visitation of an Entitlement object
	 * @param entitlement
	 */
	void visit(Entitlement entitlement);
	
	/**
	 * Allows visitation of a Service object
	 * @param service
	 */
	void visit(Service service);
	
	/**
	 * Allows visitation of a User object
	 * @param user
	 */
	void visit(User user);
}
