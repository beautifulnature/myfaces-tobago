/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 09.03.2004 12:26:39.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.util.Iterator;

public abstract class BoxRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(BoxRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }


  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    // wenn hoehe gesetzt dann diese,
    // sonst wenn layout vorhanden dieses fragen:
    //       -> aus rowLayout berechnen
    // sonst Warnung ausgebenn und addition der children's fixedHeight

    int height =
        ComponentUtil.getIntAttribute(component, TobagoConstants.ATTR_HEIGHT, -1);
    if (height != -1) {
      return height;
    }

    // ask layoutManager
    UIComponent layout = component.getFacet("layout");
    if (layout != null) {
      RendererBase renderer = ComponentUtil.getRenderer(layout, facesContext);
      height = renderer.getFixedHeight(facesContext, layout);
      if (height > -1) {
        return height;
      }
    }

    if (LOG.isInfoEnabled()) {
      LOG.info("Can't calculate fixedHeight! "
          + "using estimation by contained components. ");
    }

    height = 0;
    for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();) {
      UIComponent child = (UIComponent) iterator.next();
      RendererBase renderer = ComponentUtil.getRenderer(child, facesContext);
      if (renderer == null
          && child instanceof UINamingContainer
          && child.getChildren().size() > 0) {
        // this is a subview component ??
        renderer = ComponentUtil.getRenderer(
            (UIComponent) child.getChildren().get(0), facesContext);
      }
      if (renderer != null) {
        int h = renderer.getFixedHeight(facesContext, child);
        if (h > 0) {
          height += h;
        }
      }
    }
    height += getHeaderHeight(facesContext, component);
    height += getPaddingHeight(facesContext, component);
    return height;
  }

// ///////////////////////////////////////////// bean getter + setter

}
