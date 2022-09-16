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

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.openmanufacturing.sds.validation.SemanticError;

public class StructuredValueShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidStructuredValueExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "structured-value-shape", "TestStructuredValue", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueMissingRequiredProperties";

      final SemanticError resultForDeconstructionRule = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.deconstructionRule, VIOLATION_URN, "" );
      final SemanticError resultForElements = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.elements, VIOLATION_URN, "" );
      final SemanticError resultForDatatype = new SemanticError(
            MESSAGE_MISSING_DATATYPE, focusNode, bammUrns.datatypeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueMissingRequiredProperties",
            metaModelVersion, resultForDeconstructionRule, resultForElements, resultForDatatype );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testStructuredValueValidationWithInvalidRegularExpressionExpectFailure(
         final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueWithInvalidRegularExpression";

      final SemanticError resultForRegularExpression = new SemanticError(
            MESSAGE_INVALID_DECONSTRUCTION_RULE, focusNode, bammUrns.deconstructionRule, VIOLATION_URN, "((((" );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueWithInvalidRegularExpression",
            metaModelVersion, resultForRegularExpression );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsContainInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "structured-value-shape", "TestStructuredValueWithInvalidElements", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( MESSAGE_INVALID_STRUCTURED_VALUE_ELEMENTS, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getValue() ).isEqualTo( "42" );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEmptyElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueWithEmptyElements";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_EMPTY_STRUCTURED_VALUE_ELEMENTS, focusNode, bammUrns.elements, VIOLATION_URN, focusNode );
      final SemanticError resultForGroups = new SemanticError(
            MESSAGE_INVALID_MATCHING_GROUPS2, focusNode, "", VIOLATION_URN, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithEmptyElements",
            metaModelVersion, resultForElements, resultForGroups );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testStructuredValueWithInvalidTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueWithInvalidDatatype";

      final SemanticError resultForDataType = new SemanticError(
            MESSAGE_STRUCTURED_VALUE_DATA_TYPE, focusNode, bammUrns.datatypeUrn, VIOLATION_URN,
            XSD.xboolean.getURI() );
      expectSemanticValidationErrors(
            "structured-value-shape", "TestStructuredValueWithInvalidDatatype",
            metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidDeconstructionExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueWithInvalidDeconstruction";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_INVALID_DECONSTRUCTION, focusNode, "", VIOLATION_URN, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithInvalidDeconstruction",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidMatchingGroupsExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueWithInvalidMatchingGroups";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_INVALID_MATCHING_GROUPS, focusNode, "", VIOLATION_URN, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithInvalidMatchingGroups",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testNonMatchingGroupsExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueWithNonMatchingGroups";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_NON_MATCHING_GROUPS, focusNode, "", VIOLATION_URN, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithNonMatchingGroups",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsContainsNoProperty( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueElementsWithoutProperties";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_ELEMENTS_MUST_CONTAIN_PROPERTIES, focusNode, bammUrns.elements, VIOLATION_URN, "foo" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueElementsWithoutProperties",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsWithNonScalarDatatype( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueElementsWithNonScalarDatatype";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_ELEMENTS_MUST_HAVE_SCALAR_TYPE, focusNode, bammUrns.elements, VIOLATION_URN,
            TEST_NAMESPACE_PREFIX + "prop" );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueElementsWithNonScalarDatatype",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsWithListCharacteristic( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestStructuredValueElementsWithListCharacteristic";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_ELEMENTS_HAVE_INVALID_CHARACTERISTIC, focusNode, bammUrns.elements, VIOLATION_URN,
            bammUrns.list );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueElementsWithListCharacteristic",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsWithStructuredValueCharacteristic( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode =
            TEST_NAMESPACE_PREFIX + "TestStructuredValueElementsWithStructuredValueCharacteristic";

      final SemanticError resultForElements = new SemanticError(
            MESSAGE_ELEMENTS_HAVE_INVALID_CHARACTERISTIC, focusNode, bammUrns.elements, VIOLATION_URN,
            bammUrns.structuredValue );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueElementsWithStructuredValueCharacteristic",
            metaModelVersion, resultForElements );
   }
}
