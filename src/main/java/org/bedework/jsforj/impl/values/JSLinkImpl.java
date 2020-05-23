/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.jsforj.impl.values;

import org.bedework.jsforj.model.JSPropertyNames;
import org.bedework.jsforj.model.values.JSLink;
import org.bedework.jsforj.model.values.dataTypes.JSUnsignedInteger;
import org.bedework.util.misc.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * User: mike Date: 10/25/19 Time: 12:45
 */
public class JSLinkImpl extends JSValueImpl
        implements JSLink {
  public JSLinkImpl(final String type,
                    final JsonNode node) {
    super(type, node);
  }

  @Override
  public void setHref(final String val) {
    setProperty(JSPropertyNames.href, val);
  }

  @Override
  public String getHref() {
    return getStringProperty(JSPropertyNames.href);
  }

  @Override
  public void setCid(final String val) {
    setProperty(JSPropertyNames.cid, val);
  }

  @Override
  public String getCid() {
    return getStringProperty(JSPropertyNames.cid);
  }

  @Override
  public void setContentType(final String val) {
    setProperty(JSPropertyNames.contentType, val);
  }

  @Override
  public String getContentType() {
    return getStringProperty(JSPropertyNames.contentType);
  }

  @Override
  public void setSize(final JSUnsignedInteger val) {
    setProperty(JSPropertyNames.size, val);
  }

  @Override
  public JSUnsignedInteger getSize() {
    return getProperty(new TypeReference<JSUnsignedInteger>() {},
                       JSPropertyNames.size).getValue();
  }

  @Override
  public void setRel(final String val) {
    setProperty(JSPropertyNames.rel, val);
  }

  @Override
  public String getRel() {
    return getStringProperty(JSPropertyNames.rel);
  }

  @Override
  public void setDisplay(final String val) {
    setProperty(JSPropertyNames.display, val);
  }

  @Override
  public String getDisplay() {
    return getStringProperty(JSPropertyNames.display);
  }

  @Override
  public void setTitle(final String val) {
    setProperty(JSPropertyNames.title, val);
  }

  @Override
  public String getTitle() {
    return getStringProperty(JSPropertyNames.title);
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof JSLink)) {
      return false;
    }

    var that = (JSLink)o;

    if (Util.cmpObjval(getHref(), that.getHref()) != 0) {
      return false;
    }

    if (Util.cmpObjval(getCid(), that.getCid()) != 0) {
      return false;
    }

    if (Util.cmpObjval(getContentType(), that.getContentType()) != 0) {
      return false;
    }

    if (Util.cmpObjval(getSize(), that.getSize()) != 0) {
      return false;
    }

    if (Util.cmpObjval(getRel(), that.getRel()) != 0) {
      return false;
    }

    if (Util.cmpObjval(getDisplay(), that.getDisplay()) != 0) {
      return false;
    }

    return Util.cmpObjval(getTitle(), that.getTitle()) == 0;
  }
}
