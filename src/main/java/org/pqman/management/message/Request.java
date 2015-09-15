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

import org.pqman.management.api.Entity;
import org.pqman.management.api.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public abstract class Request {
    private final Operation operation;

    private final List<String> locales = new ArrayList<String>();

   private final Entity entity;

   private final Map<String, Object> body;

   private Map<String,Object> additionalProperties = new HashMap<>();

    public Request(Operation operation, Entity entity, Map<String, Object> body) {
        this.operation = operation;
       this.entity = entity;
       this.body = body;
    }

   abstract Response createResponse(String statusDescription, Map<String, Object> body, String type);

   public Operation getOperation() {
      return operation;
   }

   public Entity getEntity() {
      return entity;
   }

   public Map<String, Object> getBody() {
      return body;
   }

   public Map<String,Object> getAdditionalApplicationProperties() {
      return additionalProperties;
   }
}
