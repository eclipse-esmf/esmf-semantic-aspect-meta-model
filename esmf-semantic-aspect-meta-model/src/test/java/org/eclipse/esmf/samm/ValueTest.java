package org.eclipse.esmf.samm;

import org.eclipse.esmf.samm.validation.SemanticError;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ValueTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void defaultValueDefinition( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "DefaultValue", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( "versionsStartingWith2_2_0" )
   void invalidValueDeclarationExpectedFailure( KnownVersion metaModelVersion ) {
      final SemanticError expectedError = new SemanticError( "Property needs to have at least 1 values, but found 0",
            testNamespacePrefix + "GreenLight", "urn:samm:org.eclipse.esmf.samm:meta-model:2.2.0#value", violationUrn, "" );

      expectSemanticValidationErrors( "value-shape", "InvalidValueDeclaration", metaModelVersion, expectedError );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void valueWithEnumeration( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "ValueWithEnumeration", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void multipleValueWithEnumeration( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "MultipleValueWithEnumeration", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void referenceValueWithProperty( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "ReferenceValueWithProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void anonymousDescribedValueInExampleValue( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "AnonymousDescribedValueInExampleValue", metaModelVersion );
   }
}
