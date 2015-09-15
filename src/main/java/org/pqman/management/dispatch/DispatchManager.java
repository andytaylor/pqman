/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pqman.management.dispatch;

import org.pqman.management.api.Entity;
import org.pqman.management.message.CreateRequest;
import org.pqman.management.message.Response;
import org.pqman.management.transport.Connector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class DispatchManager {
   private final Connector connector;

   public DispatchManager(Connector connector) {
      this.connector = connector;
   }

   public Response createListener(String name,
                                  String addr,
                                  String port,
                                  boolean requireSsl,
                                  long maxFrameSize,
                                  boolean requireEncryption,
                                  String role,
                                  boolean authenticatePeer) {
      Map<String, Object> body = new HashMap<>();
      body.put("addr", addr);
      body.put("port", port);
      body.put("requireSsl", requireSsl);
      body.put("maxFrameSize", maxFrameSize);
      body.put("requireEncryption", requireEncryption);
      body.put("role", role);
      body.put("authenticatePeer", authenticatePeer);

      Entity entity = new Entity(name, null, "listener");

      CreateRequest request = new CreateRequest(entity, body);
      return connector.sendRequest(request);

   }

   public Response createConnector(String name,
                                   String addr,
                                   String port,
                                   long maxFrameSize,
                                   String role, boolean allowRedirect) {
      Map<String, Object> body = new HashMap<>();
      body.put("addr", addr);
      body.put("port", port);
      body.put("maxFrameSize", maxFrameSize);
      body.put("role", role);
      body.put("allowRedirect", allowRedirect);

      Entity entity = new Entity(name, null, "connector");

      CreateRequest request = new CreateRequest(entity, body);
      return connector.sendRequest(request);

      }
}
