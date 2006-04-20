package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasVar;
import org.apache.myfaces.tobago.util.BundleMapWrapper;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * Load a resource bundle localized for the Locale of the current view
 * from the tobago resource path, and expose it (as a Map) in the request
 * attributes of the current request.
 */
@Tag(name = "loadBundle", bodyContent = BodyContent.EMPTY)
public class LoadBundleTag extends TagSupport implements HasVar {

  private String basename;

  private String var;

  public int doStartTag() throws JspException {
    Map toStore = new BundleMapWrapper(basename);
    FacesContext.getCurrentInstance().getExternalContext()
        .getSessionMap().put(var, toStore);
//        .getRequestMap().put(var, toStore);

    return EVAL_BODY_INCLUDE;
  }

  public void release() {
    basename = null;
    var = null;
  }

  public String getBasename() {
    return basename;
  }

  /**
   * Base name of the resource bundle to be loaded.
   */
  @TagAttribute(required = true)
  public void setBasename(String basename) {
    this.basename = basename;
  }

  public String getVar() {
    return var;
  }
  /**
   *
   * Name of a session-scope attribute under which the bundle data
   * will be exposed.
   *
   */
  @TagAttribute(required = true)
  public void setVar(String var) {
    this.var = var;
  }

}

