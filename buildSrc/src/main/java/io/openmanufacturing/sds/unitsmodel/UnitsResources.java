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

import static org.apache.jena.rdf.model.ResourceFactory.createProperty;
import static org.apache.jena.rdf.model.ResourceFactory.createResource;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Configures model element URIs
 */
public class UnitsResources {
   public UnitsResources( final String metaModelVersion ) {
      final String namespacePrefix = "urn:bamm:io.openmanufacturing:";
      namespaceUnits = String.format( namespacePrefix + "unit:%s#", metaModelVersion );
      namespaceBamm = String.format( namespacePrefix + "meta-model:%s#", metaModelVersion );
      preferredNameProperty = createProperty( namespaceBamm + "preferredName" );
      quantityKindClass = createResource( namespaceBamm + QUANTITY_KIND_CLASS_NAME );
      unitClass = createResource( namespaceBamm + UNIT_CLASS_NAME );
      quantityKindProperty = createProperty( namespaceBamm + QUANTITY_KIND_PROPERTY_NAME );
      commonCodeProperty = createProperty( namespaceBamm + COMMON_CODE_PROPERTY_NAME );
      conversionFactorProperty = createProperty( namespaceBamm + CONVERSION_FACTOR_PROPERTY_NAME );
      symbolProperty = createProperty( namespaceBamm + SYMBOL_PROPERTY_NAME );
      referenceUnitProperty = createProperty( namespaceBamm + REFERENCE_UNIT_NAME );
      numericConversionFactorProperty = createProperty( namespaceBamm + NUMERIC_CONVERSION_FACTOR_PROPERTY_NAME );
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
