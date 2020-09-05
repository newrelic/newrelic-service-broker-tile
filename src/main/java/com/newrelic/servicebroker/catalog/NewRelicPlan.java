package com.newrelic.servicebroker.catalog;

import java.util.UUID;


public class NewRelicPlan {
  private String planName = null;
  private String planDescription = null;
  private String licenseKey = null;
  private String planId = null;
  private String planOldGuid = null;
  private boolean oldPlan = false;
  private String guid = null;
  private String rpmAccountId = null;
  private String insightsInsertKey = null;
  private String orgs = null;


  public String getPlanName() {
    return this.planName;
  }

  public void setPlanName(String planName) {
    this.planName = planName;
  }

  public String getPlanDescription() {
    return this.planDescription;
  }

  public void setPlanDescription(String planDescription) {
    this.planDescription = planDescription;
  }

  public String getLicenseKey() {
    return this.licenseKey;
  }

  public void setLicenseKey(String licenseKey) {
    this.licenseKey = licenseKey;
  }

  public String getPlanId() {
    return this.planId;
  }

//  public void setPlanId() throws Exception {
//    if ((this.planName == null) || (this.licenseKey == null)) {
//      throw new Exception("No null planName or licenseKey allowed");
//    }
//
//    if ((!java.util.Objects.isNull(this.planOldGuid) && !this.planOldGuid.isEmpty()) || this.isOldPlan()) {
//    	this.planId = this.planOldGuid;
//    }
//    else {
//    	if (java.util.Objects.isNull(this.planId) || this.planId == "") {
//			this.planId = UUID.nameUUIDFromBytes((this.planName).getBytes()).toString();
//        }
//    }
//  }

  public void setPlanId() throws Exception {
	if ((planName == null) || (licenseKey == null)) {
		throw new Exception("No null planName or licenseKey allowed");
	}
	
	if (planOldGuid != null && !planOldGuid.isEmpty()) {
		planId = planOldGuid;
	}
	else if (isOldPlan()) { // if lienseKey was changed b4 migration from 1.12.12 this will fail
		planId = UUID.nameUUIDFromBytes((planName+licenseKey).getBytes()).toString();
    }
	 
	if (planId == null || planId.isEmpty()) { // assuming new plan if there is no valid planOldGuid and isOldPlan()=false
		planId = UUID.nameUUIDFromBytes((planName).getBytes()).toString();
	}
  }

  public String getPlanOldGuid() {
	    return this.planOldGuid;
  }

  public void setPlanOldGuid(String planOldGuid) {
		if (!this.isOldPlan()) {
			this.planOldGuid = "";
		}
		else {
			try {
				UUID uuid = UUID.fromString(planOldGuid); // verify the entered guid
		  		this.planOldGuid = uuid.toString();
			} catch (IllegalArgumentException exception){
				this.planOldGuid = "";
		}
		}
  }

  public boolean isOldPlan() {
	    return this.oldPlan;
	  }

  public void setOldPlan(boolean oldPlan) {
    this.oldPlan = oldPlan;
  }

  public String getGuid() {
    return this.guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getRpmAccountId() {
    return this.rpmAccountId;
  }

  public void setRpmAccountId(String rpmAccountId) {
    this.rpmAccountId = rpmAccountId;
  }

  public String getInsightsInsertKey() {
    return this.insightsInsertKey;
  }

  public void setInsightsInsertKey(String insightsInsertKey) {
    this.insightsInsertKey = insightsInsertKey;
  }

  public String getOrgs() {
    return this.orgs;
  }

  public void setOrgs(String orgs) {
    this.orgs = orgs.replaceAll("\\s*,\\s*", ","); //.split("\\s*,\\s*"); // return array of trimmed strings
  }
  
  public String toString() {
    return "Plan [guid=" + this.guid + 
    		", planName=" + this.planName + 
    		", licenseKey=" + this.licenseKey + 
    		", planId=" + ((!java.util.Objects.isNull(planId)) && !planId.isEmpty() ? planId : "--") + 
    		", planDescription=" + this.planDescription + 
    		", planOldGuid=" + ((!java.util.Objects.isNull(planOldGuid) && !planOldGuid.isEmpty()) ? planOldGuid : "--") + 
    		", isOldPlan=" + this.isOldPlan() + 
    		", rpmAccountId=" + this.rpmAccountId + 
    		", insightsInsertKey=" + this.insightsInsertKey + 
    		", orgs=" + this.orgs + 
    		"]";
  }
}

