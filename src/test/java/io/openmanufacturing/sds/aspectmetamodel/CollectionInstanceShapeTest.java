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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.openmanufacturing.sds.validation.SemanticError;

public class CollectionInstanceShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCollectionCharacteristicWithElementCharacteristicExpectSuccess(
         final KnownVersion metaModelVersion ) {
      checkValidity( "collection-instance-shape", "TestCollectionWithElementCharacteristic",
            metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCollectionCharacteristicWithDataTypeExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "collection-instance-shape", "TestCollectionWithDataType", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCollectionCharacteristicWithoutDataTypeOrElementCharacteristicExpectFailure(
         final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestCollectionWithoutDataTypeAndElementCharacteristic";
      final SemanticError result = getSingleSemanticValidationError(
            "collection-instance-shape", "TestCollectionWithoutDataTypeAndElementCharacteristic", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( MESSAGE_COLLECTION_WITHOUT_DATA_TYPE, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getFocusNode() ).isEqualTo( focusNode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCollectionCharacteristicWithInvalidElementCharacteristic( final KnownVersion metaModelversion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestCollectionWithInvalidElementCharacteristic";
      final SemanticError result = getSingleSemanticValidationError(
            "collection-instance-shape", "TestCollectionWithInvalidElementCharacteristic", metaModelversion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( MESSAGE_COLLECTION_WITHOUT_DATA_TYPE, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getFocusNode() ).isEqualTo( focusNode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCollectionCharacteristicWithMultipleElementCharacteristics( final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestCollectionWithMultipleElementCharacteristics";
      final SemanticError result = getSingleSemanticValidationError(
            "collection-instance-shape", "TestCollectionWithMultipleElementCharacteristics", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( MESSAGE_DUPLICATE_PROPERTY );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getFocusNode() ).isEqualTo( focusNode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void TestCollectionWithInvalidCollectionFailure( final KnownVersion metaModelVersion ) {
      final String focusNode = TEST_NAMESPACE_PREFIX + "TestCollectionWithInvalidCharacteristic";
      final SemanticError result = getSingleSemanticValidationError( "collection-instance-shape",
            "TestCollectionWithInvalidCharacteristic", metaModelVersion );
      assertThat( result.getResultMessage() ).isEqualTo( resolveValidationMessage( MESSAGE_COLLECTION_WITHOUT_DATA_TYPE, result ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getFocusNode() ).isEqualTo( focusNode );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testCollectionWithAnonymousCharacteristicSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "collection-instance-shape", "TestCollectionWithAnonymousElement", metaModelVersion );
   }
}
