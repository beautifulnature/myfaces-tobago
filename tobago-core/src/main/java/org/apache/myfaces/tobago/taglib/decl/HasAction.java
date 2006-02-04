package org.apache.myfaces.tobago.taglib.decl;

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

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasAction {
  /**
   *
   * Action to invoke when clicked.
   * Depends on 'type' attribute:
   * If type is NOT 'navigate', 'reset' or 'script' this must be a
   * MethodBinding representing the application action to invoke when
   * this component is activated by the user.
   * The expression must evaluate to a public method that takes no parameters,
   * and returns a String (the logical outcome) which is passed to the
   * NavigationHandler for this application.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  void setAction(String action);
}
