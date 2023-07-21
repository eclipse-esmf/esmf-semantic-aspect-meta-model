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

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.eclipse.esmf.samm.validation.SemanticError;

public class StructuredValueShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidStructuredValueExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "structured-value-shape", "TestStructuredValue", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueMissingRequiredProperties";

      final SemanticError resultForDeconstructionRule = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.deconstructionRule, violationUrn, "" );
      final SemanticError resultForElements = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.elements, violationUrn, "" );
      final SemanticError resultForDatatype = new SemanticError(
            messageMissingDatatype, focusNode, sammUrns.datatypeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueMissingRequiredProperties",
            metaModelVersion, resultForDeconstructionRule, resultForElements, resultForDatatype );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testStructuredValueValidationWithInvalidRegularExpressionExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueWithInvalidRegularExpression";

      final SemanticError resultForRegularExpression = new SemanticError(
            messageInvalidDeconstructionRule, focusNode, sammUrns.deconstructionRule, violationUrn, "((((" );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueWithInvalidRegularExpression",
            metaModelVersion, resultForRegularExpression );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsContainInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "structured-value-shape", "TestStructuredValueWithInvalidElements", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage(
            validator.getMessageText( "samm-c:StructuredValueShape", "samm-c:elements", "ERR_WRONG_DATATYPE", metaModelVersion ),
            result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( "42" );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEmptyElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueWithEmptyElements";

      final SemanticError resultForElements = new SemanticError(
            validator.getMessageText( "samm-c:StructuredValueShape", "samm-c:elements", "ERR_MISSING_VALUE", metaModelVersion ),
            focusNode, sammUrns.elements, violationUrn, focusNode );
      final SemanticError resultForGroups = new SemanticError(
            messageInvalidMatchingGroups2, focusNode, "", violationUrn, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithEmptyElements",
            metaModelVersion, resultForElements, resultForGroups );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testStructuredValueWithInvalidTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueWithInvalidDatatype";

      final SemanticError resultForDataType = new SemanticError(
            validator.getMessageText( "samm-c:StructuredValueShape", "samm:dataType", "ERR_WRONG_DATATYPE", metaModelVersion ),
            focusNode, sammUrns.datatypeUrn, violationUrn,
            XSD.xboolean.getURI() );
      expectSemanticValidationErrors(
            "structured-value-shape", "TestStructuredValueWithInvalidDatatype",
            metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidDeconstructionExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "TestStructuredValueWithInvalidDeconstruction";

      final SemanticError resultForElements = new SemanticError(
            messageInvalidDeconstruction, focusNode, "", violationUrn, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithInvalidDeconstruction",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidMatchingGroupsExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "TestStructuredValueWithInvalidMatchingGroups";

      final SemanticError resultForElements = new SemanticError(
            messageInvalidMatchingGroups, focusNode, "", violationUrn, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithInvalidMatchingGroups",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testNonMatchingGroupsExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "TestStructuredValueWithNonMatchingGroups";

      final SemanticError resultForElements = new SemanticError(
            messageNonMatchingGroups, focusNode, "", violationUrn, "" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueWithNonMatchingGroups",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsContainsNoProperty( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueElementsWithoutProperties";

      final SemanticError resultForElements = new SemanticError(
            validator.getMessageText( "samm-c:StructuredValueShape", "samm-c:elements", "ERR_MISSING_PROPERTY", metaModelVersion ),
            focusNode, sammUrns.elements, violationUrn, "foo" );
      expectSemanticValidationErrors( "structured-value-shape", "TestStructuredValueElementsWithoutProperties",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsWithNonScalarDatatype( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueElementsWithNonScalarDatatype";

      final SemanticError resultForElements = new SemanticError(
            "Properties referred to in StructuredValue's '{$this}' elements must have a Characteristic with a scalar dataType",
            focusNode, sammUrns.elements, violationUrn,
            testNamespacePrefix + "prop" );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueElementsWithNonScalarDatatype",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsWithListCharacteristic( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStructuredValueElementsWithListCharacteristic";

      final SemanticError resultForElements = new SemanticError(
            validator.getMessageText( "samm-c:StructuredValueShape", "samm-c:elements", "ERR_WRONG_USAGE", metaModelVersion ),
            focusNode, sammUrns.elements, violationUrn,
            sammUrns.list );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueElementsWithListCharacteristic",
            metaModelVersion, resultForElements );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testElementsWithStructuredValueCharacteristic( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode =
            testNamespacePrefix + "TestStructuredValueElementsWithStructuredValueCharacteristic";

      final SemanticError resultForElements = new SemanticError(
            validator.getMessageText( "samm-c:StructuredValueShape", "samm-c:elements", "ERR_WRONG_USAGE", metaModelVersion ),
            focusNode, sammUrns.elements, violationUrn,
            sammUrns.structuredValue );
      expectSemanticValidationErrors( "structured-value-shape",
            "TestStructuredValueElementsWithStructuredValueCharacteristic",
            metaModelVersion, resultForElements );
   }
}
