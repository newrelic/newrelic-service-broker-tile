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

import com.newrelic.servicebroker.AbstractDeserializationTest;
import com.newrelic.servicebroker.binding.BindingRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public final class BindingRequestTest extends AbstractDeserializationTest<BindingRequest> {

    public BindingRequestTest() {
        super(BindingRequest.class);
    }

    @Override
    protected void assertContents(BindingRequest instance) {
        assertEquals("test-service-id", instance.getServiceId());
        try {
        	assertEquals("38400000-8cf0-11bd-b23e-10b96e4ef00d", instance.getPlanId());
        } catch(Exception e) {
        	e.printStackTrace();
        }
        assertEquals("test-app-guid", instance.getAppGuid());
    }

    @Override
    protected Map getMap() {
        Map<String, String> m = new HashMap<>();
        m.put("service_id", "test-service-id");
        m.put("plan_id", "38400000-8cf0-11bd-b23e-10b96e4ef00d");
        m.put("app_guid", "test-app-guid");

        return m;
    }

}
