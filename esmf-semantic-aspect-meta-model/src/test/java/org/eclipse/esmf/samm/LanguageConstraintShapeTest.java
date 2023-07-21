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

public class LanguageConstraintShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "language-constraint-shape", "TestLanguageConstraint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredProperties2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLanguageConstraintMissingRequiredProperties";

      final SemanticError resultForLanguageCode = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.languageCodeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "language-constraint-shape",
            "TestLanguageConstraintMissingRequiredProperties",
            metaModelVersion, resultForLanguageCode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMultipleLanguageCodeProperties( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLanguageConstraintMultipleLanguageCodeProperties";

      final SemanticError result = new SemanticError( messageDuplicateProperty, focusNode,
            sammUrns.languageCodeUrn, violationUrn, "" );
      expectSemanticValidationErrors(
            "language-constraint-shape", "TestLanguageConstraintMultipleLanguageCodeProperties",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageCodeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLanguageConstraintInvalidLanguageCode";
      final String expectedMessage = validator.getMessageText( "samm-c:LanguageConstraintShape", "samm-c:languageCode",
            "ERR_WRONG_LANGCODE",
            metaModelVersion );
      final SemanticError result = new SemanticError( expectedMessage, focusNode,
            sammUrns.languageCodeUrn, violationUrn, "DE_de" );
      expectSemanticValidationErrors( "language-constraint-shape", "TestLanguageConstraintInvalidLanguageCode", metaModelVersion, result );
   }
}
