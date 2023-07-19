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

public class StateShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testStateValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "state-shape", "TestState", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStateMissingRequiredProperties";

      final SemanticError resultForDataType = new SemanticError( messageMissingDatatype,
            focusNode, sammUrns.datatypeUrn, violationUrn, "" );
      final SemanticError resultForValues = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.valuesUrn, violationUrn, "" );
      final SemanticError resultForDefaultValue = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.defaultValueUrn, violationUrn, "" );
      expectSemanticValidationErrors( "state-shape", "TestStateMissingRequiredProperties",
            metaModelVersion, resultForDataType, resultForValues, resultForDefaultValue );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testDefaultValueNotContainedInValuesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestStateDefaultValueNotInValues";
      final String expectedMessage = validator.getMessageText( "samm-c:StateShape", "samm-c:defaultValue", "ERR_MISSING_DEFAULT_VALUE",
            metaModelVersion );
      final SemanticError resultForDefaultValue = new SemanticError( expectedMessage,
            focusNode, sammUrns.defaultValueUrn, violationUrn, "" );
      expectSemanticValidationErrors( "state-shape", "TestStateDefaultValueNotInValues",
            metaModelVersion, resultForDefaultValue );
   }
}
