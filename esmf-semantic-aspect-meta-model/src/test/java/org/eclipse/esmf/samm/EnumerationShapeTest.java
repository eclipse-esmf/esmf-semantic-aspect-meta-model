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

class EnumerationShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   void testEnumerationValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "enumeration-shape", "TestEnumeration", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   void testMissingRequiredPropertiesExpectFailure2( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEnumerationMissingRequiredProperties";

      final SemanticError resultForDataType = new SemanticError( messageMissingDatatype,
            focusNode, sammUrns.datatypeUrn, violationUrn, "" );
      final SemanticError resultForValues = new SemanticError(
            messageMissingRequiredProperty, focusNode, sammUrns.valuesUrn, violationUrn, "" );
      expectSemanticValidationErrors( "enumeration-shape", "TestEnumerationMissingRequiredProperties",
            metaModelVersion, resultForDataType, resultForValues );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   void testValueIsNotOfDefinedDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEnumerationValueIsNotOfDefinedDataType";

      final SemanticError resultForName = new SemanticError(
            "EnumerationShape ':TestEnumerationValueIsNotOfDefinedDataType': one of the values ('1.0') does not have the specified data "
                  + "type 'xsd:string'.",
            focusNode, sammUrns.valuesUrn, violationUrn, "" );
      expectSemanticValidationErrors( "enumeration-shape", "TestEnumerationValueIsNotOfDefinedDataType",
            metaModelVersion, resultForName );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   void testValueIsNotALiteralTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix + "TestEnumerationValueIsNotALiteralType";

      final SemanticError resultForName = new SemanticError(
            "EnumerationShape ':TestEnumerationValueIsNotALiteralType': DataType is a literal type but one of the values "
                  + "(':testProperty') is defined as samm:Property.",
            focusNode, sammUrns.valuesUrn, violationUrn, "" );
      expectSemanticValidationErrors( "enumeration-shape", "TestEnumerationValueIsNotALiteralType",
            metaModelVersion, resultForName );
   }
}
