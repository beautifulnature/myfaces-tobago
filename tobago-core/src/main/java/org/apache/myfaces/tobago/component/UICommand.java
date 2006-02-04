package org.apache.myfaces.tobago.component;

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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

/**
 * User: weber
 * Date: Apr 4, 2005
 * Time: 5:02:10 PM
 */
public class UICommand extends javax.faces.component.UICommand {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Command";

  public void processDecodes(FacesContext context) {

    if (context == null) {
      throw new NullPointerException();
    }

    // Skip processing if our rendered flag is false
    if (!isRendered()) {
      return;
    }

    // Process this component itself
    try {
      decode(context);
    } catch (RuntimeException e) {
      context.renderResponse();
      throw e;
    }

    Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(context);
    }

  }

  public void encodeChildren(FacesContext facesContext) throws IOException {
    if (isRendered()) {
      UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
    }
  }
}
