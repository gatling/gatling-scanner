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

import io.gatling.internal.asm.ClassReader;
import io.gatling.internal.asm.tree.ClassNode;
import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class AsmSimulationScanner {

  private static final String MODULE_INFO_NAME = "module-info";
  private static final String JAR_ENTRY_CLASS_SUFFIX = ".class";
  private static final byte[] JAVA_CLASS_MAGIC_BYTES = new byte[] {-54, -2, -70, -66};
  private static final String ROOT_CLASS_NAME = "java/lang/Object";
  private static final List<String> SIMULATION_CLASSES =
      Arrays.asList("io/gatling/javaapi/core/Simulation", "io/gatling/core/scenario/Simulation");

  private static final byte[] TO_BYTE_ARRAY_BUFFER = new byte[8 * 1024];

  // replace with InputStream#readAllBytes when we'll drop Java 8 support
  private static byte[] toByteArray(InputStream is) throws IOException {
    int n;
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      while ((n = is.read(TO_BYTE_ARRAY_BUFFER)) != -1) {
        os.write(TO_BYTE_ARRAY_BUFFER, 0, n);
      }
      return os.toByteArray();
    }
  }

  private static Optional<byte[]> bytesFromJarEntry(JarFile jar, JarEntry entry)
      throws IOException {
    if (entry.getName().endsWith(JAR_ENTRY_CLASS_SUFFIX)) {
      return Optional.of(toByteArray(new BufferedInputStream(jar.getInputStream(entry))));
    } else {
      return Optional.empty();
    }
  }

  private static boolean checkJavaClassMagicBytes(byte[] bytes) {
    if (bytes.length < JAVA_CLASS_MAGIC_BYTES.length) {
      return false;
    }
    for (int i = 0; i < JAVA_CLASS_MAGIC_BYTES.length; i++) {
      if (bytes[i] != JAVA_CLASS_MAGIC_BYTES[i]) {
        return false;
      }
    }
    return true;
  }

  private static Optional<ClassNode> classNodeFromBytes(byte[] bytes) {
    if (checkJavaClassMagicBytes(bytes)) {
      ClassReader classReader = new ClassReader(bytes);
      ClassNode classNode = new ClassNode();
      classReader.accept(classNode, ClassReader.SKIP_CODE);
      return Optional.of(classNode);
    } else {
      return Optional.empty();
    }
  }

  private static Optional<AsmClass> candidateFromJarEntry(JarFile jar, JarEntry entry)
      throws UnsupportedJavaMajorVersionException, IOException {
    return bytesFromJarEntry(jar, entry)
        .flatMap(AsmSimulationScanner::classNodeFromBytes)
        .filter(
            classNode ->
                !classNode.name.equals(MODULE_INFO_NAME)
                    && classNode.superName != null
                    && !classNode.superName.equals(ROOT_CLASS_NAME))
        .map(AsmClass::new);
  }

  private static Map<String, AsmClass> candidatesFromJar(JarFile jar) throws IOException {
    Enumeration<JarEntry> entries = jar.entries();
    Map<String, AsmClass> candidates = new HashMap<>();
    while (entries.hasMoreElements()) {
      candidateFromJarEntry(jar, entries.nextElement())
          .ifPresent(asmClass -> candidates.put(asmClass.name, asmClass));
    }
    return candidates;
  }

  private static boolean isAncestorSimulation(
      AsmClass candidate, Map<String, AsmClass> candidates) {
    return SIMULATION_CLASSES.contains(candidate.parentName)
        || Optional.ofNullable(candidates.get(candidate.parentName))
            .map(parentCandidate -> isAncestorSimulation(parentCandidate, candidates))
            .orElse(false);
  }

  /**
   * Collect simulation fully qualified names in a jar
   *
   * @param jar simulation JAR
   * @return List of discovered simulations fully qualified names in given jar
   * @throws UnsupportedJavaMajorVersionException when a class node in jar have an unsupported java
   *     major version
   */
  public static List<String> simulationFullyQualifiedNamesFromJar(JarFile jar)
      throws UnsupportedJavaMajorVersionException, IOException {
    Map<String, AsmClass> candidates = candidatesFromJar(jar);
    return candidates.values().stream()
        .filter(candidate -> candidate.concrete && isAncestorSimulation(candidate, candidates))
        .map(AsmClass::fullyQualifiedName)
        .collect(Collectors.toList());
  }

  /**
   * Collect simulation fully qualified names in a jar
   *
   * @param file simulation JAR file
   * @return List of discovered simulations fully qualified names in given jar
   * @throws UnsupportedJavaMajorVersionException when a class node in jar have an unsupported java
   *     major version
   */
  public static List<String> simulationFullyQualifiedNamesFromFile(File file)
      throws UnsupportedJavaMajorVersionException, IOException {
    try (JarFile jar = new JarFile(file)) {
      return simulationFullyQualifiedNamesFromJar(jar);
    }
  }
}
