package cscie97.asn4.ecommerce.authentication;

import java.util.UUID;

/**
 * This interface defines the publically available functions for the AuthenticationService. 
 * Functionality made available through the interface includes the abilities to create a new user, 
 * create new permissions, create a new role, create a new service, add an entitlement to an existing user, 
 * add an entitlement to an existing role, add credentials to an existing user, allow a registered user to 
 * login and logout, allow restricted methods to validate an access token, and provides a function to print 
 * out the details of the objects within the AuthenticationService
 * 
 * @author Frank O'Connor
 *
 */
public interface AuthenticationService {
	
	/**
	 * Creates a new service in the AuthenticationService. A service represents a grouping 
	 * of permissions for a particular service (tier of the mobile application system).
	 * @param authId access token id used to validate the user
	 * @param serviceId id of the service to be created
	 * @param serviceName name of the service being created
	 * @param serviceDesc description of the service being created
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void createService(UUID authId, String serviceId, String serviceName, String serviceDesc) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Creates a new Permission in the AuthenticationService. A permission represents the right 
	 * for a user to access a specific function in the system. When a permission is created it is 
	 * also associated with a Service. 
	 * @param authId access token id used to validate the user
	 * @param serviceId id of the service this permission is associated with 
	 * @param permissionId id of the permission being created
	 * @param permissionName name of the permission being created
	 * @param permissionDesc description of the permission being created
	 * @throws EntitlementChildException exception while creating child
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void createPermission(UUID authId, String serviceId, String permissionId, String permissionName, String permissionDesc) throws EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Creates a new Role in the AuthenticationService. A role represents a particular job that 
	 * requires a specific set of permissions. It is a way of grouping permissions, which can then 
	 * be assigned to different users depending on their needs.
	 * @param authId access token id used to validate the user
	 * @param roleId id of the role being created
	 * @param roleName name of the role being created
	 * @param roleDesc description of the role being created
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void createRole(UUID authId, String roleId, String roleName, String roleDesc) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Creates a new User in the AuthenticationService. A user object holds the details for a user 
	 * of the system, their credentials for logging in and what permissions they are associated with. 
	 * @param authId access token id used to validate the user
	 * @param userId id of the user being created
	 * @param userName userName of the user being created
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void createUser(UUID authId, String userId, String userName) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;

	/**
	 * Adds an existing Entitlement in the system to an existing Role within the system.
	 * @param authId access token id used to validate the user
	 * @param roleId id of an existing role
	 * @param entitleId id of an existing entitlement which is being added to the role
	 * @throws EntitlementChildException exception while interacting with child
	 * @throws InvalidReferenceException exception while referencing object 
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void addEntitlementToRole(UUID authId, String roleId, String entitleId) throws EntitlementChildException, InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Adds an existing Entitlement in the system to an existing User within the system.
	 * @param authId access token id used to validate the user
	 * @param userId id of an existing user
	 * @param entitleId id of an existing entitlement which is being added to the user
	 * @throws InvalidReferenceException exception while referencing object
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void addEntitlementToUser(UUID authId, String userId, String entitleId) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;

	/**
	 * Adds a new credential (username and password pair) to an existing User in the system. 
	 * A User may have more than one active credential for logging into the system 
	 * @param authId access token id used to validate the user
	 * @param userId id of an existing user
	 * @param loginName login name to add to the user
	 * @param password password to pair with the login name
	 * @throws InvalidReferenceException exception while referencing object 
	 * @throws AccessDeniedException when user has invalid access  
	 * @throws InvalidAccessTokenException 
	 */
	public void addCredentialToUser(UUID authId, String userId, String loginName, String password) throws InvalidReferenceException, AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Allows the User to login to the system using their valid credentials. Validates the user’s 
	 * credentials are valid and if so creates an AccessToken for the user. The UUID for the AccessToken 
	 * is then returned from this method.
	 * @param loginName a login name associated with the user
	 * @param password the users password associated with the login name
	 * @return UUID of the AccessToken created if the user is validated
	 * @throws AccessDeniedException when user has invalid access  
	 */
	public UUID login(String loginName, String password) throws AccessDeniedException;
	
	/**
	 * Allows the User to logout of the system. Invalidates all of the Users active AccessTokens.
	 * @param loginName a login name associated with the user
	 * @throws InvalidReferenceException exception while referencing object
	 */
	public void logout(String loginName) throws InvalidReferenceException;
	
	/**
	 * Called by restricted methods. This accepts the UUID from the AccessToken of the currently 
	 * logged in user trying to access the restricted method, and the permissionId associated with the 
	 * restricted method. It first confirms that the user’s token is valid and active (and has not expired). 
	 * Then confirms that the User associated with the token has a Permission with the permissionId matching 
	 * that passed. If successful true is returned. If not an Exception is thrown. 
	 * @param authTokenId id of an access token associated with a user
	 * @param permissionId id of the permission required to validate the token id against
	 * @return boolean (whether a user has access or not)
	 * @throws AccessDeniedException when user has invalid access 
	 * @throws InvalidAccessTokenException 
	 */
	public boolean validateAccessToken(UUID authTokenId, String permissionId) throws AccessDeniedException, InvalidAccessTokenException;
	
	/**
	 * Prints out inventory of all Users, Services, Roles and Permissions in the AuthenticationService.
	 */
	public void showInventory();
	
	/**
	 * Accepts an object of AuthVisitor.
	 * @param visitor
	 */
	public void acceptVisitor(AuthVisitor visitor);

}
