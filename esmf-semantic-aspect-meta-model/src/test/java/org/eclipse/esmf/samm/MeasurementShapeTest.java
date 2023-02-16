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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.eclipse.esmf.samm.validation.SemanticError;

public class MeasurementShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "measurement-shape", "TestMeasurement", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestMeasurementMissingRequiredProperties";

      final SemanticError resultForDataType = new SemanticError( MESSAGE_MISSING_DATATYPE,
            focusNode, sammUrns.datatypeUrn, VIOLATION_URN, "" );
      final SemanticError resultForUnit = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY,
            focusNode, sammUrns.unitUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "measurement-shape", "TestMeasurementMissingRequiredProperties",
            metaModelVersion, resultForDataType, resultForUnit );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidUnitExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestMeasurementWithInvalidUnit";

      final String expectedMessage = validator.getMessageText( "samm-c:QuantifiableShape", "samm-c:unit", "ERR_WRONG_DATATYPE", metaModelVersion );
      final SemanticError error = new SemanticError( expectedMessage, focusNode,
            sammUrns.unitUrn, VIOLATION_URN, TEST_NAMESPACE_PREFIX + "Entity" );
      expectSemanticValidationErrors( "measurement-shape", "TestMeasurementWithInvalidUnit",
            metaModelVersion, error );
   }
}
