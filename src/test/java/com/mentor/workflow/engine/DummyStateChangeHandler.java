package com.mentor.workflow.engine;

import com.mentor.workflow.WorkflowComponent;
import com.mentor.workflow.Action;
import com.mentor.workflow.ActionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kensipe
 */
public class DummyStateChangeHandler implements ActionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean beforeAction(WorkflowComponent component) {
        return true;
    }

    @Override
    public void onAction(Action action, WorkflowComponent component) {
        logger.debug("onAction action:{} component: {}", action.getName(), component.getId());
    }
}
