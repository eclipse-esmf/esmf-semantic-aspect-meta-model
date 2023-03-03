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

public class PropertyShapeTest extends AbstractShapeTest {
   private final String FOCUS_NODE = TEST_NAMESPACE_PREFIX + "testProperty";
   private final String PROPERTY_TO_BE_REFINED_URN = TEST_NAMESPACE_PREFIX + "propertyToBeRefined";

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "property-shape", "TestProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testMissingRequiredPropertiesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY,
            FOCUS_NODE, sammUrns.nameUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyMissingRequiredProperties",
            metaModelVersion, resultForName );
   }
   
   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testRecursivePropertyWithOptionalExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "property-shape", "TestPropertyWithRecursivePropertyWithOptional", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_1_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            FOCUS_NODE, sammUrns.nameUrn, VIOLATION_URN, "" );
      final SemanticError resultForPreferredName = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            FOCUS_NODE, sammUrns.preferredNameUrn, VIOLATION_URN, "@en" );
      final SemanticError resultForDescription = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            FOCUS_NODE, sammUrns.descriptionUrn, VIOLATION_URN, "@en" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithEmptyProperties",
            metaModelVersion, resultForName, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_2_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForPreferredName = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            FOCUS_NODE, sammUrns.preferredNameUrn, VIOLATION_URN, "@en" );
      final SemanticError resultForDescription = new SemanticError( MESSAGE_EMPTY_PROPERTY,
            FOCUS_NODE, sammUrns.descriptionUrn, VIOLATION_URN, "@en" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithEmptyProperties",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForPreferredName = new SemanticError( MESSAGE_LANG_NOT_UNIQUE,
            FOCUS_NODE, sammUrns.preferredNameUrn, VIOLATION_URN, "" );
      final SemanticError resultForDescription = new SemanticError( MESSAGE_LANG_NOT_UNIQUE,
            FOCUS_NODE, sammUrns.descriptionUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyNonUniqueLangStrings",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageStringsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForPreferredName = new SemanticError(
            MESSAGE_INVALID_LANG_STRING, FOCUS_NODE, sammUrns.preferredNameUrn, VIOLATION_URN, "Test Property" );
      final SemanticError resultForDescription = new SemanticError(
            MESSAGE_INVALID_LANG_STRING, FOCUS_NODE, sammUrns.descriptionUrn, VIOLATION_URN,
            "A property with a list of numeric values." );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithInvalidLangStrings",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMultipleExampleValuesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForExampleValue = new SemanticError(
            MESSAGE_DUPLICATE_PROPERTY, TEST_NAMESPACE_PREFIX + "numericList", sammUrns.exampleValueUrn,
            VIOLATION_URN,
            "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithMultipleExampleValues",
            metaModelVersion, resultForExampleValue );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testExampleValueOnPropertyWithUndefinedCharacteristicExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForExampleValue = new SemanticError(
            validator.getMessageText( "samm:PropertyShape", "samm:exampleValue", "ERR_EXAMPLE_VALUE_NOT_ALLOWED", metaModelVersion ),
            PROPERTY_TO_BE_REFINED_URN, sammUrns.exampleValueUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithExampleValueWithoutCharacteristic",
            metaModelVersion, resultForExampleValue );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testRefinePropertyWithoutCharacteristicExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "property-shape", "TestPropertyRefiningPropertyWithoutCharacteristic", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testRefinePropertyWithCharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError result = new SemanticError(
            validator.getMessageText( "samm:PropertyShape", "samm:characteristic", "ERR_PROPERTY_REFINED", metaModelVersion ),
            PROPERTY_TO_BE_REFINED_URN, sammUrns.characteristicUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyRefiningPropertyWithCharacteristic",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testUnrefinedPropertyWithoutCharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError result = new SemanticError(
            validator.getMessageText( "samm:PropertyShape", "samm:characteristic", "ERR_PROPERTY_NOT_REFINED", metaModelVersion ),
            FOCUS_NODE, sammUrns.characteristicUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithoutCharacteristicUnrefined",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testNonScalarExampleValueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( MESSAGE_NO_LITERAL, FOCUS_NODE, sammUrns.exampleValueUrn, VIOLATION_URN, SemanticError.ANY_VALUE );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithEntityExampleValue", metaModelVersion, resultForName );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testInvalidScalarExampleValueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( MESSAGE_WRONG_EXAMPLE_VALUE_TYPE, FOCUS_NODE, sammUrns.exampleValueUrn, VIOLATION_URN,
            TEST_NAMESPACE_PREFIX + "TestEntity" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithInvalidScalarExampleValue", metaModelVersion, resultForName );
   }
}
