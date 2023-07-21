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

public class EitherShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEitherShapeExpectSuccess( final KnownVersion metaModelVersions ) {
      checkValidity( "either-shape", "TestEither", metaModelVersions );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingPropertiesExpectFailure2( final KnownVersion metaModelVersions ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersions );
      final String focusNode = testNamespacePrefix + "TestEitherMissingRequiredProperties";

      final SemanticError resultForLeft = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.leftUrn, violationUrn, "" );
      final SemanticError resultForRight = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.rightUrn, violationUrn, "" );
      expectSemanticValidationErrors( "either-shape", "TestEitherMissingRequiredProperties",
            metaModelVersions, resultForLeft, resultForRight );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValueForLeftAttributeIsNotACharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEitherLeftAttributeNotACharacteristic";

      final SemanticError resultForLeft = new SemanticError(
            messageValueMustBeCharacteristic, focusNode, sammUrns.leftUrn, violationUrn,
            testNamespacePrefix + "LeftType" );
      expectSemanticValidationErrors( "either-shape", "TestEitherLeftAttributeNotACharacteristic",
            metaModelVersion, resultForLeft );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValueForRightAttributeIsNotACharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEitherRightAttributeNotACharacteristic";

      final SemanticError resultForRight = new SemanticError(
            messageValueMustBeCharacteristic, focusNode, sammUrns.rightUrn, violationUrn,
            testNamespacePrefix + "RightType" );
      expectSemanticValidationErrors( "either-shape", "TestEitherRightAttributeNotACharacteristic",
            metaModelVersion, resultForRight );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testSameCharacteristicForLeftAndRightExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEitherSameCharacteristicForLeftAndRight";
      final String expectedMessage = validator.getMessageText( "samm-c:EitherShape", "samm-c:left", "ERR_WRONG_DATATYPE",
            metaModelVersion );
      final SemanticError result = new SemanticError( expectedMessage,
            focusNode, sammUrns.leftUrn, violationUrn, "" );
      expectSemanticValidationErrors( "either-shape", "TestEitherSameCharacteristicForLeftAndRight",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEitherDefinesDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEitherDefinesDataType";

      final SemanticError result = new SemanticError(
            messageMoreThanZeroValues, focusNode, sammUrns.datatypeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "either-shape", "TestEitherDefinesDataType",
            metaModelVersion, result );
   }
}
