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

package io.openmanufacturing.sds.validation;

import java.util.Objects;

/**
 * Represents a validation result as described by the
 * <a href="https://www.w3.org/TR/shacl/#results-validation-result">SHACL specification</a>.
 */
public class SemanticError {
   String resultMessage;
   String focusNode;
   String resultPath;
   String resultSeverity;
   String value;

   public SemanticError( final String resultMessage, final String focusNode, final String resultPath,
         final String resultSeverity,
         final String value ) {
      this.resultMessage = resultMessage;
      this.focusNode = focusNode;
      this.resultPath = resultPath;
      this.resultSeverity = resultSeverity;
      this.value = value;
   }

   @Override
   public String toString() {
      return String.format( "resultMessage:  %s\n"
                  + "focusNode:      %s\n"
                  + "resultPath:     %s\n"
                  + "resultSeverity: %s\n"
                  + "value:          %s\n",
            resultMessage, focusNode, resultPath, resultSeverity, value );
   }

   @Override
   public boolean equals( final Object o ) {
      if ( this == o ) {
         return true;
      }
      if ( o == null || getClass() != o.getClass() ) {
         return false;
      }
      final SemanticError that = (SemanticError) o;
      return Objects.equals( resultMessage, that.resultMessage ) && Objects
            .equals( focusNode, that.focusNode ) && Objects.equals( resultPath, that.resultPath )
            && Objects.equals( resultSeverity, that.resultSeverity ) && Objects
            .equals( value, that.value );
   }

   @Override
   public int hashCode() {
      return Objects.hash( resultMessage, focusNode, resultPath, resultSeverity, value );
   }

   public String getResultMessage() {
      return resultMessage;
   }

   public String getFocusNode() {
      return focusNode;
   }

   public String getResultPath() {
      return resultPath;
   }

   public String getResultSeverity() {
      return resultSeverity;
   }

   public String getValue() {
      return value;
   }
}
