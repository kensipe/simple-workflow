package com.mentor.workflow.client.workflow

import com.mentor.workflow.client.exception.InvalidWorkflowException
import spock.lang.Specification

/**
 *
 * @author kensipe
 */
class WorkflowSpec extends Specification {

    def "no initial state defined"() {

        given:
        def firstState = new WorkflowState("FirstState")
        def stateSet = new HashSet<WorkflowState>();
        stateSet.add(firstState)

        when:
        new Workflow(stateSet)

        then:
        InvalidWorkflowException e = thrown()
    }

    def "number of actions of good workflow"() {

        def workflow = conjureGoodWorkflow();

        def initState = workflow.getInitialState();

        when:
        def actions = initState.getActions();

        then:
        actions.length == 2
    }

    def "final state of good workflow"() {

        def workflow = conjureGoodWorkflow();

        when:
        def state = workflow.getState("Final");

        then:
        state.type == WorkflowStateType.FINAL
        state.actions.length == 0
    }

    def Workflow conjureGoodWorkflow() {
        /*
            begin - initial state
                2 actions (first, second) -> (FirstState, SecondState)

            FirstState
                1 action (second) -> (SecondState)
            SecondState
                2 actions (first, finished) -> (FirstState, Final)
            Final - final state

         */
        WorkflowState beginState = new WorkflowState("Begin", WorkflowStateType.INITIAL);
        WorkflowState firstState = new WorkflowState("FirstState");
        WorkflowState secondState = new WorkflowState("SecondState");
        WorkflowState finalState = new WorkflowState("Final", WorkflowStateType.FINAL);


        ActionStateMapping firstAction = new ActionStateMapping(new Action("first"), firstState);
        ActionStateMapping secondAction = new ActionStateMapping(new Action("second"), secondState);
        ActionStateMapping finalAction = new ActionStateMapping(new Action("finished"), finalState);

        beginState.addFlow(firstAction);
        beginState.addFlow(secondAction);

        firstState.addFlow(secondAction);

        secondState.addFlow(firstAction);
        secondState.addFlow(finalAction);

        Set<WorkflowState> workflowSet = new HashSet();
        workflowSet.add(beginState);
        workflowSet.add(firstState);
        workflowSet.add(secondState);
        workflowSet.add(finalState);

        Workflow workflow = new Workflow(workflowSet);
        return workflow;
    }
}
