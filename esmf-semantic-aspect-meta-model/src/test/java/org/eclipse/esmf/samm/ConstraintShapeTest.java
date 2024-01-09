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

public class ConstraintShapeTest extends AbstractShapeTest {
   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0")
   public void testConstraintWithoutProperties ( final KnownVersion metaModelVersion) {

      final String constraintName = "TestConstraintWithoutProperties";
      final String constraintId = testNamespacePrefix + constraintName;
      final String value = testNamespacePrefix + constraintName;

      final SemanticError resultForEmptyConstraint = new SemanticError( messageHasToUseSubClass,
            constraintId, "", violationUrn,  value);

      expectSemanticValidationErrors( "constraint-shape", constraintName, metaModelVersion, resultForEmptyConstraint );
   }
}
