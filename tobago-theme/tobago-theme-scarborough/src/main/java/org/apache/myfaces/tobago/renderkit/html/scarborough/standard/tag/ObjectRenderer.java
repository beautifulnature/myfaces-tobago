package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
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


import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ObjectRenderer extends RendererBase {
  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement("iframe", component);
    writer.writeAttribute("src", null, ATTR_TARGET);
    writer.writeComponentClass();
    writer.writeAttribute("style", null, ATTR_STYLE);

    String noframes = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "browser.noframe.message.prefix");
    writer.writeText(noframes + " ", null);
    writer.startElement("a", component);
    writer.writeAttribute("href", null, ATTR_TARGET);
    writer.writeText(null, ATTR_TARGET);
    writer.endElement("a");
    noframes = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "browser.noframe.message.postfix");
    writer.writeText(" " + noframes, null);

    writer.endElement("iframe");
  }
}
