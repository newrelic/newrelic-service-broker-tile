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

package com.newrelic.servicebroker.provisioning;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
final class ProvisioningRequest {

    private final String serviceId;
    private final String planId;
    private final String organizationGuid;
    private final String spaceGuid;
	private Map<String, Object> parameters;


    @JsonCreator
    ProvisioningRequest(@JsonProperty("service_id") String serviceId, 
    					@JsonProperty("plan_id") String planId,
                        @JsonProperty("organization_guid") String organizationGuid,
                        @JsonProperty("space_guid") String spaceGuid, 
                        @JsonProperty("parameters") Map<String, Object> parameters) {
        Assert.notNull(serviceId, "Service Id is null");
        Assert.notNull(planId, "Plan Id is null");
        Assert.notNull(organizationGuid, "Organization Guid is null");
        Assert.notNull(spaceGuid, "Space Guid is null");

        this.serviceId = serviceId;
        this.planId = planId;
        this.organizationGuid = organizationGuid;
        this.spaceGuid = spaceGuid;

        this.parameters = parameters;
    }

    String getServiceId() {
        return this.serviceId;
    }

    String getPlanId() {
        return this.planId;
    }

    String getOrganizationGuid() {
        return this.organizationGuid;
    }

    String getSpaceGuid() {
        return this.spaceGuid;
    }

    Map<String, Object> getParameters() {
    	return this.parameters;
    }
    
    @Override
    public String toString() {
        return String.format("service_id: %s, plan_id: %s, organization_guid: %s, space_guid: %s, parameters: %s", this.serviceId,
                this.planId, this.organizationGuid, this.spaceGuid, this.parameters);
    }
}
