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

public class AbstractEntityShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testExtendingAbstractEntityValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "abstract-entity-shape", "AbstractTestEntity", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testExtendingEntityValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "abstract-entity-shape", "ExtendedTestEntity", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testInstantiatingAbstractEntityExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "AbstractEntityInstance";
      final String value = testNamespacePrefix + "InstantiatedAbstractTestEntity";
      final String expectedMessage = validator.getMessageText( "samm:AbstractEntityDirectlyInstantiated", "ERR_ABSTRACT_USAGE", metaModelVersion );
      final SemanticError resultForInstantiatedAbstractEntity = new SemanticError( expectedMessage,
            focusNode, "", violationUrn, value );
      expectSemanticValidationErrors( "abstract-entity-shape", "InstantiatedAbstractTestEntity", metaModelVersion,
            resultForInstantiatedAbstractEntity );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testExtendingMultipleEntitiesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEntityExtendingMultipleEntities";

      final SemanticError resultForInstantiatedAbstractEntity = new SemanticError( messageDuplicateProperty,
            focusNode, sammUrns.extendsUrn, violationUrn, "" );
      expectSemanticValidationErrors( "abstract-entity-shape", "TestEntityExtendingMultipleEntities", metaModelVersion,
            resultForInstantiatedAbstractEntity );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testAmbiguousEntitiesDueToPropertyNamesExpectFailure( final KnownVersion metaModelVersion ) {

      //test payload name in Abstract Entity is equal to Property name in extending Entity
      final SemanticError resultForAmbiguousEntityA = new SemanticError(
            "The Entity ':A' contains Property 'abstractString' which causes ambiguity in combination with the ':AbstractTestEntityWithPayloadNameEqualToExtendingEntityProperty' Entity.",
            testNamespacePrefix + "A", "", violationUrn, "abstractString" );
      expectSemanticValidationErrors( "abstract-entity-shape", "AbstractTestEntityWithPayloadNameEqualToExtendingEntityProperty",
            metaModelVersion, resultForAmbiguousEntityA );

      //test extending Entity with ambiguous Property in hierarchy
      final SemanticError resultForAmbiguousExtendingEntityLevelOne = new SemanticError(
            "The Entity ':ExtendingEntityLevelOne' contains Property 'abstractTestProperty' which causes ambiguity in combination with the ':AbstractTestEntity' Entity.",
            testNamespacePrefix + "ExtendingEntityLevelOne", "", violationUrn, "abstractTestProperty" );
      final SemanticError resultForAmbiguousExtendingEntityLevelTwo = new SemanticError(
            "The Entity ':ExtendingEntityLevelTwo' contains Property 'testPropertyLevelOne' which causes ambiguity in combination with the ':ExtendingEntityLevelOne' Entity.",
            testNamespacePrefix + "ExtendingEntityLevelTwo", "", violationUrn, "testPropertyLevelOne" );

      expectSemanticValidationErrors( "abstract-entity-shape", "AbstractTestEntityWithAmbiguousEntityInHierarchy",
            metaModelVersion, resultForAmbiguousExtendingEntityLevelOne, resultForAmbiguousExtendingEntityLevelTwo );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testAmbiguousEntitiesDueToOptionalPropertyCombinationsExpectFailure( final KnownVersion metaModelVersion ) {

      //test Abstract Entity has optional Property and extending Entity uses same Property without optional
      final SemanticError resultForAmbiguousExtendingEntity = new SemanticError(
            "The Entity ':ExtendingEntity' contains Property 'abstractOptionalProperty' which causes ambiguity in combination with the ':AbstractTestEntityWithOptionalPropertyAndExtendingEntityWithSameProperty' Entity.",
            testNamespacePrefix + "ExtendingEntity", "", violationUrn, "abstractOptionalProperty" );
      expectSemanticValidationErrors( "abstract-entity-shape", "AbstractTestEntityWithOptionalPropertyAndExtendingEntityWithSameProperty",
            metaModelVersion, resultForAmbiguousExtendingEntity );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testAbstractEntityIsNotExtendedExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = new SemanticError(
            validator.getMessageText( "samm:AbstractEntityDataTypeShape", "ERR_ABSTRACT_USAGE", metaModelVersion ),
            testNamespacePrefix + "AbstractTestEntity", "", violationUrn, testNamespacePrefix + "AbstractEntity" );
      expectSemanticValidationErrors( "abstract-entity-shape", "AbstractTestEntityIsNotExtended",
            metaModelVersion, result );
   }

}
