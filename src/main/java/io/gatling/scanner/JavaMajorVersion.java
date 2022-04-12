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

import io.gatling.internal.asm.tree.ClassNode;

public enum JavaMajorVersion {
  JAVA_17(61, "Java 17");

  public final int majorVersion;
  public final String noun;

  JavaMajorVersion(int majorVersion, String noun) {
    this.majorVersion = majorVersion;
    this.noun = noun;
  }

  private static final int MAJOR_VERSION_CLASS_NODE_MASK = 0x0000FFFF;

  /**
   * Check if class node version is supported
   *
   * @param classNode class node treated
   * @param includedMax maximum java major version (included)
   * @throws UnsupportedJavaMajorVersionException when class node version isn't supported
   */
  public static void checkNode(ClassNode classNode, JavaMajorVersion includedMax)
      throws UnsupportedJavaMajorVersionException {
    int classNodeMajorVersion = classNode.version & MAJOR_VERSION_CLASS_NODE_MASK;
    if (classNodeMajorVersion > includedMax.majorVersion) {
      throw new UnsupportedJavaMajorVersionException(
          classNode.version, classNode.name, includedMax);
    }
  }
}
