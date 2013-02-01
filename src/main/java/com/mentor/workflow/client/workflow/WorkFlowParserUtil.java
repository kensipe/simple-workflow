package com.mentor.workflow.client.workflow;

import com.mentor.workflow.client.exception.InvalidWorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import org.springframework.core.io.ClassPathResource;

/**
 * Util class used by XmlWorkflowConfiguration class to generate workflow by validating & parsing user specified XML.
 *
 * @author ksipe
 */

public class WorkFlowParserUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // todo:  convert to xstream

    //    todo(kgs): schema
    private static final String SCHEMA_LOCATION = "/Workflow.xsd";

    public Workflow parse(String filename) {

//        validate(filename);
        logger.debug(" XML successfully validated.. ");
        Document doc = getDOMHandle(filename);
        logger.debug("Root element :" + doc.getDocumentElement().getNodeName());

        Element rootElement = doc.getDocumentElement();
        NodeList childNodes = rootElement.getChildNodes();

        Set<WorkflowState> workflowSet = new HashSet<WorkflowState>();
        List<ActionStateMapping> actions = new ArrayList<ActionStateMapping>();
        buildCommonActions(rootElement, actions, workflowSet);
        for (int counter = 0; counter < childNodes.getLength(); counter++) {
            Node nNode = childNodes.item(counter);
            if (nNode instanceof Element) {
                Element child = (Element) nNode;

                buildWorkFlowConfig(workflowSet, actions, child);
            }
        }
        Workflow workflow = new Workflow(workflowSet);

        return workflow;
    }

    protected void validate(String fileToValidate) {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        try {
            StreamSource streamSource = new StreamSource(getInputStreamFrom(SCHEMA_LOCATION));
            Schema schema = factory.newSchema(streamSource);
            Validator validator = schema.newValidator();

            Source source = new StreamSource(getInputStreamFrom(fileToValidate));
            validator.validate(source);
        } catch (Exception e) {
            logger.error("Exception occured while validating Xml" + e.toString());
            throw new InvalidWorkflowException(e.getMessage());
        }
    }

    private void buildCommonActions(Element tag, List<ActionStateMapping> actions, Set<WorkflowState> workflowSet) {

        NodeList childTags = tag.getChildNodes();
        for (int i = 0; i < childTags.getLength(); i++) {
            Node childTag = childTags.item(i);
            String nodeName = childTag.getNodeName();
            if (nodeName.toLowerCase().equals("common-actions")) {
                NodeList actionTags = childTag.getChildNodes();

                for (int counter = 0; counter < actionTags.getLength(); counter++) {
                    Node nNode = actionTags.item(counter);
                    if (nNode instanceof Element) {
                        Element actionTag = (Element) nNode;
                        WorkflowState state = new WorkflowState(actionTag.getAttribute("state"));
                        ActionStateMapping actionMapping = new ActionStateMapping(new Action(actionTag.getAttribute("id")), state);
                        workflowSet.add(state);
                        actions.add(actionMapping);
                    }
                }
            }
        }
    }

    private Document getDOMHandle(String filename) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse(getInputStreamFrom(filename));
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            String msg = "Exception occured while parsing Xml. " + e.toString();
            logger.debug(msg);
            throw new InvalidWorkflowException(msg);
        }
        return doc;
    }

    private InputStream getInputStreamFrom(String classPathFileLocation) throws IOException {
//        return new ClassPathResource(classPathFileLocation).getInputStream();
        // todo:  (kgs) fix this
        return null;
    }

    private void buildWorkFlowConfig(Set<WorkflowState> workflowSet, List<ActionStateMapping> actions, Element tag) {
        String tagName = tag.getTagName();

        if (tagName.toLowerCase().equals("initial-state")) {
            processStateTag(workflowSet, tag, actions);
        }
        if (tagName.toLowerCase().equals("states")) {
            processStatesTag(workflowSet, tag, actions);
        }
    }

    private void processStatesTag(Set<WorkflowState> workflowSet, Element tag, List<ActionStateMapping> actions) {
        NodeList states = tag.getChildNodes();
        for (int counter = 0; counter < states.getLength(); counter++) {
            Node nNode = states.item(counter);
            if (nNode instanceof Element) {
                Element stateTag = (Element) nNode;
                processStateTag(workflowSet, stateTag, actions);
            }
        }
    }

    private void processStateTag(Set<WorkflowState> workflowSet, Element tag, List<ActionStateMapping> actions) {
        String id = tag.getAttribute("id");
        WorkflowState state = getStateFromSet(id, workflowSet);

        if (tag.getTagName().equals("initial-state")) {
            if (state == null) {
                state = new WorkflowState(id, WorkflowStateType.INITIAL);
            } else {
                state.setType(WorkflowStateType.INITIAL);
            }
            logger.debug("Initial state created.." + state.getName());
        } else {
            if (state == null) {
                state = new WorkflowState(id);
            } else {
                state.setType(WorkflowStateType.INTERMEDIATE);
            }
            logger.debug("intermediate state created.." + state.getName());
        }
        if (tag.getChildNodes() == null || tag.getChildNodes().getLength() == 0) {
            state.setType(WorkflowStateType.FINAL);
            logger.debug("Final state created.." + state.getName());
        }

        if (tag.hasAttribute("handler")) {
            processHandlerAttribute(tag, state);
        }

        NodeList actionsNodeList = tag.getChildNodes();
        for (int i = 0; i < actionsNodeList.getLength(); i++) {
            Node actionsNode = actionsNodeList.item(i);
            String nodeName = actionsNode.getNodeName();
            if (nodeName.equals("actions")) {
                NodeList actionTags = actionsNode.getChildNodes();
                for (int j = 0; j < actionTags.getLength(); j++) {
                    Node actionNode = actionTags.item(j);
                    if (actionNode instanceof Element) {
                        Element actionTag = (Element) actionNode;

                        ActionStateMapping actionMapping = getActionFromList(actionTag.getAttribute("id"), actions);
                        if (actionMapping == null) {
                            WorkflowState nextState = getStateFromSet(actionTag.getAttribute("state"), workflowSet);
                            if (nextState == null) {
                                nextState = new WorkflowState(actionTag.getAttribute("state"));
                            }
                            actionMapping = new ActionStateMapping(new Action(actionTag.getAttribute("id")), nextState);
                        }
                        state.addFlow(actionMapping);

                    }
                }
            }
        }
        workflowSet.add(state);
    }

    private void processHandlerAttribute(Element tag, WorkflowState state) {
        String handlerName = tag.getAttribute("handler");
        try {
            @SuppressWarnings("rawtypes")
            Class classDefinition = Class.forName(handlerName);
            ActionHandler handler = (ActionHandler) classDefinition.newInstance();
            state.setTransitionHandler(handler);
        } catch (Exception e) {
            String msg = "Exception while creating action handler: " + e.toString();
            logger.error(msg);
            throw new InvalidWorkflowException(msg);
        }
    }

    private WorkflowState getStateFromSet(String id, Set<WorkflowState> workflowSet) {
        for (WorkflowState wfState : workflowSet) {
            if (wfState.getName().equals(id))
                return wfState;
        }
        return null;
    }

    private ActionStateMapping getActionFromList(String id, List<ActionStateMapping> actions) {
        for (ActionStateMapping action : actions) {
            if (action.getAction().getName().equals(id))
                return action;
        }
        return null;
    }
}
