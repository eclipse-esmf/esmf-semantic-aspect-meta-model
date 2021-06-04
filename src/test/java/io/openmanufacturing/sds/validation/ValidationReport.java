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

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Represents the result of a SHACL validation
 */
public abstract class ValidationReport {
   private ValidationReport() {
   }

   /**
    * Represents a succeeded validation
    */
   public static class ValidReport extends ValidationReport {
      @Override
      public boolean conforms() {
         return true;
      }

      @Override
      public String toString() {
         return "Validation report: Input model is valid";
      }

      @Override
      public Collection<SemanticError> getValidationErrors() {
         return Collections.emptyList();
      }
   }

   /**
    * Represents a failed validation
    */
   public static class InvalidReport extends ValidationReport {
      private final Collection<SemanticError> validationErrors;

      InvalidReport( final Collection<SemanticError> errors ) {
         this.validationErrors = errors;
      }

      @Override
      public boolean conforms() {
         return false;
      }

      @Override
      public Collection<SemanticError> getValidationErrors() {
         return validationErrors;
      }

      @Override
      public String toString() {
         return "Validation report: Validation failed: \n"
               + validationErrors.stream().map( SemanticError::toString ).collect( Collectors.joining( "\n" ) );
      }
   }

   /**
    * Determines if the validation was successful.
    *
    * @return true, if the validation was successful, false if there are validation results
    */
   public abstract boolean conforms();

   /**
    * Returns an {@link Collection} containing the validation errors, if {@link #conforms()} returns true, otherwise an
    * empty {@link Collection} is returned.
    *
    * @return an {@link Collection} containing the collection of validation errors
    */
   public abstract Collection<SemanticError> getValidationErrors();
}
