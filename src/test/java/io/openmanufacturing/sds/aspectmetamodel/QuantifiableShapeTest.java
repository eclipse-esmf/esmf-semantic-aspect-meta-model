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

      final SemanticError error = new SemanticError( MESSAGE_IS_NO_UNIT, focusNode,
            bammUrns.unitUrn, VIOLATION_URN, TEST_NAMESPACE_PREFIX + "Entity" );
      expectSemanticValidationErrors( "quantifiable-shape", "TestQuantifiableWithInvalidUnit",
            metaModelVersion, error );
   }
}
