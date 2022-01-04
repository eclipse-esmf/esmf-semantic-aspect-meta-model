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

package io.openmanufacturing.sds.aspectmetamodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;

/**
 * Downloads the BCP 47 Language Tag Registry as defined by IANA,
 *
 * @see <a href="https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry">https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry</a>,
 *       in JSON format.
 *       The types which are required for the validation of the Locale Constraint, are extracted and written to a
 *       javascript file.
 */
public class DownloadBcp47LanguageSubtagRegistry extends DefaultTask {
   @TaskAction
   @SuppressWarnings( "unchecked" )
   public void run() throws IOException {
      final File languageTagRegistryScriptFile = Path.of( "src/main/resources/bamm/scripts/language-registry.js" )
                                                     .toFile();
      final File targetDirectory = languageTagRegistryScriptFile.getParentFile();
      if ( !targetDirectory.exists() && !targetDirectory.mkdirs() ) {
         throw new IOException( "Could not create directory: " + languageTagRegistryScriptFile.getParent() );
      }
      final URL languageTagRegistryUrl = URI.create(
                                                  "https://raw.githubusercontent.com/mattcg/language-subtag-registry/master/data/json/registry.json" )
                                            .toURL();
      final ArrayList<Map<String, String>> languageTagRegistry = (ArrayList<Map<String, String>>) new JsonSlurper().parse(
            languageTagRegistryUrl );
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

      final String cleanedLanguageTagRegistryJson = JsonOutput.toJson( cleanedLanguageTagRegistry );
      final String languageRegistryScript = String.format( "var languageRegistryAsJson = '%s'",
            cleanedLanguageTagRegistryJson );
      new FileOutputStream( languageTagRegistryScriptFile ).write( languageRegistryScript.getBytes() );
   }
}
