package cscie97.asn4.ecommerce.authentication;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Singleton Concrete Implementation class of AuthenticationService. AuthenticationServiceImpl 
 * stores a map of all the Users, Entitlements and Service created for authentication purposes. 
 * 
 * @author Frank O'Connor
 *
 */
public class AuthenticationServiceImpl implements AuthenticationService {
	
	// single instance of AuthenticationService
	private static AuthenticationService instance = null;
	
	// Map containing all entitlements
	private Map<String, Entitlement> entitlementMap;
	// map containing entitlements per service
	private Map<String, Service> serviceMap;
	// map containing all users
	private Map<String, User> userMap;
	
	/**
	 * Constructor for AuthenticationServiceImpl.
	 * CollectionServiceImpl is a singleton, hence the constructor is private
	 * which prevents instantiation from classes other than getInstance
	 */
	private AuthenticationServiceImpl() {
		this.entitlementMap = new HashMap<String, Entitlement>();
		this.serviceMap = new HashMap<String, Service>();
		this.userMap = new HashMap<String, User>();
		
		bootstrapAccess();
	}
	
	/**
	 * Bootstraps the auth system by adding an initial admin user with root access
	 */
	private void bootstrapAccess(){
		// Bootstrap access accounts
		// create root user:
		User user = new User("system_admin", "Sys Admin");
		this.userMap.put("system_admin", user);
		this.userMap.get("system_admin").addCredential("sysAdmin", "rOotAcCeSsPwd");
		// root collection of collections graph
		this.entitlementMap.put("root_access", new Role("system_admin", "system_admin", "root_level_entitlement"));
		this.userMap.get("system_admin").addEntitlement("root_access");
	}
	
	/**
	 * Return instance of AuthenticationService, or creates one if not already created
	 * using synchronized to ensure thread safety
	 * @return singleton instance of AuthenticationService.
	 */
	public static synchronized AuthenticationService getInstance(){
		if (instance == null) {
            instance = new AuthenticationServiceImpl();
	    }
	    return instance;
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#createService(java.util.UUID, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createService(UUID authId, String serviceId, String serviceName, String serviceDesc) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "define_service")){
			if(!serviceMap.containsKey(serviceId)){
				Service service = new Service(serviceId, serviceName, serviceDesc);
				serviceMap.put(serviceId, service);
			}else{
				// Service already exists
				throw new InvalidReferenceException("Service with specified id already exists", "", 0, "", new Exception());
			}
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#createPermission(java.util.UUID, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createPermission(UUID authId, String serviceId, String permissionId, String permissionName, String permissionDesc) throws EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "define_permission")){
			if(!this.entitlementMap.containsKey(permissionId)){
				if(this.serviceMap.containsKey(serviceId)){
					Entitlement permission = new Permission(permissionId, permissionName, permissionDesc);
					this.entitlementMap.put(permissionId, permission);	// do we even add it to permission map ???
					
					this.serviceMap.get(serviceId).addEntitlementToService(this.entitlementMap.get(permissionId)); // !!!!!! make sure this actually updates the service !!!!!!
					// also add to root access
					this.entitlementMap.get("root_access").addChild(this.entitlementMap.get(permissionId));
					
				}else{
					// Service does not already exist
					throw new InvalidReferenceException("Service with specified id does not exist", "", 0, "", new Exception());
				}
			}else{
				// Entitlement already exists
				throw new InvalidReferenceException("Entitlement with specified id already exists", "", 0, "", new Exception());
			}
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#createRole(java.util.UUID, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createRole(UUID authId, String roleId, String roleName, String roleDesc) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "define_role")){
			if(!this.entitlementMap.containsKey(roleId)){
				Entitlement role = new Role(roleId, roleName, roleDesc);
				this.entitlementMap.put(roleId, role);
			}else{
				// Role already exists
				throw new InvalidReferenceException("Role with specified id already exists", "", 0, "", new Exception());
			}
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#createUser(java.util.UUID, java.lang.String, java.lang.String)
	 */
	@Override
	public void createUser(UUID authId, String userId, String userName) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "create_user")){
			if(!this.userMap.containsKey(userId)){
				User user = new User(userId, userName);
				this.userMap.put(userId, user);
			}else{
				// User already exists
				throw new InvalidReferenceException("User with specified id already exists", "", 0, "", new Exception());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#addEntitlementToRole(java.util.UUID, java.lang.String, java.lang.String)
	 */
	@Override
	public void addEntitlementToRole(UUID authId, String roleId, String entitleId) throws EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "add_entitlement")){
			if(this.entitlementMap.containsKey(roleId)){
				if(this.entitlementMap.containsKey(entitleId)){
					this.entitlementMap.get(roleId).addChild(this.entitlementMap.get(entitleId));
				}else{
					// Entitlement does not exist
					throw new InvalidReferenceException("Entitlement with specified id does not exist", "", 0, "", new Exception());
				}
			}else{
				// Role does not exist
				throw new InvalidReferenceException("Role with specified id does not exist", "", 0, "", new Exception());
			}
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#addEntitlementToUser(java.util.UUID, java.lang.String, java.lang.String)
	 */
	@Override
	public void addEntitlementToUser(UUID authId, String userId, String entitleId) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "add_entitlement_to_user")){
			if(this.userMap.containsKey(userId)){
				if(this.entitlementMap.containsKey(entitleId)){
					this.userMap.get(userId).addEntitlement(entitleId);
				}else{
					// Entitlement does not exist
					throw new InvalidReferenceException("Entitlement with specified id does not exist", "", 0, "", new Exception());
				}
			}else{
				// User does not exist
				throw new InvalidReferenceException("User with specified id does not exist", "", 0, "", new Exception());
			}
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#addCredentialToUser(java.util.UUID, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addCredentialToUser(UUID authId, String userId, String loginName, String password) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException {
		// Checking user has correct authentication
		if(validateAccessToken(authId, "add_credential_to_user")){
			// Confirm no user currently has that login already
			for (User user : this.userMap.values()) {
				for (Credential existingLogin : user.getCredentials()) {
					if(loginName.equals(existingLogin.getLoginName())){
						// User already has this loginName
						throw new InvalidReferenceException("Specified loginName already in use.", "", 0, "", new Exception());
					}
				}
			}
			
			if(this.userMap.containsKey(userId)){
				this.userMap.get(userId).addCredential(loginName,password);
			}else{
				// User does not exist
				throw new InvalidReferenceException("User with specified id does not exist", "", 0, "", new Exception());
			}
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public UUID login(String loginName, String password) throws AccessDeniedException {
		boolean foundLoginName = false;
		for (User user  : this.userMap.values()) {
			for(Credential userCred : user.getCredentials()){
				if(userCred.getLoginName().equals(loginName)){
					foundLoginName = true;
					AccessToken token = user.validatePassword(loginName, password);
					if(token == null){
						// Password is incorrect (Note: we do not specify whether it is the password or username that is incorrect for security practice)
						throw new AccessDeniedException("Incorrect username/password combination", "", 0, "", new Exception());
					}else{
						return token.getTokenId();
					}
				}
			}
		}
		
		if(!foundLoginName){
			// Login name does not exist (Note: we do not specify whether it is the password or username that is incorrect for security practice)
			throw new AccessDeniedException("Incorrect username/password combination", "", 0, "", new Exception());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#logout(java.lang.String)
	 */
	@Override
	public void logout(String loginName) throws InvalidReferenceException {	// maybe should use user name ???? !!!
		boolean foundLoginName = false;
		// Logouts for ALL of the users Credentials and Tokens ! (this seems like better practice than just logging out one)
		for (User user : this.userMap.values()) {
			for(Credential userCred : user.getCredentials()){
				if(userCred.getLoginName().equals(loginName)){
					foundLoginName = true;
					user.logout();
				}
			}
		}
		
		if(!foundLoginName){
			// wrong login name specified in logout
			throw new InvalidReferenceException("LoginName not found", "", 0, "", new Exception());
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#validateAccessToken(java.util.UUID, java.lang.String)
	 */
	@Override
	public boolean validateAccessToken(UUID tokenId, String permissionId) throws AccessDeniedException, InvalidAccessTokenException {
		boolean isValid = false;
		// find user with token
		for (User user : this.userMap.values()) {
			for(AccessToken userToken : user.getTokens()){
				if(userToken.getTokenId().equals(tokenId)){
					// confirm token is valid
					if(!userToken.isActive()){
						// Token is not active
						isValid = false;
						throw new InvalidAccessTokenException("AccessToken is no longer active", "", 0, "", new Exception());
					}else if(userToken.getExpirationTime().before(Calendar.getInstance())) {
						// Token is expired
						isValid = false;
						throw new InvalidAccessTokenException("AccessToken is expired", "", 0, "", new Exception());
					}else{
						// add 30 min to the token when validated
						userToken.refreshToken();
						if(user.getUserId().equals("system_admin")){
							// root user for bootstrapping
							return true;
						}else{
							// confirm user has permissions for the method in question
							for (String userEntitlement : user.getEntitlements()) {
								Entitlement userEnt = this.entitlementMap.get(userEntitlement);
								if(userEnt!=null){
									EntitlementIterator eIter = userEnt.createIterator();
									if(eIter!=null){
										// navigate through user entitlement trees
										while(eIter.hasNext()){
											Entitlement entItem = eIter.next();
											if(entItem instanceof Role){
												// keep going, only match on permission, not role
											}else if(entItem instanceof Permission){
												// is Permission
												if(entItem.getEntitlementId().equals(permissionId)){
													return true;
												}
											}
										}
									}else{
										// is Permission
										if(userEnt.getEntitlementId() == permissionId){
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(!isValid){
			throw new AccessDeniedException("User does not have access to this restricted method.", "", 0, "", new Exception());
		}
		return isValid;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#showInventory()
	 */
	@Override
	public void showInventory() {
		AuthVisitor printVisitor = new AuthPrintVisitor();
		// iterating through users and services here, as if we made the user classes public to the visitor then anyone could potentially look 
		// through all the users token info.
		System.out.println("\nAuthentication Service:");
		System.out.println("\n--Users in AuthService--");
		for(User user: this.userMap.values()){
			user.acceptVisitor(printVisitor);
			System.out.print("\n");
		}

		System.out.println("\n--Services in AuthService--");
		for(Service service: this.serviceMap.values()){
			service.acceptVisitor(printVisitor);
			System.out.print("\n");
		}

		System.out.println("\n--Entitlements in AuthService--");
		for(Entitlement entitle: this.entitlementMap.values()){
			entitle.acceptVisitor(printVisitor);
			System.out.print("\n");
		}
	}

	/* (non-Javadoc)
	 * @see cscie97.asn4.ecommerce.authentication.AuthenticationService#acceptVisitor(cscie97.asn4.ecommerce.authentication.AuthVisitor)
	 */
	@Override
	public void acceptVisitor(AuthVisitor visitor){
		visitor.visit(this);
	}

}
