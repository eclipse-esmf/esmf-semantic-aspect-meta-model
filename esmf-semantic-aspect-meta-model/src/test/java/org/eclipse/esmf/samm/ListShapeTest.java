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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.topbraid.shacl.vocabulary.SH;

public class ListShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testListTypeIsAPropertyOrOperationExpectSuccess( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final Model shapesModel = validator.loadShapes( metaModelVersion );
      final Model definitionsModel = loadMetaModelDefinitions( metaModelVersion );
      final List<Statement> shapes = List.copyOf(
            shapesModel.listStatements( null, RDF.type, SH.NodeShape ).toList()
      );
      shapes.stream().map( Statement::getSubject )
            .map( shapeResource -> shapeResource.listProperties( SH.property ).toList() )
            .flatMap( Collection::stream )
            .map( propertyStatement -> propertyStatement.getObject().asResource() )
            .map( propertyResource -> propertyResource
                  .getProperty( ResourceFactory.createProperty( sammUrns.listTypeUrn ) ) )
            .filter( Objects::nonNull )
            .map( Statement::getObject )
            .forEach( rdfNode -> assertThat( definitionsModel.containsResource( rdfNode ) ).isTrue() );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testListShapeWithMultipleListTypesExpectFailure( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );

      final Model shapesModel = validator.loadShapes( metaModelVersion );
      final List<Statement> shapes = List.copyOf(
            shapesModel.listStatements( null, RDF.type, SH.NodeShape ).toList()
      );
      shapes.stream().map( Statement::getSubject )
            .map( shapeResource -> shapeResource.listProperties( SH.property ).toList() )
            .flatMap( Collection::stream )
            .map( propertyStatement -> propertyStatement.getObject().asResource() )
            .map( propertyResource -> propertyResource
                  .listProperties( ResourceFactory.createProperty( sammUrns.listTypeUrn ) ) )
            .map( ExtendedIterator::toList )
            .map( List::size )
            .forEach( integer -> assertThat( integer ).isBetween( 0, 1 ) );
   }
}
