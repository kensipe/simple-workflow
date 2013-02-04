package com.mentor.workflow.client;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the externally exposed WorkflowComponent. It provides the current state of
 * an WorkflowComponent.
 *
 * It would be possible to extend this and pass it into the workflow, but it was in tended
 * that entities would contain instances of this which they will pass to the workflow engine.
 *
 * @author: ksipe
 */
public class WorkflowComponent {

    private long id;
    private String status;
    private String workflow;

    private Map<String, String> properties = new HashMap<String, String>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }

    public String getProperty(String property) {
        return properties.get(property);
    }
}
