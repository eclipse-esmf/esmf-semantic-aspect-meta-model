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

package org.eclipse.esmf.samm.buildtime.unitsgenerator;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents an entry (row) in the source Excel file
 */
class TableEntry {
   private final String quantity;
   private final String commonCode;
   private final String name;
   private final String conversionFactor;
   private final String symbol;
   private final Status status;

   /**
    * Maintenance indicators as described in the PDF accompanying the UNECE/Rec20 Excel file.
    */
   enum Status {
      EMPTY( "" ),
      ADDED( "+" ),
      CHANGED_NAME( "#" ),
      CHANGED_CHARACTERISTIC( "|" ),
      DEPRECATED( "D" ),
      DELETED( "X" ),
      REINSTATED( "=" );

      String statusSymbol;

      Status( final String statusSymbol ) {
         this.statusSymbol = statusSymbol;
      }

      public String getStatusSymbol() {
         return statusSymbol;
      }

      public static Optional<Status> fromStatusSymbol( final String symbol ) {
         return Arrays.stream( Status.values() )
               .filter( status -> status != Status.EMPTY )
               .filter( status -> symbol.startsWith( status.getStatusSymbol() ) )
               .findAny();
      }
   }

   TableEntry( final String quantity, final String commonCode, final String name, final String conversionFactor,
         final String symbol, final Status status ) {
      this.quantity = quantity;
      this.commonCode = commonCode;
      this.name = name;
      this.conversionFactor = conversionFactor;
      this.symbol = symbol;
      this.status = status;
   }

   String getQuantity() {
      return quantity;
   }

   String getCommonCode() {
      return commonCode;
   }

   String getName() {
      return name;
   }

   String getConversionFactor() {
      return conversionFactor;
   }

   String getSymbol() {
      return symbol;
   }

   Status getStatus() {
      return status;
   }
}
