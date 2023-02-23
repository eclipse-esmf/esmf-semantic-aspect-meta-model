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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.eclipse.esmf.samm.KnownVersion;
import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.vocabulary.SH;

import io.vavr.Tuple2;

public class Validator implements BiFunction<Model, KnownVersion, ValidationReport> {
   private final Map<KnownVersion, Model> shapesModel = new HashMap<>();

   protected Model getShapesModel( final KnownVersion version ) {
      if ( shapesModel.get( version ) != null ) {
         return shapesModel.get( version );
      }
      final Model result = loadShapes( version );
      shapesModel.put( version, result );
      return result;
   }

   @Override
   public ValidationReport apply( final Model model, final KnownVersion version ) {
      final Resource report = ValidationUtil.validateModel( model, getShapesModel( version ), true );

      if ( report.getProperty( SH.conforms ).getObject().asLiteral().getBoolean() ) {
         return new ValidationReport.ValidReport();
      }

      return new ValidationReport.InvalidReport( buildSemanticValidationErrors( report ) );
   }

   private String getValidationResultField( final Resource validationResultResource, final Property property ) {
      return Optional.ofNullable( validationResultResource.getProperty( property ) )
            .map( Statement::getObject )
            .map( RDFNode::toString )
            .orElse( "" );
   }

   private Collection<SemanticError> buildSemanticValidationErrors( final Resource report ) {
      final Collection<SemanticError> semanticValidationErrors = new ArrayList<>();
      for ( final NodeIterator it = report.getModel().listObjectsOfProperty( report, SH.result ); it.hasNext(); ) {
         final Resource validationResultResource = it.next().asResource();
         final String resultMessage = getValidationResultField( validationResultResource, SH.resultMessage );
         final String focusNode = getValidationResultField( validationResultResource, SH.focusNode );
         final String resultPath = getValidationResultField( validationResultResource, SH.resultPath );
         final String resultSeverity = getValidationResultField( validationResultResource, SH.resultSeverity );
         final String value = getValidationResultField( validationResultResource, SH.value );
         semanticValidationErrors
               .add( new SemanticError( resultMessage, focusNode, resultPath, resultSeverity, value ) );
      }
      return semanticValidationErrors;
   }

   public Model loadShapes( final KnownVersion version ) {
      final Model shapesModel = ModelLoader.createModel( List.of(
            "samm/meta-model/" + version.toVersionString() + "/prefix-declarations.ttl",
            "samm/meta-model/" + version.toVersionString() + "/aspect-meta-model-shapes.ttl",
            "samm/meta-model/" + version.toVersionString() + "/type-conversions.ttl",
            "samm/characteristic/" + version.toVersionString() + "/characteristic-shapes.ttl"
      ) );
      final Set<Tuple2<Statement, Statement>> changeSet = determineSammUrlsToReplace( shapesModel );
      changeSet.forEach( urlReplacement -> {
         shapesModel.remove( urlReplacement._1() );
         shapesModel.add( urlReplacement._2() );
      } );

      return shapesModel;
   }

   /**
    * Determines all statements that refer to a samm:// URL and their replacements where the samm:// URL has
    * been replaced with a URL that is resolvable in the current context (e.g. to the class path or via HTTP).
    *
    * @param model the input model
    * @return the tuples of the original statement to replace and the replacement statement
    */
   private Set<Tuple2<Statement, Statement>> determineSammUrlsToReplace( final Model model ) {
      return model.listStatements( null, SH.jsLibraryURL, (RDFNode) null ).toList().stream()
            .filter( statement -> statement.getObject().isLiteral() )
            .filter( statement -> statement.getObject().asLiteral().getString().startsWith( "samm://" ) )
            .flatMap( statement -> rewriteSammUrl( statement.getObject().asLiteral().getString() )
                  .stream()
                  .map( newUrl ->
                        ResourceFactory.createStatement( statement.getSubject(), statement.getPredicate(),
                              ResourceFactory.createTypedLiteral( newUrl, XSDDatatype.XSDanyURI ) ) )
                  .map( newStatement -> new Tuple2<>( statement, newStatement ) ) )
            .collect( Collectors.toSet() );
   }

   /**
    * URLs inside meta model shapes, in particular those used with sh:jsLibraryURL, are given as samm:// URLs
    * in order to decouple them from the way they are resolved (i.e. currently to a file in the class path, but
    * in the future this could be resolved using the URL of a suitable service). This method takes a samm:// URL
    * and rewrites it to the respective URL of the object on the class path.
    *
    * @param sammUrl the samm URL in the format samm://PART/VERSION/FILENAME
    * @return The corresponding class path URL to resolve the meta model resource
    */
   private Optional<String> rewriteSammUrl( final String sammUrl ) {
      final Matcher matcher = Pattern.compile( "^samm://([\\p{Alpha}-]*)/(\\d+\\.\\d+\\.\\d+)/(.*)$" )
            .matcher( sammUrl );
      if ( matcher.find() ) {
         return KnownVersion.fromVersionString( matcher.group( 2 ) ).flatMap( metaModelVersion -> {
            final String spec = String
                  .format( "samm/%s/%s/%s", matcher.group( 1 ), metaModelVersion.toVersionString(),
                        matcher.group( 3 ) );
            final URL resource = Validator.class.getClassLoader().getResource( spec );
            return Optional.ofNullable( resource ).map( URL::toString );
         } );
      }
      if ( sammUrl.startsWith( "samm://scripts/" ) ) {
         final String resourcePath = sammUrl.replace( "samm://", "samm/" );
         final URL resource = Validator.class.getClassLoader().getResource( resourcePath );
         return Optional.ofNullable( resource ).map( URL::toString );
      }
      return Optional.empty();
   }

   private final static String prefixes = "prefix mmm: <urn:samm:org.eclipse.esmf.samm:meta-meta-model:%s#> \r\n" +
         "prefix samm: <urn:samm:org.eclipse.esmf.samm:meta-model:%s#> \r\n" +
         "prefix samm-c: <urn:samm:org.eclipse.esmf.samm:characteristic:%s#> \r\n" +
         "prefix unit: <urn:samm:org.eclipse.esmf.samm:unit:%s#> \r\n" +
         "prefix sh: <http://www.w3.org/ns/shacl#> \r\n" +
         "prefix xsd: <http://www.w3.org/2001/XMLSchema#> \r\n" +
         "prefix dash: <http://datashapes.org/dash#> \r\n" +
         "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \r\n" +
         "prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \r\n";

   public String getMessageText( final String shapeName, final String propertyName, final String errId, final KnownVersion version ) {
      final String currentVersionPrefixes = String.format( prefixes, version.toVersionString(), version.toVersionString(), version.toVersionString(),
            version.toVersionString() );
      final String queryString = String.format(
            "%s select ?errorMessage where { %s sh:property ?propertyNode."
                  + "    ?propertyNode sh:path ?propertyName."
                  + "    ?propertyNode sh:sparql ?sparqlNode."
                  + "    ?sparqlNode sh:message ?errorMessage."
                  + "    ?sparqlNode sh:select ?query. "
                  + "filter ( ?propertyName = %s ) filter ( contains( ?query, '%s' ) ) }",
            currentVersionPrefixes, shapeName, propertyName, errId );
      return executeQuery( queryString, version );
   }

   public String getMessageText( final String shapeName, final String errId, final KnownVersion version ) {
      final String currentVersionPrefixes = String.format( prefixes, version.toVersionString(), version.toVersionString(), version.toVersionString(),
            version.toVersionString() );
      final String queryString = String.format(
            "%s select ?errorMessage where { %s sh:sparql ?sparqlNode. ?sparqlNode sh:message ?errorMessage. ?sparqlNode sh:select ?query. filter ( contains( ?query, '%s' ) ) }",
            currentVersionPrefixes, shapeName, errId );
      return executeQuery( queryString, version );
   }

   private String executeQuery( final String queryString, final KnownVersion version ) {
      final Query query = QueryFactory.create( queryString );
      try ( final QueryExecution qexec = QueryExecutionFactory.create( query, getShapesModel( version ) ) ) {
         final ResultSet results = qexec.execSelect();
         if ( results.hasNext() ) {
            final QuerySolution solution = results.nextSolution();
            final Literal l = solution.getLiteral( "errorMessage" );
            return l.getString();
         }
      }
      return "";
   }
}
