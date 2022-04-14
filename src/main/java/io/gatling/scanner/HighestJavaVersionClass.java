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

import java.util.Objects;

public class HighestJavaVersionClass {

  public final String clazz;
  public final int javaVersion;

  /**
   * @param clazz Required, fully qualified name
   * @param javaVersion Required, java version of the class under fully qualified name
   */
  public HighestJavaVersionClass(String clazz, int javaVersion) {
    this.clazz = clazz;
    this.javaVersion = javaVersion;
  }

  @Override
  public String toString() {
    return "HighestJavaVersionClass{"
        + "clazz='"
        + clazz
        + '\''
        + ", javaVersion="
        + javaVersion
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HighestJavaVersionClass that = (HighestJavaVersionClass) o;
    return javaVersion == that.javaVersion && Objects.equals(clazz, that.clazz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clazz, javaVersion);
  }
}
