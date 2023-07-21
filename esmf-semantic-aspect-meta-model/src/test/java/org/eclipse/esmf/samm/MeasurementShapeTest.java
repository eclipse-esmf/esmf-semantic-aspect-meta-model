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
      final String focusNode = testNamespacePrefix + "TestMeasurementMissingRequiredProperties";

      final SemanticError resultForDataType = new SemanticError( messageMissingDatatype,
            focusNode, sammUrns.datatypeUrn, violationUrn, "" );
      final SemanticError resultForUnit = new SemanticError( messageMissingRequiredProperty,
            focusNode, sammUrns.unitUrn, violationUrn, "" );
      expectSemanticValidationErrors( "measurement-shape", "TestMeasurementMissingRequiredProperties",
            metaModelVersion, resultForDataType, resultForUnit );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidUnitExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestMeasurementWithInvalidUnit";

      final String expectedMessage = validator.getMessageText( "samm-c:QuantifiableShape", "samm-c:unit", "ERR_WRONG_DATATYPE",
            metaModelVersion );
      final SemanticError error = new SemanticError( expectedMessage, focusNode,
            sammUrns.unitUrn, violationUrn, testNamespacePrefix + "Entity" );
      expectSemanticValidationErrors( "measurement-shape", "TestMeasurementWithInvalidUnit",
            metaModelVersion, error );
   }
}
