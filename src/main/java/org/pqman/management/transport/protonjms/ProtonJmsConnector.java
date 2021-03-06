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
package org.pqman.management.transport.protonjms;

import org.pqman.management.message.*;
import org.pqman.management.transport.Connector;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class ProtonJmsConnector implements Connector {
   private final String host;
   private final int port;

   public ProtonJmsConnector(String host, int port) {
      this.host = host;
      this.port = port;
   }

   private <T extends Response> T connect(Request<T> request) throws JMSException, NamingException, ResponseException {

      Hashtable<Object, Object> env = new Hashtable<>();
      env.put("connectionfactory.myFactoryLookup", "amqp://" + host + ":" + port);
      env.put("java.naming.factory.initial", "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
      env.put("queue.management", "$management");
      env.put("queue.reply", "reply");
      Context context = new InitialContext(env);

      ConnectionFactory factory = (ConnectionFactory) context.lookup("myFactoryLookup");
      Destination queue = (Destination) context.lookup("management");
      Destination reply = (Destination) context.lookup("reply");

      Connection connection = factory.createConnection();
      connection.start();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      MessageConsumer consumer = session.createConsumer(reply);

      MessageProducer messageProducer = session.createProducer(queue);
      MapMessage message = session.createMapMessage();
      message.setJMSReplyTo(reply);
      message.setStringProperty("operation", request.getOperation().toString());
      message.setStringProperty("type", request.getType());

      Map<String, Object> body = request.getBody();

      if (body != null) {
         injectParams(message, body);
      }

      Map<String, Object> properties = request.getAdditionalApplicationProperties();

      injectProperties(message, properties);

      messageProducer.send(message);

      ObjectMessage receive = (ObjectMessage) consumer.receive(5000);

      int statusCode = (int) receive.getLongProperty("statusCode");

      String statusDescription = receive.getStringProperty("statusDescription");

      T response = request.createResponse(statusCode, statusDescription, new ProtonJmsRequestResolver(receive));

      if (!response.isValidStatusCode(statusCode)) {
         throw new ResponseException(response);
      }

      connection.close();

      return response;
   }

   private void injectParams(MapMessage message, Map<String, Object> body) throws JMSException {
      for (Map.Entry<String, Object> entry : body.entrySet()) {
         Object value = entry.getValue();
         String key = entry.getKey();
         if (value instanceof String) {
            message.setString(key, (String) value);
         } else if (value instanceof Integer) {
            message.setInt(key, (int) value);
         } else if (value instanceof Long) {
            message.setLong(key, (long) value);
         } else if (value instanceof Short) {
            message.setShort(key, (short) value);
         } else if (value instanceof Boolean) {
            message.setBoolean(key, (boolean) value);
         }
      }
   }

   private void injectProperties(MapMessage message, Map<String, Object> body) throws JMSException {
         for (Map.Entry<String, Object> entry : body.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof String) {
               message.setStringProperty(key, (String) value);
            } else if (value instanceof Integer) {
               message.setIntProperty(key, (int) value);
            } else if (value instanceof Long) {
               message.setLongProperty(key, (long) value);
            } else if (value instanceof Short) {
               message.setShortProperty(key, (short) value);
            } else if (value instanceof Boolean) {
               message.setBooleanProperty(key, (boolean) value);
            }
         }
      }

   @Override
   public <T extends Response> T sendRequest(Request<T> request) throws ResponseException {
      try {
         return connect(request);
      } catch (JMSException | NamingException e) {
         e.printStackTrace();
      }
      return null;
   }
}
