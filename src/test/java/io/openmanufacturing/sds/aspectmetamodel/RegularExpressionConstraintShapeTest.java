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

import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.openmanufacturing.sds.validation.SemanticError;

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
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestRegularExpressionConstraintWithInvalidRegularExpression";

      final SemanticError resultForRegularExpression = new SemanticError(
            MESSAGE_INVALID_REGULAR_EXPRESSION, focusNode, bammUrns.valueUrn, VIOLATION_URN, "(" );
      expectSemanticValidationErrors( "regular-expression-constraint-shape",
            "TestRegularExpressionConstraintWithInvalidRegularExpression",
            metaModelVersion, resultForRegularExpression );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testRegularExpressionConstraintValidationWithInvalidTypeExpectFailure(
         final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestRegularExpressionConstraintWithInvalidType";
      final String expectedMessage = validator.getMessageText( "bamm-c:RegularExpressionConstraintShape", "ERR_WRONG_DATATYPE", metaModelVersion );
      final SemanticError resultForDataType = new SemanticError( expectedMessage,
            focusNode, "", VIOLATION_URN, XSD.xboolean.getURI() );
      expectSemanticValidationErrors(
            "regular-expression-constraint-shape", "TestRegularExpressionConstraintWithInvalidType",
            metaModelVersion, resultForDataType );
   }
}
