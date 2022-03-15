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
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsmSimulationScannerTest {

  private File testFile;

  @Test
  public void findAllSimulationFullyQualifiedNames() throws IOException {
    assertEquals(
        AsmSimulationScanner.simulationFullyQualifiedNamesFromFile(testFile),
        Arrays.asList("computerdatabase.ConcreteSimulation", "computerdatabase.BasicSimulation"));
  }

  @BeforeAll
  public void createTestFile() throws IOException {
    this.testFile = File.createTempFile("gatling-simulations", ".jar");
    IOUtils.copy(
        Objects.requireNonNull(getClass().getResource("/gatling-simulations.jar")), testFile);
  }

  @AfterAll
  public void cleanTestFile() {
    testFile.delete();
  }
}
