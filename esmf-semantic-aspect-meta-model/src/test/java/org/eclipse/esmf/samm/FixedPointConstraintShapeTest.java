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

public class FixedPointConstraintShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "fixed-point-constraint-shape", "TestFixedPoint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestFixedPointMissingRequiredProperties";

      final SemanticError resultForScale = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.scale, violationUrn, "" );
      final SemanticError resultForInteger = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.integer, violationUrn, "" );
      expectSemanticValidationErrors( "fixed-point-constraint-shape", "TestFixedPointMissingRequiredProperties",
            metaModelVersion, resultForScale, resultForInteger );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationWithInvalidDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestFixedPointWithInvalidDataType";
      final String expectedMessage = validator.getMessageText( "samm-c:FixedPointConstraintShape", "samm-c:scale", "ERR_WRONG_DATATYPE",
            metaModelVersion );
      final SemanticError resultForScale = new SemanticError(
            expectedMessage, focusNode, sammUrns.scale, violationUrn, "http://www.w3.org/2001/XMLSchema#float" );
      expectSemanticValidationErrors( "fixed-point-constraint-shape", "TestFixedPointWithInvalidDataType",
            metaModelVersion, resultForScale );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationInCharacteristicChainWithInvalidDataTypeExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestFixedPointChainedWithInvalidDataType";
      final String expectedMessage = validator.getMessageText( "samm-c:FixedPointConstraintShape", "samm-c:scale", "ERR_WRONG_DATATYPE",
            metaModelVersion );
      final SemanticError resultForScale = new SemanticError(
            expectedMessage, focusNode, sammUrns.scale, violationUrn, "http://www.w3.org/2001/XMLSchema#float" );
      expectSemanticValidationErrors( "fixed-point-constraint-shape", "TestFixedPointChainedWithInvalidDataType",
            metaModelVersion, resultForScale );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationWithInvalidAttributeDataType( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestFixedPointWithInvalidAttributeDataType";

      final SemanticError resultForScale = new SemanticError(
            messageDataTypeNotPositiveInteger, focusNode, sammUrns.scale, violationUrn,
            "5^^http://www.w3.org/2001/XMLSchema#int" );

      final SemanticError resultForInteger = new SemanticError(
            messageDataTypeNotPositiveInteger, focusNode, sammUrns.integer, violationUrn,
            "10^^http://www.w3.org/2001/XMLSchema#nonNegativeInteger" );

      expectSemanticValidationErrors( "fixed-point-constraint-shape",
            "TestFixedPointWithInvalidAttributeDataType",
            metaModelVersion, resultForScale, resultForInteger );
   }
}
