package cscie97.asn4.ecommerce.collection;

import java.util.ArrayList;
import java.util.List;

import cscie97.asn4.ecommerce.product.Product;
import cscie97.asn4.ecommerce.product.ProductCatalog;
import cscie97.asn4.ecommerce.product.ProductCatalogImpl;

/**
 * The ProductProxy class inherits from the abstract Collectable class. It represents a leaf node 
 * within a collection. It does not have any Collectable children. The class is a placeholder for 
 * a Product object. 
 * 
 * @author Frank O'Connor
 */
public class ProductProxy extends Collectable {
	
	/**
	 * Constructor of ProductProxy
	 * @param collectionId
	 * @param collectionName
	 * @param collectionDescription
	 */
	public ProductProxy(String collectionId, String collectionName, String collectionDescription) {
		super(collectionId, collectionName, collectionDescription);
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#addChild(cscie97.asn3.ecommerce.collection.Collectable)
	 * This method is overridden as no children can be added to the leaf node.
	 * An Exception is thrown if this is tried.
	 */
	@Override
	public void addChild(Collectable collectable) throws CollectableChildException {
		throw new CollectableChildException("Cannot create child of ProductProxy", "", 0, "", new Exception());
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#removeChild(java.lang.String)
	 * This method is overridden as no children exist in the leaf node.
	 * An Exception is thrown if this is tried.
	 */
	@Override
	public Collectable removeChild(String collectionId) throws CollectableChildException {
		throw new CollectableChildException("ProductProxy does not allow children", "", 0, "", new Exception());
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#getChildren()
	 * This method is overridden as no children exist in the leaf node.
	 * An empty list if returned if this is called.
	 */
	@Override
	public List<Collectable> getChildren() {
		// Just return an empty list, do not throw exception
		List<Collectable> emptyList = new ArrayList<Collectable>();
		return emptyList;
	}

	/* (non-Javadoc)
	 * @see cscie97.asn3.ecommerce.collection.Collectable#createIterator()
	 * This method is overridden as no children exist in the leaf node to iterate over
	 */
	@Override
	public CollectableIterator createIterator(){
		// no iterator returned, as we have no children !!
		return null;
	}

	/**
	 * Makes a call to the ProductCatalog API, and returns a Product with the matching Id.
	 * The collectableId is passed as a parameter to the ProductCatalog and if a Product exists
	 * that matches the Id then it is returned.
	 * @return Product with matching Id.
	 */
	public Product getProduct(){
		// calling singleton instance of productCatalog
		ProductCatalog pCatalog = ProductCatalogImpl.getInstance();
		// getting Product object from ProductCatalog
		return pCatalog.getProductById(this.getCollectionId());
	}
}
