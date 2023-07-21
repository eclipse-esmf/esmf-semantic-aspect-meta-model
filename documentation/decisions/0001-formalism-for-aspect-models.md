# Formalism for the definition of Aspect Models: RDF with Turtle syntax

## Context and Problem Statement

An appropriate format (i.e., formalism, file format and/or serialization) is required for the
formulation of Digital Twin Aspect Models. This includes both the more abstract parts of the model,
such as universally valid classes and the more concrete parts, such as domain specific subclasses or
instances. The requirements that need to be fulfilled by the format and possible candidates are
discussed below.

## Decision Drivers

* Core uses: The models are used as both domain ontologies (i.e., they capture domain semantics
  which are relevant for representation, validation and transformation of data) and as a base for
  the description of APIs (REST-based or otherwise).

* Usability of Aspect Models for developers: Even though Aspect Models are likely going to be
  created by domain experts (using suitable tools), working with the models (editing, formatting,
  reading, writing, transforming etc.) will be done by application developers, both manually and
  programatically. As such, the used formalism must be usable by application developers in a way
  that helps and supports them (improves implementation speed and quality), but should not add
  unnecessary complexity.

* Programming language agnostic code generation: In order to support proper type-safe (programming
  language level) APIs based on Aspect Models, code generation approaches will be employed. As
  multiple programming languages are going to be supported (such as Java and C#), the model format
  should be programming language-agnostic.

* Validation of data: Constraints, such as which properties in which classes and which data ranges
  on which properties are allowed, need to be validated against data at runtime. The necessary
  validation code (or comparable constructs) should be able to use the model as their configuration
  or be directly generated from the model.

* Tool-independent model representation: Enable interchange between different tooling for the
  description of domain-specific modeling tools. As can be expected, not every party that is going
  to interact with Digital Twins and corresponding service implementations will be using the same
  formats or tooling for the specification of their respective domain-specific models. Thus at some
  point, UIs, DSLs or software employing model transformation techniques are going to interact with
  the Aspect Model. The format needs to be flexible and generic enough, respectively, to enable the
  implementation of such extensions.

## Considered Options

* [JSON Schema](http://json-schema.org/)
  * Remarks
    * Evaluation assumption: JSON Schema is widely adopted.
    * JSON Schema is meant to specify the allowed structure of a JSON document and a schema is
      specified in JSON as well.
  * Advantages
    * "Everybody" already understands JSON.
    * Evaluation of data is very easy.
    * Wealth of tools (editors, validators) available, see e.g.,
      [awesome-json](https://github.com/burningtree/awesome-json#json-schema-tools).
  * Disadavantages
    * Not a modeling format per se, i.e., only the data structure is described. Basically impossible
      to express custom semantic information.
    * It is difficult to reference model elements by any kind of ID.
    * Code generation beyond POJOs will most likely need to be implemented by us.

* [RDF](https://www.w3.org/TR/rdf11-primer/) with [Turtle syntax](https://www.w3.org/TR/turtle/)
  * Remarks
    * RDF is a standard model for the representation of graphs, with multiple serialization syntaxes
      available, of which Turtle is the most appropriate for human consumption. Nodes in the graph
      are uniquely identified by URIs. The format imposes virtually no additional semantics beyond
      data types.
  * Advantages
    * Graph model is easy to mold into whatever structure needs to be represented.
    * Turtle syntax is easy to read and edit.
    * Tried and tested tooling is available (parser, libraries etc.), especially in the form of
      [SPARQL](https://www.w3.org/TR/sparql11-overview/), which could serve as a base for validation
      and code generation.
  * Disadavantages
    * Not as prevalent as JSON or YAML.
    * No existing ready-to-use solution for code generation available, would have to be implemented by us.

* [OWL](https://www.w3.org/TR/owl2-primer/) (Web Ontology Language)
  * Remarks
    * Based on RDF, but provides rich modeling (possible for the description of complex semantics).
  * Advantages
    * Almost any conceivable relationship semantics are expressible using powerful abstractions.
    * "Future proof" in the sense that possible transformation between OWL and other formats will
      most likely not result in loss of information.
    * Tooling (parser, Java API, graphical editors) are available, although of varying quality.
  * Disadavantages
    * Not as prevalent as JSON or YAML, more "niche" technology than plain RDF.
    * High learning curve, complex technology.
    * Conceptual mismatch of intended use of OWL with goals of Aspect Models: OWL's open world
      assumption would require a lot of modeling overhead because classes need to manually be
      defined as disjunct; instances need to manually be declared as different things; lengthy
      association of properties with classes via restrictions.
    * Only rudimentary solution available for code generation, would have to be implemented by us.
    * Not easily editable in a text editor (possible, but requires high degree of knowledge to
      create consistent results).

* [Eclipse Vorto](http://www.eclipse.org/vorto/)
  * Remarks
    * Eclipse-based tooling around a DSL for the description of models in the IoT context:
      * Editor with highlighting and auto-completion.
      * Code generator infrastructure + existing implementations.
      * A model repository.
      * A CLI tool.
    * Expressive power of the DSL is intentionally very limited and explicitly intended for "Data
      Types" and "Function Blocks" of IoT devices (i.e., it is not meant for the description of more
      abstract concepts or data conforming to the model).
  * Advantages
    * Based on existing Eclipse open source project.
    * Uses mature and powerful [XText](http://www.eclipse.org/Xtext/) technology.
    * A number of existing [code
      generators](https://github.com/eclipse/vorto/tree/development/generators).
  * Disadavantages
    * Strong conceptual mismatch of intended use of Vorto's custom DSL with goals of Aspect Models.
    * Questionable language building blocks, e.g., structure of mapping model is virtually useless.
      for all but the most simple contrived applications.
    * Tightly coupled to the Eclipse editor. With the DSL being based on a non-standard syntax,
      useful tool support is essential. Although XText can in principle also be used with
      [IntelliJ](https://blogs.itemis.com/en/get-started-with-xtext-and-intellij-idea-in-5-minutes),
      there seems to be no support or plans for that in the Vorto project.
    * Current state of Vorto's implementation and documentation suggests Beta quality.

## Decision Outcome

Chosen option: "RDF with Turtle Syntax".

* It best fulfills the goals described above.
* We are able to build the models according to our requirements.
* Structure and features of models can be iteratively adjusted.
* Full control of stack and tooling.

