<%--
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
--%>

<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<f:view>
  <tc:page >
    <tc:panel id="panel">
      <f:facet name="layout">
        <tc:gridLayout margin="10px" rows="fixed;fixed;1*" columns="fixed;fixed;*"/>
      </f:facet>
        <tc:selectOneChoice value="#{bbg.value}" >
          <f:selectItem itemValue="Audi" itemLabel="Audi"/>
          <f:selectItem itemValue="Mercedes" itemLabel="Mercedes"/>
          <f:facet name="click" >
            <tc:command>
              <tc:attribute name="renderedPartially" value="::panel"/>
            </tc:command>
          </f:facet>
        </tc:selectOneChoice>
        <tc:in value="#{bbg.value}" readonly="true" />
        <tc:cell/>
        <tc:messages/>
        <tc:cell/>
    </tc:panel>
  </tc:page>
</f:view>