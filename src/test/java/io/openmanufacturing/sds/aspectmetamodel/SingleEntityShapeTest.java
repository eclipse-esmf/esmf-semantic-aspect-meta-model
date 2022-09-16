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

public class SingleEntityShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCharacteristicInstanceWithEntityDataTypeValidationExpectSuccess(
         final KnownVersion metaModelVersion ) {
      checkValidity( "single-entity-shape", "TestSingleEntityWithEntityDataType", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testSingleEntityWithXsdDataTypeExpectFailure( final KnownVersion metaModelVersion ) {
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestSingleEntityWithXSDDataType";
      final String expectedMessage = validator.getMessageText( "bamm-c:SingleEntityShape", "bamm:dataType", metaModelVersion );
      final SemanticError resultForDataType = new SemanticError( expectedMessage,
            focusNode, bammUrns.datatypeUrn, VIOLATION_URN,
            "http://www.w3.org/2001/XMLSchema#integer" );
      expectSemanticValidationErrors( "single-entity-shape", "TestSingleEntityWithXSDDataType",
            metaModelVersion, resultForDataType );
   }
}
