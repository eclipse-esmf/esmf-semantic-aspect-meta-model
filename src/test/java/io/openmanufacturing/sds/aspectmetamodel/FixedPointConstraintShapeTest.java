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

public class FixedPointConstraintShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "fixed-point-constraint-shape", "TestFixedPoint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestFixedPointMissingRequiredProperties";

      final SemanticError resultForScale = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.scale, VIOLATION_URN, "" );
      final SemanticError resultForInteger = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.integer, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "fixed-point-constraint-shape", "TestFixedPointMissingRequiredProperties",
            metaModelVersion, resultForScale, resultForInteger );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationWithInvalidDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestFixedPointWithInvalidDataType";
      final String expectedMessage = validator.getMessageText( "bamm-c:FixedPointConstraintShape", "bamm-c:scale", "ERR_WRONG_DATATYPE", metaModelVersion );
      final SemanticError resultForScale = new SemanticError(
            expectedMessage, focusNode, bammUrns.scale, VIOLATION_URN, "http://www.w3.org/2001/XMLSchema#float" );
      expectSemanticValidationErrors( "fixed-point-constraint-shape", "TestFixedPointWithInvalidDataType",
            metaModelVersion, resultForScale );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationInCharacteristicChainWithInvalidDataTypeExpectFailure(
         final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestFixedPointChainedWithInvalidDataType";
      final String expectedMessage = validator.getMessageText( "bamm-c:FixedPointConstraintShape", "bamm-c:scale", "ERR_WRONG_DATATYPE", metaModelVersion );
      final SemanticError resultForScale = new SemanticError(
            expectedMessage, focusNode, bammUrns.scale, VIOLATION_URN, "http://www.w3.org/2001/XMLSchema#float" );
      expectSemanticValidationErrors( "fixed-point-constraint-shape", "TestFixedPointChainedWithInvalidDataType",
            metaModelVersion, resultForScale );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFixedPointValidationWithInvalidAttributeDataType( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestFixedPointWithInvalidAttributeDataType";

      final SemanticError resultForScale = new SemanticError(
            MESSAGE_DATA_TYPE_NOT_POSITIVE_INTEGER, focusNode, bammUrns.scale, VIOLATION_URN,
            "5^^http://www.w3.org/2001/XMLSchema#int" );

      final SemanticError resultForInteger = new SemanticError(
            MESSAGE_DATA_TYPE_NOT_POSITIVE_INTEGER, focusNode, bammUrns.integer, VIOLATION_URN,
            "10^^http://www.w3.org/2001/XMLSchema#nonNegativeInteger" );

      expectSemanticValidationErrors( "fixed-point-constraint-shape",
            "TestFixedPointWithInvalidAttributeDataType",
            metaModelVersion, resultForScale, resultForInteger );
   }
}
