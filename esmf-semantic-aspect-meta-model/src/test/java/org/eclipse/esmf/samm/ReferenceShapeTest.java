package org.eclipse.esmf.samm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ReferenceShapeTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void testValidReferenceExpectSuccess( final KnownVersion metaModelVersion ) {
      checkValidity( "reference-shape", "TestReference", metaModelVersion );
   }
}
