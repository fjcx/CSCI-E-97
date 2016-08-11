package cscie97.asn4.ecommerce.product;

/**
 * The Country class retains details about a countries export status
 * 
 * @author Frank O'Connor
 *
 */
public class Country {
	
	private String countryId;	// 2 letter CountryCode
	private String countryName;
	private boolean isExportOpen; // export status
	
	/**
	 * Constructor for Country class
	 * @param countryId 2 letter CountryCode
	 * @param countryName name of country
	 * @param isExportOpen country export status
	 * @throws ImportException if invalid counrtyId
	 */
	public Country(String countryId, String countryName, boolean isExportOpen) throws ImportException {
		// throw exception if invalid countryCode
		if(countryId.length()!=2){
			throw new ImportException("Invalid CountryId", "", 0, "", new Exception());
		}
		
		this.countryId = countryId;
		this.countryName = countryName;
		this.isExportOpen = isExportOpen;
	}
	
	/**
	 * Returns 2 letter countryId
	 * @return 2 letter countryId
	 */
	public String getCountryId() {
		return countryId;
	}

	/**
	 * Returns country name
	 * @return country name
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Returns export status
	 * @return boolean isExportOpen
	 */
	public boolean isExportOpen() {
		return isExportOpen;
	}

}
