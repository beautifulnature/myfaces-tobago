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

import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIFile;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.HttpPartWrapper;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.PartUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.validator.FileItemValidator;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.validator.Validator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class FileRenderer extends MessageLayoutRendererBase implements ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void processEvent(final ComponentSystemEvent event) {
    TobagoContext.getInstance(FacesContext.getCurrentInstance()).setEnctype("multipart/form-data");
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    final AbstractUIFile file = (AbstractUIFile) component;
    final boolean multiple = file.isMultiple() && !file.isRequired();
    final Object request = facesContext.getExternalContext().getRequest();
    if (request instanceof HttpServletRequest) {
      try {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (multiple) {
          final List<Part> parts = new ArrayList<>();
          for (final Part part : httpServletRequest.getParts()) {
            if (file.getClientId(facesContext).equals(part.getName())) {
              LOG.debug("Uploaded file '{}', size={}, type='{}'",
                  PartUtils.getSubmittedFileName(part), part.getSize(), part.getContentType());
              parts.add(new HttpPartWrapper(part));
            }
            file.setSubmittedValue(parts.toArray(new Part[0]));
          }
        } else {
          final Part part = httpServletRequest.getPart(file.getClientId(facesContext));
          final String submittedFileName = PartUtils.getSubmittedFileName(part);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Uploaded file '{}', size={}, type='{}'",
                submittedFileName, part.getSize(), part.getContentType());
          }
          if (submittedFileName.length() > 0) {
            file.setSubmittedValue(new HttpPartWrapper(part));
          }
        }
      } catch (final Exception e) {
        LOG.error("", e);
        file.setValid(false);
      }
    } else { // todo: PortletRequest
      LOG.warn("Unsupported request type: " + request.getClass().getName());
    }

    RenderUtils.decodeClientBehaviors(facesContext, component);
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIFile file = (AbstractUIFile) component;
    final String clientId = file.getClientId(facesContext);
    final String fieldId = file.getFieldId(facesContext);
    final String accept = createAcceptFromValidators(file);
    final boolean multiple = file.isMultiple() && !file.isRequired();
    final boolean disabled = file.isDisabled();
    final boolean readonly = file.isReadonly();
    if (file.isMultiple() && file.isRequired()) {
      LOG.warn("Required multiple file upload is not supported."); //TODO TOBAGO-1930
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    if (file.isLabelLayoutSkip()) {
      writer.writeIdAttribute(clientId);
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(file.getMarkup()), false);
    }
    writer.writeClassAttribute(
        TobagoClass.FILE,
        TobagoClass.FILE.createMarkup(file.getMarkup()),
        file.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, file);

    // visible fake input for a pretty look
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);
    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
    writer.writeAttribute(HtmlAttributes.ACCEPT, accept, true);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled || readonly);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    if (!disabled && !readonly) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, file.getPlaceholder(), true);
    }

    writer.writeClassAttribute(
        TobagoClass.FILE__PRETTY,
        BootstrapClass.FORM_CONTROL,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(file)));
    // TODO Focus
    //HtmlRendererUtils.renderFocus(clientId, file.isFocus(), ComponentUtils.isError(file), facesContext, writer);
    writer.endElement(HtmlElements.INPUT);

    // invisible file input
    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, multiple);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.FILE);
    writer.writeAttribute(HtmlAttributes.ACCEPT, accept, true);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeIdAttribute(fieldId);
    writer.writeClassAttribute(TobagoClass.FILE__REAL);
    writer.writeNameAttribute(clientId);
    final String multiFormat = ResourceUtils.getString(facesContext, "file.selected");
    writer.writeAttribute(DataAttributes.dynamic("tobago-file-multi-format"), multiFormat, true);
    // readonly seems not making sense in browsers.
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled || readonly);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.REQUIRED, file.isRequired());
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, file);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, file)));

    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP_APPEND);
    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TABINDEX, file.getTabIndex());
    writer.writeClassAttribute(BootstrapClass.BTN, BootstrapClass.BTN_SECONDARY);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled || readonly);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.FA, Icons.FOLDER_OPEN);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.DIV);
  }

  private String createAcceptFromValidators(final AbstractUIFile file) {
    final StringBuilder builder = new StringBuilder();
    for (final Validator validator : file.getValidators()) {
      if (validator instanceof FileItemValidator) {
        final FileItemValidator fileItemValidator = (FileItemValidator) validator;
        for (final String contentType : fileItemValidator.getContentType()) {
          builder.append(",");
          builder.append(contentType);
        }
      }
    }
    if (builder.length() > 0) {
      return builder.substring(1);
    } else {
      return null;
    }
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    final AbstractUIFile file = (AbstractUIFile) component;
    return file.getFieldId(facesContext);
  }
}
