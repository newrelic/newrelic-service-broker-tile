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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Configuration
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogFactory {

  public static List<NewRelicPlan> plans = null;

  public static NewRelicPlan getAssociatedPlanById(String nrPlanId) {
    if (plans == null) {
      return null;
    }
    for (NewRelicPlan p : plans) {
      System.out.println("\t\tNewRelicPlan: " + p + ", checking for planId: " + nrPlanId);
      if (p.getPlanId().equals(nrPlanId)) {
        return p;
      }
    }
    return null;
  }

  public static NewRelicPlan getAssociatedPlanByName(String nrPlanName) {
    if (plans == null) {
      return null;
    }
    for (NewRelicPlan p : plans) {
      System.out.println("\t\tNewRelicPlan: " + p + ", checking for planname: " + nrPlanName);
      if (p.getPlanName().equals(nrPlanName)) {
        return p;
      }
    }
    return null;
  }

  static final String FILE_PREFIX = "file:";

  /**
   * NRPLANS is normally the plans JSON. Cloud Controller limits the size of app env vars,
   * so it can also be "file:<name>" - the JSON is then read from the classpath (bundled
   * in the jar by the tile pre_deploy) or, if not found there, from the file system.
   */
  static String resolvePlans(String nrPlans) throws IOException {
    if (nrPlans == null || !nrPlans.trim().startsWith(FILE_PREFIX)) {
      return nrPlans;
    }
    String name = nrPlans.trim().substring(FILE_PREFIX.length());
    ClassPathResource resource = new ClassPathResource(name);
    if (resource.exists()) {
      try (InputStream in = resource.getInputStream()) {
        return new String(StreamUtils.copyToByteArray(in), StandardCharsets.UTF_8);
      }
    }
    return new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.UTF_8);
  }

  @Bean
  Catalog catalog(@Value("${NRPLANS}") String nrPlans) throws Exception {
	nrPlans = resolvePlans(nrPlans);

	// 7-25-18 - replace nulls with empty string in NRPLANS json object
	nrPlans = nrPlans.replaceAll(":\\s{0,10}null\\s{0,10},", ":\"\",").replaceAll(":\\s{0,10}null\\s{0,10}}", ":\"\"}");
    System.out.println("NRPLANS set to: " + nrPlans);
    
	String serviceId = UUID.nameUUIDFromBytes("NewRelic_ServiceId_v1".getBytes()).toString();
//    String serviceId = UUID.nameUUIDFromBytes("NewRelic_ServiceId_v2".getBytes()).toString(); // for testing the service broker

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    // plans = (List<NewRelicPlan>)objectMapper.readValue(nrPlans, objectMapper.getTypeFactory().constructCollectionType(List.class, NewRelicPlan.class));
    TypeFactory typeFactory = objectMapper.getTypeFactory();
    CollectionType collectionType = typeFactory.constructCollectionType(List.class, NewRelicPlan.class);
    plans = objectMapper.readValue(nrPlans, collectionType);

    for (NewRelicPlan p : plans) {
      p.setPlanId();
      System.out.println("\t\tNewRelicPlan: " + p);
    }

    return new Catalog()
    	.service()
			.id(UUID.fromString(serviceId))
			
			.name("newrelic")
//			.name("newrelic-v2-test") // for testing the service broker
			
			.description("Manage and monitor your apps")
			.bindable(Boolean.valueOf(true))
			.tags(new String[] { "newrelic", "management", "monitoring", "apm", "enduser", "analytics" })
			
//3-26-18 - SK - added for manage button // dashboardClient in app UI page
//			.dashboardClient()
//				.id("skhazai@gmail.com")
//				.secret("j00mas")
//				.redirectUri(URI.create("https://rpm.newrelic.com/accounts/192626/applications/136062476"))
//				.and()
//3-26-18 - SK - END - ###############################################################
				
			.metadata()
				.displayName("New Relic")
				.imageUrl(URI.create("https://raw.githubusercontent.com/newrelic/newrelic-service-broker-tile/master/resources/images/NR_logo.png"))
				.longDescription("New Relic is the all-in-one web app performance tool " +
					"that lets you see performance from the end user experience, " +
					"through servers, and down to the line of code.")
				.providerDisplayName("New Relic, Inc.")
				.documentationUrl(URI.create("https://docs.newrelic.com"))
				.supportUrl(URI.create("https://support.newrelic.com/home"))
				.and()
			.addAllPlans(plans)
			.plan_updateable(Boolean.valueOf(true))
			.and();
  }
}
