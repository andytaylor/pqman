/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pqman.management.message;

import org.pqman.management.api.Operation;
import org.pqman.management.transport.RequestResolver;

import java.util.Map;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class CreateRequest extends Request<CreateResponse> {

    public CreateRequest(String name, String type, Map<String, Object> body) {
        super(Operation.CREATE, type, body);
        getAdditionalApplicationProperties().put("name", name);
    }

    @Override
    public CreateResponse createResponse(int statusCode, String statusDescription, RequestResolver resolver) {
       Map<String, Object> replyBody = resolver.getBody();
       String type = resolver.getApplicationProperty("type");
       return new CreateResponse(statusCode, statusDescription, replyBody, type);
    }
}
