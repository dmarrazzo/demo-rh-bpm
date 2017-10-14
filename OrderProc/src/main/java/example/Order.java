package example;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Order implements java.io.Serializable {

	static final long serialVersionUID = 1L;

	private java.lang.String item;
	private java.util.List<example.Supplier> suppliers;
	private Boolean managerApproval;

	public Order() {
	}
	
	@Override
	public String toString() {
		return String.format("Order [item=%s, suppliers=%s, managerApproval=%s]", item, suppliers, managerApproval);
	}

	public java.lang.String getItem() {
		return this.item;
	}

	public void setItem(java.lang.String item) {
		this.item = item;
	}

	public java.util.List<example.Supplier> getSuppliers() {
		return this.suppliers;
	}

	public void setSuppliers(java.util.List<example.Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public Order(java.lang.String item, java.util.List<example.Supplier> suppliers) {
		this.item = item;
		this.suppliers = suppliers;
	}

	public Boolean getManagerApproval() {
		return managerApproval;
	}

	public void setManagerApproval(Boolean managerApproval) {
		this.managerApproval = managerApproval;
	}
}