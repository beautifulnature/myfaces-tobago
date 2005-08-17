<%--
 * Copyright 2002-2005 atanion GmbH.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
--%>
<%@ page import="org.apache.myfaces.tobago.example.demo.jsp.JspFormatter,
                 java.io.InputStreamReader,
                 java.io.PrintWriter"
%><%@ page errorPage="/errorPage.jsp"
%><%
  String jsp = request.getParameter("jsp");

  if (jsp == null || jsp.length() == 0) {
    throw new RuntimeException("There is no 'jsp' parameter in the request!");
  }

  JspFormatter.writeJsp(
      new InputStreamReader(
          pageContext.getServletContext().getResourceAsStream(jsp)),
      new PrintWriter(out));
%>
