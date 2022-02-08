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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Writes the Units model to stdout
 */
public class UnitsWriter extends DefaultTask {
   private String outputPath;
   private String modelVersion;
   private String namespacePrefix;

   @TaskAction
   @SuppressWarnings( "unchecked" )
   public void run() {
      try ( final OutputStream outputStream = StringUtils.isBlank( outputPath ) ? System.out : new FileOutputStream( outputPath ) ) {
         UnitsResources unitsResources = new UnitsResources( namespacePrefix, modelVersion );
         new ModelSerializer( new Units( unitsResources ).createModel(), outputStream, unitsResources ).write();
      } catch ( final IOException e ) {
         getLogger().error( String.format( "An Exception occurred. Following output path was defined: %s", outputPath ), e );
      }
   }

   public void setOutputPath( String outputPath ) {
      this.outputPath = outputPath;
   }

   public void setModelVersion( String modelVersion ) {
      this.modelVersion = modelVersion;
   }

   public void setNamespacePrefix() {
      this.namespacePrefix = namespacePrefix;
   }
}
