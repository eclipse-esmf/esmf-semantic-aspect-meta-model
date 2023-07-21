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

public class SammUrns {
   final String nameUrn;
   final String preferredNameUrn;
   final String descriptionUrn;
   final String propertiesUrn;
   final String operationsUrn;
   final String datatypeUrn;
   final String characteristicUrn;
   final String baseCharacteristicUrn;
   final String listTypeUrn;
   final String exampleValueUrn;
   final String valueUrn;
   final String inputUrn;
   final String seeUrn;
   final String leftUrn;
   final String rightUrn;
   final String defaultValueUrn;
   final String allowDuplicatesUrn;
   final String orderedUrn;
   final String unitUrn;
   final String minValueUrn;
   final String maxValueUrn;
   final String valuesUrn;
   final String languageCodeUrn;
   final String localeCodeUrn;
   final String lowerBoundDefinition;
   final String upperBoundDefinition;
   final String deconstructionRule;
   final String elements;
   final String list;
   final String structuredValue;
   final String scale;
   final String integer;
   final String extendsUrn;
   final String xUrn;
   final String yUrn;
   final String zUrn;

   SammUrns( final KnownVersion testedMetaModelVersion ) {
      final String metaMetaModelUrn = "urn:samm:org.eclipse.esmf.samm:meta-meta-model:%s#";
      final String metaModelUrn = "urn:samm:org.eclipse.esmf.samm:meta-model:%s#";
      final String characteristicsUrn = "urn:samm:org.eclipse.esmf.samm:characteristic:%s#";
      final String entitiesUrn = "urn:samm:org.eclipse.esmf.samm:entity:%s#";

      listTypeUrn = String.format( metaMetaModelUrn + "listType", testedMetaModelVersion.toVersionString() );
      allowDuplicatesUrn = String
            .format( metaMetaModelUrn + "allowDuplicates", testedMetaModelVersion.toVersionString() );
      orderedUrn = String.format( metaMetaModelUrn + "ordered", testedMetaModelVersion.toVersionString() );

      nameUrn = String.format( metaModelUrn + "name", testedMetaModelVersion.toVersionString() );
      preferredNameUrn = String.format( metaModelUrn + "preferredName", testedMetaModelVersion.toVersionString() );
      descriptionUrn = String.format( metaModelUrn + "description", testedMetaModelVersion.toVersionString() );
      propertiesUrn = String.format( metaModelUrn + "properties", testedMetaModelVersion.toVersionString() );
      operationsUrn = String.format( metaModelUrn + "operations", testedMetaModelVersion.toVersionString() );
      datatypeUrn = String.format( metaModelUrn + "dataType", testedMetaModelVersion.toVersionString() );
      characteristicUrn = String.format( metaModelUrn + "characteristic", testedMetaModelVersion.toVersionString() );
      exampleValueUrn = String.format( metaModelUrn + "exampleValue", testedMetaModelVersion.toVersionString() );
      valueUrn = String.format( metaModelUrn + "value", testedMetaModelVersion.toVersionString() );
      inputUrn = String.format( metaModelUrn + "input", testedMetaModelVersion.toVersionString() );
      seeUrn = String.format( metaModelUrn + "see", testedMetaModelVersion.toVersionString() );
      leftUrn = String.format( characteristicsUrn + "left", testedMetaModelVersion.toVersionString() );
      rightUrn = String.format( characteristicsUrn + "right", testedMetaModelVersion.toVersionString() );

      defaultValueUrn = String.format( characteristicsUrn + "defaultValue", testedMetaModelVersion.toVersionString() );
      unitUrn = String.format( characteristicsUrn + "unit", testedMetaModelVersion.toVersionString() );
      minValueUrn = String.format( characteristicsUrn + "minValue", testedMetaModelVersion.toVersionString() );
      maxValueUrn = String.format( characteristicsUrn + "maxValue", testedMetaModelVersion.toVersionString() );
      valuesUrn = String.format( characteristicsUrn + "values", testedMetaModelVersion.toVersionString() );
      lowerBoundDefinition = String
            .format( characteristicsUrn + "lowerBoundDefinition", testedMetaModelVersion.toVersionString() );
      upperBoundDefinition = String
            .format( characteristicsUrn + "upperBoundDefinition", testedMetaModelVersion.toVersionString() );
      deconstructionRule = String
            .format( characteristicsUrn + "deconstructionRule", testedMetaModelVersion.toVersionString() );
      elements = String.format( characteristicsUrn + "elements", testedMetaModelVersion.toVersionString() );
      list = String.format( characteristicsUrn + "List", testedMetaModelVersion.toVersionString() );
      structuredValue = String.format( characteristicsUrn + "StructuredValue", testedMetaModelVersion.toVersionString() );
      scale = String.format( characteristicsUrn + "scale", testedMetaModelVersion.toVersionString() );
      integer = String.format( characteristicsUrn + "integer", testedMetaModelVersion.toVersionString() );
      languageCodeUrn = String.format( characteristicsUrn + "languageCode", testedMetaModelVersion.toVersionString() );
      localeCodeUrn = String.format( characteristicsUrn + "localeCode", testedMetaModelVersion.toVersionString() );
      baseCharacteristicUrn = String
            .format( characteristicsUrn + "baseCharacteristic", testedMetaModelVersion.toVersionString() );
      extendsUrn = String.format( metaModelUrn + "extends", testedMetaModelVersion.toVersionString() );

      xUrn = String.format( entitiesUrn + "x", testedMetaModelVersion.toVersionString() );
      yUrn = String.format( entitiesUrn + "y", testedMetaModelVersion.toVersionString() );
      zUrn = String.format( entitiesUrn + "z", testedMetaModelVersion.toVersionString() );
   }
}
