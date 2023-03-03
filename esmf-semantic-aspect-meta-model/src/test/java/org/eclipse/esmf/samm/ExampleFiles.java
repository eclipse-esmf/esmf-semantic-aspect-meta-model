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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

/**
 * This is a JUnit {@link ArgumentsProvider} that provides the paths of the example TTL files used in the textual specifcation.
 * The goal is to be able to validate the example files. It is used in the {@link SpecificationExamplesValidityTest}.
 */
public class ExampleFiles implements ArgumentsProvider {
   @Override
   public Stream<? extends Arguments> provideArguments( final ExtensionContext context ) throws Exception {
      final Path examplesDirectory = Paths.get( System.getProperty( "user.dir" ) )
            .resolve( ".." )
            .resolve( "documentation" )
            .resolve( "modules" )
            .resolve( "ROOT" )
            .resolve( "examples" );
      return Files.list( examplesDirectory )
            .map( Path::toString )
            .sorted()
            .map( FilenameArguments::new );
   }

   private static class FilenameArguments implements Arguments {
      private final String filename;
      private final File file;

      public FilenameArguments( final String path ) {
         file = new File( path );
         filename = file.getName();
      }

      @Override
      public Object[] get() {
         return new Object[] { filename, file };
      }
   }
}
