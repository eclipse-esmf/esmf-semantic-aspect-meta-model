/*
 * Copyright (c) 2022 Robert Bosch Manufacturing Solutions GmbH
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

package io.openmanufacturing.sds.unitsmodel;

import static org.apache.jena.rdf.model.ResourceFactory.createStatement;
import static org.apache.jena.rdf.model.ResourceFactory.createTypedLiteral;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

/**
 * For all units U in a model, this function takes U's conversion factor, extracts the non-numeric (i.e. unit) part
 * from it, finds out if there is a unit U' defined with this symbol and adds U' as a reference to U.
 */
public class UnitsReferencesAdder implements UnaryOperator<Model> {

   private final UnitsResources unitResources;

   public UnitsReferencesAdder( final UnitsResources unitsResources ) {
      unitResources = unitsResources;
   }

   /**
    * From a conversionFactor such as "6.894757 x 10³ Pa x m³/s", retrieves the numeric part,
    * i.e. 6894.757
    *
    * @param conversionFactor The conversion factor as given in the original Excel file
    * @return The numeric value of that conversion factor
    */
   private Double getNumericPart( final String conversionFactor ) {
      final String numericPart = conversionFactor.replaceAll( "([\\d×¹²³⁴⁵⁶⁷⁸⁹⁰.⁻ ]*).*", "$1" )
            .replace( " ", "" )
            .replaceAll( "10([¹²³⁴⁵⁶⁷⁸⁹⁰⁻]+)", "e$1" )
            .replace( "×", "" )
            .replaceAll( "^e", "1e" )
            .replace( '⁻', '-' )
            .replace( '¹', '1' )
            .replace( '²', '2' )
            .replace( '³', '3' )
            .replace( '⁴', '4' )
            .replace( '⁵', '5' )
            .replace( '⁶', '6' )
            .replace( '⁷', '7' )
            .replace( '⁸', '8' )
            .replace( '⁹', '9' )
            .replace( '⁰', '0' );

      if ( numericPart.isEmpty() ) {
         return 1.0d;
      }
      return Double.parseDouble( numericPart );
   }

   /**
    * From a conversionFactor such as "6.894757 x 10³ Pa x m³/s", retrieves the symbol part,
    * i.e. "Pa·m³/s"
    *
    * @param conversionFactor The conversion factor as given in the original Excel file
    * @return The symbol part that corresponds with the symbol of another unit
    */
   private String getSymbolPart( final String conversionFactor ) {
      return conversionFactor.replaceAll( "[\\d×¹²³⁴⁵⁶⁷⁸⁹⁰.⁻ ]*(.*)", "$1" )
            .replaceAll( " [×] ", "·" );
   }

   @Override
   public Model apply( final Model model ) {
      final Set<Statement> additionalStatements = new HashSet<>();
      for ( final StmtIterator statementIterator = model.listStatements( null, unitResources.getConversionFactorProperty(),
            (RDFNode) null ); statementIterator.hasNext(); ) {
         final Statement statement = statementIterator.next();
         final Resource unit = statement.getSubject();

         final String conversionFactor = statement.getObject().asLiteral().toString();
         final String conversionFactorSymbol = getSymbolPart( conversionFactor );

         // If the unit's conversion factor symbol is empty, this means there is no reference unit
         if ( conversionFactorSymbol.isEmpty() ) {
            continue;
         }

         // Check if conversion factor and symbol are the same, which makes no sense
         final Statement unitSymbolStatement = unit.getProperty( unitResources.getSymbolProperty() );
         if ( unitSymbolStatement != null && unitSymbolStatement.getObject().asLiteral().toString()
               .equals( conversionFactorSymbol ) ) {
            continue;
         }
         final Double numericConversionFactor = getNumericPart( conversionFactor );
         final Literal numericConversionFactorLiteral = createTypedLiteral( numericConversionFactor );

         final ResIterator iterator = model.listSubjectsWithProperty( unitResources.getSymbolProperty(), conversionFactorSymbol );
         if ( iterator.hasNext() ) {
            final Resource referenceUnit = iterator.next();
            additionalStatements.add( createStatement( unit, unitResources.getReferenceUnitProperty(), referenceUnit ) );
            additionalStatements.add(
                  createStatement( statement.getSubject(), unitResources.getNumericConversionFactorProperty(), numericConversionFactorLiteral ) );
         }
      }
      additionalStatements.forEach( model::add );

      return model;
   }
}
