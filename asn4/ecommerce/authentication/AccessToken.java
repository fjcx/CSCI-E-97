package cscie97.asn4.ecommerce.authentication;

import java.util.Calendar;
import java.util.UUID;

/**
 * A token created when a user logs into the system. Associated with a specific user. 
 * When a token is created it is given an expiration time of 30 mins. When a User 
 * logouts all their tokens are set to not active.
 * 
 * @author Frank O'Connor
 *
 */
public class AccessToken {
	
	private UUID tokenId;
	private Calendar expirationTime;
	private boolean isActive;
	
	/**
	 * Constructor of the AccessToken
	 * @param tokenId id of the token
	 * @param expirationTime current expiration time of the token
	 * @param isActive whether the token is still active or not
	 */
	public AccessToken(UUID tokenId, Calendar expirationTime, boolean isActive) {
		this.tokenId = tokenId;
		this.expirationTime = expirationTime;
		this.isActive = isActive;
	}
	
	/**
	 * Gets the id of the token
	 * @return tokenId
	 */
	public UUID getTokenId() {
		return tokenId;
	}

	/**
	 * Sets the id of the token
	 * Note: not a public method. So people cannot modify!
	 * @param tokenId
	 */
	private void setTokenId(UUID tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * Gets the current expiration time of the token
	 * @return expirationTime
	 */
	public Calendar getExpirationTime() {
		return expirationTime;
	}

	/**
	 * Sets the current expiration time of the token
	 * @param expirationTime
	 */
	protected void setExpirationTime(Calendar expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	/**
	 * Refreshes the tokens expiration time
	 * @param expirationTime
	 */
	protected void refreshToken() {
		this.expirationTime.add(Calendar.MINUTE, 30);
	}

	/**
	 * Returns whether the token is active or not
	 * @return isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	
	/**
	 * Sets isActive to false for the token.
	 * Note: only allow public expiration of token (not make it active)
	 */
	public void expire() {
		this.isActive = false;
	}

}
