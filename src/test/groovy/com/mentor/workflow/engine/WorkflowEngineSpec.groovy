package com.mentor.workflow.engine

import com.mentor.workflow.WorkflowComponent
import com.mentor.workflow.exception.InvalidWorkflowException
import com.mentor.workflow.*
import spock.lang.Specification

/**
 *
 * @author kensipe
 */
class WorkflowEngineSpec extends Specification {
    def engine = new WorkflowEngine()
    def workflow
    def states

    def setup() {
        states = new HashSet<WorkflowState>()
        workflow = new Workflow()
    }

    def "available actions throws Exception when state is absent"() {
        states.add(buildState("initial", WorkflowStateType.INITIAL))
        workflow.setWorkflowStates(states)

        when:
        engine.getAvailableActions(workflow, "other")

        then:
        InvalidWorkflowException e = thrown()

    }

    def "available actions is an empty List when final state"() {
        states.add(buildState("initial", WorkflowStateType.INITIAL))
        states.add(buildState("state", WorkflowStateType.FINAL))
        workflow.setWorkflowStates(states)

        when:
        def list = engine.getAvailableActions(workflow, "state")

        then:
        list != null
        list.isEmpty()
    }

    def "exception is throw anytime there is no initial state"() {
        states.add(buildState("state", WorkflowStateType.FINAL))


        when:
        workflow.setWorkflowStates(states)
        def list = engine.getAvailableActions(workflow, "state")

        then:
        InvalidWorkflowException e = thrown()
        list == null
    }

    def "available actions on a happy case"() {
        states.add(buildState("initial", WorkflowStateType.INITIAL))
        states.add(buildStateWithActions("state"))
        workflow.setWorkflowStates(states)

        when:
        def list = engine.getAvailableActions(workflow, "state")

        then:
        list != null
        list.size() == 1
    }

    def "invoke action fails when action is not accessible from current state"() {

        states.add(buildState("initial", WorkflowStateType.INITIAL))
        states.add(buildState("state", WorkflowStateType.FINAL))

        when:
        engine.invokeAction(workflow, new WorkflowComponent(status: "initial"), new Action("other"))

        then:
        InvalidWorkflowException e = thrown()
    }

    // todo:  what to do if the component is null or has a bad state

    def "throws exception on action in final state"() {
        states.add(buildState("initial", WorkflowStateType.INITIAL));
        states.add(buildState("state", WorkflowStateType.FINAL));
        workflow.setWorkflowStates(states);

        when:
        engine.invokeAction(workflow, new WorkflowComponent(status: "initial"), new Action("state"));

        then:
        InvalidWorkflowException e = thrown()
    }

    def "state depends on handler returns"() {
        def handler = Mock(ActionHandler);

        WorkflowState state = buildStateWithActions("state");
        state.setTransitionHandler(handler);
        WorkflowState initial = buildState("initial", WorkflowStateType.INITIAL);
        initial.addFlow(new ActionStateMapping(new Action("try"), state));
        states.add(initial);
        states.add(state);
        workflow.setWorkflowStates(states);

        WorkflowComponent component = new WorkflowComponent();
        component.setStatus(initialState);

        when:
        def stateAfterInvokeAttempt = engine.invokeAction(workflow, component, new Action("try"));

        then:
        stateAfterInvokeAttempt == finalState
        1 * handler.beforeAction(_) >> returnValue

        where:
        initialState | finalState | returnValue
        "initial"    | "initial"  | false
        "initial"    | "state"    | true

    }


    def WorkflowState buildState(String stateName, WorkflowStateType type) {
        return new WorkflowState(stateName, type);
    }

    def buildStateWithActions(String stateName) {
        WorkflowState state = new WorkflowState(stateName);
        WorkflowState nextState = new WorkflowState("nextState");
        ActionStateMapping mapping1 = new ActionStateMapping(new Action("action"), nextState);
        state.addFlow(mapping1);
        return state;
    }
}
