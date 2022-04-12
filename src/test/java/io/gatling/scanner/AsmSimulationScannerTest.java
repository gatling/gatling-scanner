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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.*;

public class AsmSimulationScannerTest {

  private File testJar(String name) throws URISyntaxException {
    return Paths.get(AsmSimulationScannerTest.class.getResource("/" + name + ".jar").toURI())
        .toFile();
  }

  @Test
  public void findAllSimulationFullyQualifiedNames()
      throws UnsupportedJavaMajorVersionException, URISyntaxException, IOException {
    assertEquals(
        AsmSimulationScanner.simulationFullyQualifiedNamesFromFile(testJar("gatling-simulations")),
        Arrays.asList("computerdatabase.ConcreteSimulation", "computerdatabase.BasicSimulation"));
  }

  @Test
  public void rejectJava18() {
    assertThrows(
        UnsupportedJavaMajorVersionException.class,
        () ->
            AsmSimulationScanner.simulationFullyQualifiedNamesFromFile(
                testJar("simulation-java-18")));
  }
}
