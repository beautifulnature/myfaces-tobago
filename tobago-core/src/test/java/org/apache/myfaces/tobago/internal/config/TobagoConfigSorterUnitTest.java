/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.apache.myfaces.tobago.internal.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TobagoConfigSorterUnitTest {

  @Test
  public void testCompare() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    final TobagoConfigFragment c = new TobagoConfigFragment();
    c.setName("c");

    final TobagoConfigFragment d = new TobagoConfigFragment();
    d.setName("d");

    final TobagoConfigFragment e = new TobagoConfigFragment();
    e.setName("e");

    final TobagoConfigFragment f = new TobagoConfigFragment();
    f.setName("f");

    final TobagoConfigFragment m = new TobagoConfigFragment();
    m.setName("m");

    final TobagoConfigFragment n = new TobagoConfigFragment();
    n.setName("n");

    // unnamed
    final TobagoConfigFragment u1 = new TobagoConfigFragment();
    final TobagoConfigFragment u2 = new TobagoConfigFragment();
    final TobagoConfigFragment u3 = new TobagoConfigFragment();

    // before
    a.getBefore().add("b");
    b.getBefore().add("c");

    u1.getBefore().add("d");
    u2.getBefore().add("d");

    u2.getBefore().add("y"); // not relevant

    // after
    e.getAfter().add("d");
    f.getAfter().add("e");

    u1.getAfter().add("c");
    u2.getAfter().add("c");

    u2.getAfter().add("z"); // not relevant

    n.getAfter().add("m");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);
    list.add(b);
    list.add(c);
    list.add(d);
    list.add(e);
    list.add(f);
    list.add(u1);
    list.add(u2);
    list.add(u3);
    list.add(m);
    list.add(n);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.createRelevantPairs();

    Assertions.assertEquals(9, sorter.getPairs().size()); // all but these with "z" and "y"

    sorter.makeTransitive();

    Assertions.assertEquals(28, sorter.getPairs().size());

    sorter.ensureIrreflexive();

    sorter.ensureAntiSymmetric();

    sorter.sort0();

    Assertions.assertEquals(a, list.get(0));
    Assertions.assertEquals(b, list.get(1));
    Assertions.assertEquals(c, list.get(2));
    Assertions.assertEquals(u1, list.get(3));
    Assertions.assertEquals(u2, list.get(4));
    Assertions.assertEquals(d, list.get(5));
    Assertions.assertEquals(e, list.get(6));
    Assertions.assertEquals(f, list.get(7));
    Assertions.assertEquals(u3, list.get(8));
    Assertions.assertEquals(m, list.get(9));
    Assertions.assertEquals(n, list.get(10));
  }

  @Test
  public void testCycle() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    // before
    a.getBefore().add("b");
    b.getBefore().add("a");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);
    list.add(b);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.createRelevantPairs();

    Assertions.assertEquals(2, sorter.getPairs().size()); // all but these with "z" and "y"

    sorter.makeTransitive();

    try {
      sorter.ensureIrreflexive();
      sorter.ensureAntiSymmetric();

      Assertions.fail("Cycle was not found");
    } catch (final RuntimeException e) {
      // must find the cycle
    }
  }

  @Test
  public void testCycle2() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    // before
    a.getBefore().add("b");
    // after
    a.getAfter().add("b");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);
    list.add(b);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.createRelevantPairs();

    Assertions.assertEquals(2, sorter.getPairs().size()); // all but these with "z" and "y"

    sorter.makeTransitive();

    try {
      sorter.ensureIrreflexive();
      sorter.ensureAntiSymmetric();

      Assertions.fail("Cycle was not found");
    } catch (final RuntimeException e) {
      // must find the cycle
    }
  }
}
