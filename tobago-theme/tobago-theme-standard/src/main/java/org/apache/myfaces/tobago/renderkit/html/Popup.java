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

package org.apache.myfaces.tobago.renderkit.html;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.util.ComponentUtils;

public class Popup {

  /**
   * Can be "open" or "close"
   */
  private String command;

  /**
   * true for close immediately, false for close after submit
   */
  private Boolean immediate;

  private Popup(String command, Boolean immediate) {
    this.command = command;
    this.immediate = immediate;
  }

  public static Popup createPopup(AbstractUICommandBase component) {
    String command = null;
    Boolean immediate = null;

    final String popupClose = (String) component.getAttributes().get(Attributes.POPUP_CLOSE);
    if (popupClose != null) {
      command = "close";
      immediate = popupClose.equals("immediate");
    } else {
      boolean popupAction = ComponentUtils.containsPopupActionListener(component);
      if (popupAction) {
        command = "open";
      }
    }
    if (command != null) {
      return new Popup(command, immediate);
    } else {
      return null;
    }
  }

  public String getCommand() {
    return command;
  }

  public Boolean isImmediate() {
    return immediate;
  }
}
