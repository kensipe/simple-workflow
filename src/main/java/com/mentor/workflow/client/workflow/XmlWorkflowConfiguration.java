package com.mentor.workflow.client.workflow;


/**
 * This class can be used to configure the plugin with workflow specified in a Xml.
 *
 * @author: ksipe
 */
public class XmlWorkflowConfiguration implements WorkflowConfiguration {

    private Workflow workflow;

    public XmlWorkflowConfiguration(String filename) {

        WorkFlowParserUtil parser = new WorkFlowParserUtil();
        workflow = parser.parse(filename);
    }

    public Workflow getWorkflow() {
        return workflow;
    }
}
