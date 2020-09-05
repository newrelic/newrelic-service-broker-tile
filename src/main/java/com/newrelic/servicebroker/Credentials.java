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

package com.newrelic.servicebroker;

/**
 * A holder for credential information such as the license key
 */

public final class Credentials {

  private String licenseKey;

  private String rpmAccountId;
  private String insightsInsertKey;
  private String orgs;

  public Credentials(String licenseKey, String rpmAccountId, String insightsInsertKey, String orgs) {
	  this.licenseKey = licenseKey;
	  
	  this.rpmAccountId = rpmAccountId;
	  this.insightsInsertKey = insightsInsertKey;
	  this.orgs = orgs;
  }

  public String getLicenseKey() throws Exception {
	  return this.licenseKey;
  }

  
  public String getRpmAccountId() throws Exception {
	  return this.rpmAccountId;
  }

  public String getInsightsInsertKey() throws Exception {
	  return this.insightsInsertKey;
  }

  public String getOrgs() throws Exception {
	  return this.orgs;
  }
}
