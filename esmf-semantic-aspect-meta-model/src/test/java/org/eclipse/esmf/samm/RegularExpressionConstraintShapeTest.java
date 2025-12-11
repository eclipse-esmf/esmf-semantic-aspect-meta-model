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

import java.util.Map;

import org.eclipse.esmf.samm.validation.SemanticError;

import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RegularExpressionConstraintShapeTest extends AbstractShapeTest {

   static final String SPEC_PATH = "regular-expression-constraint-shape";

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   void testRegularExpressionConstraintValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( SPEC_PATH, "TestRegularExpressionConstraint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsUpToIncluding2_2_0" )
   void testRegularExpressionConstraintValidationWithInvalidRegularExpressionExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestRegularExpressionConstraintWithInvalidRegularExpression";

      final SemanticError resultForRegularExpression = new SemanticError(
            messageInvalidRegularExpression, focusNode, sammUrns.valueUrn, violationUrn, "(" );
      expectSemanticValidationErrors( SPEC_PATH,
            "TestRegularExpressionConstraintWithInvalidRegularExpression",
            metaModelVersion, resultForRegularExpression );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_3_0" )
   void testRegularExpressionConstraintValidationWithInvalidRegularExpressionExpectFailureAfter2_3_0(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final String focusNode = testNamespacePrefix + "TestRegularExpressionConstraintWithInvalidRegularExpression";
      final String focusNode1 = focusNode + "2";
      final String focusNode2 = focusNode + "3";

      final SemanticError resultForRegularExpression = new SemanticError(
            messageInvalidRegularExpression, focusNode, sammUrns.valueUrn, violationUrn, "(" );
      final SemanticError resultForRegularExpression2 = new SemanticError(
            messageInvalidRegularExpression, focusNode1, sammUrns.valueUrn, violationUrn, "*abc" );
      final SemanticError resultForRegularExpression3 = new SemanticError(
            messageInvalidRegularExpression, focusNode2, sammUrns.valueUrn, violationUrn, "x(?<named>[a-z])y" );

      expectSemanticValidationErrors( SPEC_PATH,
            "TestRegularExpressionConstraintWithInvalidRegularExpression",
            metaModelVersion, resultForRegularExpression, resultForRegularExpression2, resultForRegularExpression3 );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   void testRegularExpressionConstraintValidationWithInvalidTypeExpectFailure(
         final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "TestRegularExpressionConstraintWithInvalidType";
      final String expectedMessage = validator.getMessageText( "samm-c:RegularExpressionConstraintShape", "ERR_WRONG_DATATYPE",
            metaModelVersion );
      final SemanticError resultForDataType = new SemanticError( expectedMessage,
            focusNode, "", violationUrn, XSD.xboolean.getURI() );
      expectSemanticValidationErrors(
            SPEC_PATH, "TestRegularExpressionConstraintWithInvalidType",
            metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void testValidExampleValuesValidation( final KnownVersion metaModelVersion ) {
      checkValidity( SPEC_PATH, "TestPropertyWithExampleValues", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void testInvalidExampleValuesValidation( final KnownVersion metaModelVersion ) {
      final String aspect = "TestPropertyWithInvalidExampleValues";
      final String focusNode = testNamespacePrefix + "TestRegularExpressionConstraint";
      final String expectedMessage = validator.getMessageText(
            "samm-c:RegularExpressionConstraintShape",
            "ERR_WRONG_EXAMPLE_VALUE",
            metaModelVersion );
      final SemanticError resultForDataType = new SemanticError( expectedMessage,
            focusNode, "", violationUrn, focusNode )
            .withParams( Map.of(
                  "{?property}",testNamespacePrefix + "InvalidRegexProperty",
                  "{?exampleValue}", "-ABC123456-000",
                  "{?regex}", "^[A-Z]{3}\\d{6}-\\d{3}$"
            ));
      expectSemanticValidationErrors(
            SPEC_PATH,
            aspect,
            metaModelVersion,
            resultForDataType );
   }

}
