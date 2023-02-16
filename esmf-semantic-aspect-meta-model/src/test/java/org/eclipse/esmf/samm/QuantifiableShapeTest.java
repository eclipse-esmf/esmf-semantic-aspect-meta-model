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

public class QuantifiableShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testQuantifiableWithUnitValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "quantifiable-shape", "TestQuantifiable", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testQuantifiableWithoutUnitPropertyExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "quantifiable-shape", "TestQuantifiableWithoutUnitProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestQuantifiableWithMissingProperties";

      final SemanticError resultForDataType = new SemanticError( MESSAGE_MISSING_DATATYPE,
            focusNode, bammUrns.datatypeUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "quantifiable-shape", "TestQuantifiableWithMissingProperties",
            metaModelVersion, resultForDataType );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testInvalidUnitExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestQuantifiableWithInvalidUnit";
      final String expectedMessage = validator.getMessageText( "bamm-c:QuantifiableShape", "bamm-c:unit", "ERR_WRONG_DATATYPE", metaModelVersion );
      final SemanticError error = new SemanticError( expectedMessage, focusNode,
            bammUrns.unitUrn, VIOLATION_URN, TEST_NAMESPACE_PREFIX + "Entity" );
      expectSemanticValidationErrors( "quantifiable-shape", "TestQuantifiableWithInvalidUnit",
            metaModelVersion, error );
   }
}
