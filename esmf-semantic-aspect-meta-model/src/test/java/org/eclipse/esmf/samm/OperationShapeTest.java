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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.eclipse.esmf.samm.validation.SemanticError;

public class OperationShapeTest extends AbstractShapeTest {
   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testOperationValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "operation-shape", "TestOperation", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testMissingRequiredPropertiesExpectFailureSamm_1_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String operationName = "TestOperationMissingRequiredProperties";
      final String operationId = testNamespacePrefix + operationName;
      final SemanticError resultForName = new SemanticError( messageMissingRequiredProperty,
            operationId, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForInput = new SemanticError(
            messageMissingRequiredProperty, operationId, sammUrns.inputUrn, violationUrn, "" );
      expectSemanticValidationErrors( "operation-shape", operationName,
            metaModelVersion, resultForName, resultForInput );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testMissingRequiredPropertiesExpectFailureSamm_2_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String operationName = "TestOperationMissingRequiredProperties";
      final String operationId = testNamespacePrefix + operationName;
      final SemanticError resultForInput = new SemanticError(
            messageMissingRequiredProperty, operationId, sammUrns.inputUrn, violationUrn, "" );
      expectSemanticValidationErrors( "operation-shape", operationName,
            metaModelVersion, resultForInput );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_1_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String aspectName = "TestOperationWithEmptyProperties";
      final String operationName = "TestOperation";
      final String operationId = testNamespacePrefix + operationName;
      final SemanticError resultForName = new SemanticError( messageEmptyProperty,
            operationId, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            operationId, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            operationId, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "operation-shape", aspectName,
            metaModelVersion, resultForName, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_2_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String aspectName = "TestOperationWithEmptyProperties";
      final String operationName = "TestOperation";
      final String operationId = testNamespacePrefix + operationName;
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            operationId, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            operationId, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "operation-shape", aspectName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String aspectName = "TestOperationNonUniqueLangStrings";
      final String operationName = "TestOperation";
      final String operationId = testNamespacePrefix + operationName;
      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            operationId, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            operationId, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "operation-shape", aspectName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageStringsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String aspectName = "TestOperationWithInvalidLangStrings";
      final String operationName = "TestOperation";
      final String operationId = testNamespacePrefix + operationName;
      final SemanticError resultForPreferredName = new SemanticError(
            messageInvalidLangString, operationId, sammUrns.preferredNameUrn, violationUrn, "Test Operation" );
      final SemanticError resultForDescription = new SemanticError(
            messageInvalidLangString, operationId, sammUrns.descriptionUrn, violationUrn, "Test Operation." );
      expectSemanticValidationErrors( "operation-shape", aspectName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInputListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "operation-shape", "TestOperationWithInvalidInput", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( messageValueIsNoProperty );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( testNamespacePrefix + "input" );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testOutputListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "operation-shape", "TestOperationWithInvalidOutput", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( messageValueIsNoProperty );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( testNamespacePrefix + "output" );
   }
}
