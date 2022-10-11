/*
 * Copyright (c) 2021 Robert Bosch Manufacturing Solutions GmbH
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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;

import io.openmanufacturing.sds.validation.ModelLoader;
import io.openmanufacturing.sds.validation.SemanticError;
import io.openmanufacturing.sds.validation.ValidationReport;
import io.openmanufacturing.sds.validation.Validator;

public abstract class AbstractShapeTest {
   final static String TEST_NAMESPACE = "io.openmanufacturing.test";
   final static String TEST_NAMESPACE_VERSION = "1.0.0";
   final static Validator validator = new Validator();
   private final Map<KnownVersion, Model> metaModel = new HashMap<>();

   protected Model getMetaModel( final KnownVersion version ) {
      if ( metaModel.get( version ) != null ) {
         return metaModel.get( version );
      }
      final Model result = loadMetaModelDefinitions( version );
      metaModel.put( version, result );
      return result;
   }

   protected static Stream<KnownVersion> allVersions() {
      return KnownVersion.getVersions().stream().dropWhile( KnownVersion.BAMM_1_0_0::isNewerThan );
   }

   protected static Stream<KnownVersion> versionsUpToIncluding( final KnownVersion version ) {
      return allVersions().takeWhile( v -> version.isNewerThan( v ) || v.equals( version ) );
   }

   protected static Stream<KnownVersion> versionsStartingWith( final KnownVersion version ) {
      return allVersions().dropWhile( version::isNewerThan );
   }

   protected static Stream<KnownVersion> versionsStartingWith2_0_0() {
      return versionsStartingWith( KnownVersion.BAMM_2_0_0 );
   }

   protected static Stream<KnownVersion> versionsUpToIncluding1_0_0() {
      return versionsUpToIncluding( KnownVersion.BAMM_1_0_0 );
   }

   final String VIOLATION_URN = "http://www.w3.org/ns/shacl#Violation";
   final String WARNING_URN = "http://www.w3.org/ns/shacl#Warning";

   final String TEST_NAMESPACE_PREFIX = "urn:bamm:io.openmanufacturing.test:1.0.0#";

   final String MESSAGE_MISSING_REQUIRED_PROPERTY = "Property needs to have at least 1 values, but found 0";
   final String MESSAGE_EMPTY_PROPERTY = "Value has less than 1 characters";
   final String MESSAGE_LANG_NOT_UNIQUE = "Language \\\"en\\\" used more than once";
   final String MESSAGE_INVALID_LANG_STRING = "Value must be a valid literal of type langString";
   final String MESSAGE_DUPLICATE_PROPERTY = "Property may only have 1 value, but found 2";
   final String MESSAGE_MISSING_DATATYPE = "No datatype is defined on the Characteristic instance '{$this}'.";
   final String MESSAGE_INVALID_ENTRY_ENTITY_PROPERTY_LIST = "Each element in the Entity's '{$this}' properties list must be either a Property or a blank node "
         + "referring to a Property and defining either bamm:optional \"true\"^^xsd:boolean and/or bamm:payloadName or bamm:notInPayload "
         + "\"true\"^^xsd:boolean.";
   final String MESSAGE_INVALID_ENTITY_PROPERTY_DEFINITION =
         "A Property of the Entity '{$this}' may not be defined with both bamm:optional \"true\"^^xsd:boolean and bamm:notInPayload \"true\"^^xsd:boolean.";
   final String MESSAGE_INVALID_ENTITY_PROPERTY_PAYLOAD_NAME = "A Property of the Entity '{$this}' may not be defined with both bamm:payloadName "
         + "and bamm:notInPayload \"true\"^^xsd:boolean.";
   final String MESSAGE_ENTITY_NOT_USED_IN_ENUMERATION = "A Property of the Entity '{$this}' may only be defined with bamm:notInPayload \"true\"^^xsd:boolean "
         + "when the Entity, or one of its parent Entities, is used as the data type of an Enumeration.";

   final String MESSAGE_INVALID_ENTITY_WITH_SINGLE_PROPERTY = "Entity '{$this}' defining a Property with bamm:notInPayload \"true\"^^xsd:boolean must define "
         + "at least one more Property.";
   final String MESSAGE_VALUE_NOT_OF_DATATYPE = "EnumerationShape ':TestEnumerationValueIsNotOfDefinedDataType': one of the values ('1.0') is not of the specified data type 'xsd:string'.";
   final String MESSAGE_VALUE_IS_PROPERTY = "EnumerationShape ':TestEnumerationValueIsNotALiteralType': DataType is a literal type but one of the values (':testProperty') is defined as bamm:Property.";
   final String MESSAGE_NO_OPERATION = "Value must be an instance of bamm:Operation";
   final String MESSAGE_VALUE_IS_NO_PROPERTY = "Value must be an instance of bamm:Property";
   final String MESSAGE_NO_ADDITIONAL_PROPERTIES = "Entity '{$this}' refining another Entity may not declare additional Properties.";
   final String MESSAGE_INVALID_ENCODING = "Value must be one of [bamm:US-ASCII bamm:ISO-8859-1 bamm:UTF-8 bamm:UTF-16 bamm:UTF-16BE bamm:UTF-16LE]";
   final String MESSAGE_RANGE_NEEDS_MIN_MAX = "RangeConstraint '{$this}' must have at least one minValue or maxValue.";
   final String MESSAGE_INVALID_REGULAR_EXPRESSION = "The RegularExpressionConstraint's value is no valid regular expression.";
   final String MESSAGE_INVALID_DECONSTRUCTION_RULE = "The StructuredValue's deconstructionRule is no valid regular expression.";
   final String MESSAGE_VALUE_DOES_NOT_HAVE_NODE_KIND_IRI = "Value does not have node kind IRI";
   final String MESSAGE_VALUE_MUST_BE_CHARACTERISTIC = "Value must be an instance of bamm:Characteristic";
   final String MESSAGE_MORE_THAN_ZERO_VALUES = "Property may only have 0 values, but found 1";
   final String MESSAGE_INVALID_LOWER_BOUND_DEFINITION_VALUE = "Value must be exactly one of [bamm-c:AT_LEAST, bamm-c:GREATER_THAN]";
   final String MESSAGE_INVALID_UPPER_BOUND_DEFINITION_VALUE = "Value must be exactly one of [bamm-c:LESS_THAN, bamm-c:AT_MOST]";
   final String MESSAGE_INVALID_DECONSTRUCTION = "Deconstruction rule did not match Properties in elements";
   final String MESSAGE_INVALID_MATCHING_GROUPS = "Number of matching capture groups (2) in deconstructionRule does not match number of Properties in "
         + "elements (1)";
   final String MESSAGE_INVALID_MATCHING_GROUPS2 = "Number of matching capture groups (1) in deconstructionRule does not match number of Properties in "
         + "elements (0)";
   final String MESSAGE_NON_MATCHING_GROUPS = "Given Property exampleValue (prop1) does not match group 1 from deconstructionRule (prop1x)";
   final String MESSAGE_INSTANCE_MISSING_REQUIRED_PROPERTY = "Entity instance '{$this}' is missing required Property.";
   final String MESSAGE_DATA_TYPE_NOT_POSITIVE_INTEGER = "Value must be a valid literal of type positiveInteger";
   final String MESSAGE_COLLECTION_WITHOUT_DATA_TYPE = "Collection '{$this}' must be defined with either a 'bamm:dataType' or a 'bamm-c:elementCharacteristic'.";
   final String MESSAGE_DATA_TYPE_NOT_STRING = "Value must be a valid literal of type string";
   final String MESSAGE_RECURSIVE_PROPERTY = "A cycle in the Aspect Model was detected via '{$this}'.";
   final String MESSAGE_INVALID_DATA_TYPE = "The dataType '{?value}' used on Characteristic '{$this}' is neither an allowed xsd or rdf type, "
         + "nor a type that is defined as rdfs:Class.";
   final String MESSAGE_NO_LITERAL = "Value does not have node kind Literal";

   Model loadMetaModelDefinitions( final KnownVersion version ) {
      return ModelLoader.createModel( List.of(
            "bamm/meta-model/" + version.toVersionString() + "/aspect-meta-model-definitions.ttl",
            "bamm/meta-model/" + version.toVersionString() + "/type-conversions.ttl",
            "bamm/characteristic/" + version.toVersionString() + "/characteristic-definitions.ttl",
            "bamm/characteristic/" + version.toVersionString() + "/characteristic-instances.ttl",
            "bamm/entity/" + version.toVersionString() + "/FileResource.ttl",
            "bamm/entity/" + version.toVersionString() + "/TimeSeriesEntity.ttl",
            "bamm/entity/" + version.toVersionString() + "/Point3d.ttl",
            "bamm/unit/" + version.toVersionString() + "/units.ttl"
      ) );
   }

   protected void checkValidity( final String path, final String ttlDefinition, final KnownVersion testedVersion ) {
      final ValidationReport validationReport = validator.apply( loadModel( path, ttlDefinition, testedVersion ), testedVersion );
      if ( !validationReport.conforms() ) {
         System.out.println( validationReport );
      }
      assertThat( validationReport.conforms() ).isTrue();
      assertThat( validationReport.getValidationErrors() ).isEmpty();
   }

   protected SemanticError getSingleSemanticValidationError( final String path, final String ttlDefinition, final KnownVersion testedVersion ) {
      final Model model = loadModel( path, ttlDefinition, testedVersion );
      final ValidationReport validationReport = validator.apply( model, testedVersion );
      assertThat( validationReport.conforms() ).isFalse();
      final SemanticError error = validationReport.getValidationErrors().iterator().next();
      error.setContext( model );
      return error;
   }

   protected void expectSemanticValidationErrors( final String path, final String ttlDefinition, final KnownVersion testedVersion,
         final SemanticError... errors ) {
      final Model model = loadModel( path, ttlDefinition, testedVersion );
      expectSemanticValidationErrors( model, testedVersion, errors );
   }

   protected void expectSemanticValidationErrors( final Model model, final KnownVersion testedVersion, final SemanticError... errors ) {
      for ( final SemanticError error : errors ) {
         error.resolveGenericMessage( model );
      }
      final int numberOfValidationResults = errors.length;
      final ValidationReport validationReport = validator.apply( model, testedVersion );
      assertThat( validationReport.conforms() ).isFalse();
      assertThat( validationReport.getValidationErrors() ).hasSize( numberOfValidationResults );
      assertThat( validationReport.getValidationErrors() ).contains( errors );
   }

   protected Model loadModel( final String path, final String ttlDefinition, final KnownVersion knownVersion ) {
      final Model model = ModelLoader.createModel( String
            .format( "%s/%s/%s/%s/%s.ttl", knownVersion.toString().toLowerCase(), path, TEST_NAMESPACE,
                  TEST_NAMESPACE_VERSION, ttlDefinition ) );
      model.add( getMetaModel( knownVersion ) );
      return model;
   }

   static String modelToString( final Model model ) {
      final StringWriter stringWriter = new StringWriter();
      model.write( stringWriter, "TURTLE" );
      return stringWriter.toString();
   }

   protected String resolveValidationMessage( final String genericValidationMessage, final SemanticError validationResult ) {
      return genericValidationMessage.replace( "{$this}", remapNamespace( validationResult.getContext(), validationResult.getFocusNode() ) )
            .replace( "{?value}", remapNamespace( validationResult.getContext(), validationResult.getValue() ) )
            .replace( "\"", "\\\"" ); // why is this not properly resolved in the SemanticError ?
   }

   private String remapNamespace( final Model model, final String elementName ) {
      final int hashPosition = elementName.indexOf( '#' );
      if ( -1 == hashPosition ) { // some unnamed element
         return elementName;
      }
      final String name = elementName.substring( hashPosition + 1 );
      return model.getNsURIPrefix( elementName.substring( 0, hashPosition + 1 ) ) + ":" + name;
   }
}
