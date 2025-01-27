package org.eclipse.esmf.samm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class QuantityTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void testQuantityValidationEntity( final KnownVersion metaModelVersion ) {
      checkValidity( "quantity", "QuantityTestEntity", metaModelVersion );
   }
}
