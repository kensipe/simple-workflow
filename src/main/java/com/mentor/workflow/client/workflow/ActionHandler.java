package com.mentor.workflow.client.workflow;


import com.mentor.workflow.client.Opportunity;

/**
 * @author ksipe
 */
public interface ActionHandler {

    /**
     * A pre-action transition safe guard to determine whether the transition should occur
     *
     * @param opportunity the opportunity for which this workflow applies will be passed in
     * @return a boolean that determines whether the action transition should occur
     */
    boolean beforeAction(Opportunity opportunity);

    /**
     * A callback handler that will be fired on successful transition to this state
     *
     * @param opportunity the opportunity for which this workflow applies will be passed in
     */
    void onAction(Opportunity opportunity);
}
