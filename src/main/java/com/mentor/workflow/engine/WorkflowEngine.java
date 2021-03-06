package com.mentor.workflow.engine;

import com.mentor.workflow.WorkflowComponent;
import com.mentor.workflow.exception.InvalidWorkflowException;
import com.mentor.workflow.Action;
import com.mentor.workflow.ActionHandler;
import com.mentor.workflow.Workflow;
import com.mentor.workflow.WorkflowState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import org.springframework.stereotype.Service;

/**
 * @author: ksipe
 */
public class WorkflowEngine {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<Action> getAvailableActions(Workflow workflow, WorkflowComponent component) {
        return getAvailableActions(workflow,component.getStatus());
    }

    public List<Action> getAvailableActions(Workflow workflow, String currentState) {

        WorkflowState workflowState =  currentState == null? workflow.getInitialState() : workflow.getState(currentState);
        List<Action> actions;
        if (workflowState == null) {
            // we are in a bad state... a state that doesn't exist in the workflow
            String message = currentState + " state does not exist in the workflow: " + workflow.getName();
            logger.error(message);
            throw new InvalidWorkflowException(message);
        }
        if (!workflowState.isFinalState()) {
            actions = new ArrayList<>(Arrays.asList(workflowState.getActions()));
        } else {
            // we ignore actions if it is the final state
            actions = new ArrayList<>();
        }
        return actions;
    }

    public String invokeAction(Workflow workflow, WorkflowComponent component, Action action) {

        String currentState = component.getStatus();
        if (!isValidAction(workflow, currentState, action)) {
            throw new InvalidWorkflowException("invoked action: " + action.getName() + " invalidated for state: " + currentState);
        }

        WorkflowState workflowState = getWorkflowState(workflow, action, currentState);
        ActionHandler handler = getActionHandler(workflow, action, workflowState);

        if (continueAfterBeforeAction(handler, component)) {
            currentState = getNextStateName(workflowState, action);
            component.setStatus(currentState);
            invokeHandlerOnAction(component, handler, action);
        }

        return currentState;
    }

    private boolean isValidAction(Workflow workflow, String currentState, Action action) {
        List<Action> actions = getAvailableActions(workflow, currentState);
        return actions.contains(action);
    }

    private WorkflowState getWorkflowState(Workflow workflow, Action action, String currentState) {
        WorkflowState workflowState = workflow.getState(currentState);
        if (workflowState.isFinalState()) {
            throw new InvalidWorkflowException("invoked action: " + action.getName() + " on final state not allowed");
        }
        return workflowState;
    }

    private ActionHandler getActionHandler(Workflow workflow, Action action, WorkflowState workflowState) {
        ActionHandler handler = null;
        if (workflow.getState(workflowState.getStateForAction(action).getName()) != null) {
            handler = workflow.getState(workflowState.getStateForAction(action).getName()).getTransitionHandler();
        }
        // consider a list of action handlers... that would be better
        return handler;
    }

    private void invokeHandlerOnAction(WorkflowComponent component, ActionHandler handler, Action action) {
        if (handler != null) {
            try {
                handler.onAction(action, component);
            } catch (Exception e) {
                logger.error("error on handler.onAction {}", handler.getClass(), e);
            }
        }
    }

    private boolean continueAfterBeforeAction(ActionHandler handler, WorkflowComponent component) {
        boolean continueWorkflow = true;
        if (handler != null) {
            try {
                continueWorkflow = handler.beforeAction(component);
            } catch (Exception e) {
                logger.error("handler failure: {}", handler.getClass(), e);
                continueWorkflow = false;
            }
        }
        return continueWorkflow;
    }

    private String getNextStateName(WorkflowState workflowState, Action action) {
        return workflowState.getStateForAction(action).getName();
    }
}
