package cscie97.asn4.ecommerce.product;

/**
 * Custom Exception to be thrown if error occurs while querying.
 * @author Frank O'Connor
 *
 */
public class QueryProcessorException extends Exception {
	private String lineWhereFailed;
	private int lineIndexWhereFailed;
	private String filename;

	/**
	 * @param msg the Exception msg
	 * @param line the current line being read
	 * @param lineNum the current line number
	 * @param filename the name of the file being read
	 * @param cause the originating Exception
	 */
	public QueryProcessorException(String msg,String line, int lineNum, String filename, Throwable cause) {
		super("QueryEngineException has occured: " + msg
				+ "\nFilename: "+ filename
				+ "\nQuery File Line: "+ line
				+ "\nQuery File Line Index #: "+ lineNum +"\n", cause);
		this.lineWhereFailed = line;
		this.lineIndexWhereFailed = lineNum;
		this.filename = filename;
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

}
