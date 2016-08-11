package cscie97.asn4.ecommerce.product;

/**
 * The Device class retains details about device
 * 
 * @author Frank O'Connor
 *
 */
public class Device {

	private String deviceId;
	private String deviceName;
	private String manufacturer;
	
	/**
	 * Device class constructor
	 * @param deviceId id of the device
	 * @param deviceName name of the device
	 * @param manufacturer of the device
	 */
	public Device(String deviceId, String deviceName, String manufacturer) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.manufacturer = manufacturer;
	}
	
	/**
	 * Returns device id
	 * @return device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Returns device name
	 * @return device name
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Returns device manufacturer
	 * @return manufacturer
	 */
	public String getManufacturer() {
		return manufacturer;
	}

}
