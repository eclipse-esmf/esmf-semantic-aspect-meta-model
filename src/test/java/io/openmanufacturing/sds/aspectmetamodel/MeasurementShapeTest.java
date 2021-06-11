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

public class MeasurementShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "measurement-shape", "TestMeasurement", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestMeasurementMissingRequiredProperties";

      final SemanticError resultForDataType = new SemanticError( MESSAGE_MISSING_DATATYPE,
            focusNode, bammUrns.datatypeUrn, VIOLATION_URN, "" );
      final SemanticError resultForUnit = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY,
            focusNode, bammUrns.unitUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "measurement-shape", "TestMeasurementMissingRequiredProperties",
            metaModelVersion, resultForDataType, resultForUnit );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidUnitExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestMeasurementWithInvalidUnit";

      final SemanticError error = new SemanticError( MESSAGE_IS_NO_UNIT, focusNode,
            bammUrns.unitUrn, VIOLATION_URN, TEST_NAMESPACE_PREFIX + "Entity" );
      expectSemanticValidationErrors( "measurement-shape", "TestMeasurementWithInvalidUnit",
            metaModelVersion, error );
   }
}
