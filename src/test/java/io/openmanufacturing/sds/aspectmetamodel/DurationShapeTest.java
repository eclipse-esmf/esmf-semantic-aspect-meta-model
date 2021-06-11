/*
 * Copyright (c) 2021 Robert Bosch Manufacturing Solutions GmbH
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

public class DurationShapeTest extends AbstractShapeTest {
   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testDurationValidationExpectSuccess( final KnownVersion metaModelVersions ) {
      checkValidity( "duration-shape", "TestDuration", metaModelVersions );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testDurationWithInvalidQuantityKind( final KnownVersion metaModelVersions ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersions );
      final UnitUrns unitUrns = new UnitUrns( "unit", metaModelVersions );

      final String durationName = "TestDurationWithInvalidUnit";
      final String durationId = TEST_NAMESPACE_PREFIX + durationName;
      final SemanticError invalidQuantityKind = new SemanticError(
            MESSAGE_INVALID_QUANTITY_KIND, durationId, bammUrns.unitUrn, VIOLATION_URN, unitUrns.voltUrn );
      expectSemanticValidationErrors( "duration-shape", durationName,
            metaModelVersions, invalidQuantityKind );
   }
}
