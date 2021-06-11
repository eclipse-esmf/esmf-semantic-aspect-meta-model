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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.openmanufacturing.sds.validation.SemanticError;

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
}
