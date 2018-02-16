package example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import model.OrderInfo;
import model.SupplierInfo;

public class LocalTest {
	KieSession ksession;
	private static KieContainer kieContainer = null;

	
	@Before
	public void before() {
		ksession = getKieContainer().getKieBase("kbase").newKieSession();
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
			//kieContainer = kieServices.getKieClasspathContainer();
			kieContainer = kieServices.newKieContainer(kieServices.newReleaseId("example", "OrderRules", "1.0-SNAPSHOT"));
		}

		return kieContainer;
	}

	
	//@Test
	public void test_1() {

		// -------------
		OrderInfo order = new OrderInfo();
		order.setCategory("basic");
		
		List<SupplierInfo> suppliers = new ArrayList<>();
		
		SupplierInfo supplier;
		
		supplier = new SupplierInfo();
		supplier.setOffer(1000);		
		supplier.setSelected(true);
		suppliers.add(supplier);
		
		supplier = new SupplierInfo();
		supplier.setOffer(1200);		
		suppliers.add(supplier);

		supplier = new SupplierInfo();
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
	
	//Order [orderId=52, item=Laptop Dell XPS 15, suppliers=
	//         [SupplierInfo [user=supplier1, offer=100.0, deliveryDate=Wed Jan 24 00:00:00 CET 2018, selected=true]]
	//, price=100.0, managerApproval=false, rejectionReason=null, category=basic, urgency=low];
	@Test
	public void test_2() {

		// -------------
		OrderInfo order = new OrderInfo(52,"Laptop Dell XPS 15",null,1000,false,null,"basic","low");
		
		List<SupplierInfo> suppliers = new ArrayList<>();
		
		SupplierInfo supplier;
		
		supplier = new SupplierInfo();
		supplier.setUser("supplier1");
		supplier.setOffer(100);
		supplier.setDeliveryDate(new Date());
		supplier.setSelected(true);
		suppliers.add(supplier);
		
		order.setSuppliers(suppliers);
		
		KieServices kieServices = KieServices.Factory.get();
		
		KieCommands commandsFactory = kieServices.getCommands();
		List<Command<?>> commands = new ArrayList<Command<?>>();

		commands.add(commandsFactory.newInsert(order,"fact", true, null));
		commands.add(commandsFactory.newFireAllRules("Fired"));
		BatchExecutionCommand executionCommand = commandsFactory.newBatchExecution(commands);
		ExecutionResults executionResults = ksession.execute(executionCommand);
		System.out.println("rules fired: " + executionResults.getValue("Fired"));


		Collection<FactHandle> factHandles = ksession.getFactHandles();
		for (FactHandle factHandle : factHandles) {
			Object object = ksession.getObject(factHandle);
			System.out.println(object);
		}
	}
	
}
