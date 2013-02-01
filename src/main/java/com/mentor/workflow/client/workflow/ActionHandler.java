package com.mentor.workflow.client.workflow;


import com.mentor.workflow.client.WorkflowComponent;

/**
 * @author ksipe
 */
public interface ActionHandler {

    /**
     * A pre-action transition safe guard to determine whether the transition should occur
     *
     * @param component the component for which this workflow applies will be passed in
     * @return a boolean that determines whether the action transition should occur
     */
    boolean beforeAction(WorkflowComponent component);

    /**
     * A callback handler that will be fired on successful transition to this state the component will
     * have the state updated
     *
     * @param component the component for which this workflow applies will be passed in
     */
    void onAction(WorkflowComponent component);
}
