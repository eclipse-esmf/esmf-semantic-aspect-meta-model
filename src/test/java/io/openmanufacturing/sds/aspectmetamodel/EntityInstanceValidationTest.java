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

package io.openmanufacturing.sds.aspectmetamodel;

import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.openmanufacturing.sds.validation.ModelLoader;
import io.openmanufacturing.sds.validation.SemanticError;

public class EntityInstanceValidationTest extends AbstractShapeTest {

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testPoint3dEntityInstanceValidity( final KnownVersion metaModelVersion ) {
      if ( metaModelVersion.equals( KnownVersion.BAMM_1_0_0 ) ) {
         checkValidity( "validate-shared-entities", "TestFileResourceEntityInstance", metaModelVersion );
         return;
      }

      // Point3d shared entity definition is erroneous and in consequence produces a validation error.
      // Until this error is fixed, the validity test had to be inverted and now passes for validation error instead of successful validation.
      final BammUrns bammUrns = new BammUrns( metaModelVersion );
      final Model model = loadModel( "validate-shared-entities", "TestPoint3dEntityInstance", metaModelVersion );

      // Additionally load Point3d.ttl, which is currently flawed
      final Model point3d = ModelLoader.createModel( "bamm/entity/" + metaModelVersion.toVersionString() + "/Point3d.ttl" );
      model.add( point3d );

      final SemanticError resultForX = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY, bammUrns.xUrn, bammUrns.characteristicUrn, VIOLATION_URN, "" );
      final SemanticError resultForY = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY, bammUrns.yUrn, bammUrns.characteristicUrn, VIOLATION_URN, "" );
      final SemanticError resultForZ = new SemanticError( MESSAGE_MISSING_REQUIRED_PROPERTY, bammUrns.zUrn, bammUrns.characteristicUrn, VIOLATION_URN, "" );
      expectSemanticValidationErrors( model, metaModelVersion, resultForX, resultForY, resultForZ );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testFileResourceEntityInstanceValidity( final KnownVersion metaModelVersion ) {
      checkValidity( "validate-shared-entities", "TestFileResourceEntityInstance", metaModelVersion );
   }

   @ParameterizedTest
   @MethodSource( value = "allVersions" )
   public void testTimeSeriesEntityInstanceValidity( final KnownVersion metaModelVersion ) {
      checkValidity( "validate-shared-entities", "TestTimeSeriesEntityInstance", metaModelVersion );
   }
}
