package enterprises.mccollum.wmapp;

/**
 * Created by smccollum on 04.04.17.
 */
public interface AcctInfoReceiver {
	/**
	 * Receive the account info
	 * <p>
	 * If null is passed, the account doesn't exist
	 */
	void refreshTokens();
}
