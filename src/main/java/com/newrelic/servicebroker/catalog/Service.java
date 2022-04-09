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

package com.newrelic.servicebroker.catalog;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.simple.parser.ParseException;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
final class Service {

    private final Catalog catalog;

    private final Object monitor = new Object();

    private volatile UUID id;

    private volatile String name;

    private volatile String description;

    private volatile Boolean bindable;

    private volatile List<String> tags;

    private volatile ServiceMetadata serviceMetadata;

    private volatile List<String> requires;

    private volatile List<Plan> plans;

    private volatile Boolean plan_updateable;

    private volatile DashboardClient dashboardClient;

    // private final static String DEFAULT_UUID = "794A2090-EF9F-40AB-A8FE-9B0B00C5B136"; // change for the next release

    Service(Catalog catalog) {
        this.catalog = catalog;
    }

    UUID getId() {
        synchronized (this.monitor) {
            Assert.notNull(this.id, "Services must specify an id");
            return this.id;
        }
    }

    String getName() {
        synchronized (this.monitor) {
            Assert.notNull(this.name, "Services must specify a name");
            return this.name;
        }
    }

    String getDescription() {
        synchronized (this.monitor) {
            Assert.notNull(this.description, "Services must specify a description");
            return this.description;
        }
    }

    Boolean getBindable() {
        synchronized (this.monitor) {
            Assert.notNull(this.bindable, "Object is null");
            return this.bindable;
        }
    }

    List<String> getTags() {
        synchronized (this.monitor) {
            return this.tags;
        }
    }

    @JsonProperty("metadata")
    ServiceMetadata getServiceMetadata() {
        synchronized (this.monitor) {
            return this.serviceMetadata;
        }
    }

    List<String> getRequires() {
        synchronized (this.monitor) {
            return this.requires;
        }
    }

    List<Plan> getPlans() {
        synchronized (this.monitor) {
            return this.plans;
        }
    }

    Boolean getPlan_updateable() {
        synchronized (this.monitor) {
            Assert.notNull(this.plan_updateable, "Object is null");
            return this.plan_updateable;
        }
    }

    @JsonProperty("dashboard_client")
    DashboardClient getDashboardClient() {
        synchronized (this.monitor) {
            return this.dashboardClient;
        }
    }

    Catalog and() {
        synchronized (this.monitor) {
            return this.catalog;
        }
    }

    Service id(UUID id) {
        synchronized (this.monitor) {
            this.id = id;
            return this;
        }
    }

    Service name(String name) {
        synchronized (this.monitor) {
            this.name = name;
            return this;
        }
    }

    Service description(String description) {
        synchronized (this.monitor) {
            this.description = description;
            return this;
        }
    }

    Service bindable(Boolean bindable) {
        synchronized (this.monitor) {
            this.bindable = bindable;
            return this;
        }
    }

    Service tags(String... tags) {
        synchronized (this.monitor) {
            if (this.tags == null) {
                this.tags = new ArrayList<>();
            }
            Arrays.stream(tags).forEach(this.tags::add);
            return this;
        }
    }

    ServiceMetadata metadata() {
        synchronized (this.monitor) {
            if (this.serviceMetadata == null) {
                this.serviceMetadata = new ServiceMetadata(this);
            }

            return this.serviceMetadata;
        }
    }

    Service requires(String... requires) {
        synchronized (this.monitor) {
            if (this.requires == null) {
                this.requires = new ArrayList<>();
            }
            Arrays.stream(requires).forEach(this.requires::add);
            return this;
        }
    }

    Plan plan() {
        synchronized (this.monitor) {
            if (this.plans == null) {
                this.plans = new ArrayList<>();
            }

            Plan plan = new Plan(this);
            this.plans.add(plan);
            return plan;
        }
    }

    Service addAllPlans(/*@Value("${NRPLANS}")*/ List<NewRelicPlan> nrPlans) throws ParseException {

        for (NewRelicPlan nr_plan: nrPlans) {
            String nrPlanId = nr_plan.getPlanId();
            String nrPlanName = nr_plan.getPlanName();
            // String licenseKey = nr_plan.getLicenseKey();

            if (nrPlanName.equalsIgnoreCase("flex")) {
                plan()
                .id(UUID.fromString(nrPlanId))
                .name(nrPlanName)
                .description("Service Plan for " + nrPlanName)
                .metadata()
                    .bullets("JVM Performance analyzer", "Browser End User Monitoring", "Database call response time & throughput",
                        "Performance data API access", "Synthetics Monitoring", "Software Analytics", 
                        "--- INSTRUCTIONS", "------ SET ENVIRONMENT VARIABLE:", "------ NEW_RELIC_LICENSE_KEY")
                    .displayName(nrPlanName.toUpperCase())
                    .cost()
                        .amount("usd", 0.0)
                        .unit("MONTHLY")
                        .and()
                    .and()
                .free(true);          	
            }
            else {
                plan()
                .id(UUID.fromString(nrPlanId))
                .name(nrPlanName)
                .description("Service Plan for " + nrPlanName)
                .metadata()
                    .bullets("JVM Performance analyzer", "Browser End User Monitoring", "Database call response time & throughput",
                        "Performance data API access", "Synthetics Monitoring", "Software Analytics")
                    .displayName(nrPlanName.toUpperCase())
                    .cost()
                        .amount("usd", 0.0)
                        .unit("MONTHLY")
                        .and()
                    .and()
                .free(true);
            }
        }
        return this;
    }

    Service plan_updateable(Boolean plan_updateable) {
        synchronized (this.monitor) {
            this.plan_updateable = plan_updateable;
            return this;
        }
    }

    DashboardClient dashboardClient() {
        synchronized (this.monitor) {
            this.dashboardClient = new DashboardClient(this);
            return this.dashboardClient;
        }
    }

}
