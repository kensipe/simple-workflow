package com.mentor.workflow.engine;

import com.mentor.workflow.WorkflowParserUtil;
import com.mentor.workflow.Workflow;

import java.io.File;

/**
 * @author kensipe
 */
public class XmlWorkflowConfigurationParser {

    private static WorkflowParserUtil parser = new WorkflowParserUtil();  // using older parser...


    public static WorkflowConfiguration getWorkflowConfiguration(String fileName) {

        Workflow workflow =  parser.parseFromFile(fileName);
        WorkflowConfiguration configuration =  new XmlWorkflowConfiguration(workflow);
        return configuration;
    }

    public static WorkflowConfiguration getWorkflowConfiguration(File file) {

        Workflow workflow =  parser.parseFromFile(file);
        WorkflowConfiguration configuration =  new XmlWorkflowConfiguration(workflow);
        return configuration;
    }
}
