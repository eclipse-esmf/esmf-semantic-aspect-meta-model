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

package io.openmanufacturing.buildtime.aspectmetamodel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.openmanufacturing.buildtime.FileDownloader;

/**
 * Downloads the BCP 47 Language Tag Registry as defined by IANA, see
 * <a href="https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry">https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry</a>,
 * in JSON format. The types which are required for the validation of the Locale Constraint, are extracted and written to a
 * JavaScript file.
 */
@Mojo( name = "downloadBcp47LanguageSubtagRegistry", defaultPhase = LifecyclePhase.GENERATE_RESOURCES )
public class DownloadBcp47LanguageSubtagRegistryMojo extends AbstractMojo {
   private final Logger LOG = LoggerFactory.getLogger( DownloadBcp47LanguageSubtagRegistryMojo.class );

   @Parameter( required = true )
   private String inputUrl;

   @Parameter( required = true )
   private String output;

   @Override
   @SuppressWarnings( "unchecked" )
   public void execute() throws MojoExecutionException {
      final File outputFile = Path.of( output ).toFile();
      if ( outputFile.exists() ) {
         LOG.info( "{}: Language subtag registry script file {} already exists. Skipping writing.",
               DownloadBcp47LanguageSubtagRegistryMojo.class.getSimpleName(), output );
         return;
      }

      final File targetDirectory = outputFile.getParentFile();
      if ( !targetDirectory.exists() && !targetDirectory.mkdirs() ) {
         throw new MojoExecutionException( "Could not create directory: " + outputFile.getParent() );
      }

      try ( final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final InputStream input = FileDownloader.download( inputUrl )
      ) {
         final String content = new String( input.readAllBytes(), StandardCharsets.UTF_8 );

         final ObjectMapper objectMapper = new ObjectMapper();
         final List<Map<String, String>> languageTagRegistry = objectMapper.readValue( content.getBytes(), List.class );
         final Map<String, ArrayList<String>> cleanedLanguageTagRegistry = buildCleanedLanguageTagRegistry( languageTagRegistry );
         out.write( "var languageRegistryAsJson = '".getBytes() );
         objectMapper.writeValue( out, cleanedLanguageTagRegistry );
         out.write( "'".getBytes() );
         try ( final FileOutputStream file = new FileOutputStream( outputFile ) ) {
            file.write( out.toByteArray() );
         }
      } catch ( final IOException exception ) {
         throw new MojoExecutionException( "Could not write file", exception );
      }

   }

   private static Map<String, ArrayList<String>> buildCleanedLanguageTagRegistry( final List<Map<String, String>> languageTagRegistry ) {
      final Map<String, ArrayList<String>> cleanedLanguageTagRegistry = new HashMap<>();
      final ArrayList<String> grandfathered = new ArrayList<>();
      final ArrayList<String> languages = new ArrayList<>();
      final ArrayList<String> extlangs = new ArrayList<>();
      final ArrayList<String> scripts = new ArrayList<>();
      final ArrayList<String> regions = new ArrayList<>();
      final ArrayList<String> variants = new ArrayList<>();
      languageTagRegistry.forEach( languageTagRegistryEntry -> {
         final String type = languageTagRegistryEntry.get( "Type" );
         if ( type.equals( "grandfathered" ) ) {
            final String tag = languageTagRegistryEntry.get( "Tag" );
            grandfathered.add( tag );
            return;
         }
         if ( type.equals( "language" ) ) {
            final String tag = languageTagRegistryEntry.get( "Subtag" );
            languages.add( tag );
            return;
         }
         if ( type.equals( "extlang" ) ) {
            final String tag = languageTagRegistryEntry.get( "Subtag" );
            extlangs.add( tag );
            return;
         }
         if ( type.equals( "script" ) ) {
            final String tag = languageTagRegistryEntry.get( "Subtag" );
            scripts.add( tag );
            return;
         }
         if ( type.equals( "region" ) ) {
            final String tag = languageTagRegistryEntry.get( "Subtag" );
            regions.add( tag );
            return;
         }
         if ( type.equals( "variant" ) ) {
            final String tag = languageTagRegistryEntry.get( "Subtag" );
            variants.add( tag );
         }
      } );
      cleanedLanguageTagRegistry.put( "grandfathered", grandfathered );
      cleanedLanguageTagRegistry.put( "languages", languages );
      cleanedLanguageTagRegistry.put( "extlangs", extlangs );
      cleanedLanguageTagRegistry.put( "scripts", scripts );
      cleanedLanguageTagRegistry.put( "regions", regions );
      cleanedLanguageTagRegistry.put( "variants", variants );
      return cleanedLanguageTagRegistry;
   }
}
