package com.mentor.workflow.client.workflow.engine;

import com.mentor.workflow.client.Opportunity;
import com.mentor.workflow.client.exception.InvalidWorkflowException;
import com.mentor.workflow.client.workflow.Action;
import com.mentor.workflow.client.workflow.ActionHandler;
import com.mentor.workflow.client.workflow.Workflow;
import com.mentor.workflow.client.workflow.WorkflowState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.springframework.stereotype.Service;

/**
 * @author: ksipe
 */
//@Service
public class WorkflowEngine {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<Action> getAvailableActions(Workflow workflow, String currentState) {
        WorkflowState workflowState = workflow.getState(currentState);
        List<Action> actions = null;
        if (workflowState == null) {
            // we are in a bad state... a state that doesn't exist in the workflow
            String message = currentState + " state does not exist in the workflow: " + workflow.getName();
            logger.error(message);
            throw new InvalidWorkflowException(message);
        }
        if (!workflowState.isFinalState()) {
            actions = new ArrayList<Action>(Arrays.asList(workflowState.getActions()));
        } else {
            // we ignore actions if it is the final state
            actions = new ArrayList<Action>();
        }
        return actions;
    }

    public String invokeAction(Workflow workflow, Opportunity opportunity, Action action) {

        String currentState = opportunity.getStatus();
        List<Action> actions = getAvailableActions(workflow, currentState);
        if (!actions.contains(action)) {
            throw new InvalidWorkflowException("invoked action: " + action.getName() + " invalidated for current state");
        }

        WorkflowState workflowState = workflow.getState(currentState);
        if (workflowState.isFinalState()) {
            throw new InvalidWorkflowException("invoked action: " + action.getName() + " on final state not allowed");
        }
        ActionHandler handler = null;
        if (workflow.getState(workflowState.getStateForAction(action).getName()) != null)
            handler = workflow.getState(workflowState.getStateForAction(action).getName()).getTransitionHandler();
        if (handler != null) {

            if (handler.beforeAction(opportunity)) {
                // set state for actionHandler callback (persisted in OpportunityManagerImpl afterwards),
                // so state appears to have changed for api users writing ActionHandlers
                opportunity.setStatus(getNextStateName(workflowState, action));
                handler.onAction(opportunity);
            } else {
                return currentState;
            }
        }

        return getNextStateName(workflowState, action);
    }

    private String getNextStateName(WorkflowState workflowState, Action action) {
        return workflowState.getStateForAction(action).getName();
    }
}
