/*
 * Copyright (c) 2023 Robert Bosch Manufacturing Solutions GmbH
 *
 * See the AUTHORS file(s) distributed with this work for additional
 * information regarding authorship.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */

package org.eclipse.esmf.samm;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The known versions of the Aspect Meta Model
 */
public enum KnownVersion {
   SAMM_1_0_0( 1, 0, 0 ),
   SAMM_2_0_0( 2, 0, 0 ),
   SAMM_2_1_0( 2, 1, 0 ),
   SAMM_2_2_0( 2, 2, 0 );

   private final int major;
   private final int minor;
   private final int patch;
   private final String versionString;

   KnownVersion( int major, int minor, int patch ) {
      this.major = major;
      this.minor = minor;
      this.patch = patch;
      this.versionString = String.format( "%d.%d.%d", major, minor, patch );
   }

   /**
    * Returns this version as a standard version string, e.g. 1.2.3
    *
    * @return version string representation
    */
   public String toVersionString() {
      return versionString;
   }

   public static Optional<KnownVersion> fromVersionString( final String version ) {
      return Arrays.stream( values() )
            .filter( v -> v.versionString.equals( version ) )
            .findAny();
   }

   public static KnownVersion getLatest() {
      return Arrays.stream( values() )
            .max( Comparator.comparingInt( KnownVersion::versionCode ) )
            .orElseThrow(); // Should never be empty
   }

   public static List<KnownVersion> getVersions() {
      return List.of( values() );
   }

   public boolean isNewerThan( final KnownVersion other ) {
      return this.versionCode() > other.versionCode();
   }

   public boolean isOlderThan( final KnownVersion other ) {
      return this.versionCode() < other.versionCode();
   }

   private int versionCode() {
      return major * 1_000_000 + minor * 1_000 + patch;
   }
}
