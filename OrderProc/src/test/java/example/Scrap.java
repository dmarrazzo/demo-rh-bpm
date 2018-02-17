package example;

import java.util.ArrayList;
import java.util.List;

import model.OrderInfo;
import model.SupplierInfo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Scrap {
	org.kie.api.runtime.process.ProcessContext kcontext;
	model.OrderInfo order = new OrderInfo();
	java.util.List suppliers = new java.util.ArrayList();;

	private void init() {
		model.SupplierInfo supplier = new model.SupplierInfo();
		supplier.setDeliveryDate(new java.util.Date());
		supplier.setOffer(1200.0);
		supplier.setSelected(true);
		suppliers.add(supplier);
		supplier = new model.SupplierInfo();
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
		order = new model.OrderInfo();
		suppliers = new java.util.ArrayList();
		model.SupplierInfo supplier = new model.SupplierInfo();
		supplier.setDeliveryDate(new java.util.Date());
		supplier.setOffer(1000.0);
		suppliers.add(supplier);
		
		order.setSuppliers((List<SupplierInfo>) suppliers);		
		
		kcontext.setVariable("order",order);

		//-------------------------------------------------
		List<SupplierInfo> suppliersOut = new ArrayList<>();
		for (int i = 0; i < suppliersOut.size(); i++) {
			if (suppliersOut.get(i) == null) 
				suppliersOut.remove(i);
		}
	}
	
	public void scrap3() {
		java.util.List<model.SupplierInfo> suppliersT = (java.util.List<model.SupplierInfo>) suppliers;
		
		double selectedPrice = suppliersT.stream()
								        .filter(s -> s.getSelected())
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
