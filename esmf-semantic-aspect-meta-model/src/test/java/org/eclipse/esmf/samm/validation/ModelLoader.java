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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFLanguages;

/**
 * Utilities to load RDF/Turtle files
 */
public class ModelLoader {
   public static Model createModel( final String resourcePath ) {
      final URL resourceUrl = ModelLoader.class.getClassLoader().getResource( resourcePath );
      if ( resourceUrl != null )
         return createModel( resourceUrl );
      throw new IllegalArgumentException( String.format( "The requested model \"%s\" is not present.", resourcePath ) );
   }

   public static Model createModel( final URL input ) {
      try {
         return createModel( input.openStream() );
      } catch ( final IOException exception ) {
         throw new RuntimeException( exception );
      }
   }

   public static Model createModel( final InputStream inputStream ) {
      final Model streamModel = ModelFactory.createDefaultModel();
      streamModel.read( inputStream, "", RDFLanguages.TURTLE.getName() );
      return streamModel;
   }

   public static Model createModel( final List<String> resourcePaths ) {
      final Model model = ModelFactory.createDefaultModel();
      resourcePaths.stream().map( ModelLoader::createModel ).forEach( model::add );
      return model;
   }
}
