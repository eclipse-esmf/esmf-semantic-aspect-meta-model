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

public class CharacteristicShapeTest extends AbstractShapeTest {
   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicInstance", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicSubClassValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicSubClass", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceMissingRequiredPropertiesExpectFailure2(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceMissingRequiredProperties";
      final String instanceId = testNamespacePrefix + instanceName;
      final SemanticError resultForDataType = new SemanticError(
            validator.getMessageText( "samm:CharacteristicShape", "samm:dataType", "ERR_NO_DATATYPE", metaModelVersion ),
            instanceId, sammUrns.datatypeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "characteristic-shape", instanceName, metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceNotRequiredToSetDataTypeExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicInstanceNotRequiredToSetDataType",
            metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceEmptyPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceWithEmptyProperties";
      final String instanceUri = testNamespacePrefix + instanceName;
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            instanceUri, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            instanceUri, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "characteristic-shape", instanceName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicWithGDay( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicInstanceWithGDay",
            metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceInvalidLanguageStringsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceWithInvalidLangStrings";
      final String instanceId = testNamespacePrefix + instanceName;
      final SemanticError resultForPreferredName = new SemanticError( messageInvalidLangString, instanceId,
            sammUrns.preferredNameUrn, violationUrn, "Test Characteristic Instance" );
      final SemanticError resultForDescription = new SemanticError( messageInvalidLangString, instanceId,
            sammUrns.descriptionUrn, violationUrn, "Test Characteristic Instance" );
      expectSemanticValidationErrors( "characteristic-shape", instanceName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceNonUniqueLangStrings";
      final String instanceId = testNamespacePrefix + instanceName;
      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            instanceId, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            instanceId, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "characteristic-shape", instanceName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicSubClassLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String className = "TestCharacteristicSubClassNonUniqueLangStrings";
      final String classId = testNamespacePrefix + className;
      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            classId, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            classId, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "characteristic-shape", className,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceMultipleDataTypesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceWithMultipleDataTypes";
      final String instanceId = testNamespacePrefix + instanceName;
      final SemanticError resultForDataType = new SemanticError(
            messageDuplicateProperty, instanceId, sammUrns.datatypeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "characteristic-shape", instanceName, metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicSubClassMultipleDataTypesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String className = "TestCharacteristicSubClassWithMultipleDataTypes";
      final String classId = testNamespacePrefix + className;
      final SemanticError resultForDataType = new SemanticError( messageDuplicateProperty,
            classId, sammUrns.datatypeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "characteristic-shape", className, metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceDefinesDisallowedDataType( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceWithDisallowedDataType";
      final String instanceId = testNamespacePrefix + instanceName;
      final SemanticError resultForDataType = new SemanticError( messageInvalidDataType,
            instanceId, sammUrns.datatypeUrn, violationUrn, "http://www.w3.org/2001/XMLSchema#maxExclusive" );
      expectSemanticValidationErrors( "characteristic-shape", instanceName, metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceWithCharacteristicAsDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String instanceName = "TestCharacteristicInstanceWithCharacteristicAsDataType";
      final String instanceId = testNamespacePrefix + instanceName;
      final String value = String.format( "urn:samm:org.eclipse.esmf.samm:characteristic:%s#Text", metaModelVersion.toVersionString() );
      final SemanticError resultForDataType = new SemanticError( messageInvalidDataType,
            instanceId, sammUrns.datatypeUrn, violationUrn, value );
      expectSemanticValidationErrors( "characteristic-shape", instanceName, metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testCharacteristicWithEntityDataType( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicInstanceWithEntityDataType",
              metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   public void testCharacteristicWithAbstractEntityDataType( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicInstanceWithAbstractEntityDataType",
              metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   public void testCharacteristicWithExtendedInstantiatedAbstractEntityDataType( final KnownVersion metaModelVersion ) {
      checkValidity( "characteristic-shape", "TestCharacteristicInstanceWithExtendedInstantiatedAbstractEntityDataType",
              metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   public void testCharacteristicWithDirectInstantiatedAbstractEntityDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "InstantiatedAbstractEntityInstance";
      final String value = testNamespacePrefix + "AbstractTestEntity";
      final String errorAbstractUsageMessage = validator.getMessageText( "samm:AbstractEntityDirectlyInstantiated", "ERR_ABSTRACT_USAGE", metaModelVersion );
      final SemanticError resultErrorAbstractUsageMessage = new SemanticError( errorAbstractUsageMessage,
              focusNode, "", violationUrn, value );
      expectSemanticValidationErrors( "characteristic-shape", "TestCharacteristicInstanceWithDirectInstantiatedAbstractEntityDataType", metaModelVersion,
              resultErrorAbstractUsageMessage );
   }
}
