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

package org.apache.myfaces.tobago.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.faces.component.UIComponent;
import java.lang.reflect.Method;

public class AttributesOfGeneratedUIComponentsUnitTest extends AbstractGeneratedUIComponentsUnitTest {

  @Test
  public void test() {

    for (final Class<? extends UIComponent> uiComponent : getUiComponents()) {
      final Method[] methods = uiComponent.getMethods();
      for (final Method method : methods) {

        if (!method.getDeclaringClass().equals(uiComponent)) {
          // check only the method, that have an implementation in the generated class
          continue;
        }

        final String methodName = method.getName();
        if (!methodName.startsWith("set") || methodName.length() <= 3) {
          continue;
        }

        String property = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        if (property.equals("for")) {
          property = "forValue";
        }
        final Attributes attribute;
        try {
          attribute = Attributes.valueOf(property);
        } catch (final IllegalArgumentException e) {
          Assertions.fail(e.getMessage());
        }

      }
    }
  }

}
