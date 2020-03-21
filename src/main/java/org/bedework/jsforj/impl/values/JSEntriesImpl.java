/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.jsforj.impl.values;

import org.bedework.jsforj.model.JSCalendarObject;
import org.bedework.jsforj.model.values.JSEntries;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mike Date: 10/25/19 Time: 12:45
 */
public class JSEntriesImpl extends JSValueImpl
        implements JSEntries {
  public JSEntriesImpl(final String type,
                       final JsonNode node) {
    super(type, node);
  }

  @Override
  public List<JSCalendarObject> getEntries() {
    var entries = new ArrayList<JSCalendarObject>();
    var nd = getNode();

    for (var it = nd.fieldNames(); it.hasNext(); ) {
      var fieldName = it.next();

      //TODO - check validity?

      entries.add(factory.makeCalObj(nd.findValue(fieldName)));
    }

    return entries;
  }

  @Override
  public void addEntry(final JSCalendarObject val) {
    throw new RuntimeException("Not implemented");
  }
}