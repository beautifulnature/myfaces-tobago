/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:21:55.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.mock.servlet.MockPageContext;
import com.atanion.util.jsp.TldParser;
import com.atanion.util.Resource;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import javax.servlet.jsp.tagext.TagInfo;
import javax.servlet.jsp.tagext.TagAttributeInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;

public class GenericTagUnitTest extends GenericTestBase {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(GenericTagUnitTest.class);

// --------------------------------------------------------------- constructors

  public GenericTagUnitTest(String name) {
    super(name);
  }

// ----------------------------------------------------------- business methods

  private TagLibraryInfo getTagLibraryInfo() throws IOException, SAXException {
    InputStream tld = Resource.getInputStream("com/atanion/tobago/taglib/component/tobago.tld");
    assertNotNull(tld);
    TldParser parser = new TldParser();
    TagLibraryInfo tagLibraryInfo = parser.parse(tld);
    return tagLibraryInfo;
  }

  public void testReleaseOrdinaryTags() throws IllegalAccessException,
      NoSuchMethodException, InvocationTargetException, IOException,
      SAXException {
    for (int i = 0; i < ordinaryTagList.length; i++) {
      Tag tag = ordinaryTagList[i];
      testRelease(tag);
    }
  }

  public void testReleaseUIComponentTags() throws IllegalAccessException,
      NoSuchMethodException, InvocationTargetException, IOException,
      SAXException {
    for (int i = 0; i < componentTagList.length; i++) {
      Tag tag = componentTagList[i];
      testRelease(tag);
    }
  }

  public void testSetterExist() throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, IOException,
      SAXException, ClassNotFoundException, InstantiationException {

    TagLibraryInfo tagLibraryInfo = getTagLibraryInfo();
    TagInfo[] tags = tagLibraryInfo.getTags();

    for (int i = 0; i < tags.length; i++) {
      TagInfo tag = tags[i];
      String className = tag.getTagClassName();
      TagAttributeInfo[] attributes = tag.getAttributes();
      for (int j = 0; j < attributes.length; j++) {
        TagAttributeInfo attribute = attributes[j];
        String name = attribute.getName();
        checkSetter(className, name);
      }
    }
  }

  private void checkSetter(String className, String name)
      throws IllegalAccessException, InstantiationException,
      NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
    Class tagClass = Class.forName(className);
    Tag tagObject = (Tag) tagClass.newInstance();
    PropertyDescriptor propertyDescriptor
        = PropertyUtils.getPropertyDescriptor(tagObject, name);
    assertNotNull("setter '" + name + "' of class " + className + " exists.",
        propertyDescriptor.getWriteMethod());
  }

  private void testRelease(Tag tag) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, IOException,
      SAXException {
    tag.setPageContext(new MockPageContext());

    HashMap initialValues = new HashMap();
    PropertyDescriptor descriptors[] =
        PropertyUtils.getPropertyDescriptors(tag);
//    TagLibraryInfo tagLibraryInfo = getTagLibraryInfo();
//    TagInfo[] tags = tagLibraryInfo.getTags();

    // store initial values
    for (int i = 0; i < descriptors.length; i++) {
      if (isTagProperty(descriptors[i])) {
        String name = descriptors[i].getName();

        Object value = PropertyUtils.getSimpleProperty(tag, name);
        initialValues.put(name, value);
      }
    }

    // set new values
    for (int i = 0; i < descriptors.length; i++) {
      if (isTagProperty(descriptors[i])) {
        String name = descriptors[i].getName();

        Class propertyType = descriptors[i].getPropertyType();
        Object value = null;
        if (propertyType == String.class) {
          value = new String("bla");
        } else if (propertyType == Integer.TYPE) {
          value = new Integer(42);
        } else if (propertyType == Boolean.TYPE) {
          value = Boolean.TRUE;
        } else {
          LOG.debug("Unsupported property type '" + propertyType
              + "' for property '" + name + "'");
        }
        PropertyUtils.setSimpleProperty(tag, name, value);
      }
    }

    tag.release();

    // check released values
    for (int i = 0; i < descriptors.length; i++) {
      if (isTagProperty(descriptors[i])) {
        String name = descriptors[i].getName();
        // XXX: who releases id?
        if (name.equals("id")) continue;
        try {
          Object newValue = PropertyUtils.getSimpleProperty(tag, name);
          Object oldValue = initialValues.get(name);
          String msg = "release of property '" + name + "' for tag '"
              + tag.getClass().getName() + "' failed.";
          assertEquals(msg, oldValue, newValue);
          // XXX: first error stops loop
          // if (newValue != null && !newValue.equals(oldValue)) {
        } catch (NoSuchMethodException e1) {
          LOG.error("", e1);
        }
      }
    }
  }

  private boolean isTagProperty(PropertyDescriptor descriptor) {
    if ("parent".equals(descriptor.getName())) {
      return false;
    } else {
      return descriptor.getReadMethod() != null
          && descriptor.getWriteMethod() != null;
    }
  }
}

