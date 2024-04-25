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
   private final String FOCUS_NODE = testNamespacePrefix + "testProperty";
   private final String PROPERTY_TO_BE_REFINED_URN = testNamespacePrefix + "propertyToBeRefined";

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "property-shape", "TestProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testMissingRequiredPropertiesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( messageMissingRequiredProperty,
            FOCUS_NODE, sammUrns.nameUrn, violationUrn, "" );
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

      final SemanticError resultForName = new SemanticError( messageEmptyProperty,
            FOCUS_NODE, sammUrns.nameUrn, violationUrn, "" );
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            FOCUS_NODE, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            FOCUS_NODE, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithEmptyProperties",
            metaModelVersion, resultForName, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testEmptyPropertiesExpectFailureSamm_2_0_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            FOCUS_NODE, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            FOCUS_NODE, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithEmptyProperties",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            FOCUS_NODE, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            FOCUS_NODE, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyNonUniqueLangStrings",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageStringsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForPreferredName = new SemanticError(
            messageInvalidLangString, FOCUS_NODE, sammUrns.preferredNameUrn, violationUrn, "Test Property" );
      final SemanticError resultForDescription = new SemanticError(
            messageInvalidLangString, FOCUS_NODE, sammUrns.descriptionUrn, violationUrn,
            "A property with a list of numeric values." );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithInvalidLangStrings",
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   public void testExampleValueOnPropertyWithValidLangStringValueExpectFailure(
         final KnownVersion metaModelVersion ) {
      checkValidity("property-shape", "TestPropertyWithValidLangStringExampleValue", metaModelVersion);
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMultipleExampleValuesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForExampleValue = new SemanticError(
            messageDuplicateProperty, testNamespacePrefix + "numericList", sammUrns.exampleValueUrn,
            violationUrn,
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
            PROPERTY_TO_BE_REFINED_URN, sammUrns.exampleValueUrn, violationUrn, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithExampleValueWithoutCharacteristic",
            metaModelVersion, resultForExampleValue );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   public void testExampleValueOnPropertyWithInvalidBooleanFormatExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final SemanticError resultForExampleValue = new SemanticError( messageWrongBooleanValues,
            FOCUS_NODE, sammUrns.exampleValueUrn, violationUrn,  "");

      expectSemanticValidationErrors( "property-shape", "TestPropertyWithInvalidBooleanExampleValue",
            metaModelVersion, resultForExampleValue );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testRefinePropertyWithoutCharacteristicExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "property-shape", "TestPropertyRefiningPropertyWithoutCharacteristic", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testRefinePropertyWithCharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError result = new SemanticError(
            validator.getMessageText( "samm:PropertyShape", "samm:characteristic", "ERR_PROPERTY_REFINED", metaModelVersion ),
            PROPERTY_TO_BE_REFINED_URN, sammUrns.characteristicUrn, violationUrn, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyRefiningPropertyWithCharacteristic",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding1_0_0" )
   public void testUnrefinedPropertyWithoutCharacteristicExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError result = new SemanticError(
            validator.getMessageText( "samm:PropertyShape", "samm:characteristic", "ERR_PROPERTY_NOT_REFINED", metaModelVersion ),
            FOCUS_NODE, sammUrns.characteristicUrn, violationUrn, "" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithoutCharacteristicUnrefined",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testNonScalarExampleValueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( messageNoLiteral, FOCUS_NODE, sammUrns.exampleValueUrn, violationUrn,
            SemanticError.ANY_VALUE );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithEntityExampleValue", metaModelVersion, resultForName );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testInvalidScalarExampleValueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final SemanticError resultForName = new SemanticError( messageWrongExampleValueType, FOCUS_NODE, sammUrns.exampleValueUrn,
            violationUrn,
            testNamespacePrefix + "TestEntity" );
      expectSemanticValidationErrors( "property-shape", "TestPropertyWithInvalidScalarExampleValue", metaModelVersion, resultForName );
   }
}
