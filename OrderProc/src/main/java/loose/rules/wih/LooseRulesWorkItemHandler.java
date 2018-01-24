package loose.rules.wih;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.internal.runtime.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LooseRulesWorkItemHandler implements WorkItemHandler, Cacheable {
	public final static String WORKITEMHANDLER_NAME = "LOOSE_RULES_WIH";
	
    private KieServices kieServices = KieServices.Factory.get();
    private KieCommands commandsFactory = kieServices.getCommands();
    private KieContainer kieContainer;
    private KieScanner kieScanner;

	private Logger log = LoggerFactory.getLogger(getClass());

	private KieBase kieBase;

    public LooseRulesWorkItemHandler(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, -1);
    }
    
	public LooseRulesWorkItemHandler(String groupId, String artifactId, String version, long scannerInterval) {
	    log.debug("About to create KieContainer for {}, {}, {} with scanner interval {}", groupId, artifactId, version, scannerInterval);
        kieContainer = kieServices.newKieContainer(kieServices.newReleaseId(groupId, artifactId, version));
        
        if (scannerInterval > 0) {
            kieScanner = kieServices.newKieScanner(kieContainer);
            kieScanner.start(scannerInterval);
            log.debug("Scanner started for {} with poll interval set to {}", kieContainer, scannerInterval);
        }
	}
	
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		try {
			Map<String, Object> parameters = new HashMap<>(workItem.getParameters());
			
			log.trace("{} executeWorkItem parameters = {}", WORKITEMHANDLER_NAME, parameters);
			
			Map<String, Object> results = new HashMap<>();
			
			parameters.remove("TaskName");
			String kbaseName = (String) parameters.remove("kbaseName");
			
			kieBase = kieContainer.getKieBase(kbaseName);
			
			handleStateless(workItem, parameters, results);
			manager.completeWorkItem(workItem.getId(), results);
		} catch (Exception e) {
			e.printStackTrace();
			manager.abortWorkItem(workItem.getId());
		}
	}

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		log.trace("{} abortWIH", WORKITEMHANDLER_NAME);
		manager.abortWorkItem(workItem.getId());
	}

	protected void handleStateless(WorkItem workItem, Map<String, Object> parameters, Map<String, Object> results) {
		log.debug("Evalating rules in stateless session");

		StatelessKieSession kieSession = kieBase.newStatelessKieSession();
		List<Command<?>> commands = new ArrayList<Command<?>>();

		for (Entry<String, Object> entry : parameters.entrySet()) {
			String inputKey = workItem.getId() + "_" + entry.getKey();

			commands.add(commandsFactory.newInsert(entry.getValue(), inputKey, true, null));
		}
		commands.add(commandsFactory.newFireAllRules("Fired"));
		BatchExecutionCommand executionCommand = commandsFactory.newBatchExecution(commands);
		ExecutionResults executionResults = kieSession.execute(executionCommand);
		log.debug("{} rules fired", executionResults.getValue("Fired"));
		System.out.println("rules fired: " + executionResults.getValue("Fired"));
		for (Entry<String, Object> entry : parameters.entrySet()) {
			String inputKey = workItem.getId() + "_" + entry.getKey();
			String key = entry.getKey().replaceAll(workItem.getId() + "_", "");
			results.put(key, executionResults.getValue(inputKey));
		}
	}

	@Override
	public void close() {
		if (kieScanner != null) {
			kieScanner.shutdown();
			log.debug("Scanner shutdown for kie container {}", kieContainer);
		}
		kieContainer.dispose();
	}
}
