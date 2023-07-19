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

import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.eclipse.esmf.samm.validation.SemanticError;

public class RegularExpressionConstraintShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testRegularExpressionConstraintValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "regular-expression-constraint-shape", "TestRegularExpressionConstraint", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testRegularExpressionConstraintValidationWithInvalidReqularExpressionExpectFailure(
         final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestRegularExpressionConstraintWithInvalidRegularExpression";

      final SemanticError resultForRegularExpression = new SemanticError(
            messageInvalidRegularExpression, focusNode, sammUrns.valueUrn, violationUrn, "(" );
      expectSemanticValidationErrors( "regular-expression-constraint-shape",
            "TestRegularExpressionConstraintWithInvalidRegularExpression",
            metaModelVersion, resultForRegularExpression );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testRegularExpressionConstraintValidationWithInvalidTypeExpectFailure(
         final KnownVersion metaModelVersion ) {
      final String focusNode = testNamespacePrefix + "TestRegularExpressionConstraintWithInvalidType";
      final String expectedMessage = validator.getMessageText( "samm-c:RegularExpressionConstraintShape", "ERR_WRONG_DATATYPE",
            metaModelVersion );
      final SemanticError resultForDataType = new SemanticError( expectedMessage,
            focusNode, "", violationUrn, XSD.xboolean.getURI() );
      expectSemanticValidationErrors(
            "regular-expression-constraint-shape", "TestRegularExpressionConstraintWithInvalidType",
            metaModelVersion, resultForDataType );
   }
}
