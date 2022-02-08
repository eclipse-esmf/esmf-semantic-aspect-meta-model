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

import java.io.InputStream;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.RDFLanguages;

public class StaticRdfProvider implements Supplier<Stream<Statement>> {
   private final Model model;

   public StaticRdfProvider( final String filename ) {
      final InputStream inputStream = StaticRdfProvider.class.getClassLoader().getResourceAsStream( filename );
      model = ModelFactory.createDefaultModel();
      model.read( inputStream, "", RDFLanguages.TURTLE.getName() );
   }

   @Override
   public Stream<Statement> get() {
      return model.listStatements().toList().stream();
   }
}
