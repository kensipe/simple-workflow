package com.mentor.workflow.client.workflow;

import com.mentor.workflow.client.exception.InvalidWorkflowException;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author: ksipe
 */
public class WorkflowConfigurationTest extends TestCase {

    public void testGoodWorkflow() throws InvalidWorkflowException {

        Workflow workflow = conjureGoodWorkflow();

        WorkflowState initState = workflow.getInitialState();
        Action[] actions = initState.getActions();
        assertEquals(2, actions.length);
        // can't guarantee order
//        Action[] expectedActions = {new Action("first"), new Action("second")};
//        assertArrayEquals(expectedActions, actions);

        WorkflowState firstState = workflow.getState("FirstState");
        Action[] expectedFirstActions = {new Action("second")};
        actions = firstState.getActions();
        assertArrayEquals(expectedFirstActions, actions);

        WorkflowState finalState = workflow.getState("Final");
        assertEquals(WorkflowStateType.FINAL, finalState.getType());

    }

    public void testNoInitialState() {

        WorkflowState firstState = new WorkflowState("FirstState");
        Set<WorkflowState> stateSet = new HashSet<WorkflowState>();
        stateSet.add(firstState);

        try {
            Workflow workflow = new Workflow(stateSet);
            fail("workflow should fail if there is no initial state defined");
        } catch (Exception e) {

        }

    }

    private Workflow conjureGoodWorkflow() throws InvalidWorkflowException {
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
