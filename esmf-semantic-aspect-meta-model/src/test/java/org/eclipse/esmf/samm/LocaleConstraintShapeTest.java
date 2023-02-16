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

public class LocaleConstraintShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPropertyValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "locale-constraint-shape", "TestLocaleConstraint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidGrandfatheredLanguageTagExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "locale-constraint-shape", "TestLocaleConstraintValidGrandfatheredLanguageTag", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredProperties2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLocaleConstraintMissingRequiredProperties";

      final SemanticError resultForLocaleCode = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, sammUrns.localeCodeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "locale-constraint-shape",
            "TestLocaleConstraintMissingRequiredProperties", metaModelVersion, resultForLocaleCode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMultipleLocaleCodeProperties( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLocaleConstraintMultipleLocaleCodeProperties";

      final SemanticError result = new SemanticError( MESSAGE_DUPLICATE_PROPERTY, focusNode,
            sammUrns.localeCodeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors(
            "locale-constraint-shape", "TestLocaleConstraintMultipleLocaleCodeProperties",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageTagExpectError( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLocaleConstraintInvalidLanguageTag";

      final SemanticError result = new SemanticError( "Invalid language in locale code.", focusNode,
            sammUrns.localeCodeUrn, VIOLATION_URN, "ac" );
      expectSemanticValidationErrors(
            "locale-constraint-shape", "TestLocaleConstraintInvalidLanguageTag",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidLanguageInvalidRegionExpectError( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestLocaleConstraintValidLanguageInvalidRegion";

      final SemanticError result = new SemanticError( "Invalid region in locale code.", focusNode,
            sammUrns.localeCodeUrn, VIOLATION_URN, "de-AB" );
      expectSemanticValidationErrors(
            "locale-constraint-shape", "TestLocaleConstraintValidLanguageInvalidRegion",
            metaModelVersion, result );
   }
}
