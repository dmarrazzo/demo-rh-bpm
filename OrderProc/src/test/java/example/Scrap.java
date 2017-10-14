package example;

public class Scrap {
	org.kie.api.runtime.process.ProcessContext kcontext;
	example.Order order;

	public void scrap() {
		java.util.List<example.Supplier> suppliers = null;

		//-------------------------------------------------

		kcontext.setVariable("suppliers",order.getSuppliers());

		//-------------------------------------------------

		order.setSuppliers(suppliers);
		kcontext.setVariable("order",order);

		//-------------------------------------------------
		//order.getManagerApproval() == false;

		//-------------------------------------------------
	}
	
	public void scrap2() {
		order = new example.Order();
		java.util.List<example.Supplier> suppliers = new java.util.ArrayList<>();
		example.Supplier supplier = new example.Supplier();
		supplier.setDeliveryDate(new java.util.Date());
		supplier.setOffer(1000.0);
		suppliers.add(supplier);
		order.setSuppliers(suppliers);		
		
		kcontext.setVariable("order",order);
		
	}
}
