package com.mentor.workflow.client;

import java.util.Date;

/**
 * This is the externally exposed Opportunity. It provides the current state of
 * an Opportunity, if all the details of the opportunity values contained in the
 * OpportunityModel.
 * 
 * @author: ksipe
 */
public class Opportunity {

	private long opportunityId;
	private String opportunityType;
	private Date lastUpdateDate;
	private Date insertDate;
	private Date termDate = null;
	private String status;
    private String memberId;
    private String memberIdType;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public long getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(long opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getOppType() {
		return opportunityType;
	}

	public void setOppType(String oppType) {
		this.opportunityType = oppType;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDt) {
		this.lastUpdateDate = lastUpdateDt;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDt) {
		this.insertDate = insertDt;
	}

    public Date getTermDate() {
        return termDate;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberIdType() {
        return memberIdType;
    }

    public void setMemberIdType(String memberIdType) {
        this.memberIdType = memberIdType;
    }

    public boolean isDeleted() {
		return termDate != null && termDate.before(new Date());
	}
}
