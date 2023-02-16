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
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLanguageConstraintMissingRequiredProperties";

      final SemanticError resultForLanguageCode = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.languageCodeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "language-constraint-shape",
            "TestLanguageConstraintMissingRequiredProperties",
            metaModelVersion, resultForLanguageCode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMultipleLanguageCodeProperties( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLanguageConstraintMultipleLanguageCodeProperties";

      final SemanticError result = new SemanticError( MESSAGE_DUPLICATE_PROPERTY, focusNode,
            bammUrns.languageCodeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors(
            "language-constraint-shape", "TestLanguageConstraintMultipleLanguageCodeProperties",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageCodeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLanguageConstraintInvalidLanguageCode";
      final String expectedMessage = validator.getMessageText( "bamm-c:LanguageConstraintShape", "bamm-c:languageCode", "ERR_WRONG_LANGCODE",
            metaModelVersion );
      final SemanticError result = new SemanticError( expectedMessage, focusNode,
            bammUrns.languageCodeUrn, VIOLATION_URN, "DE_de" );
      expectSemanticValidationErrors( "language-constraint-shape", "TestLanguageConstraintInvalidLanguageCode", metaModelVersion, result );
   }
}
