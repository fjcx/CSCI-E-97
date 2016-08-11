package cscie97.asn4.ecommerce.authentication;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * The User class represents a registered user of the mobile application store system. 
 * It contains info about the user’s login and credentials, the AccessTokens associated 
 * with the User and the Entitlements the user has been granted.
 * 
 * @author Frank O'Connor
 *
 */
public class User {

	private String userId;
	private String userName;
	private List<Credential> credentials;
	private List<String> entitlements;
	private List<AccessToken> tokens;
	
	/**
	 * Constructor of the User class
	 * @param userId id of the user object
	 * @param userName name of the user object
	 */
	public User(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
		
		this.credentials = new ArrayList<Credential>();
		this.entitlements = new ArrayList<String>();
		this.tokens = new ArrayList<AccessToken>();
	}
	
	/**
	 * Accepts an object of AuthVisitor.
	 * @param visitor
	 */
	public void acceptVisitor(AuthVisitor visitor){
		visitor.visit(this);
	}
	
	/**
	 * Adds a new credential to the user object 
	 * @param loginName login name of the new credential
	 * @param pWord password associated with the login name
	 */
	public void addCredential(String loginName, String pWord) {
		UUID credId = java.util.UUID.randomUUID();
		Credential cred = new Credential(credId, loginName, hashPword(pWord));		
		this.credentials.add(cred);
	}
	
	/**
	 * Gets list of credentials associated the user
	 * @return list of user credentials
	 */
	public List<Credential> getCredentials() {
		return credentials;
	}

	/**
	 * Sets list of credentials associated the user
	 * @param credentials list of user credentials
	 */
	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}

	/**
	 * Gets list of access tokens associated with the user
	 * @return tokens access token list of the user
	 */
	public List<AccessToken> getTokens() {
		return tokens;
	}

	/**
	 * Sets list of access tokens associated with the user
	 * @param tokens
	 */
	public void setTokens(List<AccessToken> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Gets the id associated with the user
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the id for the user
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user name associated with the user
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name associated with the user
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Gets list of entitlements associated with the user
	 * @return entitlements
	 */
	public List<String> getEntitlements() {
		return entitlements;
	}

	/**
	 * Sets list of entitlements associated with the user
	 * @param entitlements
	 */
	public void setEntitlements(List<String> entitlements) {
		this.entitlements = entitlements;
	}

	/**
	 * Adds a new entitlement association to a user
	 * @param entId
	 */
	public void addEntitlement(String entId) {
		// Using strings here, so we can reference authService values !!!
		if(!this.entitlements.contains(entId)){
			this.entitlements.add(entId);
		}else{
			// user already has the entitlement
		}
		
	}
	
	/**
	 * Invalidates all of the users access tokens
	 */
	public void logout() {
		for (AccessToken aToken : this.tokens) {
			aToken.expire();
		}
	}
	
	/**
	 * Validates the login name and password of the user.
	 * Adds a new AccessToken to the user if credentials are valid
	 * @param loginName to be validated
	 * @param pWord to be validated
	 * @return new AccessToken if credentials are valid (null if invalid)
	 */
	public AccessToken validatePassword(String loginName, String pWord) {
		String hashedPWord = hashPword(pWord);
		for (Credential userCred : credentials) {
			if(userCred.getLoginName().equals(loginName)){
				if(userCred.getPasswordHash().equals(hashedPWord)){
					// create new Token 
					return addAccessToken();
				}
			}
		}
		return null;
	}
	
	/**
	 * Creates a new AccessToken and associates it with the user object.
	 * @return new AccessToken (associated with user)
	 */
	private AccessToken addAccessToken(){
		UUID tokenId = java.util.UUID.randomUUID();
		
		Calendar expirTime = Calendar.getInstance();
		// timeout/expiration of 30 min from creation of token
		expirTime.add(Calendar.MINUTE, 30);	
		
		AccessToken userAToken = new AccessToken(tokenId, expirTime, true);
		this.tokens.add(userAToken);
		
		return userAToken;
	}
	
	/**
	 * Hashes the string passed in using md5
	 * @param md5 string to be hashed
	 * @return hashed string output
	 */
	private String hashPword(String pWord) {
		try {
			MessageDigest messDig = MessageDigest.getInstance("MD5");
			byte[] array = messDig.digest(pWord.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			// return hash of the password
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			// this is always MD5, we know it exists, so we should not see this exception
		}
		return null;
	}

}
