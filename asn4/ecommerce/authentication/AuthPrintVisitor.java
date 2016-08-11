package cscie97.asn4.ecommerce.authentication;

/**
 * A concrete implementation of the AuthVisitor interface. It visits whichever object 
 * is passed to it and prints out the details regarding that object type.
 * @author Frank O'Connor
 *
 */
public class AuthPrintVisitor implements AuthVisitor {

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthVisitor#visit(cscie97.asn4.ecommerce.authentication.AuthenticationService)
	 */
	@Override
	public void visit(AuthenticationService authService) {
		authService.showInventory();
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthVisitor#visit(cscie97.asn4.ecommerce.authentication.Service)
	 */
	@Override
	public void visit(Service service) {
		// print service details
		System.out.println("Service id: "+ service.getServiceId());
		System.out.println("Service name: "+ service.getServiceName());
		System.out.println("Service desc: "+ service.getServiceDesc());
		System.out.println("The Services direct Entitlements are: ");
		for(Entitlement entitles :service.getEntitlementList()){
			System.out.println("Entitlement Id:"+ entitles.getEntitlementId());
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthVisitor#visit(cscie97.asn4.ecommerce.authentication.User)
	 */
	@Override
	public void visit(User user) {
		// print user details
		System.out.println("User id: "+ user.getUserId());
		System.out.println("User name: "+ user.getUserName());
		System.out.println("User desc: "+ user.getEntitlements());
		
		// Note: We do not print out the users tokens or credentials for security reasons here !!	
		for(String entitles :user.getEntitlements()){
			System.out.println("Entitlement Id:"+ entitles);
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthVisitor#visit(cscie97.asn4.ecommerce.authentication.Entitlement)
	 */
	@Override
	public void visit(Entitlement entitlement) {
		// print entitlement details
		System.out.println("Entitlement id: "+ entitlement.getEntitlementId());
		System.out.println("Entitlement name: "+ entitlement.getEntitlementName());
		System.out.println("Entitlement desc: "+ entitlement.getEntitlementDesc());
		
		EntitlementIterator eIter = entitlement.createIterator();
		if(eIter!=null){
			System.out.println("Entitlement has the following child entitlements:");
			// navigate through user entitlement trees
			while(eIter.hasNext()){
				Entitlement entItem = eIter.next();
				if(entItem instanceof Role){
					System.out.println("Child Role id: "+ entItem.getEntitlementId());
				}else if(entItem instanceof Permission){
					// is Permission
					System.out.println("Child Permission id: "+ entItem.getEntitlementId());
				}
			}
		}		
	}

}
