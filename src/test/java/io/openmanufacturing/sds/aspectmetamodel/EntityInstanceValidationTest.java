/*
 * Copyright (c) 2022 Robert Bosch Manufacturing Solutions GmbH
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

package io.openmanufacturing.sds.aspectmetamodel;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class EntityInstanceValidationTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPoint3dEntityInstanceValidity( final KnownVersion metaModelVersion ) {
         checkValidity( "validate-shared-entities", "TestPoint3dEntityInstance", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFileResourceEntityInstanceValidity( final KnownVersion metaModelVersion ) {
      checkValidity( "validate-shared-entities", "TestFileResourceEntityInstance", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testTimeSeriesEntityInstanceValidity( final KnownVersion metaModelVersion ) {
      checkValidity( "validate-shared-entities", "TestTimeSeriesEntityInstance", metaModelVersion );
   }
}
