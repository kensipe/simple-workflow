package com.mentor.workflow.client.workflow;

import com.mentor.workflow.client.exception.InvalidWorkflowException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: ksipe
 */
public class Workflow {

    private String name;
    private WorkflowState initialState;
    private Map<String, WorkflowState> stateMap = new HashMap<String, WorkflowState>();

    public Workflow() {
    }

    public Workflow(String name) {
        this.name = name;
    }

    public Workflow(Set<WorkflowState> states) {
        initStateMap(states);
    }

    private void initStateMap(Set<WorkflowState> states) {
        for (WorkflowState state : states) {
            stateMap.put(state.getName(), state);
            if (state.getType() == WorkflowStateType.INITIAL) {
                initialState = state;
            }
        }

        if (initialState == null) {
            throw new InvalidWorkflowException("initial workflow state is required");
        }
    }

    public WorkflowState getInitialState() {
        return initialState;
    }

    public WorkflowState getState(String stateName) {
        return stateMap.get(stateName);
    }

    public void setWorkflowStates(Set<WorkflowState> states) {
        initStateMap(states);
    }

    public Collection<WorkflowState> getWorkflowStates() {
        return stateMap.values();
    }

    public String getName() {
        return name;
    }
}
