package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.LayoutManager;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.component.UIGridLayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:25:02 PM
 * User: bommel
 * $Id$
 */
public class GridLayoutRenderer extends FoRendererBase
    implements LayoutManager {
  private static int suppe = 0;
  private static final Log LOG = LogFactory.getLog(GridLayoutRenderer.class);

  public boolean getRendersChildren() {
    return false;
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** end      " + component);
    }
    try {
      layoutEnd(facesContext, component);

    } catch (RuntimeException e) {
      LOG.error("catched " + e + " :" + e.getMessage(), e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("*   end      " + component);
    }
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** begin    " + component);
    }
    try {


      layoutBegin(facesContext, component);
    } catch (RuntimeException e) {
      LOG.error("catched RuntimeException :", e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("*   begin    " + component);
    }
  }

  public void layoutBegin(FacesContext facesContext, UIComponent component)  {

  }



  public void layoutEnd(FacesContext facesContext, UIComponent component) {
    Layout layout = Layout.getLayout(component.getParent());
    if (layout == null) {
      throw new IllegalStateException("no Layout from "+component.getParent()+" "+component.getParent().getClientId(facesContext));
    }
    layout.setOrientation(Layout.TOP_ORIENTATION);
    if (component.getAttributes().get("columns")!=null) {
       layout.setOrientation(Layout.LEFT_ORIENTATION);
    }
    Layout.putLayout(component, layout);
    LOG.error(layout);
    //new Exception().printStackTrace();
    //suppe++;
    //if (suppe==10) {
    //  throw new IllegalStateException("");
    //}
    List children = component.getParent().getChildren();
    ResponseWriter writer = facesContext.getResponseWriter();
    LOG.error("parent ist " +
        component.getParent() + "|"+ component.getParent().getClientId(facesContext)+
        " component ist " + component + "|"+component.getClientId(facesContext));
    if (children.size()>0) {
      int incrementX = layout.getWidth()/children.size();
      int incrementY = layout.getHeight()/children.size();
      int height = layout.getHeight();
      int width = layout.getWidth();
      int x = layout.getX();
      int y = layout.getY();

    for (int i = 0; i<children.size();i++) {
      LOG.error("i = " + i + " size = " + children.size());

      UIComponent cell = (UIComponent) children.get(i);
      //LOG.error(cell+ " | "+cell.getClientId(facesContext));
      if (! (cell instanceof UIMessages||cell instanceof UIGridLayout)) {
      try {
        FoUtils.startBlockContainer(writer, component);
        if (layout.getOrientation()==Layout.TOP_ORIENTATION) {

          FoUtils.layoutBlockContainer(writer, incrementY,
              width, x, y+incrementY*i);
        } else {
          FoUtils.layoutBlockContainer(writer, height,
              incrementY, x+incrementX*i, y);
        }
        Layout.setInLayout(cell, true);
        
        encodeSuppe(facesContext, cell);

        FoUtils.endBlockContainer(writer);
      } catch (IOException e) {
        LOG.error("", e);
      }
      }
    }
    }

  }

  private void encodeSuppe(FacesContext facesContext, UIComponent component) throws IOException {
    if (component.getRendersChildren()) {
      component.encodeBegin(facesContext);
      component.encodeChildren(facesContext);
    } else {
      Iterator kids = component.getChildren().iterator();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        encodeSuppe(facesContext, kid);
      }
    }
    component.encodeEnd(facesContext);
  }

}
