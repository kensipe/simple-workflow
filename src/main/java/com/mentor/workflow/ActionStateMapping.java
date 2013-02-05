package com.mentor.workflow;

/**
 * @author: ksipe
 */
public class ActionStateMapping {

    private Action action;
    private WorkflowState state;

    public ActionStateMapping(Action action, WorkflowState state) {
        this.action = action;
        this.state = state;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public WorkflowState getState() {
        return state;
    }

    public void setState(WorkflowState state) {
        this.state = state;
    }
}
