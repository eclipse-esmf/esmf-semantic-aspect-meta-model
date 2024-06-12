# Storage of copyright and license information in Aspect Models

## Context and Problem Statement

Aspect Models are created, edited and shared in the same way as source code files in any software
project and therefore parties working with them have the same requirements torward making copyright
and licence information explicit. Unlike most software source code however, Aspect Models are not
only edited textually but are sometimes interpreted as an RDF graph and are edited like this; and
the approaches for storing such meta information in RDF and textual source code can differ. This ADR
therefore describes the canonical way to address this.

## Decision Drivers

* Existing/established approaches for storing copyright and license information in Aspect Models
  specifically.
* Common practices for storing copyright and license information in source code in general.
* Common practices for storing copyright and license information in RDF documents.

## Considered Options

* Use RDF/Turtle comments block at the start of a file.
* Use a (well-known) third-party RDF vocabulary.
* Introduce specific vocabulary in SAMM, e.g., `samm:license` and `samm:copyright`.

## Decision Outcome

Chosen option: "Use RDF/Turtle comments block at the start of a file", based on the number and
weight of the pros in the pros and cons of the options.

## Pros and Cons of the Options

### Use RDF/Turtle comments block at the start of a file

* Good, because existing bodies of Aspect Models don't need changes.
* Good, because it allows for straightforward integration with automated source code license
  checkers.
* Good, because editing is straightforward and it is the well-established way to do it in all
  software development ecosystems.
* Good, because no meta model changes are necessary.
* Good, because the SAMM namespace is not cluttered with meta data that is irrelevant for the actual
  semantic models.
* Neutral, because systems that store Aspect Models not using files (e.g., in RDF triple stores) can
  and must decide which information they keep and how.
* Neutral, because copyright and license information can only be attached to a file, not to a model
  element or a namespace.
* Bad, because copyright and license information is invisible to tools working purely on the RDF
  level.

### Use a (well-known) third-party RDF vocabulary

* Good, because it makes copyright and license information explicit also on the RDF graph level.
* Good, because the SAMM namespace is not cluttered with meta data that is irrelevant for the actual
  semantic models.
* Neutral, because systems that store Aspect Models not using files (e.g., in RDF triple stores) can
  and must decide which information they keep and how.
* Neutral, because separate copyright and license information can be attached any model element.
* Bad, because it contradicts the reasoning in [ADR-0005](0005-rdf-vocabulary.md) (SAMM as a
  self-contained vocabulary).
* Bad, because existing bodies of Aspect Models could - by definition - not declare any copyright
  and license information; only with the update to the new SAMM version that
  includes/allows/describes this change this would be possible. Therefore all Aspect Models where
  this information is relevant must be updated.
* Bad, because automated source code license checkers can not be expected to understand this
  information, thus potentially creating lots of manual maintenance overhead.
  
### Introduce specific vocabulary in SAMM, e.g., `samm:license` and `samm:copyright`

* Good, because it makes copyright and license information explicit also on the RDF graph level.
* Neutral, because separate copyright and license information can be attached any model element.
* Bad, because existing bodies of Aspect Models could - by definition - not declare any copyright
  and license information; only with the update to the new SAMM version that
  includes/allows/describes this change this would be possible. Therefore all Aspect Models where
  this information is relevant must be updated.
* Bad, because automated source code license checkers can not be expected to understand this
  information, thus potentially creating lots of manual maintenance overhead.

## More Information

The decision to use an RDF/Turtle comment block at the beginning of each aspect model file must also
imply concrete rules how copyright and license information may look like to allow for tool
interoperability.

* Copyright information and license information are optional.
* When copyright and/or license information are to be set for an aspect model file, the following
  rules apply:
   * All copyright and license information for an aspect model file is given at the beginning of a
     file in one continuous block ("the comment block") of commented lines (lines that start with
     the `#` symbol), before any `@prefix` declarations.
   * The comment block can have any length and can contain any text.
   * To declare copyright information, a line in the comment block must use the pattern:\
     `# Copyright [optional additional text] XXXX[-YYYY] COPYRIGHT HOLDER`\
     where sections in [] are optional, XXXX and YYYY are years.
   * The comment block can contain any number of lines with such a copyright pattern.
   * To declare license information, exactly one line in the comment block must use the pattern:\
   `# SPDX-License-Identifier: IDENTIFIER`\
   where IDENTIFIER is a valid [SPDX license identifer](https://spdx.org/licenses/).
* Tools that process (i.e., read and write) Aspect Models should repeat the comment block unchanged
  when writing a file.
