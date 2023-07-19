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

public class UniqueEntityInstancesShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testIdenticalInstancesExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "Instance";
      final String expectedMessage = validator.getMessageText( "samm:UniqueEntityInstances", "ERR_DUPLICATE_ENTRY", metaModelVersion );
      final SemanticError resultForName = new SemanticError( expectedMessage,
            focusNode, "", warningUrn, focusNode + "2" );
      expectSemanticValidationErrors( "unique-entity-instance-shape",
            "TestEntityInstanceIdenticalInstances",
            metaModelVersion, resultForName );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInstancesWithSomeIdenticalValuesExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "unique-entity-instance-shape", "TestEntityInstanceSomeIdenticalValues", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_0_0" )
   public void testIdenticalExtendingEntityInstancesExpectFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "ExtendingEntityInstance";
      final String expectedMessage = validator.getMessageText( "samm:UniqueEntityInstances", "ERR_DUPLICATE_ENTRY", metaModelVersion );
      final SemanticError resultForIdenticalInstance = new SemanticError( expectedMessage, focusNode, "",
            warningUrn, testNamespacePrefix + "ExtendingEntityInstanceTwo" );
      expectSemanticValidationErrors( "unique-entity-instance-shape", "ExtendingEntityIdenticalInstances",
            metaModelVersion, resultForIdenticalInstance );
   }
}
