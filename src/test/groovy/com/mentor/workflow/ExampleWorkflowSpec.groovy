package com.mentor.workflow

import com.mentor.workflow.engine.WorkflowManager
import com.mentor.workflow.engine.XmlWorkflowConfigurationParser
import com.mentor.workflow.exception.InvalidWorkflowException
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 *
 * @author kensipe
 */
class ExampleWorkflowSpec extends Specification {
    @Rule
    final TemporaryFolder temporaryFolder = new TemporaryFolder()

    def "parse workflow with 2 actions on init state, one from common action"() {
        def workflowXML = """<?xml version="1.0" encoding="UTF-8"?>
<workflow>
    <initial-state id="init">
        <actions>
            <action id="user-valid" state="validCustomer"/>
            <action id="close" />
        </actions>
    </initial-state>

    <states>
        <state id="validCustomer">
            <actions>
                <action id="billing-valid" state="validBilling" />
                <action id="close" />
            </actions>
        </state>


        <state id="validBilling">
            <actions>
                <action id="aircraft-valid" state="validAircraft"/>
                <action id="close" />
            </actions>
        </state>

        <state id="validAircraft">
            <actions>
                <action id="aircraft-valid" state="validAircraft"/>
                <action id="close" />
                <action id="final" state="Final" />
            </actions>
        </state>



        <state id="Closed" />
        <state id="Final" /> <!-- final states can be assumed as states with no actions-->
    </states>

    <!-- maybe even -->
    <common-actions>
        <action id="close" state="Closed" />
    </common-actions>
</workflow>"""

        def manager = new WorkflowManager()
        def ssaComponent = new WorkflowComponent(id: 123, workflow: "ssa-workflow")


        when:
        def workflowFile = file("ssa-workflow.xml") << workflowXML
        def config = new XmlWorkflowConfigurationParser().getWorkflowConfiguration(workflowFile);
        manager.init(config)
        manager.startWorkflow(ssaComponent)

        then:
        config.workflow.initialState.name == "init"
        config.workflow.initialState.actions.length == 2
        manager.getActions(ssaComponent).size() == 2

        when:
        manager.invokeAction(ssaComponent, new Action(name: "user-valid"))

        then:
        ssaComponent.status == "validCustomer"

        when:
        manager.invokeAction(ssaComponent, new Action(name: "user-valid"))

        then:
        InvalidWorkflowException e = thrown()
    }


    File file(String name) {
        def file = new File(temporaryFolder.root, name)
        file.parentFile.mkdirs()
        return file
    }
}
