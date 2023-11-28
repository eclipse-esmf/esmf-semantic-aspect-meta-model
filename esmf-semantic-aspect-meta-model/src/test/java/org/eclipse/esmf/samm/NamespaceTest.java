package org.eclipse.esmf.samm;

import org.eclipse.esmf.samm.validation.SemanticError;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class NamespaceTest extends AbstractShapeTest{

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0" )
   void testEmptyPropertiesExpectFailureSamm_2_2_0( final KnownVersion metaModelVersion ) {
      final SammUrns sammUrns = new SammUrns( metaModelVersion );
      final String focusNode = testNamespacePrefix;

      final SemanticError resultForPreferredName = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.preferredNameUrn, violationUrn, "@en" );
      final SemanticError resultForDescription = new SemanticError( messageEmptyProperty,
            focusNode, sammUrns.descriptionUrn, violationUrn, "@en" );
      expectSemanticValidationErrors( "namespace", "TestNamespaceEmptyProperties",
            metaModelVersion,
            resultForPreferredName,
            resultForDescription );
   }

   @ParameterizedTest
   @MethodSource( value = "versionsStartingWith2_2_0")
   void testNamespaceInvalidUrn( final KnownVersion metaModelVersion) {
      final String focusNode = "urn:samm:org.eclipse.esmf.samm.test:1.q.0#";

      final SemanticError resultForInvalidUrnNamespace = new SemanticError(
              "Namespace '<urn:samm:org.eclipse.esmf.samm.test:1.q.0#>' uses an invalid URN pattern",
              focusNode, "", violationUrn, focusNode
      );
      expectSemanticValidationErrors( "namespace", "TestNamespaceInvalidUrn",
              metaModelVersion, resultForInvalidUrnNamespace );
   }
}
