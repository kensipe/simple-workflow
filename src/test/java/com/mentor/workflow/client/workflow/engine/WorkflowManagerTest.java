package com.mentor.workflow.client.workflow.engine;

import junit.framework.TestCase;

/**
 * @author: ksipe
 */
public class WorkflowManagerTest extends TestCase {

    private WorkflowManager workflowManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        workflowManager = new WorkflowManager();
//        ReflectionTestUtils.setField(workflowManager, "pluginMan", pluginManager);
//        ReflectionTestUtils.setField(workflowManager, "workflowEngine", new WorkflowEngine());
    }

    public void testFake() {
        assertNotNull(workflowManager);
    }
        /*
         * The build up of this test are in the DummyPlugin... it contains the following workflow:
         *
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

         finalState.addFlow(firstAction);   // this is not allowed and is here for testing


         Set<WorkflowState> workflowSet = new HashSet<WorkflowState>();
         workflowSet.add(beginState);
         workflowSet.add(firstState);
         workflowSet.add(secondState);
         workflowSet.add(finalState);
         */

    // todo:  get these working
//    public void testGetActions() throws Exception {
//
//        WorkflowComponent opportunity = new WorkflowComponent();
//        opportunity.setOppType("DummyPlugin");
//        opportunity.setStatus("Begin");
//
//        List<Action> actions = workflowManager.getActions(opportunity);
//        assertEquals(2, actions.size());
//
//        opportunity.setStatus("FirstState");
//        actions = workflowManager.getActions(opportunity);
//        assertEquals(1, actions.size());
//        assertEquals("second", actions.get(0).getName());
//
//        opportunity.setStatus("SecondState");
//        actions = workflowManager.getActions(opportunity);
//        assertEquals(2, actions.size());
//
//        // testing actions on final state... even though this test configuration
//        // has an action on the final state the engine should indicate there are zero
//        opportunity.setStatus("Final");
//        actions = workflowManager.getActions(opportunity);
//        assertEquals(0, actions.size());
//
//        opportunity.setStatus("BAD-STATUS");
//        try {
//            workflowManager.getActions(opportunity);
//            fail("blah engine should fail for bad state");
//        } catch (InvalidWorkflowException e) {
//            // expected
//            System.out.println("dumbing stuff to pass pmd");
//        }
//
//    }

    // todo:  get these working!!!
//    public void testInvokeAction() throws Exception {
//        WorkflowComponent opportunity = new WorkflowComponent();
//        opportunity.setOppType("DummyPlugin");
//        opportunity.setStatus("Begin");
//        String status = workflowManager.invokeAction(opportunity, new Action("first"));
//        assertEquals("FirstState", status);
//
//        opportunity.setStatus("Begin");
//        status = workflowManager.invokeAction(opportunity, new Action("second"));
//        assertEquals("SecondState", status);
//
//        opportunity.setStatus("BAD-STATE");
//        try {
//            workflowManager.invokeAction(opportunity, new Action("second"));
//            fail("action on an opportunity which is in a bad state (status) should have an exception");
//        } catch (Exception e) {
//            System.out.println("dumbing stuff to pass pmd");
//        }
//
//        opportunity.setStatus("FirstState");
//        status = workflowManager.invokeAction(opportunity, new Action("second"));
//        assertEquals("SecondState", status);
//
//        opportunity.setStatus("SecondState");
//        status = workflowManager.invokeAction(opportunity, new Action("finished"));
//        assertEquals("Final", status);
//
//        opportunity.setStatus("Begin");
//        try {
//            workflowManager.invokeAction(opportunity, new Action("bad-action"));
//            fail("bad action should cause an exception");
//        } catch (Exception e) {
//            System.out.println("dumbing stuff to pass pmd");
//        }
//
//        opportunity.setStatus("Final");
//        try {
//            workflowManager.invokeAction(opportunity, new Action("first"));
//            fail("invoking an action on a final state... even if it is configured should result in an exception");
//        } catch (Exception e) {
//            System.out.println("dumbing stuff to pass pmd");
//        }
//    }
}
