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

import static org.apache.jena.rdf.model.ResourceFactory.createLangLiteral;
import static org.apache.jena.rdf.model.ResourceFactory.createPlainLiteral;
import static org.apache.jena.rdf.model.ResourceFactory.createResource;
import static org.apache.jena.rdf.model.ResourceFactory.createStatement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.text.CaseUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates model elements from the source Excel file
 */
public class UnitsFromRec20ExcelSupplier implements Supplier<Stream<Statement>> {
   public UnitsFromRec20ExcelSupplier( final UnitsResources unitsResources ) {
      unitsResource = unitsResources;
   }

   private final UnitsResources unitsResource;
   private static final Logger LOG = LoggerFactory.getLogger( UnitsFromRec20ExcelSupplier.class );

   private static final String REC20_EXCEL = "/rec20_Rev7e_2010.xls";

   private static class WorksheetConfiguration {
      final int worksheetNumber;
      final int columnQuantity;
      final int columnStatus;
      final int columnCommonCode;
      final int columnName;
      final int columnConversionFactor;
      final int columnSymbol;

      WorksheetConfiguration( final int worksheetNumber, final int columnQuantity, final int columnStatus,
            final int columnCommonCode, final int columnName, final int columnConversionFactor,
            final int columnSymbol ) {
         this.worksheetNumber = worksheetNumber;
         this.columnQuantity = columnQuantity;
         this.columnStatus = columnStatus;
         this.columnCommonCode = columnCommonCode;
         this.columnName = columnName;
         this.columnConversionFactor = columnConversionFactor;
         this.columnSymbol = columnSymbol;
      }
   }

   private final WorksheetConfiguration annex1 = new WorksheetConfiguration( 1, 3, 5, 6, 7, 8, 9 );
   private final WorksheetConfiguration annex2 = new WorksheetConfiguration( 2, -1, 0, 1, 2, 6, 5 );

   private String sanitizeIdentifier( final String input ) {
      // Handle symbol of unit NIL
      if ( input.equals( "()" ) ) {
         return input;
      }

      final String treated = input
            .trim()
            .replace( "per cubiv metre", "per cubic metre" )
            .replaceAll( "([Cc])elcius", "$1elsius" )
            .replaceAll( "[\n\r]*", "" )
            .replace( '\u03A9', '\u2126' ) // Replace U+03A9: (GREEK CAPITAL LETTER OMEGA) with the
            // correct symbol U+2126: (OHM SIGN)
            .replace( 'º', '°' );
      if ( treated.startsWith( "(" ) && treated.endsWith( ")" ) ) {
         return treated
               .replaceAll( "^\\(", "" )
               .replaceAll( "\\)$", "" );
      } else {
         return treated;
      }
   }

   private String getNameForUnitPreferredName( final String preferredName ) {
      return getNameForPreferredName(
            preferredName.replace( "length", "length unit" ) // To prevent name clash with length quantity kind
      );
   }

   private String getNameForPreferredName( final String preferredName ) {
      final String pretreated = preferredName
            .replace( "%", "percent" ) // Appears in kiloGramOfSubstance90%Dry
            .replaceAll( "^30", "thirty" ) // Appears in 30-day month
            .replaceAll( "^8", "eight" ) // Appears in 8-part cloud cover
            .replace( "ü", "ue" ) // Appears in the Grüneisen Parameter quantity kind
            .replace( "é", "e" ) // Appears in the Néel Temperature quantity kind
            .replace( "'", "" )
            .replace( ".", "" )
            .replace( ", ", " " )
            .replace( ",", "point" )
            .replace( "actual/", "actual per" ) // Appears in actual/360
            .replace( "MMSCF/day", "mmscf per day" )
            .replace( "/", "or" )
            .replace( "º", "degrees" )
            .replace( "°", "degrees" )
            .replace( ' ', ' ' ) // non-breakable space
            .replace( '[', '(' )
            .replace( ']', ')' )
            .replace( '-', ' ' );
      return CaseUtils.toCamelCase( pretreated, false, '(', ')' );
   }

   private Stream<Statement> convertQuantityStringToRdf( final String quantityString ) {
      final Stream<String> quantityKindPreferredNames = extractQuantityKindPreferredNames( quantityString );

      return quantityKindPreferredNames.flatMap( quantityKindPreferredName -> {
         final String quantityName = getNameForPreferredName( quantityKindPreferredName );
         final Resource quantity = createResource( unitsResource.getNamespaceUnits() + quantityName );
         final Literal preferredNameLiteral = createLangLiteral( quantityKindPreferredName, "en" );

         return Stream.of(
               createStatement( quantity, RDF.type, unitsResource.getQuantityKindClass() ),
               createStatement( quantity, unitsResource.getPreferredNameProperty(), preferredNameLiteral ) );
      } );
   }

   private Stream<String> extractQuantityKindPreferredNames( final String quantityString ) {
      if ( quantityString.isEmpty() ) {
         return Stream.empty();
      }

      final Stream<String> quantityKindPreferredNames;
      if ( quantityString.equals( "specific heat capacity at: - constant pressure, -constant volume,- saturation" ) ) {
         quantityKindPreferredNames = Stream.of( "constant pressure", "constant volume", "saturation" )
               .map( str -> "specific heat capacity at " + str );
      } else {
         quantityKindPreferredNames = Arrays.stream( quantityString.split( "," ) );
      }
      return quantityKindPreferredNames.map( this::sanitizeIdentifier );
   }

   private String sanitizeConversionFactor( final String conversionFactor ) {
      return conversionFactor.replace( ',', '.' ) // Use regular decimal point
            .replaceAll( "(\\d) +(\\d)", "$1$2" ) // Remove spaces in numbers
            .replaceAll( "\\p{Space}\\p{Space}*", " " ) // Replace consecutive spaces
            .replace( 'x', '×' ) // Uniformly use correct multiplication symbol
            .replace( ' ', ' ' ) // Non-breakable space
            .trim();
   }

   private Stream<Statement> convertTableEntryToRdf( final TableEntry tableEntry ) {
      if ( tableEntry.getStatus() == TableEntry.Status.DELETED
            || tableEntry.getStatus() == TableEntry.Status.DEPRECATED ) {
         return Stream.empty();
      }

      final String preferredName = sanitizeIdentifier( tableEntry.getName() );
      final String unitName = getNameForUnitPreferredName( preferredName );
      final Resource unit = createResource( unitsResource.getNamespaceUnits() + unitName );
      final Literal preferredNameLiteral = createLangLiteral( preferredName, "en" );
      final Literal commonCodeLiteral = createPlainLiteral( tableEntry.getCommonCode() );
      final Stream<Literal> conversionFactorLiteral = tableEntry.getConversionFactor().isEmpty() ?
            Stream.empty() : Stream.of( createPlainLiteral( sanitizeConversionFactor( tableEntry.getConversionFactor() ) ) );
      final Stream<Literal> symbolLiteral = tableEntry.getSymbol().isEmpty() ?
            Stream.empty() :
            Stream.of( createPlainLiteral( sanitizeIdentifier( tableEntry.getSymbol() ) ) );

      final Stream<Statement> quantityKindReferences = extractQuantityKindPreferredNames( tableEntry.getQuantity() )
            .map( this::getNameForPreferredName ).flatMap( quantityKindName -> {
                     final Resource quantityKind = createResource( unitsResource.getNamespaceUnits() + quantityKindName );
                     final Statement quantityKindReference = createStatement( unit, unitsResource.getQuantityKindProperty(), quantityKind );
                     return Stream.of( quantityKindReference );
                  }
            );

      final Stream<Statement> unitStream = Stream.of(
                  Stream.of( createStatement( unit, RDF.type, unitsResource.getUnitClass() ) ),
                  Stream.of( createStatement( unit, unitsResource.getPreferredNameProperty(), preferredNameLiteral ) ),
                  Stream.of( createStatement( unit, unitsResource.getCommonCodeProperty(), commonCodeLiteral ) ),
                  conversionFactorLiteral.map( literal -> createStatement( unit, unitsResource.getConversionFactorProperty(), literal ) ),
                  symbolLiteral.map( literal -> createStatement( unit, unitsResource.getSymbolProperty(), literal ) ) )
            .reduce( Stream.empty(), Stream::concat );

      return Stream.of( unitStream, convertQuantityStringToRdf( tableEntry.getQuantity() ), quantityKindReferences )
            .reduce( Stream::concat )
            .orElse( Stream.empty() );
   }

   private Stream<TableEntry> getExcelTableEntriesForAnnex( final Workbook wb, final WorksheetConfiguration configuration ) {
      final Sheet sheet = wb.getSheetAt( configuration.worksheetNumber );

      return StreamSupport.stream( sheet.spliterator(), false ).skip( 1 ).map( row -> {
         final String quantity = configuration.columnQuantity > 0 ? row.getCell( configuration.columnQuantity ).toString() : "";
         final String commonCode = row.getCell( configuration.columnCommonCode ).toString();
         final String name = row.getCell( configuration.columnName ).toString();
         final String conversionFactor = row.getCell( configuration.columnConversionFactor ).toString();
         final String symbol = row.getCell( configuration.columnSymbol ).toString();
         final String status = row.getCell( configuration.columnStatus ).toString();
         return new TableEntry( quantity, commonCode, name, conversionFactor, symbol,
               TableEntry.Status.fromStatusSymbol( status ).orElse( TableEntry.Status.EMPTY ) );
      } );
   }

   @Override
   public Stream<Statement> get() {
      try ( final InputStream inputStream = new FileInputStream( "buildSrc/" + REC20_EXCEL );
            final Workbook workbook = WorkbookFactory.create( inputStream ) ) {
         return Stream.concat(
               getExcelTableEntriesForAnnex( workbook, annex1 ),
               getExcelTableEntriesForAnnex( workbook, annex2 ) ).flatMap( this::convertTableEntryToRdf );
      } catch ( final IOException e ) {
         LOG.error( e.getMessage() );
         return Stream.empty();
      }
   }
}
