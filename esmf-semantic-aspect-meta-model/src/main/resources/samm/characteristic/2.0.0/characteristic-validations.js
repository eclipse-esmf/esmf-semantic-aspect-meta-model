/*
  Copyright (c) 2021 Robert Bosch Manufacturing Solutions GmbH

  See the AUTHORS file(s) distributed with this work for additional
  information regarding authorship.

  This Source Code Form is subject to the terms of the Mozilla Public
  License, v. 2.0. If a copy of the MPL was not distributed with this
  file, You can obtain one at https://mozilla.org/MPL/2.0/.

 SPDX-License-Identifier: MPL-2.0
*/

/**
 * Checks if a given value is a regular expression.
 *
 * @param $this The context of the Constraint, i.e. the focus node
 * @param $value The value that can be reached from the focus node following the Shape's sh:path
 */
function isValidRegularExpression($this, $value) {
    if (!$value.isLiteral()) {
        return false;
    }

    var isValid = true;
    try {
        new RegExp($value.lex);
    } catch (e) {
        isValid = false;
    }

    return isValid;
}

var languageTagRegex =
    "  (" + //group grandfathered
    "    (en-GB-oed|i-ami|i-bnn|i-default|i-enochian|i-hak|i-klingon|i-lux|i-mingo|i-navajo|i-pwn|i-tao|i-tay|i-tsu|sgn-BE-FR|sgn-BE-NL|sgn-CH-DE)" + //group irregular
    "    |" +
    "    (art-lojban|cel-gaulish|no-bok|no-nyn|zh-guoyu|zh-hakka|zh-min|zh-min-nan|zh-xiang)" + //group regular
    "  )" +
    "  |" +
    "  (" + //group language tag
    "    (([a-z]{2,3})(-([a-z]{3})){0,3})" + //group language with or without extlang
    "    (-([A-Z][a-z]{3}))?" + //group script
    "    (-([A-Z]{2}|[0-9]{3}))?" + //group region
    "    (-([a-z0-9]{5,8}|[0-9][a-z0-9]{3}))*" + //group variant
    "    (-([a-z0-9-[x]](-[a-z0-9]{2,8})+))*" + //group extensions
    "    (-x(-([a-z0-9]{1,8}))+)?" + //group private use
    "  )" +
    "  |" +
    "  (x(-([a-z0-9]{1,8}))+)" //group private use

/**
 * Checks if a given value is a valid language tag according to BCP 47.
 *
 * @param $this The context of the Constraint, i.e. the focus node
 * @param $value The value that can be reached from the focus node following the Shape's sh:path
 */
function isValidBCP47LanguageTag($this, $value) {
    var languageTagRegexWithoutSpaces = languageTagRegex.replace( /\s+/g, '' );
    var regex = new RegExp( languageTagRegexWithoutSpaces, 'g' );
    var languageTagSections = regex.exec( $value );
    if ( languageTagSections == null ) {
        return {message: "Invalid locale code.", value: $value};
    }
    var grandfathered = languageTagSections[2]
    var language = languageTagSections[6]
    var extlang = languageTagSections[8]
    var script = languageTagSections[10]
    var region = languageTagSections[12]
    var variant = languageTagSections[14]

    var languageRegistry = JSON.parse( languageRegistryAsJson );

    if ( grandfathered && languageRegistry.grandfathered.indexOf( grandfathered ) === -1 ) {
        return {message: "Invalid grandfathered locale code.", value: $value};
    }
    if ( language && languageRegistry.languages.indexOf( language ) === -1 ) {
        return {message: "Invalid language in locale code.", value: $value};
    }
    if ( extlang && languageRegistry.extlangs.indexOf( extlang ) === -1 ) {
        return {message: "Invalid extlang in locale code.", value: $value};
    }
    if ( script && languageRegistry.scripts.indexOf( script ) === -1 ) {
        return {message: "Invalid script in locale code.", value: $value};
    }
    if ( region && languageRegistry.regions.indexOf( region ) === -1 ) {
        return {message: "Invalid region in locale code.", value: $value};
    }
    if ( variant && languageRegistry.variants.indexOf( variant ) === -1 ) {
        return {message: "Invalid variant in locale code.", value: $value};
    }

    return true;
}

function testStructuredValueConstruction($this) {
    var samm = "urn:samm:org.eclipse.esmf.samm:meta-model:2.0.0#";
    var sammc = "urn:samm:org.eclipse.esmf.samm:characteristic:2.0.0#";
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
