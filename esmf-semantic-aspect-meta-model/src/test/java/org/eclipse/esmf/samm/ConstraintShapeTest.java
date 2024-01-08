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
import org.eclipse.esmf.samm.validation.ValidationReport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.eclipse.esmf.samm.validation.SemanticError;

public class ConstraintShapeTest extends AbstractShapeTest {
   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding2_1_0" )
   public void testConstraintValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "constraint-shape", "TestConstraint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding2_1_0" )
   public void testEmptyPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String constraintName = "TestConstraintWithEmptyProperties";
      final String constraintId = testNamespacePrefix + constraintName;
      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            constraintId, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            constraintId, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "constraint-shape", constraintName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0")
   public void testConstraint ( final KnownVersion metaModelVersion) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String constraintName = "TestConstraintWithoutProperties";
      final String constraintId = testNamespacePrefix + constraintName;
      final String value = testNamespacePrefix + constraintName;

      final SemanticError resultForEmptyConstraint = new SemanticError( messageHasToUseSubClass,
            constraintId, "", violationUrn,  value);

      expectSemanticValidationErrors( "constraint-shape", constraintName, metaModelVersion, resultForEmptyConstraint );

   }


   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding2_1_0" )
   public void testLanguageStringNotUniqueExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String constraintName = "TestConstraintNonUniqueLangStrings";
      final String constraintId = testNamespacePrefix + constraintName;
      final SemanticError resultForPreferredName = new SemanticError( messageLangNotUnique,
            constraintId, sammUrns.preferredNameUrn, violationUrn, "" );
      final SemanticError resultForDescription = new SemanticError( messageLangNotUnique,
            constraintId, sammUrns.descriptionUrn, violationUrn, "" );
      expectSemanticValidationErrors( "constraint-shape", constraintName, metaModelVersion, resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding2_1_0" )
   public void testInvalidLanguageStringsExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String constraintName = "TestConstraintWithInvalidLangStrings";
      final String constraintId = testNamespacePrefix + constraintName;
      final SemanticError resultForPreferredName = new SemanticError(
            messageInvalidLangString, constraintId, sammUrns.preferredNameUrn, violationUrn, "Test Constraint" );
      final SemanticError resultForDescription = new SemanticError(
            messageInvalidLangString, constraintId, sammUrns.descriptionUrn, violationUrn, "TestConstraint" );
      expectSemanticValidationErrors( "constraint-shape", constraintName,
            metaModelVersion, resultForPreferredName, resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding2_1_0" )
   public void testConstraintDefinesDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String constraintName = "TestConstraintWithDataType";
      final String constraintId = testNamespacePrefix + constraintName;
      final SemanticError resultForDataType = new SemanticError( messageMoreThanZeroValues, constraintId,
            sammUrns.datatypeUrn, violationUrn, "" );
      expectSemanticValidationErrors( "constraint-shape", constraintName, metaModelVersion, resultForDataType );
   }
}
