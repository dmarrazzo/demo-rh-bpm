package example;

import java.util.List;

import model.Order;
import model.Supplier;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Scrap {
	org.kie.api.runtime.process.ProcessContext kcontext;
	model.Order order = new Order();
	java.util.List suppliers = new java.util.ArrayList();;

	private void init() {
		model.Supplier supplier = new model.Supplier();
		supplier.setDeliveryDate(new java.util.Date());
		supplier.setOffer(1200.0);
		supplier.setSelected(true);
		suppliers.add(supplier);
		supplier = new model.Supplier();
		supplier.setDeliveryDate(new java.util.Date());
		supplier.setOffer(1000.0);
		suppliers.add(supplier);
		order.setSuppliers(suppliers);
	}

	public void scrap() {
	
		order.setOrderId(kcontext.getProcessInstance().getId());
		kcontext.setVariable("order",order);
		
		//-------------------------------------------------

		kcontext.setVariable("suppliers",order.getSuppliers());

		//-------------------------------------------------

		order.setSuppliers(suppliers);
		kcontext.setVariable("order",order);

		//-------------------------------------------------
		//order.getManagerApproval() == false;

		//-------------------------------------------------
		kcontext.getProcessInstance().getId();
		//-------------------------------------------------
		order.setManagerApproval(new Boolean(true));
		kcontext.setVariable("order",order);
		
	}
	
	public void scrap2() {
		order = new model.Order();
		suppliers = new java.util.ArrayList();
		model.Supplier supplier = new model.Supplier();
		supplier.setDeliveryDate(new java.util.Date());
		supplier.setOffer(1000.0);
		suppliers.add(supplier);
		
		order.setSuppliers((List<Supplier>) suppliers);		
		
		kcontext.setVariable("order",order);

		//-------------------------------------------------
	}
	
	public void scrap3() {
		java.util.List<model.Supplier> suppliersT = (java.util.List<model.Supplier>) suppliers;
		
		double selectedPrice = suppliersT.stream()
								        .filter(s -> s.isSelected())
								        .findFirst()
								        .map(s -> s.getOffer())
								        .orElse(0.0);
		
		order.setPrice(selectedPrice);
		//kcontext.setVariable("order",order);
		System.out.println(order);
	}
	
	public static void main(String[] args) {
		Scrap scrap = new Scrap();
		scrap.init();
		scrap.scrap3();
	}

}
