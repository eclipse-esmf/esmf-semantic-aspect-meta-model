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
      final BammUrns bammUrns = new BammUrns( metaModelVersions );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEitherMissingRequiredProperties";

      final SemanticError resultForLeft = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.leftUrn, VIOLATION_URN, "" );
      final SemanticError resultForRight = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.rightUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "either-shape", "TestEitherMissingRequiredProperties",
            metaModelVersions, resultForLeft, resultForRight );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValueForLeftAttributeIsNotACharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEitherLeftAttributeNotACharacteristic";

      final SemanticError resultForLeft = new SemanticError(
            MESSAGE_VALUE_MUST_BE_CHARACTERISTIC, focusNode, bammUrns.leftUrn, VIOLATION_URN,
            TEST_NAMESPACE_PREFIX + "LeftType" );
      expectSemanticValidationErrors( "either-shape", "TestEitherLeftAttributeNotACharacteristic",
            metaModelVersion, resultForLeft );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValueForRightAttributeIsNotACharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEitherRightAttributeNotACharacteristic";

      final SemanticError resultForRight = new SemanticError(
            MESSAGE_VALUE_MUST_BE_CHARACTERISTIC, focusNode, bammUrns.rightUrn, VIOLATION_URN,
            TEST_NAMESPACE_PREFIX + "RightType" );
      expectSemanticValidationErrors( "either-shape", "TestEitherRightAttributeNotACharacteristic",
            metaModelVersion, resultForRight );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testSameCharacteristicForLeftAndRightExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEitherSameCharacteristicForLeftAndRight";
      final String expectedMessage = validator.getMessageText( "bamm-c:EitherShape", "bamm-c:left", "ERR_WRONG_DATATYPE", metaModelVersion );
      final SemanticError result = new SemanticError( expectedMessage,
            focusNode, bammUrns.leftUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "either-shape", "TestEitherSameCharacteristicForLeftAndRight",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEitherDefinesDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEitherDefinesDataType";

      final SemanticError result = new SemanticError(
            MESSAGE_MORE_THAN_ZERO_VALUES, focusNode, bammUrns.datatypeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "either-shape", "TestEitherDefinesDataType",
            metaModelVersion, result );
   }
}
