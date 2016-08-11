package cscie97.asn4.ecommerce.collection;

/**
 * Custom Exception to be thrown if error occurs while parsing line commands from the 
 * collections input csv file.
 * @author Frank O'Connor
 *
 */
public class CollectionImportException extends Exception {

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
	public CollectionImportException(String msg, String line, int lineNum, String filename, Throwable cause) {
		super("ImportException has occured: " + msg
				+ "\nFilename: "+ filename
				+ "\nInput File Line: "+ line
				+ "\nInput File Line Index #: "+ lineNum +"\n", cause);
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
