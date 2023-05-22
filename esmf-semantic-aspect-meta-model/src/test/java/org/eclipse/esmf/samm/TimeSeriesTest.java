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

import org.eclipse.esmf.samm.validation.SemanticError;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TimeSeriesTest extends AbstractShapeTest {

    @ParameterizedTest
    @MethodSource( value = "versionsStartingWith2_0_0")
    void testTimeSeriesValidationExtendedEntity (final KnownVersion metaModelVersion) {
        checkValidity("time-series", "TimeSeriesTestEntity", metaModelVersion);
    }

    @ParameterizedTest
    @MethodSource( value = "versionsStartingWith2_0_0")
    void testTimeSeriesInvalidExtendedValidationEntity (final KnownVersion metaModelVersion) {
        final SammUrns sammUrns = new SammUrns( metaModelVersion );
        final String focusNode = TEST_NAMESPACE_PREFIX + "TimeSeries";

        final SemanticError resultForInvalidTimeSeriesEntity = new SemanticError(
                "Used data type ':TimeSeriesEntry' on ':TimeSeries' must extend TimeSeriesEntity.",
                focusNode, sammUrns.datatypeUrn, VIOLATION_URN, ""
        );

        expectSemanticValidationErrors("time-series", "TimeSeriesInvalidExtendedTestEntity",
                metaModelVersion, resultForInvalidTimeSeriesEntity);
    }

}
