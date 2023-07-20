# Aspect Model File Extension: Use .ttl

## Context and Problem Statement

In [ADR-0001](0001-formalism-for-aspect-models.md), an RDF/Turtle-based model format was chosen for
the definition of Aspect Models. How should Aspect Model files be named, which file extension should
they use?

## Decision Drivers

* Well-known file extension `.ttl` for RDF/Turtle already exists
* Tools (editors, IDEs, ...) will base their support, such as syntax highlighting, on the file
  extension
* Recognizability

## Considered Options

* Use `.ttl` as file extension
* Use a custom file extension, e.g., `.am`

## Decision Outcome

Chosen option: "Use `.ttl` as file extension", because it will improve out-of-the-box support of
many tools with the files and allows editors and IDEs to provide at least RDF-level-support such as
syntax highlighting.

### Consequences

* Good, because we get basic tool support for free.
* Bad, because even though every Aspect Model is RDF/Turtle, not every RDF/Turtle file is an Aspect
  Model; non-experts might mix up what the `.ttl` extension exactly implies.

