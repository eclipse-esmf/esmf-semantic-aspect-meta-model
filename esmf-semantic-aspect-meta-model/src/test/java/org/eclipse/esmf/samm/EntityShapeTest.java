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

public class EntityShapeTest extends AbstractShapeTest {
   private final String secondTestEntity = testNamespacePrefix + "SecondTestEntity";

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "TestEntity", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityValidationWithRefinedEntityExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "RefinedTestEntity", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testMissingRequiredPropertiesExpectFailureSamm_1_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEntityMissingRequiredProperties";

      final SemanticError resultForName = new SemanticError( messageMissingRequiredProperty,
            focusNode, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForProperties = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.propertiesUrn, violationUrn, "" );
      expectSemanticValidationErrors( "entity-shape", "TestEntityMissingRequiredProperties", metaModelVersion,
            resultForName, resultForProperties );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testEntityWithoutPropertiesExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "TestEntityWithoutProperties", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_1_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEntityWithEmptyProperties";

      final SemanticError resultForName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "entity-shape", "TestEntityWithEmptyProperties",
            metaModelVersion, resultForName, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_2_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEntityWithEmptyProperties";

      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "entity-shape", "TestEntityWithEmptyProperties",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEntityNonUniqueLangStrings";

      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            focusNode, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "entity-shape", "TestEntityNonUniqueLangStrings", metaModelVersion,
            resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageStringsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEntityWithInvalidLangStrings";

      final SemanticError resultForPreferredName = new SemanticError(
            messageInvalidLangString, focusNode, sammUrns.preferredNameUrn, violationUrn, "Test Entity" );
      final SemanticError resultForDescription = new SemanticError(
            messageInvalidLangString, focusNode, sammUrns.descriptionUrn, violationUrn, "A test Entity" );
      expectSemanticValidationErrors( "entity-shape", "TestEntityWithInvalidLangStrings", metaModelVersion,
            resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithInvalidProperties", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntryEntityPropertyList, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( secondTestEntity );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testEntityRefiningEntityDeclaresAdditionalPropertiesExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityRefiningEntityDeclaresAdditionalProperties", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageNoAdditionalProperties, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( "" );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithOptionalPropertyExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "TestEntityWithOptionalProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithInvalidOptionalPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithInvalidOptionalProperty", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntryEntityPropertyList, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithMissingOptionalPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithMissingOptionalProperty", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntryEntityPropertyList, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithNotInPayloadPropertyExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "TestEntityWithNotInPayloadProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithNotInPayloadAndOptionalPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithNotInPayloadAndOptionalProperty", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntityPropertyDefinition, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( "urn:samm:org.eclipse.esmf.samm.test:1.0.0#testPropertyTwo" );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithInvalidNotInPayloadPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithInvalidNotInPayloadProperty", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntryEntityPropertyList, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithOnlyNotInPayloadPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithSingleNotInPayloadProperty", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntityWithSingleProperty, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( "" );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithNotInPayloadPropertyNotUsedInEnumerationExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithNotInPayloadPropertyWithoutEnumeration", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage(
            metaModelVersion.isNewerThan( KnownVersion.SAMM_1_0_0 ) ?
                  messageEntityNotUsedInEnumeration : messageEntityNotUsedInEnumeration1,
            result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testNestedEntityWithNotInPayloadPropertyExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "NestedEntityWithNotInPayloadProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithPropertyAndPayloadNameExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "TestEntityWithPropertyAndPayloadName", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithOptionalPropertyAndPayloadNameExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "entity-shape", "TestEntityWithOptionalPropertyAndPayloadName", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEntityWithNotInPayloadPropertyAndPayloadNameExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "entity-shape", "TestEntityWithNotInPayloadPropertyAndPayloadName", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( messageInvalidEntityPropertyPayloadName, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( violationUrn );
      assertThat( result.getValue() ).isEqualTo( "urn:samm:org.eclipse.esmf.samm.test:1.0.0#testPropertyTwo" );
   }
}
