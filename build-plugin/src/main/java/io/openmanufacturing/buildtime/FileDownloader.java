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

package io.openmanufacturing.buildtime;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownloader {
   public static InputStream download( final String urlString ) throws IOException {
      final URL url = URI.create( urlString ).toURL();
      String proxySetting = System.getenv( "http_proxy" );
      if ( proxySetting == null ) {
         proxySetting = System.getenv( "HTTP_PROXY" );
      }
      final URLConnection urlConnection;
      if ( proxySetting != null ) {
         final Pattern proxyPattern = Pattern.compile( "^http://([^:]*):(\\d*)" );
         final Matcher matcher = proxyPattern.matcher( proxySetting );
         if ( !matcher.find() ) {
            System.err.println( "Could not parse proxy configuration " + proxySetting );
            urlConnection = url.openConnection();
         } else {
            final String proxyHost = matcher.group( 1 );
            final int proxyPort = Integer.parseInt( matcher.group( 2 ) );
            final SocketAddress address = new InetSocketAddress( proxyHost, proxyPort );
            final Proxy proxy = new Proxy( Proxy.Type.HTTP, address );
            urlConnection = url.openConnection( proxy );
         }
      } else {
         urlConnection = url.openConnection();
      }

      return  urlConnection.getInputStream();
   }
}
