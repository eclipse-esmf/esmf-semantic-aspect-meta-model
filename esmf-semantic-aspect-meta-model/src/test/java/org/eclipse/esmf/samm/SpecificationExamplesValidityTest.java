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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import org.eclipse.esmf.samm.validation.ModelLoader;
import org.eclipse.esmf.samm.validation.ValidationReport;

/**
 * This class validates the Aspect Model examples that are part of the textual specification
 */
public class SpecificationExamplesValidityTest extends AbstractShapeTest {
   @ParameterizedTest( name = "[{index}] {0}" )
   @ArgumentsSource( ExampleFiles.class )
   public void testExampleFile( @SuppressWarnings( "unused" /* It's used in the @ParameterizedTest name format */ ) final String filename,
         final File exampleFile ) throws FileNotFoundException {
      final KnownVersion metaModelVersion = KnownVersion.getLatest();
      final Model exampleModel = ModelLoader.createModel( new FileInputStream( exampleFile ) );
      exampleModel.add( getMetaModel( metaModelVersion ) );
      final ValidationReport validationReport = validator.apply( exampleModel, metaModelVersion );
      if ( !validationReport.conforms() ) {
         validationReport.getValidationErrors().forEach( System.out::println );
      }
      assertThat( validationReport.conforms() ).isTrue();
   }
}
