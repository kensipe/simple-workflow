package com.mentor.workflow.client.workflow.engine;

import com.mentor.workflow.client.workflow.WorkFlowParserUtil;
import com.mentor.workflow.client.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kensipe
 */
public class XmlWorkflowConfigurationParser {

    public static WorkflowConfiguration getWorkflowConfiguration(String fileName) {

        Workflow workflow =  new WorkFlowParserUtil().parse(fileName);
        WorkflowConfiguration configuration =  new XmlWorkflowConfiguration(workflow);
        return configuration;
    }

    public static void main(String[] args) {

         WorkflowConfiguration configuration =  XmlWorkflowConfigurationParser.getWorkflowConfiguration("example-workflow.xml");
         configuration.getWorkflow();
    }
}
