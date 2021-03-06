package org.bedework.jsforj.model.values;

import org.bedework.jsforj.JsforjException;
import org.bedework.jsforj.model.JSProperty;
import org.bedework.jsforj.model.values.dataTypes.JSUnsignedInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * User: mike Date: 10/24/19 Time: 10:21
 */
public interface JSValue {
  /**
   * Flags this value is being generated for overrides. Will be
   * skipped when generating patches.
   */
  void markOverrideGenerated();

  /**
   *
   * @return true if this is fenerated for overrides.
   */
  boolean getOverrideGenerated();

  /**
   *
   * @return type passed to constructor
   */
  String getObjectType();

  /**
   *
   * @return node which currently represents this object
   */
  JsonNode getNode();

  /**
   * Called before we output the object. Objects MUST call all
   * children to allow any processing before output - e.g. generate
   * patches.
   */
  void preWrite();

  /**
   * @return true if this value was changed - i.e a value was changed
   * or a sub-property added or removed.
   */
  boolean getChanged();

  /**
   * @return true if this or any sub-value was changed.
   */
  boolean hasChanges();

  /**
   * @return next value up in hierarchy
   */
  JSValue getOwner();

  /**
   *
   * @return property containing this value.
   */
  JSProperty<?> getParentProperty();

  /**
   *
   * @return the type of the value
   */
  String getType();

  /**
   *
   * @param propertyName to test for
   * @return true if value contains named property
   */
  boolean hasProperty(String propertyName);

  /** Return all contained properties
   *
   * @return properties
   * throws JsforjException if not an object
   */
  List<JSProperty<?>> getProperties();

  /** Return named property
   *
   * @param type expected type
   * @param name of property
   * @return property or null
   * throws JsforjException if not an object
   */
  <T extends JSValue> JSProperty<T> getProperty(
          TypeReference<T> type,
          String name);

  /** Return named property
   *
   * @param name of property
   * @return property or null
   * throws JsforjException if not an object
   */
  JSProperty<?> getProperty(String name);

  /** Return a deep copy of this value
   *
   * @return value
   */
  default JSValue copy() {
    try {
      final Constructor<?> constructor =
              getClass().getConstructor(String.class,
                                        JsonNode.class);
      return (JSValue)constructor.newInstance(getObjectType(),
                                              getNode().deepCopy());
    } catch (final Throwable t) {
      throw new JsforjException("Exception thrown creating JSValue copy");
    }
  }

  /** Remove named property
   *
   * throws JsforjException if not an object
   */
  void removeProperty(String name);

  /**
   * Remove all contained properties and values
   */
  void clear();

  /** Add or replace the named property
   *
   * @param val the property - non null
   * @return the property
   */
  <ValType extends JSValue> JSProperty<ValType> setProperty(
          JSProperty<ValType> val);

  /** Set the value for a string type property
   *
   * @param name the property name - non null
   * @param val the property value - non null
   * @return the property
   */
  JSProperty<?> setProperty(String name, String val);

  /** Get the value. Return null if absent.
   *
   * @param name of property
   * @return the value or null
   */
  JSValue getPropertyValue(String name);

  /** Returns value of named boolean property.
   *
   * @param name the property name - non null
   * @return the value of the property - false if absent
   * throws JsforjException if not a boolean property
   */
  boolean getBooleanProperty(String name);

  /** Returns value of named String property.
   *
   * @param name the property name - non null
   * @return the value of the property
   * throws JsforjException if not a String property
   */
  String getStringProperty(String name);

  /**
   *
   * @return true if this is a string
   */
  boolean isString();

  /** Set the value for an JSValue type property
   *
   * @param name the property name - non null
   * @param val the property value - non null
   * @return the property
   */
  JSProperty<?> setProperty(String name, JSValue val);

  /** Set the value for an UnsignedInteger type property
   *
   * @param name the property name - non null
   * @param val the property value - non null
   * @return the property
   */
  JSProperty<?> setProperty(String name, Integer val);

  /** Set the value for a boolean type property
   *
   * @param name the property name - non null
   * @param val the property value
   * @return the property
   */
  JSProperty<?> setProperty(String name, boolean val);

  /** Add a property of given type.
   *
   * @param name the property name - non null
   * @param type the property type
   * @return the property
   * throws JsforjException if property already exists
   */
  <T extends JSValue> JSProperty<T> makeProperty(
          TypeReference<T> typeRef,
          String name, String type);

  /** Create a property of the given type. NOT added or set
   *
   * @param name the property name - non null
   * @param type the property type
   * @return the property
   * throws JsforjException if property already exists
   */
  <T extends JSValue> JSProperty<T> newProperty(
          TypeReference<T> typeRef,
          String name, String type);

  /** Returns value of named UnsignedInteger property.
   *
   * @param name the property name - non null
   * @return the value of the property
   * throws JsforjException if not a String property
   */
  JSUnsignedInteger getUnsignedIntegerProperty(String name);

  /** Returns value as a String.
   *
   * @return the value of the property
   * throws JsforjException if not a String property
   */
  String getStringValue();

  /** Returns value as a boolean.
   *
   * @return the value of the property
   * throws JsforjException if not a boolean property
   */
  boolean getBooleanValue();

  /** Convert to json with this as root
   *
   * @param wtr to write to
   * @param mapper to convert
   */
  void writeValue(Writer wtr,
                  ObjectMapper mapper);

  /** Convert to json with this as root
   *
   * @param mapper to convert
   * @return Json
   */
  String writeValueAsString(ObjectMapper mapper);

  /** Convert to formatted json with this as root
   *
   * @param mapper to convert
   * @return Json
   */
  String writeValueAsStringFormatted(ObjectMapper mapper);

  /** Returns a value of the desired type and adds as a sub-property
   * of this value.
   *
   * @param type reference
   * @param pname property name
   * @param create true if it shoudl be created if absent
   * @param <T> type of value
   * @return value
   */
  <T extends JSValue> T getValue(TypeReference<T> type,
                                 String pname,
                                 boolean create);
}
