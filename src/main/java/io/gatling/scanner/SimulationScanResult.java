/*
 * Copyright 2011-2022 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gatling.scanner;

import java.util.List;
import java.util.Objects;

public class SimulationScanResult {

  private final List<String> simulationClasses;
  private final HighestJavaVersionClass highestJavaVersionClass;

  /**
   * @param simulationClasses Required, simulations fully qualified names. May be empty.
   * @param highestJavaVersionClass Nullable, the highest java version with fully qualified name
   *     available
   */
  public SimulationScanResult(
      List<String> simulationClasses, HighestJavaVersionClass highestJavaVersionClass) {
    this.simulationClasses = simulationClasses;
    this.highestJavaVersionClass = highestJavaVersionClass;
  }

  public List<String> getSimulationClasses() {
    return simulationClasses;
  }

  public HighestJavaVersionClass getHighestJavaVersionClass() {
    return highestJavaVersionClass;
  }

  @Override
  public String toString() {
    return "SimulationScanResult{"
        + "simulationClasses="
        + simulationClasses
        + ", highestJavaVersionClass="
        + highestJavaVersionClass
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SimulationScanResult that = (SimulationScanResult) o;
    return Objects.equals(simulationClasses, that.simulationClasses)
        && Objects.equals(highestJavaVersionClass, that.highestJavaVersionClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(simulationClasses, highestJavaVersionClass);
  }
}
