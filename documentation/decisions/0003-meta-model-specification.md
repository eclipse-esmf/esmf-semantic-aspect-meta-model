# Formal and informal Meta Model Specification

## Context and Problem Statement

Technical specifications are often writen as prose, albeit as precise as possible. For the Aspect
Meta Model, the specification should either be defined in terms of a formal (= machine readable)
format or at least be augmented by a formal specification. The goal here is to be able to use the
already-as-precise-as-possible formal specification as a basis to build tooling surrounding the
models, such as tools for validating models. By making the formal specification a first-class
citizen of the overall specification, tools are "automatically" up-to-date and don't need to be
manually adjusted with every new specification version.

This raises the questions (which can only reasonably be answered in conjunction):
* Which formalism should be used for the formal specification?
* How is the formal specification of the meta model used to validate models?
* Is it possible to have _only_ a machine readable representation as the specification?
* If the formal specification is the "leading" part of the overall specification, how are formal and
  informal (i.e., human-readble explanations) kept in sync?

## Decision Drivers

* Should be based on established open standards.
* Reasonable tool support should exist.

## Considered Options

* [SHACL](https://www.w3.org/TR/shacl/) (Shapes Constraint Language): What XML Schema is to XML,
  SHACL is to RDF. With possible extensions using SPARQL and JavaScript functions, this is a
  powerful formalism. SHACL Shapes are valid RDF documents themselves.
* [ShEx](https://shex.io/) (Shape Expressions): Similar to SHACL in its goals, but uses a custom
  syntax.
* Use programmatic validation (e.g., implement the Aspect Model validation rules in plain Java).
* Devise a custom validation mechanism or domain-specific language.

## Decision Outcome

Chosen option: "SHACL (Shapes Constraint Language)", because it is both an established, expressive
validation language for RDF and it is implementation language agnostic. By specifying the Aspect
Model validation rules in SHACL, it is possible to easily implement validators in every language
for which a SHACL validator exists. With shapes being defined as RDF documents themselves, handling
(parsing/translating/serlializing...) them programmatically is straightforward since this needs to
be done for the Aspect Models as well.

### Consequences

* Good, because validation rules can implemented declaratively and programming language agnostically
* Bad, because SHACL rules in themselves are no replacement for an additional textual specifciation.
  Development of the overall specifciation must take care to keep the textual specifciation and the
  formal specifciation in sync.

## Outlook

It might be possible to implement a small specification-specific RDF vocabulary that could be used
to attach detailed textual descriptions directly to the SHACL shapes, thus creating a
self-containing machine-readable specification document.
