/**
 * Copyright (c) 2016 DataTorrent, Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.contrib.hdht;

import java.util.Comparator;

/**
 * Range is defined by start and end inclusive of start and end.
 * This class contains helper methods to perform various operations
 * on Range.
 *
 * @since 3.3.0
 *
 * @param <T>
 */
class Range<T>
{
  T start;
  T end;

  public Range(T start, T end)
  {
    this.start = start;
    this.end = end;
  }

  public boolean contains(T key, Comparator<T> cmp)
  {
    return ((cmp.compare(start, key) <= 0) && (cmp.compare(end, key) >= 0));
  }

  public boolean subsetOf(Range<T> p, Comparator<T> cmp)
  {
    return ((cmp.compare(start, p.start) >= 0) && (cmp.compare(end, p.end) <= 0));
  }

  public boolean overlapsWith(Range<T> o, Comparator<T> cmp)
  {
    return !((cmp.compare(start, o.end) > 0) || (cmp.compare(end, o.start) < 0));
  }

  public boolean equal(Range<T> o, Comparator<T> cmp)
  {
    return (cmp.compare(start, o.start) == 0 && (cmp.compare(end, o.end) == 0));
  }

  public void extend(Range<T> range, Comparator<T> cmp)
  {
    start = (cmp.compare(start, range.start) < 0) ? start : range.start;
    end = (cmp.compare(end, range.end) < 0) ? range.end : end;
  }

  /**
   * RangeComparator
   *
   * This comparator is helpful to sort the ranges by their
   * start value. It takes individual element comparator for comparing
   * start and last values.
   * 
   * @since 3.3.0
   *
   * @param <T>
   */
  public static class RangeComparator<T> implements Comparator<Range<T>>
  {

    Comparator<T> cmp;

    public RangeComparator(Comparator<T> cmp)
    {
      this.cmp = cmp;
    }

    @Override
    public int compare(Range<T> o1, Range<T> o2)
    {
      /* consider null start key as lowest key */
      if (o1.start == null) {
        return -1;
      }
      if (o2.start == null) {
        return 1;
      }
      int res = cmp.compare(o1.start, o2.start);
      if (res != 0) {
        return res;
      }
      /* consider null end key as a higher key */
      if (o1.end == null) {
        return 1;
      }
      if (o2.end == null) {
        return -1;
      }
      return cmp.compare(o1.end, o2.end);
    }
  }
}
