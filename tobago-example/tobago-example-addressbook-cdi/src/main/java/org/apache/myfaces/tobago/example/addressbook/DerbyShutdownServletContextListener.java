/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.addressbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.DriverManager;

@WebListener
public class DerbyShutdownServletContextListener implements ServletContextListener {

  private static final Logger LOG = LoggerFactory.getLogger(DerbyShutdownServletContextListener.class);

  public void contextInitialized(ServletContextEvent servletContextEvent) {

  }

  // todo: should not be shut down in case of the "InMemoryAddressDao" alternative

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    try {
      DriverManager.getConnection("jdbc:derby:target/addressDB;shutdown=true");
    } catch (Exception e) {
      LOG.error("", e); 
    }
  }
}