package com.mentor.workflow.engine

import com.mentor.workflow.exception.InvalidWorkflowException
import com.mentor.workflow.Action
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * 
 * @author kensipe
 */
class XmlWorkflowConfigurationParserSpec extends Specification {

    @Rule final TemporaryFolder temporaryFolder = new TemporaryFolder()

    def "parse workflow with 2 actions on init state, one from common action"() {
       def workflow = """<workflow>

        	<initial-state id="Begin">
                <actions>
                    <action id="first"/>
                    <action id="second" state="SecondState" />
                </actions>
            </initial-state>

            <states>
                <state id="FirstState">
                    <actions>
                        <action id="second" state="SecondState" />
                    </actions>
                </state>

                <state id="SecondState">
                    <actions>
                        <action id="first" state="FirstState"/>
                        <action id="finished" state="Final" />
                    </actions>
                </state>

                <state id="Final" /> <!-- final states can be assumed as states with no actions-->
            </states>

            <!-- maybe even -->
            <common-actions>
                <action id="first" state="FirstState" />
            </common-actions>
        </workflow>
        """
        def workflowFile = file("work1.xml") << workflow

        when:
        def config =  new XmlWorkflowConfigurationParser().getWorkflowConfiguration(workflowFile);

        then:
        config.workflow.initialState.actions.length == 2
        config.workflow.initialState.getStateForAction(new Action("first")).name == "FirstState"

    }

    def "parse workflow with missing handler"() {
        def workflow = """<workflow>

         	<initial-state id="Begin">
                 <actions>
                     <action id="first"/>
                     <action id="second" state="SecondState" />
                 </actions>
             </initial-state>

             <states>
                 <state id="FirstState">
                     <actions>
                         <action id="second" state="SecondState" />
                     </actions>
                 </state>

             <state id="SecondState" handler="com.mentor.workflow.DummyStateChangeHandler">
                     <actions>
                         <action id="first" state="FirstState"/>
                         <action id="finished" state="Final" />
                     </actions>
                 </state>

                 <state id="Final" /> <!-- final states can be assumed as states with no actions-->
             </states>

             <!-- maybe even -->
             <common-actions>
                 <action id="first" state="FirstState" />
             </common-actions>
         </workflow>
         """
         def workflowFile = file("work1.xml") << workflow

         when:
         def config =  new XmlWorkflowConfigurationParser().getWorkflowConfiguration(workflowFile);

         then:
         InvalidWorkflowException e = thrown()
    }

    def "parse with good handler"() {
        def workflow = """<workflow>

         	<initial-state id="Begin">
                 <actions>
                     <action id="first"/>
                     <action id="second" state="SecondState" />
                 </actions>
             </initial-state>

             <states>
                 <state id="FirstState">
                     <actions>
                         <action id="second" state="SecondState" />
                     </actions>
                 </state>

                 <state id="SecondState" handler="com.mentor.workflow.engine.DummyStateChangeHandler">
                     <actions>
                         <action id="first" state="FirstState"/>
                         <action id="finished" state="Final" />
                     </actions>
                 </state>

                 <state id="Final" /> <!-- final states can be assumed as states with no actions-->
             </states>

             <!-- maybe even -->
             <common-actions>
                 <action id="first" state="FirstState" />
             </common-actions>
         </workflow>
         """
         def workflowFile = file("work1.xml") << workflow

         when:
         def config =  new XmlWorkflowConfigurationParser().getWorkflowConfiguration(workflowFile);

         then:
         config.workflow.getState("SecondState").transitionHandler instanceof DummyStateChangeHandler

    }

    File file(String name) {
        def file = new File(temporaryFolder.root, name)
        file.parentFile.mkdirs()
        return file
    }

}
