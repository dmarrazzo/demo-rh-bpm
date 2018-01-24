package example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import model.Order;
import model.Supplier;

public class LocalTest {
	KieSession ksession;
	private static KieContainer kieContainer = null;

	
	@Before
	public void before() {
		ksession = getKieContainer().getKieBase("orderRules").newKieSession();
	}
	
	@After
	public void after() {
		ksession.dispose();
		if (kieContainer == null) {
			kieContainer.dispose();
		}
	}

	private static synchronized KieContainer getKieContainer() {
		if (kieContainer == null) {
			KieServices kieServices = KieServices.Factory.get();
			kieContainer = kieServices.getKieClasspathContainer();
		}

		return kieContainer;
	}

	
	@Test
	public void test_1() {

		// -------------
		Order order = new Order();
		List<Supplier> suppliers = new ArrayList<>();
		
		Supplier supplier;
		
		supplier = new Supplier();
		supplier.setOffer(1000);		
		supplier.setSelected(true);
		suppliers.add(supplier);
		
		supplier = new Supplier();
		supplier.setOffer(1200);		
		suppliers.add(supplier);

		supplier = new Supplier();
		supplier.setOffer(1100);
		suppliers.add(supplier);

		order.setSuppliers(suppliers);
		
		ksession.insert(order);

		ksession.fireAllRules();

		Collection<FactHandle> factHandles = ksession.getFactHandles();
		for (FactHandle factHandle : factHandles) {
			Object object = ksession.getObject(factHandle);
			System.out.println(object);
		}
	}
	
}
