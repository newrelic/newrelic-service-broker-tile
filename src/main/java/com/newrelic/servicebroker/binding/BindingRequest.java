/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.newrelic.servicebroker.binding;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.util.Assert;

import com.newrelic.servicebroker.catalog.CatalogFactory;
import com.newrelic.servicebroker.catalog.NewRelicPlan;

@JsonIgnoreProperties(ignoreUnknown = true)
final class BindingRequest {

    private final String serviceId;
    private final String planId;
    private final String appGuid;
	private Map<String, Object> parameters;


    @JsonCreator
    BindingRequest(@JsonProperty("service_id") String serviceId, 
    				@JsonProperty("plan_id") String planId,
    				@JsonProperty("app_guid") String appGuid, 
    				@JsonProperty("parameters") Map<String, Object> parameters) {
        Assert.notNull(serviceId);
        Assert.notNull(planId);
        Assert.notNull(appGuid);

        this.serviceId = serviceId;
        this.planId = planId;
        this.appGuid = appGuid;
                
    	this.parameters = parameters;
    	System.out.println("Binding Request >>>>>> " + this.toString());
    }

    String getServiceId() {
        return this.serviceId;
    }

    String getPlanId() throws Exception {
    	NewRelicPlan plan = CatalogFactory.getAssociatedPlanById(this.planId);
    	System.out.println("PLAN >>>>>> " + plan.toString());
    	// license key parameter name in order of precedence
    	// NEW_RELIC_LICENSE_KEY, license_key, licenseKey, NEWRELIC_LICENSEKEY, NEWRELIC_LICENSE_KEY, NR_LICENSE_KEY, LICENSEKEY
    	if (plan.getPlanName().equalsIgnoreCase("flex") || plan.getPlanName().equalsIgnoreCase("flexplan") || 
    			plan.getPlanName().equalsIgnoreCase("nrflex") || plan.getPlanName().equalsIgnoreCase("nrflexplan") ||
    			plan.getPlanName().equalsIgnoreCase("newrelicflexplan")) {
    		try {
	    		if (parameters.containsKey("NEW_RELIC_LICENSE_KEY")) {
	    			plan.setLicenseKey(parameters.get("NEW_RELIC_LICENSE_KEY").toString());
	    		}
	    		else if (parameters.containsKey("license_key")) {
	    			plan.setLicenseKey(parameters.get("license_key").toString());
	    		}
	    		else if (parameters.containsKey("licenseKey")) {
	    			plan.setLicenseKey(parameters.get("licenseKey").toString());
	    		}
	    		else if (parameters.containsKey("NEWRELIC_LICENSEKEY")) {
	    			plan.setLicenseKey(parameters.get("NEWRELIC_LICENSEKEY").toString());
	    		}
	    		else if (parameters.containsKey("NEWRELIC_LICENSE_KEY")) {
	    			plan.setLicenseKey(parameters.get("NEWRELIC_LICENSE_KEY").toString());
	    		}
	    		else if (parameters.containsKey("NR_LICENSE_KEY")) {
	    			plan.setLicenseKey(parameters.get("NR_LICENSE_KEY").toString());
	    		}
	    		else if (parameters.containsKey("LICENSEKEY")) {
	    			plan.setLicenseKey(parameters.get("LICENSEKEY").toString());
	    		}
	    		else {
	    			throw new Exception("Error: no license key specified for New Relic Flex Plan\n >>> bind parameters: " + parameters.toString());
	    		}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		System.out.println("FLEXPLAN >>>>>> " + plan.toString());
    	}
    	
    	// if following bind parameters are set use them in the plan
    	if (parameters != null && !parameters.isEmpty()) {
	    	for (Map.Entry<String, Object> param : parameters.entrySet()) {
			    String key = param.getKey();
			    String value = (String)param.getValue();
	    		switch (key.toLowerCase()) {
	    		case "rpm_account_id":
	    		case "rpmaccountid":
	    		case "rpmaccount":
	    			plan.setRpmAccountId(value);
	    			break;
	    			
	    		case "insights_insert_key":
	    		case "insights_key":
	    		case "insert_key":
	    		case "insertkey":
	    		case "insightskey":
	    		case "insightsinsertkey":
	    			plan.setInsightsInsertKey(value);
	    			break;
	    			
	    		case "orgs":
	    		case "orgnames":
	    		case "org_names":
	    			plan.setOrgs(value);
	    			break;
	    		}
	    	}
    	}
    	
        return this.planId;
    }

    String getAppGuid() {
        return this.appGuid;
    }

    Map<String, Object> getParameters() {
    	return this.parameters;
    }
    
    @Override
    public String toString() {
        return String.format("service_id: %s, plan_id: %s, app_guid: %s, parameters: %s", this.serviceId, this.planId, this.appGuid, this.parameters);
    }
}
