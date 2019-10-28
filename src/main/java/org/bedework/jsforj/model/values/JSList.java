package org.bedework.jsforj.model.values;

import java.util.List;

/** Have type xxx[Boolean]
 *
 * User: mike Date: 10/25/19 Time: 12:46
 */
public interface JSList<T> extends JSValue {
  /**
   * Returns the number of elements in this list.  If this list contains
   * more than {@code Integer.MAX_VALUE} elements, returns
   * {@code Integer.MAX_VALUE}.
   *
   * @return the number of elements in this list
   */
  int size();

  /**
   *
   * @return all the values or empty list
   */
  List<T> get();

  /**
   *
   * @param index of entry
   * @return the value
   * @throws RuntimeException if index out of bounds
   */
  T get(int index);

  /**
   *
   * @param val to be added to list
   */
  void add(T val);

  /**
   *
   * @param val to remove
   * @throws RuntimeException if index out of bounds
   */
  void remove(T val);
}
