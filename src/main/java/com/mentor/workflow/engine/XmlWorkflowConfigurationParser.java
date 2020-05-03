package com.mentor.workflow.engine;

import com.mentor.workflow.WorkflowParserUtil;
import com.mentor.workflow.Workflow;

import java.io.File;

/**
 * @author kensipe
 */
public class XmlWorkflowConfigurationParser {

    private static final WorkflowParserUtil parser = new WorkflowParserUtil();  // using older parser...


    public static WorkflowConfiguration getWorkflowConfiguration(String fileName) {

        Workflow workflow =  parser.parseFromFile(fileName);
        return new XmlWorkflowConfiguration(workflow);
    }

    public static WorkflowConfiguration getWorkflowConfiguration(File file) {

        Workflow workflow =  parser.parseFromFile(file);
        return new XmlWorkflowConfiguration(workflow);
    }
}
