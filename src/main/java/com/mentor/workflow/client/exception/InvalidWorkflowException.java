package com.mentor.workflow.client.exception;

/**
 * @author: ksipe
 */
public class InvalidWorkflowException extends RuntimeException {

    public InvalidWorkflowException() {
    }

    public InvalidWorkflowException(String message) {
        super(message);
    }
}
