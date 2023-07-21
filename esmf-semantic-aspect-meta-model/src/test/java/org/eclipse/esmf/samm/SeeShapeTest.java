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

public class SeeShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testSeePropertyValidationExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "see-shape", "TestSeeProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testSeePropertyDoesNotHaveNodeKindIriExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String aspectName = "TestSeePropertyDoesNotHaveNodeKindIri";
      final String focusNode = testNamespacePrefix + aspectName;
      final SemanticError resultForSee = new SemanticError(
            messageValueDoesNotHaveNodeKindIri, focusNode, sammUrns.seeUrn, violationUrn,
            "https://eclipse-esmf.github.io/" );
      expectSemanticValidationErrors( "see-shape", aspectName, metaModelVersion, resultForSee );
   }
}
