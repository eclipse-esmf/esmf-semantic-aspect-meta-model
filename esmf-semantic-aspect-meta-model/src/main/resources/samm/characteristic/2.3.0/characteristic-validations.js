/*
  Copyright (c) 2025 Robert Bosch Manufacturing Solutions GmbH

  See the AUTHORS file(s) distributed with this work for additional
  information regarding authorship.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at https://mozilla.org/MPL/2.0/.

 SPDX-License-Identifier: MPL-2.0
*/

function testStructuredValueConstruction($this) {
    var samm = "urn:samm:org.eclipse.esmf.samm:meta-model:2.3.0#";
    var sammc = "urn:samm:org.eclipse.esmf.samm:characteristic:2.3.0#";
    var rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    var exampleValueUrn = TermFactory.namedNode(samm + "exampleValue");
    var deconstructionRuleUrn = TermFactory.namedNode(sammc + "deconstructionRule");
    var elementsUrn = TermFactory.namedNode(sammc + "elements");
    var rdfRest = TermFactory.namedNode(rdf + "rest");
    var rdfFirst = TermFactory.namedNode(rdf + "first");
    var rdfNil = TermFactory.namedNode(rdf + "nil");

    var deconstructionRuleIterator = $data.find($this, deconstructionRuleUrn, null);
    var deconstructionRule = null;
    for (var it = deconstructionRuleIterator.next(); it; it = deconstructionRuleIterator.next()) {
        deconstructionRule = it.object.lex;
    }
    deconstructionRuleIterator.close();

    if (deconstructionRule == null) {
        return null;
    }

    var listNodeIterator = $data.find($this, elementsUrn, null);
    var listNode = listNodeIterator.next().object;
    var stringToMatch = "";
    var propertyValues = [];
    while (listNode != null && !("" + listNode === "" + rdfNil)) {
        var listElementIterator = $data.find(listNode, rdfFirst, null);
        var listElement = listElementIterator.next().object;
        if (!listElement.isURI() && !listElement.isLiteral()) {
            listElementIterator.close();
            return true;
        }
        var targetLiteral;
        if (listElement.isLiteral()) {
            targetLiteral = listElement.lex;
        } else {
            var exampleValueIterator = $data.find(listElement, exampleValueUrn, null);
            var exampleValue = exampleValueIterator.next();
            if (exampleValue == null) {
                // If one of the exampleValues is missing, skip additional validation
                exampleValueIterator.close();
                listElementIterator.close();
                return true;
            }
            targetLiteral = exampleValue.object.lex;
            exampleValueIterator.close();
            propertyValues.push(targetLiteral);
        }
        listElementIterator.close();
        stringToMatch += targetLiteral;
        listNodeIterator.close();
        listNodeIterator = $data.find(listNode, rdfRest, null);
        listNode = listNodeIterator.next().object;
    }
    listNodeIterator.close();

    var regex = new RegExp(deconstructionRule, 'g');
    var match = regex.exec(stringToMatch);

    if (match == null) {
        return {message: "Deconstruction rule did not match Properties in elements", value: deconstructionRule};
    }

    if (match.length - 1 != propertyValues.length) {
        return {
            message: "Number of matching capture groups (" + (match.length - 1) + ") in deconstructionRule does not match number of Properties in elements (" + propertyValues.length + ")",
            value: deconstructionRule
        };
    }

    for (var group = 1; group <= propertyValues.length; group++) {
        if (match[group] !== propertyValues[group - 1]) {
            return {
                message: "Given Property exampleValue (" + propertyValues[group - 1] + ") does not match group " + group + " from deconstructionRule (" + match[group] + ")",
                value: deconstructionRule
            };

        }
    }

    return true;
}
