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
      final String META_META_MODEL_URN = "urn:samm:org.eclipse.samm:meta-meta-model:%s#";
      final String META_MODEL_URN = "urn:samm:org.eclipse.samm:meta-model:%s#";
      final String CHARACTERISTICS_URN = "urn:samm:org.eclipse.samm:characteristic:%s#";
      final String ENTITIES_URN = "urn:samm:org.eclipse.samm:entity:%s#";

      listTypeUrn = String.format( META_META_MODEL_URN + "listType", testedMetaModelVersion.toVersionString() );
      allowDuplicatesUrn = String
            .format( META_META_MODEL_URN + "allowDuplicates", testedMetaModelVersion.toVersionString() );
      orderedUrn = String.format( META_META_MODEL_URN + "ordered", testedMetaModelVersion.toVersionString() );

      nameUrn = String.format( META_MODEL_URN + "name", testedMetaModelVersion.toVersionString() );
      preferredNameUrn = String.format( META_MODEL_URN + "preferredName", testedMetaModelVersion.toVersionString() );
      descriptionUrn = String.format( META_MODEL_URN + "description", testedMetaModelVersion.toVersionString() );
      propertiesUrn = String.format( META_MODEL_URN + "properties", testedMetaModelVersion.toVersionString() );
      operationsUrn = String.format( META_MODEL_URN + "operations", testedMetaModelVersion.toVersionString() );
      datatypeUrn = String.format( META_MODEL_URN + "dataType", testedMetaModelVersion.toVersionString() );
      characteristicUrn = String.format( META_MODEL_URN + "characteristic", testedMetaModelVersion.toVersionString() );
      exampleValueUrn = String.format( META_MODEL_URN + "exampleValue", testedMetaModelVersion.toVersionString() );
      valueUrn = String.format( META_MODEL_URN + "value", testedMetaModelVersion.toVersionString() );
      inputUrn = String.format( META_MODEL_URN + "input", testedMetaModelVersion.toVersionString() );
      seeUrn = String.format( META_MODEL_URN + "see", testedMetaModelVersion.toVersionString() );
      leftUrn = String.format( CHARACTERISTICS_URN + "left", testedMetaModelVersion.toVersionString() );
      rightUrn = String.format( CHARACTERISTICS_URN + "right", testedMetaModelVersion.toVersionString() );

      defaultValueUrn = String.format( CHARACTERISTICS_URN + "defaultValue", testedMetaModelVersion.toVersionString() );
      unitUrn = String.format( CHARACTERISTICS_URN + "unit", testedMetaModelVersion.toVersionString() );
      minValueUrn = String.format( CHARACTERISTICS_URN + "minValue", testedMetaModelVersion.toVersionString() );
      maxValueUrn = String.format( CHARACTERISTICS_URN + "maxValue", testedMetaModelVersion.toVersionString() );
      valuesUrn = String.format( CHARACTERISTICS_URN + "values", testedMetaModelVersion.toVersionString() );
      lowerBoundDefinition = String
            .format( CHARACTERISTICS_URN + "lowerBoundDefinition", testedMetaModelVersion.toVersionString() );
      upperBoundDefinition = String
            .format( CHARACTERISTICS_URN + "upperBoundDefinition", testedMetaModelVersion.toVersionString() );
      deconstructionRule = String
            .format( CHARACTERISTICS_URN + "deconstructionRule", testedMetaModelVersion.toVersionString() );
      elements = String.format( CHARACTERISTICS_URN + "elements", testedMetaModelVersion.toVersionString() );
      list = String.format( CHARACTERISTICS_URN + "List", testedMetaModelVersion.toVersionString() );
      structuredValue = String.format( CHARACTERISTICS_URN + "StructuredValue", testedMetaModelVersion.toVersionString() );
      scale = String.format( CHARACTERISTICS_URN + "scale", testedMetaModelVersion.toVersionString() );
      integer = String.format( CHARACTERISTICS_URN + "integer", testedMetaModelVersion.toVersionString() );
      languageCodeUrn = String.format( CHARACTERISTICS_URN + "languageCode", testedMetaModelVersion.toVersionString() );
      localeCodeUrn = String.format( CHARACTERISTICS_URN + "localeCode", testedMetaModelVersion.toVersionString() );
      baseCharacteristicUrn = String
            .format( CHARACTERISTICS_URN + "baseCharacteristic", testedMetaModelVersion.toVersionString() );
      extendsUrn = String.format( META_MODEL_URN + "extends", testedMetaModelVersion.toVersionString() );

      xUrn = String.format( ENTITIES_URN + "x", testedMetaModelVersion.toVersionString() );
      yUrn = String.format( ENTITIES_URN + "y", testedMetaModelVersion.toVersionString() );
      zUrn = String.format( ENTITIES_URN + "z", testedMetaModelVersion.toVersionString() );
   }
}
