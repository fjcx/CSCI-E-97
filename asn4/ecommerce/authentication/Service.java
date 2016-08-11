package cscie97.asn4.ecommerce.authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * The Service class represents a grouping of all the permissions associated with a 
 * specific service. (e.g. it may contain all the permissions associated with the 
 * ProductCatalog, or the CollectionService)
 * 
 * @author Frank O'Connor
 *
 */
public class Service {

	private String serviceId;
	private String serviceName;
	private String serviceDesc;	
	private List<Entitlement> entitlementList;
	
	/**
	 * Constructor class of the Service object
	 * @param serviceId id associated with the Service
	 * @param serviceName name associated with the Service
	 * @param serviceDesc description associated with the Service
	 */
	public Service(String serviceId, String serviceName, String serviceDesc) {
		this.serviceId = serviceId;
		this.serviceName = serviceName;
		this.serviceDesc = serviceDesc;
		this.entitlementList = new ArrayList<Entitlement>();
	}
	
	/**
	 * Accepts the vistor object
	 * @param visitor
	 */
	public void acceptVisitor(AuthVisitor visitor){
		visitor.visit(this);
	}

	/**
	 * Adds an entitlement association to the Service
	 * @param ent entitlement to be added to the Service
	 */
	public void addEntitlementToService(Entitlement ent) {
		
		if(!this.entitlementList.contains(ent)){
			this.entitlementList.add(ent);
		}else{
			// ??? exception ?? !!!
		}
	}

	/**
	 * Gets the id of the Service
	 * @return serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the id of the Service
	 * @param serviceId
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Gets the name of the Service
	 * @return serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the name of the Service
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the description of the Service
	 * @return serviceDesc
	 */
	public String getServiceDesc() {
		return serviceDesc;
	}

	/**
	 * Sets the description of the Service
	 * @param serviceDesc
	 */
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	/**
	 * Gets the list of entitlements associated with the Service
	 * @return entitlementList
	 */
	public List<Entitlement> getEntitlementList() {
		return entitlementList;
	}

	
	/**
	 * Sets the list of entitlements associated with the Service
	 * @param entitlementList
	 */
	public void setEntitlementList(List<Entitlement> entitlementList) {
		this.entitlementList = entitlementList;
	}

}
