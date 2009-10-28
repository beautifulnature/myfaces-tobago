package org.apache.myfaces.tobago.taglib.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasRenderRange;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;

/*
 * Created: Aug 5, 2005 6:11:03 PM
 * User: bommel
 * $Id$
 */
/**
 * Render a set of radiobuttons.
 */
@Tag(name = "selectOneRadio")
@BodyContentDescription(anyTagOf = "(<f:selectItems>|<f:selectItem>|<tc:selectItem>)+ <f:facet>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISelectOneRadio",
    uiComponentBaseClass = "org.apache.myfaces.tobago.component.AbstractUISelectOneRadio",
    rendererType = RendererTypes.SELECT_ONE_RADIO,
    allowedChildComponenents = {
        "javax.faces.SelectItem",
        "javax.faces.SelectItems"
        },
    facets = {
    @Facet(name= Facets.CLICK,
        description =
            "This facet can contain a UICommand that is invoked in case of a click event from the component",
        allowedChildComponenents = "org.apache.myfaces.tobago.Command"),
    @Facet(name=Facets.CHANGE,
        description =
            "This facet can contain a UICommand that is invoked in case of a change event from the component",
        allowedChildComponenents = "org.apache.myfaces.tobago.Command")
        })
public interface SelectOneRadioTagDeclaration extends SelectOneTagDeclaration, IsDisabled, IsReadonly, HasId,
    IsInline, HasRenderRange, IsRendered, HasBinding, HasConverter {

  /**
   * Flag indicating that selecting an Item representing a Value is Required.
   * If an SelectItem was choosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  void setRequired(String required);
}
