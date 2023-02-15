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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import io.openmanufacturing.sds.FileDownloader;

/**
 * Downloads the Recommendation 20 zip file, extracts the Excel from it, transforms the Excel to RDF/TTL and writes the
 * corresponding units.ttl file
 */
public class UnitsWriter {
   /*
    * args[0]: URL of Rec20 zip file
    * args[1]: Meta model version
    * args[2]: Custom RDF input directory (path of folder with .ttl files to include)
    * args[3]: Output path (path of units.ttl to write)
    */
   public static void main( final String[] args ) throws IOException {
      final Logger root = (Logger) LoggerFactory.getLogger( org.slf4j.Logger.ROOT_LOGGER_NAME );
      root.setLevel( Level.OFF );
      if ( args.length < 3 ) {
         System.err.printf( "%s: Missing arguments%n", UnitsWriter.class.getSimpleName() );
         return;
      }

      final String rec20ZipUrl = args[0];
      final String metaModelVersion = args[1];
      final String customRdfInputDirectory = args[2];
      final String outputPath = args[3];

      final File outputFile = new File( outputPath );
      if ( outputFile.exists() ) {
         System.out.printf( "%s: Units file %s already exists. Skipping writing.%n", UnitsWriter.class.getSimpleName(), outputPath );
         return;
      }

      final byte[] rec20Excel = getRec20Excel( rec20ZipUrl );
      try ( final OutputStream outputStream = new FileOutputStream( outputPath ) ) {
         final UnitsResources unitsResources = new UnitsResources( metaModelVersion );
         new ModelSerializer( new Units( unitsResources, customRdfInputDirectory, rec20Excel ).createModel(), outputStream, unitsResources ).write();
         outputStream.flush();
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
