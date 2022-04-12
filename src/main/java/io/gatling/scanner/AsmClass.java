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

import io.gatling.internal.asm.Opcodes;
import io.gatling.internal.asm.tree.ClassNode;

public class AsmClass {

  public final String name;
  public final String parentName;
  public final boolean concrete;

  AsmClass(ClassNode classNode) throws UnsupportedJavaMajorVersionException {
    JavaMajorVersion.checkNode(classNode, JavaMajorVersion.JAVA_17);
    this.name = classNode.name;
    this.parentName = classNode.superName;
    this.concrete = (classNode.access & Opcodes.ACC_ABSTRACT) == 0;
  }

  public String fullyQualifiedName() {
    return name.replace('/', '.');
  }
}
