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
package org.pqman.management;

import org.junit.Test;
import org.pqman.management.dispatch.DispatchManager;
import org.pqman.management.message.CreateResponse;
import org.pqman.management.message.Response;
import org.pqman.management.message.ErrorResponse;
import org.pqman.management.transport.protonjms.ProtonJmsConnectorFactory;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class DispatchTest {
   @Test
   public void createListener() {
      String name="createListenerTest";
      boolean requireSsl=false;
      long maxFrameSize=65536;
      boolean requireEncryption=false;
      String role="normal";
      boolean authenticatePeer=false;
      String port="amqp";
      String addr="127.0.0.1";
      DispatchManager manager = new DispatchManager(new ProtonJmsConnectorFactory().createConnector("localhost", 5672));
      CreateResponse listener = (CreateResponse) manager.createListener(name, addr, port, requireSsl, maxFrameSize, requireEncryption, role, authenticatePeer);
      assertEquals("listener", listener.getType());
      Map<String, Object> body = listener.getBody();
      assertEquals(name, body.get("name"));
      assertEquals(maxFrameSize, body.get("maxFrameSize"));
      assertEquals(requireEncryption, body.get("requireEncryption"));
      assertEquals(role, body.get("role"));
      assertEquals(authenticatePeer, body.get("authenticatePeer"));
      assertEquals(port, body.get("port"));
      assertEquals(addr, body.get("addr"));
   }

   @Test
   public void createListener2() {
      String name="createListenerTest2";
      boolean requireSsl=true;
      long maxFrameSize=55555;
      boolean requireEncryption=true;
      String role="normal";
      boolean authenticatePeer=true;
      String port="1234";
      String addr="127.0.0.1";
      DispatchManager manager = new DispatchManager(new ProtonJmsConnectorFactory().createConnector("localhost", 5672));
      CreateResponse listener = (CreateResponse) manager.createListener(name, addr, port, requireSsl, maxFrameSize, requireEncryption, role, authenticatePeer);
      assertEquals("listener", listener.getType());
      Map<String, Object> body = listener.getBody();
      assertEquals(name, body.get("name"));
      assertEquals(maxFrameSize, body.get("maxFrameSize"));
      assertEquals(requireEncryption, body.get("requireEncryption"));
      assertEquals(role, body.get("role"));
      assertEquals(authenticatePeer, body.get("authenticatePeer"));
      assertEquals(port, body.get("port"));
      assertEquals(addr, body.get("addr"));
   }

   @Test
   public void createSameListenerTwice() {
      String name="createListenerTwiceTest";
      boolean requireSsl=false;
      long maxFrameSize=65536;
      boolean requireEncryption=false;
      String role="normal";
      boolean authenticatePeer=false;
      String port="4567";
      String addr="127.0.0.1";
      DispatchManager manager = new DispatchManager(new ProtonJmsConnectorFactory().createConnector("localhost", 5672));
      manager.createListener(name, addr, port, requireSsl, maxFrameSize, requireEncryption, role, authenticatePeer);
      Response error = manager.createListener(name, addr, port, requireSsl, maxFrameSize, requireEncryption, role, authenticatePeer);
      assertTrue(error instanceof ErrorResponse);
   }

   @Test
   public void createConnector() {
      String name="createConnectorTest";
      long maxFrameSize=65536;
      String role="normal";
      String port="amqp";
      String addr="127.0.0.1";
      boolean allowRedirect = true;
      DispatchManager manager = new DispatchManager(new ProtonJmsConnectorFactory().createConnector("localhost", 5672));
      CreateResponse connector = (CreateResponse) manager.createConnector(name, addr, port, maxFrameSize, role, allowRedirect);
      assertEquals("connector", connector.getType());
      Map<String, Object> body = connector.getBody();
      System.out.println("body = " + body);
      assertEquals(name, body.get("name"));
      assertEquals(maxFrameSize, body.get("maxFrameSize"));
      assertEquals(role, body.get("role"));
      assertEquals(port, body.get("port"));
      assertEquals(addr, body.get("addr"));
   }
}
