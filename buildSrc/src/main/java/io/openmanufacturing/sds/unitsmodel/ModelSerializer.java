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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.RDF;

/**
 * Custom Turtle Serializer for the units catalog: Unlike Jena's pretty printer, this makes sure that the
 * order of units is always identical (i.e. sorted), making it easier to track and understand incremental changes.
 */
public class ModelSerializer {
   private static final String INDENT = "   ";
   private final Model model;
   private final PrintWriter writer;
   private UnitsResources unitsResources;

   private final Comparator<Statement> subjectComparator = Comparator.comparing( statement -> serialize(
         statement.getSubject() ) );

   private final Comparator<Statement> propertyComparator = ( statement1, statement2 ) -> {
      final Property predicate1 = statement1.getPredicate();
      final Property predicate2 = statement2.getPredicate();
      if ( predicate1.equals( RDF.type ) ) {
         return -1;
      }
      if ( predicate2.equals( RDF.type ) ) {
         return 1;
      }
      if ( predicate1.equals( this.unitsResources.getPreferredNameProperty() ) ) {
         return -1;
      }
      if ( predicate2.equals( this.unitsResources.getPreferredNameProperty() ) ) {
         return 1;
      }
      return predicate1.getLocalName().compareTo( predicate2.getLocalName() );
   };

   private final Comparator<Statement> objectComparator = Comparator.comparing( statement -> serialize(
         statement.getObject() ) );

   public ModelSerializer( final Model model, final OutputStream outputStream, final UnitsResources unitsResources ) {
      this.model = model;
      this.unitsResources = unitsResources;
      final OutputStreamWriter utf8OutputStreamWriter = new OutputStreamWriter( outputStream, StandardCharsets.UTF_8 );
      writer = new PrintWriter( utf8OutputStreamWriter );
      PrintUtil.registerPrefixMap( model.getNsPrefixMap() );
   }

   void write() {
      writeCopyrightHeader();
      writer.format( "%n" );
      writePrefixes();
      writer.format( "%n" );
      writeQuantityKinds();
      writer.format( "%n" );
      writeUnits();
      writer.flush();
      writer.close();
   }

   private void writeCopyrightHeader() {
      writer.format( "# Copyright (c) 2022 Robert Bosch Manufacturing Solutions GmbH %n"
            + "# %n"
            + "# See the AUTHORS file(s) distributed with this work for additional %n"
            + "# information regarding authorship. %n"
            + "# %n"
            + "# This Source Code Form is subject to the terms of the Mozilla Public %n"
            + "# License, v. 2.0. If a copy of the MPL was not distributed with this %n"
            + "# file, You can obtain one at https://mozilla.org/MPL/2.0/. %n"
            + "# %n"
            + "# SPDX-License-Identifier: MPL-2.0 %n"
            + "# %n"
            + "# This file was automatically generated, do not modify.%n" );
   }

   private void writePrefixes() {
      final Set<Map.Entry<String, String>> entries = model.getNsPrefixMap().entrySet();
      final int maxLength = entries.stream().map( Map.Entry::getKey ).map( String::length )
            .collect( Collectors.summarizingInt( Integer::intValue ) ).getMax();
      entries.forEach( entry ->
            writer.format( "@prefix %-" + (maxLength + 1) + "s <%s> .%n", entry.getKey() + ":", entry.getValue() ) );
   }

   private String serialize( final RDFNode rdfNode ) {
      if ( rdfNode.isURIResource() && rdfNode.asResource().getURI().equals( RDF.type.getURI() ) ) {
         return "a";
      }

      if ( rdfNode.isLiteral() ) {
         final Literal literal = rdfNode.asLiteral();
         final String value = literal.getLexicalForm();

         final RDFDatatype type = rdfNode.asNode().getLiteralDatatype();
         if ( type == null || type.equals( XSDDatatype.XSDstring ) ) {
            // Special case: unit secondUnitOfAngle that has the symbol "
            if ( value.equals( "\"" ) ) {
               return "'\"'";
            }
            return "\"" + value + "\"";
         }

         if ( type.getURI().equals( RDF.langString.getURI() ) ) {
            return "\"" + value + "\"@" + literal.getLanguage();
         }

         return "\"" + value + "\"^^" + serialize( model.getResource( rdfNode.asNode().getLiteralDatatypeURI() ) );
      }

      return PrintUtil.print( rdfNode ).replace( '\'', '"' );
   }

   private Stream<Statement> getStatements( final Resource s, final Property p, final Resource o,
         final Comparator<Statement> comparator ) {
      final Iterable<Statement> statementIterable = () -> model.listStatements( s, p, o );
      return StreamSupport.stream( statementIterable.spliterator(), false )
            .sorted( comparator );
   }

   private Stream<Resource> getResourcesByType( final Resource type ) {
      return getStatements( null, RDF.type, type, subjectComparator )
            .map( Statement::getSubject );
   }

   private Stream<Statement> getStatementsForResource( final Resource element ) {
      return getStatements( element, null, null, propertyComparator );
   }

   private String serializeQuantityKinds( final Resource resource ) {
      return getStatements( resource, this.unitsResources.getQuantityKindProperty(), null, objectComparator )
            .map( Statement::getObject )
            .map( RDFNode::asResource )
            .map( Resource::getLocalName )
            .map( name -> ":" + name )
            .collect( Collectors.joining( ", " ) );
   }

   private Predicate<Statement> distinctProperty() {
      final Set<Object> seen = ConcurrentHashMap.newKeySet();
      return statement -> seen.add( statement.getPredicate() );
   }

   private void writeResource( final Resource resource ) {
      writer.format( ":%s%n", resource.getLocalName() );
      final String serialized = getStatementsForResource( resource ).filter( distinctProperty() ).map( statement -> {
         final Resource predicate = statement.getPredicate();
         final String serializedObject = predicate.equals( this.unitsResources.getQuantityKindProperty() ) ?
               serializeQuantityKinds( resource ) : serialize( statement.getObject() );
         return String.format( "%s%-25s %s", INDENT, serialize( predicate ), serializedObject );
      } ).collect( Collectors.joining( " ;\n", "", " ." ) );
      writer.format( "%s%n%n", serialized );
   }

   private void writeQuantityKinds() {
      writer.format( "# Quantity Kinds%n%n" );
      getResourcesByType( this.unitsResources.getQuantityKindClass() ).forEach( this::writeResource );
   }

   private void writeUnits() {
      writer.format( "# Units%n%n" );
      getResourcesByType( this.unitsResources.getUnitClass() ).forEach( this::writeResource );
   }
}
