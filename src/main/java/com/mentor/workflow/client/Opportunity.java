package com.mentor.workflow.client;

/**
 * This is the externally exposed Opportunity. It provides the current state of
 * an Opportunity, if all the details of the opportunity values contained in the
 * OpportunityModel.
 *
 * @author: ksipe
 */
public class Opportunity {

    private long opportunityId;
    private String status;
    private String oppType;

    public long getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(long opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOppType() {
        return oppType;
    }

    public void setOppType(String oppType) {
        this.oppType = oppType;
    }
}
