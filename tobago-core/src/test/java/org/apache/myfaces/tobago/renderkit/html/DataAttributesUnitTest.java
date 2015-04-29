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

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class DataAttributesUnitTest {

  @Test
  public void testAttributeNames() throws IllegalAccessException {
    for (final Field field : DataAttributes.class.getFields()) {

      if (field.getAnnotation(Deprecated.class) != null) {
        // ignore the check for deprecated fields
        continue;
      }
      final String value = (String) field.get(null);
      Assert.assertTrue("Regexp check: value='" + value + "'", value.matches("data(-tobago)?(-[a-z0-9]+)*-[a-z0-9]+"));

      final String extension
          = value.startsWith("data-tobago-")
          ? value.substring("data-tobago-".length())
          : value.substring("data-".length());
      final String name = field.getName();
      Assert.assertEquals(name, extension.toUpperCase().replaceAll("-", "_"));
    }
  }
}
