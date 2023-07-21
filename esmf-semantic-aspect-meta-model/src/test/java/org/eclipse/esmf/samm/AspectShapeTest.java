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

public class AspectShapeTest extends AbstractShapeTest {
   private final String entityNode = testNamespacePrefix + "Entity";

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "aspect-shape", "TestAspect", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testMissingRequiredPropertiesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestAspectMissingRequiredAspectProperties";

      final SemanticError resultForName = new SemanticError( messageMissingRequiredProperty,
            focusNode, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForOperations = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.operationsUrn, violationUrn, "" );
      final SemanticError resultForProperties = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.propertiesUrn, violationUrn, "" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectMissingRequiredAspectProperties",
            metaModelVersion, resultForName, resultForOperations, resultForProperties );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testAspectWithoutPropertiesAndOperationsExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "aspect-shape", "TestAspectWithoutPropertiesAndOperations", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testEmptyPropertiesExpectFailureSamm1_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestAspectWithEmptyProperties";

      final SemanticError resultForName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectWithEmptyProperties",
            metaModelVersion,
            resultForName,
            resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_2_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestAspectWithEmptyProperties";

      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectWithEmptyProperties",
            metaModelVersion,
            resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestAspectNonUniqueLangStrings";

      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            focusNode, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectNonUniqueLangStrings", metaModelVersion,
            resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageStringExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestAspectWithInvalidLangStrings";

      final SemanticError resultForPreferredName = new SemanticError(
            messageInvalidLangString, focusNode, sammUrns.preferredNameUrn, violationUrn, "Test Aspect" );
      final SemanticError resultForDescription = new SemanticError(
            messageInvalidLangString, focusNode, sammUrns.descriptionUrn, violationUrn,
            "Test Aspect Description" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectWithInvalidLangStrings", metaModelVersion,
            resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidProperties", metaModelVersion );
      final String validationMessage = validator.getMessageText( "samm:AspectShape", "samm:properties", "ERR_INVALID_PROPERTY",
            metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( validationMessage, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( entityNode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testOperationListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidOperations", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( messageNoOperation );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( entityNode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithOptionalPropertyExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "aspect-shape", "TestAspectWithOptionalProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithInvalidOptionalPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidOptionalProperty", metaModelVersion );
      final String validationMessage = validator.getMessageText( "samm:AspectShape", "samm:properties", "ERR_INVALID_PROPERTY",
            metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( validationMessage, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithMissingOptionalPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithMissingOptionalProperty", metaModelVersion );
      final String validationMessage = validator.getMessageText( "samm:AspectShape", "samm:properties", "ERR_INVALID_PROPERTY",
            metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( validationMessage, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithInvalidNotInPayloadPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidNotInPayloadProperty", metaModelVersion );
      final String validationMessage = validator.getMessageText( "samm:AspectShape", "samm:properties", "ERR_INVALID_PROPERTY",
            metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( validationMessage, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithPropertyWithPayloadNameExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "aspect-shape", "TestAspectWithPropertyWithPayloadName", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithOptionalPropertyWithPayloadNameExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "aspect-shape", "TestAspectWithOptionalPropertyWithPayloadName", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testAspectWithInvalidPropertyReferenceExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithDoubleInstantiation", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo(
            "':MyText' can not be an instance of 'samm-c:Text', because 'samm-c:Text' is an instance itself." );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isNotEmpty();
   }
}
