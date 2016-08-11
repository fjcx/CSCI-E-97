package cscie97.asn4.ecommerce.authentication;

import java.util.UUID;

/**
 * A loginName and hash of the paired password that User may use to login to 
 * the system. When a credential is created the User’s password is hashed and 
 * stored instead of storing in plaintext. 
 * 
 * @author Frank O'Connor
 *
 */
public class Credential {
	
	private UUID credentialId;
	private String loginName;
	private String passwordHash;
	
	/**
	 * Constructor of the credential object
	 * @param credId id of the credential
	 * @param loginName name used by user to login
	 * @param hashedPassword hashed password associated with login name
	 */
	public Credential(UUID credId, String loginName, String hashedPassword) {
		this.credentialId = credId;
		this.loginName = loginName;
		this.passwordHash = hashedPassword;
	}
	
	/**
	 * Gets id of the credential
	 * @return credentialId
	 */
	public UUID getCredentialId() {
		return credentialId;
	}
	
	/**
	 * Sets id of the credential
	 * @param credentialId
	 */
	public void setCredentialId(UUID credentialId) {
		this.credentialId = credentialId;
	}
	
	/**
	 * Gets the loginName of the credential
	 * @return loginName
	 */
	public String getLoginName() {
		return loginName;
	}
	
	/**
	 * Sets the loginName of the credential
	 * @param loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	/**
	 * Gets the hashed password of the credential
	 * @return passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Sets the hashed password of the credential
	 * @param passwordHash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
}
