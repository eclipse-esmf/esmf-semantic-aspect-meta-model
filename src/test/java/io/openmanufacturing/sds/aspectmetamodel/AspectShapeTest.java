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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.openmanufacturing.sds.validation.SemanticError;

public class AspectShapeTest extends AbstractShapeTest {
   private final String ENTITY_NODE = TEST_NAMESPACE_PREFIX + "Entity";

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "aspect-shape", "TestAspect", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestAspectMissingRequiredAspectProperties";

      final SemanticError resultForName = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY,
            focusNode, bammUrns.nameUrn, VIOLATION_URN, "" );
      final SemanticError resultForOperations = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.operationsUrn, VIOLATION_URN, "" );
      final SemanticError resultForProperties = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.propertiesUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectMissingRequiredAspectProperties",
            metaModelVersion, resultForName, resultForOperations, resultForProperties );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEmptyPropertiesExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestAspectWithEmptyProperties";

      final SemanticError resultForName = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            focusNode, bammUrns.nameUrn, VIOLATION_URN, "" );
      final SemanticError resultForPreferredName = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            focusNode, bammUrns.preferredNameUrn, VIOLATION_URN, "@en" );
      final SemanticError resultForDescription = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            focusNode, bammUrns.descriptionUrn, VIOLATION_URN, "@en" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectWithEmptyProperties",
            metaModelVersion,
            resultForName,
            resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestAspectNonUniqueLangStrings";

      final SemanticError resultForPreferredName = new SemanticError( MESSAGE_LANG_NOT_UNIQUE,
            focusNode, bammUrns.preferredNameUrn, VIOLATION_URN, "" );
      final SemanticError resultForDescription = new SemanticError( MESSAGE_LANG_NOT_UNIQUE,
            focusNode, bammUrns.descriptionUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectNonUniqueLangStrings", metaModelVersion,
            resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageStringExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestAspectWithInvalidLangStrings";

      final SemanticError resultForPreferredName = new SemanticError(
            MESSAGE_INVALID_LANG_STRING, focusNode, bammUrns.preferredNameUrn, VIOLATION_URN, "Test Aspect" );
      final SemanticError resultForDescription = new SemanticError(
            MESSAGE_INVALID_LANG_STRING, focusNode, bammUrns.descriptionUrn, VIOLATION_URN,
            "Test Aspect Description" );
      expectSemanticValidationErrors( "aspect-shape", "TestAspectWithInvalidLangStrings", metaModelVersion,
            resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidProperties", metaModelVersion );
      assertThat( result.getResultMessage() )
            .satisfies( s -> assertThat( s ).isEqualTo( MESSAGE_INVALID_ENTRY_ASPECT_PROPERTY_LIST ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getValue() ).isEqualTo( ENTITY_NODE );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testOperationListContainsInvalidElementsExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidOperations", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( MESSAGE_NO_OPERATION );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getValue() ).isEqualTo( ENTITY_NODE );
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
      assertThat( result.getResultMessage() )
            .satisfies( s -> assertThat( s ).isEqualTo( MESSAGE_INVALID_ENTRY_ASPECT_PROPERTY_LIST ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithMissingOptionalPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithMissingOptionalProperty", metaModelVersion );
      assertThat( result.getResultMessage() )
            .satisfies( s -> assertThat( s ).isEqualTo( MESSAGE_INVALID_ENTRY_ASPECT_PROPERTY_LIST ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getValue() ).isNotEmpty();
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithInvalidNotInPayloadPropertyExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "aspect-shape", "TestAspectWithInvalidNotInPayloadProperty", metaModelVersion );
      assertThat( result.getResultMessage() )
            .satisfies( s -> assertThat( s ).isEqualTo( MESSAGE_INVALID_ENTRY_ASPECT_PROPERTY_LIST ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
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
}
