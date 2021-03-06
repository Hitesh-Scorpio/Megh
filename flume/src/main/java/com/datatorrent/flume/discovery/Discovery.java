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
package com.datatorrent.flume.discovery;

import java.util.Collection;

/**
 * When DTFlumeSink server instance binds to the network interface, it can publish
 * its whereabouts by invoking advertise method on the Discovery object. Similarly
 * when it ceases accepting any more connections, it can publish its intent to do
 * so by invoking unadvertise.<p />
 * Interesting parties can call discover method to get the list of addresses where
 * they can find an available DTFlumeSink server instance.
 *
 * @author Chetan Narsude <chetan@datatorrent.com>
 * @param <T> - Type of the objects which can be discovered
 * @since 0.9.3
 */
public interface Discovery<T>
{
  /**
   * Recall the previously published address as it's no longer valid.
   *
   * @param service
   */
  void unadvertise(Service<T> service);

  /**
   * Advertise the host/port address where DTFlumeSink is accepting a client connection.
   *
   * @param service
   */
  void advertise(Service<T> service);

  /**
   * Discover all the addresses which are actively accepting the client connections.
   *
   * @return - Active server addresses which can accept the connections.
   */
  Collection<Service<T>> discover();

  interface Service<T>
  {
    String getHost();

    int getPort();

    T getPayload();

    String getId();

  }

}
