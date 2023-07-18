# Aspect Meta Model RDF Vocabulary

## Context and Problem Statement

In [ADR-0001](0001-formalism-for-aspect-models.md), an RDF/Turtle-based model format was chosen for
the definition of Aspect Models. Should the Aspect Meta Model be based on certain RDF vocabularies,
well-known or otherwise, such as [RDFS](https://www.w3.org/TR/rdf11-schema/),
[OWL](https://www.w3.org/TR/owl2-syntax/), [Dublin Core](https://www.dublincore.org/), etc.? If yes,
which ones and why?

## Decision Drivers

* Ability to build robust tooling.
* Ability adjust semantics of structures incrementally.
* Familiar modeling primitives for model authors.

## Considered Options

* Use external RDF vocabularies or ontologies as basis for the definition of the Aspect Meta Model.
* Define the Aspect Meta Model in a self-contained way, without references to external vocabularies.

## Decision Outcome

Chosen option: "Define the Aspect Meta Model in a self-contained way, without references to external
vocabularies".

Having a well-defined closed vocabulary for the meta model emphasizes its suitability to serve as
the basis for actual implementations. In other words, only by disallowing the common RDF modeling
practice modeling of using arbitrary vocabularies, the implementation of robust and interoperable
tools working on and with Aspect Models becomes possible.

On the _model level_, this can be easily understood using an example: Let's say one modeler likes to
add element descriptions using `rdfs:comment` and another likes to use `dcterms:description`. To
reliably generate code, documentation and other artifacts from the model, we have to choose one way
to add descriptions and disallow all others. Extrapolate this problem from just adding descriptions
to all modeling questions.

On the _meta model level_, there are different problems. Incorporating externally defined
vocabularies would in many cases either break the justified decision in
[ADR-0004](0004-urn-as-identifiers.md) (usage of URNs as identifiers) or incur additional
implementation overhead for special handling. Apart from that, 3rd-party vocabularies can be roughly
put into one of two categories: (1) "Core" vocabularies that have implications for semantics, such
as RDFS and OWL and (2) "Community" vocabularies (everything else). By basing the Aspect Meta Model
directly on a "core" vocabulary, there must be no mismatch between semantics of its primitives and
the intended corresponding Aspect Meta Model primitives, which is neither the case for RDFS nor for
OWL. Incremental adjustment of meta model semantics might prove impossible due to reliance on
externally-defined semantics. Basing the Aspect Meta Model on "community" vocabularies, is broadly
impossible, because their usage would often times convey a different understanding.

For example, the Aspect Meta Model could specify that descriptions for Aspect Model elements must be
asserted using the Dublin Core vocabulary: `:element dcterms:description "description"`. But now the
semantics should be changed so that a description must not be an `xsd:string`, but an
`rdf:langString`, i.e., every description must have a language tag. Dublin Core does not define
this, so this would mean the Aspect Meta Model would need to define additional divergent rules on
top of the existing vocabulary. This approach is error prone for implementations, confusing for
model authors and non-standard - one might even say, breaking semantic web assumptions. As such as
mismatch could happen in any new meta model version, no external vocabularies are used in the meta
model definition.

### Consequences

* Good, because meta model and model definitions become self-containing and enable implementation of
  robust tooling that does not have to rely on changing or unfitting external definitions.
* Bad, because certain terms (e.g., description) for which definitions already exist need to be
  re-defined in the Aspect Meta Model scope.
* Indifferent: Existing external tools based on an understanding of certain vocabularies or
  ontologies might not properly work with Aspect Models. This is however not considered a problem
  since those tools will not understand the majority of Aspect Model semantics, even if certain
  properties are defined using a well-known external RDF vocabulary.

## Outlook

To adapt an Aspect Model so that it works in the context of other RDF-based formats and their tools
is trivial, as long as there are matching semantics. For example, if it were required to make an
Aspect Model a valid OWL ontology (by focusing on similar model element structure (owl:Class roughly
equivalent to :Entity, owl:DatatypeProperty/owl:ObjectProperty roughly equivalent to :Property, and
so on), and ignoring the mismatch in semantics), one could just add the corresponding assertions in
the RDF, i.e., turn this:

```turtle
:FooEntity a :Entity .
```

into

```turtle
:FooEntity a :Entity, owl:Class .
```

For cases where the semantics matter - which _should_ be all cases - this approach would not work,
but a more detailed metamodel-to-metamodel-translation would be required, for example, by expressing
the relationships of `:FooEntity`'s Properties in terms of OWL restrictions.
