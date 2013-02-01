package com.mentor.workflow.client.workflow.engine;

import com.mentor.workflow.client.Opportunity;
import com.mentor.workflow.client.exception.InvalidWorkflowException;
import com.mentor.workflow.client.workflow.Action;
import com.mentor.workflow.client.workflow.Workflow;
import com.mentor.workflow.client.workflow.WorkflowConfiguration;
import com.mentor.workflow.client.workflow.WorkflowState;
import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates all the workflow logic.
 *
 * @author: ksipe
 */
//@Service
public class WorkflowManager  {

    // todo:  create a spring workflow mgr
    static Logger logger = Logger.getLogger(WorkflowManager.class);

//    @Autowired
    private WorkflowEngine workflowEngine;

//    @Autowired
//    private ApplicationContext ctx;

    public void register()  {

        WorkflowConfiguration workflowConfiguration = null; //plugin.getWorkflowConfiguration();
    	if (isWorkflowConfig(workflowConfiguration)) {
            logger.debug("plugin has no workflow configuration");
            return;
        }
        Workflow workflow = workflowConfiguration.getWorkflow();
      
        if(workflow.getInitialState() == null) {

        }
    }
    
    private boolean isWorkflowConfig(WorkflowConfiguration workflowConfiguration) {
        	return workflowConfiguration == null ||  workflowConfiguration.getWorkflow()== null;
	}
		
    public void registerActionHandlers(WorkflowConfiguration config) {

        if (isInvalidWorkflowConfig(config)) {
            logger.debug("plugin has no workflow configuration");
            return;
        }

        Workflow workflow =  config.getWorkflow();

        logger.debug("Plugin: " + config);

        for (WorkflowState state : workflow.getWorkflowStates()) {
            if (state.getTransitionHandler() != null) {
//                ctx.getAutowireCapableBeanFactory().autowireBean(state.getTransitionHandler());
            }
        }
    }
    
    private boolean isInvalidWorkflowConfig(WorkflowConfiguration workflowConfiguration) {
        return workflowConfiguration == null ||  workflowConfiguration.getWorkflow() == null;
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

//        OpportunityPlugin plugin = pluginMan.getPlugin(opportunity.getOppType());
//        validatePlugin(opportunity, plugin);

        WorkflowConfiguration workflowConfiguration = null; //plugin.getWorkflowConfiguration();
        return workflowConfiguration != null ? workflowConfiguration.getWorkflow() : null;
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
