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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.esmf.samm.buildtime.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Downloads the Recommendation 20 zip file, extracts the Excel from it, transforms the Excel to RDF/TTL and writes the
 * corresponding units.ttl file
 */
@Mojo( name = "generateUnits", defaultPhase = LifecyclePhase.GENERATE_RESOURCES )
public class UnitsGeneratorMojo extends AbstractMojo {
   private final Logger LOG = LoggerFactory.getLogger( UnitsGeneratorMojo.class );

   @Parameter( required = true )
   String rec20Url;

   @Parameter( required = true )
   String metaModelVersion;

   @Parameter( required = true )
   String customRDFInputDirectory;

   @Parameter( required = true )
   String outputPath;

   @Override
   public void execute() throws MojoExecutionException {
      final File outputFile = new File( outputPath );
      if ( outputFile.exists() ) {
         LOG.info( "{}: Units file {} already exists. Skipping writing.", UnitsGeneratorMojo.class.getSimpleName(), outputPath );
         return;
      }

      LOG.info( "Generating {}", outputPath );
      try ( final OutputStream outputStream = new FileOutputStream( outputPath ) ) {
         final byte[] rec20Excel = getRec20Excel( rec20Url );
         final UnitsResources unitsResources = new UnitsResources( metaModelVersion );
         new ModelSerializer( new Units( unitsResources, customRDFInputDirectory, rec20Excel ).createModel(), outputStream, unitsResources ).write();
         outputStream.flush();
      } catch ( final Exception exception ) {
         throw new MojoExecutionException( "Could not write file", exception );
      }
   }

   /**
    * Download and unzip the Rec20 Excel file
    * @param rec20ZipUrl the URL of the zip file
    * @return the content of the .xls file
    * @throws IOException if something can not be downloaded or accessed
    */
   private static byte[] getRec20Excel( final String rec20ZipUrl ) throws IOException {
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      final InputStream inputStream = FileDownloader.download( rec20ZipUrl );
      final ZipInputStream zip = new ZipInputStream( inputStream );
      ZipEntry entry;
      final byte[] buffer = new byte[2048];
      while ( (entry = zip.getNextEntry()) != null ) {
         if ( !entry.getName().endsWith( ".xls" ) ) {
            continue;
         }
         int len;
         while ( (len = zip.read( buffer )) > 0 ) {
            outputStream.write( buffer, 0, len );
         }
         zip.closeEntry();
      }
      zip.close();
      outputStream.close();
      return outputStream.toByteArray();
   }
}
