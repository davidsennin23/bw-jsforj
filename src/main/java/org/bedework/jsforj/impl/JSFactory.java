/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.jsforj.impl;

import org.bedework.jsforj.impl.properties.JSPropertyImpl;
import org.bedework.jsforj.impl.values.JSEventImpl;
import org.bedework.jsforj.impl.values.JSGroupImpl;
import org.bedework.jsforj.impl.values.JSTaskImpl;
import org.bedework.jsforj.impl.values.JSValueImpl;
import org.bedework.jsforj.model.JSCalendarObject;
import org.bedework.jsforj.model.JSEvent;
import org.bedework.jsforj.model.JSGroup;
import org.bedework.jsforj.model.JSProperty;
import org.bedework.jsforj.model.JSPropertyNames;
import org.bedework.jsforj.model.JSTask;
import org.bedework.jsforj.model.JSTypes;
import org.bedework.jsforj.model.values.JSValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mike Date: 10/24/19 Time: 10:51
 */
public class JSFactory {
  private final static JSFactory factory = new JSFactory();

  private final static JsonNodeFactory nodeFactory =
          JsonNodeFactory.withExactBigDecimals(false);

  private final static Map<Class, JSValueFactory> valueFactories =
          new HashMap<>();

  public static JSFactory getFactory() {
    return factory;
  }

  public JSCalendarObject makeCalObj(final JsonNode nd) {
    if (!nd.isObject()) {
      throw new RuntimeException("Not a calendar object");
    }

    final String type = factory.getType(nd);

    switch (type) {
      case JSTypes.typeJSEvent:
        return parseEvent(nd);
      case JSTypes.typeJSTask:
        return parseTask(nd);
      case JSTypes.typeJSGroup:
        return parseGroup(nd);
      default:
        throw new RuntimeException(
                "Unknown or unsupported type: " +
                        type);
    }
  }

  public JSValue makePropertyValue(final String propertyName,
                                   final JsonNode nd) {
    var typeInfo = JSPropertyAttributes.getPropertyTypeInfo(propertyName);

    final String type;
    if (typeInfo == null) {
      if ((nd == null) || (!nd.isObject())) {
        type = JSTypes.typeUnknown;
      } else {
        type = getType(nd);
      }
    } else {
      var types = typeInfo.getTypes();
      if (types.size() == 1) {
        type = types.get(0);
      } else {
        // Better be object
        if (!nd.isObject()) {
          throw new RuntimeException("Cannot determine type for " + nd);
        }

        type = getType(nd);
        if (!types.contains(type)) {
          throw new RuntimeException("Invalid type for " + nd);
        }
      }
    }

    return newValue(type, nd);
  }

  public JSProperty makeProperty(final String propertyName,
                                 final JsonNode nd) {
    //final var pInfo = JSPropertyAttributes.getPropertyTypeInfo(name);
    final var value = makePropertyValue(propertyName, nd);

    return new JSPropertyImpl(propertyName, value);
  }

  public JSValue newStringValue(final String val) {
    return new JSValueImpl(JSTypes.typeString,
                           nodeFactory.textNode(val));
  }

  public JSValue newValue(final String type,
                          final List<JSValue> val) {
    final ArrayNode nd = nodeFactory.arrayNode(val.size());

    for (final var el: val) {
      nd.add(((JSValueImpl)el).getNode());
    }

    return new JSValueImpl(type, nd);
  }

  public JSValue newValue(final String type) {
    return newValue(type, (JsonNode)null);
  }

  public JSValue newValue(final String type,
                          final JsonNode node) {
    final var typeInfo = JSPropertyAttributes.getTypeInfo(type);

    if (typeInfo == null) {
      return new JSValueImpl(type, node);
    }

    final var factoryClass = typeInfo.getFactoryClass();

    if (factoryClass == null) {
      // Use generic class.
      return new JSValueImpl(type, node);
    }

    JSValueFactory vfactory = valueFactories.get(factoryClass);

    if (vfactory == null) {
      try {
        vfactory =
                (JSValueFactory)factoryClass
                        .getConstructor().newInstance();
      } catch (final Throwable t) {
        throw new RuntimeException(t);
      }

      valueFactories.put(factoryClass, vfactory);
    }

    return vfactory.newValue(type, node);
  }

  public String getType(final JsonNode nd) {
    final JsonNode typeNode = nd.get(JSPropertyNames.type);

    if (typeNode == null) {
      throw new RuntimeException("No @type for calendar object: " + nd);
    }

    if (!typeNode.isTextual()) {
      throw new RuntimeException("Wrong type for @type");
    }

    return typeNode.asText();
  }

  private JSEvent parseEvent(final JsonNode nd) {
    final JSEventImpl ent = new JSEventImpl(JSTypes.typeJSEvent,
                                            nd);

    //parseProperties(ent, nd);

    return ent;
  }

  private JSTask parseTask(final JsonNode nd) {
    final JSTaskImpl ent = new JSTaskImpl(JSTypes.typeJSTask,
                                          nd);

    //parseProperties(ent, nd);

    return ent;
  }

  private JSGroup parseGroup(final JsonNode nd) {
    final JSGroupImpl ent = new JSGroupImpl(JSTypes.typeJSGroup,
                                            nd);

    //parseProperties(ent, nd);

    return ent;
  }

  void parseProperties(final JSValue val,
                       final JsonNode nd) {
    for (var it = nd.fieldNames(); it.hasNext(); ) {
      var fieldName = it.next();

      //TODO - check validity?

      JsonNode fldNode = nd.findValue(fieldName);
      val.addProperty(factory.makeProperty(fieldName, fldNode));
    }
  }
}
