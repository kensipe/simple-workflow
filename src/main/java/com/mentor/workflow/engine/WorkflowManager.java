package com.mentor.workflow.engine;

import com.mentor.workflow.Action;
import com.mentor.workflow.Workflow;
import com.mentor.workflow.WorkflowComponent;
import com.mentor.workflow.exception.InvalidWorkflowException;
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
    // cache of workflow... assumes 1 workflow for all components managed under the manager
    private WorkflowConfiguration configuration;


    public void init(WorkflowConfiguration configuration) {
        this.configuration = configuration;

        Workflow workflow = configuration.getWorkflow();
        if (workflow.getInitialState() == null) {
            logger.error("no initial state!");
        }
    }

    public void startWorkflow(WorkflowComponent component) {
        // there may a desire to have an action handler here... or other init stuff of a workflow
        component.setStatus(getWorkflow(component).getInitialState().getName());
    }

    private boolean isInvalidWorkflowConfig(WorkflowConfiguration workflowConfiguration) {
        return workflowConfiguration == null || workflowConfiguration.getWorkflow() == null;
    }

    public List<Action> getActions(WorkflowComponent component) {

        List<Action> actions;
        Workflow workflow = getWorkflow(component);

        if (workflow != null) {
            actions = workflowEngine.getAvailableActions(workflow, component.getStatus());
        } else {
            logger.error("Managed component requested workflow actions when a workflow has been configured");
            actions = new ArrayList<Action>();
        }

        return actions;
    }

    private Workflow getWorkflow(WorkflowComponent component) {

        // this might allow a component to know the workflow it is associated with and pull the right one
        logger.debug("component: {} with status: {} needs workflow: {}", component.getId(), component.getStatus(), configuration.getWorkflow().getName());

        return configuration.getWorkflow();
    }

    public String invokeAction(WorkflowComponent component, Action action) {

        String status = "";
        Workflow workflow = getWorkflow(component);
        if (workflow != null) {
            try {
                status = workflowEngine.invokeAction(workflow, component, action);
            } catch (InvalidWorkflowException e) {
                String message = e.getMessage(); // improve
                logger.error(message);
                throw new InvalidWorkflowException(message);
            }

        } else {
            logger.error("workflow component: {} invoked action: {}", component.getId(), action.getName());
        }
        return status;
    }
}
