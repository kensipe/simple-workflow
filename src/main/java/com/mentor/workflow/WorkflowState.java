package com.mentor.workflow;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author: ksipe
 */
public class WorkflowState {

    private WorkflowStateType type = WorkflowStateType.INTERMEDIATE;

    private String name;

    private ActionHandler transitionHandler;

    private final Set<ActionStateMapping> allowedActions = new HashSet<>();

    public WorkflowState(String name) {
        this.name = name;
    }

    public WorkflowState(String name, WorkflowStateType type) {
        this.name = name;
        this.type = type;
    }

    public WorkflowStateType getType() {
        return type;
    }

    public void setType(WorkflowStateType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActionHandler getTransitionHandler() {
        return transitionHandler;
    }

    public void setTransitionHandler(ActionHandler transitionHandler) {
        this.transitionHandler = transitionHandler;
    }

    public void addFlow(ActionStateMapping action) {
        this.allowedActions.add(action);
    }

    public Action[] getActions() {

        Action[] actions = new Action[allowedActions.size()];
        int index = 0;
        for (ActionStateMapping mapping : allowedActions) {
            actions[index++] = mapping.getAction();
        }
        return actions;
    }

    public WorkflowState getStateForAction(Action action) {
        WorkflowState state = null;
        for (ActionStateMapping mapping : allowedActions) {
            if (mapping.getAction().equals(action)) {
                state = mapping.getState();
                break;
            }
        }

        return state;
    }

    public boolean isInitialState() {
        return type == WorkflowStateType.INITIAL;
    }

    public boolean isFinalState() {
        return type == WorkflowStateType.FINAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkflowState state = (WorkflowState) o;

        return Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
