/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 24.06.2003 08:53:35.
 * Id: $
 */
package com.atanion.tobago.config;

import com.atanion.tobago.context.Theme;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TobagoConfigParser {

  private static final Log LOG = LogFactory.getLog(TobagoConfigParser.class);

  public static void parse(ServletContext context, TobagoConfig tobagoConfig) {

    Digester digester = new Digester();
    configure(tobagoConfig, digester);
    parse(context, digester);
  }

  private static Digester configure(TobagoConfig config, Digester digester) {

    digester.push(config);
    digester.setValidating(true);

    digester.addObjectCreate("tobago-config/theme", Theme.class);
    digester.addSetProperties("tobago-config/theme");
    digester.addSetNext("tobago-config/theme", "addTheme");
    digester.addCallMethod("tobago-config/theme/name", "setName", 0);
    digester.addCallMethod(
        "tobago-config/theme/display-name", "setDisplayName", 0);
    digester.addCallMethod("tobago-config/theme/fallback", "setFallback", 0);

    digester.addObjectCreate("tobago-config/mapping-rule", MappingRule.class);
    digester.addSetNext("tobago-config/mapping-rule", "addMappingRule");
    digester.addCallMethod(
        "tobago-config/mapping-rule/request-uri", "setRequestUri", 0);
    digester.addCallMethod(
        "tobago-config/mapping-rule/forward-uri", "setForwardUri", 0);
    digester.addObjectCreate(
        "tobago-config/mapping-rule/attribute", Attribute.class);
    digester.addSetNext(
        "tobago-config/mapping-rule/attribute", "addAttribute");
    digester.addCallMethod(
        "tobago-config/mapping-rule/attribute/key", "setKey", 0);
    digester.addCallMethod(
        "tobago-config/mapping-rule/attribute/value", "setValue", 0);

    return digester;
  }

  private static void parse(ServletContext context, Digester digester) {

    final String configPath = "/WEB-INF/tobago-config.xml";
    InputStream input = null;
    registerDtd(digester);
    try {
      input = context.getResourceAsStream(configPath);
      if (input != null) {
        digester.parse(input);
      } else {
        if (LOG.isInfoEnabled()) {
          LOG.info(
              "No config file found: '" + configPath + "'. " +
              "Continuing without TobagoConfig");
        }
      }
    } catch (Throwable e) {
      LOG.error(configPath, e);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          ;
        }
      }
    }
  }

  private static void registerDtd(Digester digester) {

    final String TOBAGO_CONFIG_DTD
        = "/com/atanion/tobago/config/tobago-config_1_0.dtd";
    URL url = TobagoConfigParser.class.getResource(TOBAGO_CONFIG_DTD);
    if (LOG.isDebugEnabled()) {
      LOG.debug("registering dtd: url=" + url);
    }
    if (null != url) {
      digester.register(
          "-//Atanion GmbH//DTD Tobago Config 1.0//EN",
          url.toString());
    } else {
      LOG.warn(
          "unable to retrieve local DTD '" + TOBAGO_CONFIG_DTD
          + "'; trying external URL");
    }
  }

}
