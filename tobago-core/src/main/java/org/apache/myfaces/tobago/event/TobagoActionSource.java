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

package org.apache.myfaces.tobago.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.ActionSource2;
import javax.faces.el.MethodBinding;

public interface TobagoActionSource extends ActionSource2 {

  Logger LOG = LoggerFactory.getLogger(TobagoActionSource.class);

  /**
   * @deprecated Replaced by ActionSource2.getActionExpression
   */
  @Override
  @Deprecated
  default MethodBinding getAction() {
    LOG.debug("JSF 1.1 style is no longer supported!");
    return null;
  }

  /**
   * @deprecated Replaced by ActionSource2.setActionExpression
   */
  @Override
  @Deprecated
  default void setAction(MethodBinding action) {
    LOG.debug("JSF 1.1 style is no longer supported!");
  }

  /**
   * @deprecated Replaced by getActionListeners
   */
  @Override
  @Deprecated
  default MethodBinding getActionListener() {
    LOG.debug("JSF 1.1 style is no longer supported!");
    return null;
  }

  /**
   * @deprecated Replaced by getActionListeners
   */
  @Override
  @Deprecated
  default void setActionListener(MethodBinding actionListener) {
    LOG.debug("JSF 1.1 style is no longer supported!");
  }
}
