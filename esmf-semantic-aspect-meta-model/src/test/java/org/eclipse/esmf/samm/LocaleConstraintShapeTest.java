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

import org.apache.jena.rdf.model.Model;
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
   public void testMissingRequiredProperties( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLocaleConstraintMissingRequiredProperties";

      final SemanticError resultForLocaleCode = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.localeCodeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "locale-constraint-shape",
            "TestLocaleConstraintMissingRequiredProperties", metaModelVersion, resultForLocaleCode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMultipleLocaleCodeProperties( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLocaleConstraintMultipleLocaleCodeProperties";

      final SemanticError result = new SemanticError( messageDuplicateProperty, focusNode,
            sammUrns.localeCodeUrn, violationUrn, "" );
      expectSemanticValidationErrors(
            "locale-constraint-shape", "TestLocaleConstraintMultipleLocaleCodeProperties",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidLanguageTagExpectError( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLocaleConstraintInvalidLanguageTag";

      final SemanticError result = new SemanticError( "Invalid language in locale code.", focusNode,
            sammUrns.localeCodeUrn, violationUrn, "ac" );
      expectSemanticValidationErrors(
            "locale-constraint-shape", "TestLocaleConstraintInvalidLanguageTag",
            metaModelVersion, result );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValidLanguageInvalidRegionExpectError( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestLocaleConstraintValidLanguageInvalidRegion";

      final SemanticError result = new SemanticError( "Invalid region in locale code.", focusNode,
            sammUrns.localeCodeUrn, violationUrn, "de-AB" );
      expectSemanticValidationErrors(
            "locale-constraint-shape", "TestLocaleConstraintValidLanguageInvalidRegion",
            metaModelVersion, result );
   }
}
