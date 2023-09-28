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

package org.eclipse.esmf.samm.validation;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.jena.rdf.model.Model;

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

   Model context;

   Map<String, String> messageParams;

   public final static String ANY_VALUE = UUID.randomUUID().toString();

   public SemanticError( final String resultMessage, final String focusNode, final String resultPath, final String resultSeverity, final String value ) {
      this.resultMessage = resultMessage;
      this.focusNode = focusNode;
      this.resultPath = resultPath;
      this.resultSeverity = resultSeverity;
      this.value = value;
   }

   public void resolveGenericMessage( final Model model ) {
      resultMessage = resultMessage.replace( "{$this}", remapNamespace( model, focusNode ) )
            .replace( "{?value}", remapNamespace( model, value ) );
      Optional.ofNullable( messageParams ).stream().map( Map::entrySet ).flatMap( Collection::stream )
            .forEach( e -> resultMessage = resultMessage.replace( e.getKey(), remapNamespace( model, e.getValue() ) ) );
   }

   private String remapNamespace( final Model model, final String elementName ) {
      final int hashPosition = elementName.indexOf( '#' );
      if ( -1 == hashPosition ) {
         return elementName;
      }
      final String name = elementName.substring( hashPosition + 1 );
      return model.getNsURIPrefix( elementName.substring( 0, hashPosition + 1 ) ) + ":" + name;
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
            && Objects.equals( resultSeverity, that.resultSeverity )
            && (value.equals( ANY_VALUE ) || that.value.equals( ANY_VALUE ) || Objects.equals( value, that.value ));
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

   public Model getContext() {
      return context;
   }

   public void setContext( final Model context ) {
      this.context = context;
   }

   public SemanticError withParams( Map<String, String> messageParams ) {
      this.messageParams = messageParams;
      return this;
   }
}
