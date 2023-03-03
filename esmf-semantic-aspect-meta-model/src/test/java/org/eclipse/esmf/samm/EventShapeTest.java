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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.eclipse.esmf.samm.validation.SemanticError;

public class EventShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithEventExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "event-shape", "AspectWithEvent", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithEventMissingParametersExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "event-shape", "AspectWithEventMissingParameters", metaModelVersion );
      assertThat( result.getResultMessage() )
            .satisfies( s -> assertThat( s ).isEqualTo( MESSAGE_MISSING_REQUIRED_PROPERTY ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testAspectWithEventWithInvalidParametersExpectFailure( final KnownVersion metaModelVersion ) {
      final SemanticError result = getSingleSemanticValidationError(
            "event-shape", "AspectWithEventWithInvalidParameters", metaModelVersion );
      assertThat( result.getResultMessage() )
            .satisfies( s -> assertThat( s ).isEqualTo( MESSAGE_VALUE_IS_NO_PROPERTY ) );
      assertThat( result.getResultSeverity() ).isEqualTo( VIOLATION_URN );
      assertThat( result.getValue() ).isNotEmpty();
   }
}
