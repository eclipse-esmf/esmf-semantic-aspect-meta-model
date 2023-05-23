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

package org.eclipse.esmf.samm.buildtime.diagram;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Mojo to render .dot (Graphviz) and .pu (PlantUML) diagrams to SVG. The created SVG will have the used font embedded.
 */
@Mojo( name = "renderDiagram", defaultPhase = LifecyclePhase.GENERATE_SOURCES )
public class DiagramRenderer extends AbstractMojo {
   private static final String FONT_FILE = "RobotoCondensed-Regular.ttf";
   private static final String FONT_NAME = "Roboto Condensed";
   private static final Pattern PRAGMA_LAYOUT_ENGINE = Pattern.compile( "^.*PRAGMA LAYOUT-ENGINE:\\s*(\\w+)$", Pattern.MULTILINE );
   private final Logger LOG = LoggerFactory.getLogger( DiagramRenderer.class );

   @Parameter( property = "sourcePath", required = true )
   private String sourcePath;

   @Parameter( property = "targetPath", required = false )
   private String targetPath;

   @Override
   public void execute() throws MojoExecutionException {
      // SILENCE!
      System.setProperty( "org.slf4j.simpleLogger.log.guru.nidi", "error" );

      LOG.info( "Generating diagram(s) for: " + sourcePath );
      final File source = new File( sourcePath );
      try {
         if ( source.exists() && source.isFile() ) {
            renderDiagram( source );
         } else if ( source.exists() && source.isDirectory() ) {
            for ( final File file : source.listFiles() ) {
               if ( file.isFile() ) {
                  renderDiagram( file );
               }
            }
         }
      } catch ( final IOException exception ) {
         throw new MojoExecutionException( "Could not read or write file", exception );
      }
   }

   private File outputFor( final File input ) {
      final String svgFilename = input.getName().substring( 0, input.getName().indexOf( "." ) ) + ".svg";
      if ( targetPath == null ) {
         return input.toPath().getParent().resolve( svgFilename ).toFile();
      }
      return new File( targetPath ).toPath().resolve( svgFilename ).toFile();
   }

   private void renderDiagram( final File input ) throws IOException {
      if ( input.getName().endsWith( ".dot" ) ) {
         renderDotDiagram( input );
      } else if ( input.getName().endsWith( ".pu" ) ) {
         renderPlantUmlDiagram( input );
      }
   }

   private void renderDotDiagram( final File input ) throws IOException {
      final File outputFile = outputFor( input );
      try ( final FileInputStream dotInputStream = new FileInputStream( input );
            final FileOutputStream output = new FileOutputStream( outputFile ) ) {
         final String dotDocument = new String( slurp( dotInputStream ), StandardCharsets.UTF_8 );

         final MutableGraph graph = new Parser().read( dotDocument );
         final Engine graphvizEngine = determineEngine( dotDocument );
         final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         Graphviz.fromGraph( graph ).engine( graphvizEngine ).render( Format.SVG ).toOutputStream( outputStream );
         final String diagram = outputStream.toString( StandardCharsets.UTF_8 );
         outputStream.close();
         final String diagramWithEmbeddedFont = augmentSvgDiagram( diagram );
         output.write( diagramWithEmbeddedFont.getBytes( StandardCharsets.UTF_8 ) );
         LOG.info( "Written " + outputFile.getCanonicalPath() );
      }
   }

   private void renderPlantUmlDiagram( final File input ) throws IOException {
      final File outputFile = outputFor( input );
      try ( final FileInputStream plantUmlInputStream = new FileInputStream( input );
            final FileOutputStream output = new FileOutputStream( outputFile ) ) {
         final String plantUmlDocument = new String( slurp( plantUmlInputStream ), StandardCharsets.UTF_8 );
         final SourceStringReader reader = new SourceStringReader( plantUmlDocument );
         final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         reader.outputImage( outputStream, new FileFormatOption( FileFormat.SVG ) );
         outputStream.close();
         final String diagramWithEmbeddedFont = augmentSvgDiagram( outputStream.toString( StandardCharsets.UTF_8 ) );
         output.write( diagramWithEmbeddedFont.getBytes( StandardCharsets.UTF_8 ) );
         LOG.info( "Written " + outputFile.getCanonicalPath() );
      }
   }

   private String augmentSvgDiagram( final String diagram ) throws IOException {
      try ( final InputStream fontStream = getClass().getClassLoader().getResourceAsStream( FONT_FILE ) ) {
         final String fontInBase64 = base64EncodeInputStream( fontStream );
         final String css = "\n<style>\n"
               + "@font-face {\n"
               + "    font-family: \"" + FONT_NAME + "\";\n"
               + "    src: url(\"data:application/font-truetype;charset=utf-8;base64," + fontInBase64 + "\");\n"
               + "}\n"
               + "</style>";
         return diagram.replaceFirst( "<g", css + "<g" );
      }
   }

   private Engine determineEngine( final String dotDocument ) {
      final Matcher matcher = PRAGMA_LAYOUT_ENGINE.matcher( dotDocument );
      if ( matcher.find() ) {
         return Engine.valueOf( matcher.group( 1 ).toUpperCase() );
      }
      return Engine.DOT;
   }

   private String base64EncodeInputStream( final InputStream in ) throws IOException {
      return Base64.getEncoder().encodeToString( slurp( in ) );
   }

   private byte[] slurp( final InputStream input ) throws IOException {
      final ByteArrayOutputStream output = new ByteArrayOutputStream();
      IOUtils.copy( input, output );
      return output.toByteArray();
   }
}
