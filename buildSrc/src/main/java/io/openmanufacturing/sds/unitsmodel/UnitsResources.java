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

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Configures model element URIs
 */
public class UnitsResources {
   public UnitsResources( final String namespacePrefix, final String version ) {
      var versionToSet = StringUtils.isAllBlank( version ) ? getVersion() : version;
      var namespacePrefixToSet = StringUtils.isAllBlank( namespacePrefix ) ? "urn:bamm:io.openmanufacturing:" : namespacePrefix;
      namespaceUnits = String.format( namespacePrefixToSet + "unit:%s#", versionToSet );
      namespaceBamm = String.format( namespacePrefixToSet + "meta-model:%s#", versionToSet );
      preferredNameProperty = ResourceFactory.createProperty( namespaceBamm + "preferredName" );
      quantityKindClass = ResourceFactory.createResource( namespaceUnits + QUANTITY_KIND_CLASS_NAME );
      unitClass = ResourceFactory.createResource( namespaceUnits + UNIT_CLASS_NAME );
      quantityKindProperty = ResourceFactory.createProperty( namespaceUnits + QUANTITY_KIND_PROPERTY_NAME );
      commonCodeProperty = ResourceFactory.createProperty( namespaceUnits + COMMON_CODE_PROPERTY_NAME );
      conversionFactorProperty = ResourceFactory.createProperty( namespaceUnits + CONVERSION_FACTOR_PROPERTY_NAME );
      symbolProperty = ResourceFactory.createProperty( namespaceUnits + SYMBOL_PROPERTY_NAME );
      referenceUnitProperty = ResourceFactory.createProperty( namespaceUnits + REFERENCE_UNIT_NAME );
      numericConversionFactorProperty = ResourceFactory.createProperty( namespaceUnits + NUMERIC_CONVERSION_FACTOR_PROPERTY_NAME );
   }

   private String getVersion() {
      ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
      try {
         var site = mapper.readTree( new File( "site.yml" ) );
         return site.at( "/asciidoc/attributes/meta-model-version" ).textValue();
      } catch ( IOException e ) {
         throw new RuntimeException( "The version couldn't be extracted from the site.yml." );
      }
   }

   static final String QUANTITY_KIND_CLASS_NAME = "QuantityKind";
   static final String UNIT_CLASS_NAME = "Unit";
   static final String QUANTITY_KIND_PROPERTY_NAME = "quantityKind";
   static final String COMMON_CODE_PROPERTY_NAME = "commonCode";
   static final String CONVERSION_FACTOR_PROPERTY_NAME = "conversionFactor";
   static final String SYMBOL_PROPERTY_NAME = "symbol";
   static final String REFERENCE_UNIT_NAME = "referenceUnit";
   static final String NUMERIC_CONVERSION_FACTOR_PROPERTY_NAME = "numericConversionFactor";

   private final String namespaceUnits;
   private final String namespaceBamm;
   private final Property preferredNameProperty;
   private final Resource quantityKindClass;
   private final Resource unitClass;
   private final Property quantityKindProperty;
   private final Property commonCodeProperty;
   private final Property conversionFactorProperty;
   private final Property symbolProperty;
   private final Property referenceUnitProperty;
   private final Property numericConversionFactorProperty;

   public Property getPreferredNameProperty() {
      return preferredNameProperty;
   }

   public Resource getQuantityKindClass() {
      return quantityKindClass;
   }

   public Resource getUnitClass() {
      return unitClass;
   }

   public Property getQuantityKindProperty() {
      return quantityKindProperty;
   }

   public Property getCommonCodeProperty() {
      return commonCodeProperty;
   }

   public Property getConversionFactorProperty() {
      return conversionFactorProperty;
   }

   public Property getSymbolProperty() {
      return symbolProperty;
   }

   public Property getReferenceUnitProperty() {
      return referenceUnitProperty;
   }

   public Property getNumericConversionFactorProperty() {
      return numericConversionFactorProperty;
   }

   public String getNamespaceBamm() {
      return namespaceBamm;
   }

   public String getNamespaceUnits() {
      return namespaceUnits;
   }

}
