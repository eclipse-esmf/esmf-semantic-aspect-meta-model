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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.jena.rdf.model.Model;

import org.eclipse.esmf.samm.validation.ModelLoader;
import org.eclipse.esmf.samm.validation.SemanticError;
import org.eclipse.esmf.samm.validation.ValidationReport;
import org.eclipse.esmf.samm.validation.Validator;

public abstract class AbstractShapeTest {
   final static String TEST_NAMESPACE = "org.eclipse.esmf.test";
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
      return KnownVersion.getVersions().stream().dropWhile( KnownVersion.SAMM_1_0_0::isNewerThan );
   }

   protected static Stream<KnownVersion> versionsUpToIncluding( final KnownVersion version ) {
      return allVersions().takeWhile( v -> version.isNewerThan( v ) || v.equals( version ) );
   }

   protected static Stream<KnownVersion> versionsStartingWith( final KnownVersion version ) {
      return allVersions().dropWhile( version::isNewerThan );
   }

   protected static Stream<KnownVersion> versionsStartingWith2_0_0() {
      return versionsStartingWith( KnownVersion.SAMM_2_0_0 );
   }

   protected static Stream<KnownVersion> versionsStartingWith2_2_0() {
      return versionsStartingWith( KnownVersion.SAMM_2_2_0 );
   }

   protected static Stream<KnownVersion> versionsUpToIncluding1_0_0() {
      return versionsUpToIncluding( KnownVersion.SAMM_1_0_0 );
   }

   protected static Stream<KnownVersion> versionsUpToIncluding2_1_0() {
      return versionsUpToIncluding( KnownVersion.SAMM_2_1_0 );
   }

   final String violationUrn = "http://www.w3.org/ns/shacl#Violation";
   final String warningUrn = "http://www.w3.org/ns/shacl#Warning";

   final String testNamespacePrefix = "urn:samm:org.eclipse.esmf.samm.test:1.0.0#";

   final String messageMissingRequiredProperty = "Property needs to have at least 1 values, but found 0";
   final String messageEmptyProperty = "Value has less than 1 characters";
   final String messageLangNotUnique = "Language \\\"en\\\" used more than once";
   final String messageInvalidLangString = "Value must be a valid literal of type langString";
   final String messageDuplicateProperty = "Property may only have 1 value, but found 2";
   final String messageMissingDatatype = "No datatype is defined on the Characteristic instance '{$this}'.";

   final String messageHasToUseSubClass = "It is necessary to use a SubClass and not its parent.";
   final String messageInvalidEntryEntityPropertyList =
         "Element '{?value}' in the Entity's '{$this}' properties list must be a property - either directly or " +
               "via a reference to a property with an attribute samm:optional \"true\"^^xsd:boolean and/or samm:payloadName or "
               + "samm:notInPayload \"true\"^^xsd:boolean.";
   final String messageInvalidEntityPropertyDefinition =
         "Property '{?value}' of the Entity '{$this}' must not be defined as both samm:optional \"true\"^^xsd:boolean and "
               + "samm:notInPayload \"true\"^^xsd:boolean.";
   final String messageInvalidEntityPropertyPayloadName =
         "Property '{?value}' of the Entity '{$this}' must not be defined as both samm:payloadName "
               + "and samm:notInPayload \"true\"^^xsd:boolean.";
   final String messageEntityNotUsedInEnumeration =
         "Property '{?value}' of the Entity '{$this}' may only be defined with samm:notInPayload \"true\"^^xsd:boolean "
               + "when the Entity, or one of its parent Entities/Abstract Entities, is used as the data type of an Enumeration.";

   final String messageEntityNotUsedInEnumeration1 =
         "Property '{?value}' of the Entity '{$this}' may only be defined with samm:notInPayload \"true\"^^xsd:boolean "
               + "when the Entity, or one of its parent Entities, is used as the data type of an Enumeration.";

   final String messageCollectionWithoutDataType = "Collection '{$this}' must be defined with either a 'samm:dataType' or a "
         + "'samm-c:elementCharacteristic'.";
   final String messageInvalidEntityWithSingleProperty =
         "Entity '{$this}' defining a Property with samm:notInPayload \"true\"^^xsd:boolean must define "
               + "at least one more Property.";
   final String messageNoOperation = "Value must be an instance of samm:Operation";
   final String messageValueIsNoProperty = "Value must be an instance of samm:Property";
   final String messageNoAdditionalProperties = "Entity '{$this}' refining another Entity may not declare additional Properties.";
   final String messageInvalidEncoding = "Value must be one of [samm:US-ASCII samm:ISO-8859-1 samm:UTF-8 samm:UTF-16 samm:UTF-16BE "
         + "samm:UTF-16LE]";
   final String messageInvalidRegularExpression = "The RegularExpressionConstraint's value is no valid regular expression.";
   final String messageInvalidDeconstructionRule = "The StructuredValue's deconstructionRule is no valid regular expression.";
   final String messageValueDoesNotHaveNodeKindIri = "Value does not have node kind IRI";
   final String messageValueMustBeCharacteristic = "Value must be an instance of samm:Characteristic";
   final String messageMoreThanZeroValues = "Property may only have 0 values, but found 1";
   final String messageInvalidLowerBoundDefinitionValue = "Value must be exactly one of [samm-c:AT_LEAST, samm-c:GREATER_THAN]";
   final String messageInvalidUpperBoundDefinitionValue = "Value must be exactly one of [samm-c:LESS_THAN, samm-c:AT_MOST]";
   final String messageInvalidDeconstruction = "Deconstruction rule did not match Properties in elements";
   final String messageInvalidMatchingGroups =
         "Number of matching capture groups (2) in deconstructionRule does not match number of Properties in "
               + "elements (1)";
   final String messageInvalidMatchingGroups2 =
         "Number of matching capture groups (1) in deconstructionRule does not match number of Properties in "
               + "elements (0)";
   final String messageNonMatchingGroups = "Given Property exampleValue (prop1) does not match group 1 from deconstructionRule (prop1x)";
   final String messageDataTypeNotPositiveInteger = "Value must be a valid literal of type positiveInteger";
   final String messageDataTypeNotString = "Value must be a valid literal of type string";
   final String messageInvalidDataType = "The dataType '{?value}' used on Characteristic '{$this}' is neither an allowed xsd or rdf type, "
         + "nor a type that is defined as rdfs:Class.";
   final String messageNoLiteral = "Value does not have node kind Literal";
   final String messageWrongExampleValueType = "The datatype '{?value}' of the exampleValue neither matches nor can be cast to the "
         + "Property's '{$this}' Characteristic's dataType.";

   Model loadMetaModelDefinitions( final KnownVersion version ) {
      return ModelLoader.createModel( List.of(
            "samm/meta-model/" + version.toVersionString() + "/aspect-meta-model-definitions.ttl",
            "samm/meta-model/" + version.toVersionString() + "/type-conversions.ttl",
            "samm/characteristic/" + version.toVersionString() + "/characteristic-definitions.ttl",
            "samm/characteristic/" + version.toVersionString() + "/characteristic-instances.ttl",
            "samm/entity/" + version.toVersionString() + "/FileResource.ttl",
            "samm/entity/" + version.toVersionString() + "/TimeSeriesEntity.ttl",
            "samm/entity/" + version.toVersionString() + "/Point3d.ttl",
            "samm/unit/" + version.toVersionString() + "/units.ttl"
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

   protected SemanticError getSingleSemanticValidationError( final String path, final String ttlDefinition,
         final KnownVersion testedVersion ) {
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
      for ( final SemanticError error : errors ) {
         error.resolveGenericMessage( model );
      }
      final int numberOfValidationResults = errors.length;
      final ValidationReport validationReport = validator.apply( model, testedVersion );
      assertThat( validationReport.conforms() ).describedAs( "Model %s should contain violations but does not", path ).isFalse();
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

   @SuppressWarnings( "unused" ) // This method is intended to be used in debugger view configuration
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
