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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.lang.invoke.MethodHandles;

public abstract class DecodingCommandRendererBase extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {

    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    final String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("sourceId = '" + sourceId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (clientId.equals(sourceId)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("queueEvent = '" + clientId + "'");
      }
      commandActivated(component);
    }

    RenderUtils.decodeClientBehaviors(facesContext, component);

  }

  protected void commandActivated(final UIComponent component) {
    component.queueEvent(new ActionEvent(component));
  }
}
