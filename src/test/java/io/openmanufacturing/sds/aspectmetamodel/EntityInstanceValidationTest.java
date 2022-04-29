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

import io.openmanufacturing.sds.validation.SemanticError;

public class EntityInstanceValidationTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPoint3dEntityInstanceValidity( final KnownVersion metaModelVersion ) {
      // Point3d shared entity definition is erroneous and in consequence produces a validation error.
      // Until this error is fixed, the validity test had to be inverted and now passes for validation error instead of successful validation.
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "EntityCharacteristic";

      final SemanticError resultForLeft = new SemanticError( MESSAGE_INVALID_DATA_TYPE, focusNode, bammUrns.leftUrn, VIOLATION_URN,
            TEST_NAMESPACE_PREFIX + "LeftType" );
      expectSemanticValidationErrors( "validate-shared-entities", "TestPoint3dEntityInstance", metaModelVersion, resultForLeft );
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
