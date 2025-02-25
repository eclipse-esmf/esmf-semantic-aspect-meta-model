package org.eclipse.esmf.samm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ValueTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void defaultValueDefinition ( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "DefaultValue", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void valueWithEnumeration ( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "ValueWithEnumeration", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void multipleValueWithEnumeration ( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "MultipleValueWithEnumeration", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void valueWithProperty ( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "ValueWithProperty", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void valueWithExampleValue ( final KnownVersion metaModelVersion ) {
      checkValidity( "value-shape", "ValueWithExampleValue", metaModelVersion );
   }
}
