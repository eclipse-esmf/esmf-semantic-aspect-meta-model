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

import java.io.File;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

/**
 * Main API class that creates the BAMM Units model
 */
public class Units {
   private final UnitsResources unitsResources;
   private final String customRdfInputDirectory;
   private final byte[] rec20Excel;

   public Units( final UnitsResources unitsResources, final String customRdfInputDirectory, final byte[] rec20Excel ) {
      this.unitsResources = unitsResources;
      this.customRdfInputDirectory = customRdfInputDirectory;
      this.rec20Excel = rec20Excel;
   }

   private Model create() {
      final Model model = ModelFactory.createDefaultModel();

      model.setNsPrefix( "unit", unitsResources.getNamespaceUnits() );
      model.setNsPrefix( "rdf", RDF.getURI() );
      model.setNsPrefix( "rdfs", RDFS.getURI() );
      model.setNsPrefix( "xsd", XSD.getURI() );
      model.setNsPrefix( "bamm", unitsResources.getNamespaceBamm() );

      return model;
   }

   private Model addRec20( final Model model ) {
      new UnitsFromRec20ExcelSupplier( unitsResources, rec20Excel ).get().forEach( model::add );
      return new UnitsReferencesAdder( unitsResources ).apply( model );
   }

   public Model createModel() {
      final Model model = addRec20( create() );

      Stream.of(
                  "custom-quantitykinds.ttl",
                  "custom-units.ttl" )
            .map( filename -> new File( customRdfInputDirectory + filename ) )
            .map( StaticRdfProvider::new )
            .flatMap( Supplier::get )
            .forEach( model::add );

      return model;
   }
}
