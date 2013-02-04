package com.mentor.workflow.client.workflow.engine;


import com.mentor.workflow.client.workflow.WorkFlowParserUtil;
import com.mentor.workflow.client.workflow.Workflow;

/**
 * This class can be used to configure the plugin with workflow specified in a Xml.
 *
 * @author: ksipe
 */
public class XmlWorkflowConfiguration implements WorkflowConfiguration {

    private Workflow workflow;

    protected XmlWorkflowConfiguration(Workflow workflow) {
        this.workflow = workflow;
    }

    protected void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Workflow getWorkflow() {
        return workflow;
    }
}
