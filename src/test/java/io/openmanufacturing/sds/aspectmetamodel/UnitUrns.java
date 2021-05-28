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

public class UnitUrns {

   private final String UNITS_URN = "urn:bamm:io.openmanufacturing:%s:%s#";

   String voltUrn;

   UnitUrns( final String elementType, final KnownVersion testedBammVersion ) {
      voltUrn = String.format( UNITS_URN + "volt", elementType, testedBammVersion.toVersionString() );
   }
}
