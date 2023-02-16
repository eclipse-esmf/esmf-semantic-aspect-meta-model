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

package io.openmanufacturing.buildtime.unitsmodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFLanguages;

public class StaticRdfProvider implements Supplier<Stream<Statement>> {
   private final Model model;

   public StaticRdfProvider( final File file ) {
      try ( final FileInputStream inputStream = new FileInputStream( file ) ) {
         model = ModelFactory.createDefaultModel();
         model.read( inputStream, "", RDFLanguages.TURTLE.getName() );
      } catch ( final IOException exception ) {
         throw new RuntimeException( exception );
      }
   }

   @Override
   public Stream<Statement> get() {
      return model.listStatements().toList().stream();
   }
}
