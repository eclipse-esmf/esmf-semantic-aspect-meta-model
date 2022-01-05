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

public class EnumerationShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testEnumerationValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "enumeration-shape", "TestEnumeration", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEnumerationMissingRequiredProperties";

      final SemanticError resultForDataType = new SemanticError( MESSAGE_MISSING_DATATYPE,
            focusNode, bammUrns.datatypeUrn, VIOLATION_URN, "" );
      final SemanticError resultForValues = new SemanticError(
            MESSAGE_MISSING_REQUIRED_PROPERTY, focusNode, bammUrns.valuesUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "enumeration-shape", "TestEnumerationMissingRequiredProperties",
            metaModelVersion, resultForDataType, resultForValues );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValueIsNotOfDefinedDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEnumerationValueIsNotOfDefinedDataType";

      final SemanticError resultForName = new SemanticError( MESSAGE_VALUE_NOT_OF_DATATYPE,
            focusNode, bammUrns.valuesUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "enumeration-shape", "TestEnumerationValueIsNotOfDefinedDataType",
            metaModelVersion, resultForName );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testValueIsNotALiteralTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestEnumerationValueIsNotALiteralType";

      final SemanticError resultForName = new SemanticError(
            "DataType is a literal type but one of the values is defined as bamm:Property.", focusNode,
            bammUrns.valuesUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( "enumeration-shape", "TestEnumerationValueIsNotALiteralType",
            metaModelVersion, resultForName );
   }
}
