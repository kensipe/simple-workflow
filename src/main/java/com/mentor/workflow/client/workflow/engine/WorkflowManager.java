package com.mentor.workflow.client.workflow.engine;

import com.mentor.workflow.client.Opportunity;
import com.mentor.workflow.client.exception.InvalidWorkflowException;
import com.mentor.workflow.client.workflow.Action;
import com.mentor.workflow.client.workflow.Workflow;
import com.mentor.workflow.client.workflow.WorkflowConfiguration;
import com.mentor.workflow.client.workflow.WorkflowState;
import com.mentor.workflow.client.workflow.XmlWorkflowConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;

/**
 * Coordinates all the workflow logic.
 *
 * @author: ksipe
 */
//@Service
public class WorkflowManager {

    // todo:  create a spring workflow mgr
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //    @Autowired
    private WorkflowEngine workflowEngine = new WorkflowEngine();

//    @Autowired
//    private ApplicationContext ctx;

    public void register() {

        // todo:  obviously rewrite!
        WorkflowConfiguration workflowConfiguration = new XmlWorkflowConfiguration("");
        if (isWorkflowConfig(workflowConfiguration)) {
            logger.debug("plugin has no workflow configuration");
            return;
        }
        Workflow workflow = workflowConfiguration.getWorkflow();

        if (workflow.getInitialState() == null) {
            logger.error("no initial state!");
        }
    }

    private boolean isWorkflowConfig(WorkflowConfiguration workflowConfiguration) {
        return workflowConfiguration == null || workflowConfiguration.getWorkflow() == null;
    }

    public void registerActionHandlers(WorkflowConfiguration config) {

        if (isInvalidWorkflowConfig(config)) {
            logger.debug("plugin has no workflow configuration");
            return;
        }

        Workflow workflow = config.getWorkflow();

        logger.debug("Plugin: " + config);

        for (WorkflowState state : workflow.getWorkflowStates()) {
            if (state.getTransitionHandler() != null) {
                logger.debug("dummy stuff because it isn't spring");  // todo:  fix me!
//                ctx.getAutowireCapableBeanFactory().autowireBean(state.getTransitionHandler());
            }
        }
    }

    private boolean isInvalidWorkflowConfig(WorkflowConfiguration workflowConfiguration) {
        return workflowConfiguration == null || workflowConfiguration.getWorkflow() == null;
    }

    public List<Action> getActions(Opportunity opportunity) {

        List<Action> actions;
        Workflow workflow = getWorkflow(opportunity);

        if (workflow != null) {
            actions = workflowEngine.getAvailableActions(workflow, opportunity.getStatus());
        } else {
            logger.error("opp-type: " + opportunity.getOppType() + " requested workflow actions when a workflow has been configured");
            actions = new ArrayList<Action>();
        }

        return actions;
    }

    private Workflow getWorkflow(Opportunity opportunity) {

        logger.debug(opportunity.getStatus());  // todo:  bogus.. rewrite
//        OpportunityPlugin plugin = pluginMan.getPlugin(opportunity.getOppType());
//        validatePlugin(opportunity, plugin);
        // todo:  obviously rewrite this!
        WorkflowConfiguration workflowConfiguration = new XmlWorkflowConfiguration("");
        return workflowConfiguration.getWorkflow();
    }

    public String invokeAction(Opportunity opportunity, Action action) {

        String status = "";
        Workflow workflow = getWorkflow(opportunity);
        if (workflow != null) {
            try {
                status = workflowEngine.invokeAction(workflow, opportunity, action);
            } catch (InvalidWorkflowException e) {
                String message = "plugin: " + opportunity.getOppType() + " " + e.getMessage();
                logger.error(message);
                throw new InvalidWorkflowException(message);
            }

        } else {
            logger.error("opp-type: " + opportunity.getOppType() + " invoked action: " + action.getName());
        }
        return status;
    }
}
