package cscie97.asn4.ecommerce.authentication;

/**
 * Custom Exception to be thrown if exception occurs while creating a Entitlement object.
 * @author Frank O'Connor
 *
 */
public class EntitlementChildException extends Exception {

	private static final long serialVersionUID = 1L;
	private String lineWhereFailed;
	private int lineIndexWhereFailed;
	private String filename;
	private String reason;

	/**
	 * @param msg the Exception msg
	 * @param line the current line being read
	 * @param lineNum the current line number
	 * @param filename the name of the file being read
	 * @param cause the originating Exception
	 */
	public EntitlementChildException(String msg, String line, int lineNum, String filename, Throwable cause) {
		super("EntitlementChildException has occured: " + msg, cause);
		// allowing for variables to be passed regarding csv file importing
		this.lineWhereFailed = line;
		this.lineIndexWhereFailed = lineNum;
		this.filename = filename;
		this.reason = msg;
	}
	
	public String getLineWhereFailed() {
		return lineWhereFailed;
	}

	public void setLineWhereFailed(String lineWhereFailed) {
		this.lineWhereFailed = lineWhereFailed;
	}

	public int getLineIndexWhereFailed() {
		return lineIndexWhereFailed;
	}

	public void setLineIndexWhereFailed(int lineIndexWhereFailed) {
		this.lineIndexWhereFailed = lineIndexWhereFailed;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
