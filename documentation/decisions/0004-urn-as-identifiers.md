# Usage of URNs as identifiers for model elements

## Context and Problem Statement

In [ADR-0001](0001-formalism-for-aspect-models.md), an RDF/Turtle-based model format was chosen for
the definition of Aspect Models. In RDF, named elements are identified by URIs. In the context of
semantic applications in the original sense of the semantic web, usually URLs are used to identify
elements in RDF documents (URLs are a specific type of URI) since they represent resources on the
web. However, Aspect Models could be used in contexts where it might be undesirable or even
impossible to directly resolve resources from the web. RDF-based or OWL-based applications usually
use an approach of a mapping between "logical URL" (which is the identifier of the element) and
"physical URL" (which is where the document containing the element is located). This often causes
confusion with developers because seemingly resolveable URLs can't be used as such. Additionally,
several software tools actually try to resolve URLs from RDF documents when they encounter them even
though they shouldn't (for example, the Protégé editor will only ask you to manually resolve a
document after it already went into a timeout on resolution). Therefore, this decision record is
intended to make the split between "URI as an identifier" and "URI/URL as a resolution mechanism"
explicit. It proposes usage of exlusively using URNs for identifiers (URNs are also a specific type
of URI, albeit they have no well-defined resolution mechanims built-in).

Using URNs thus has two effects: Firstly, it communicates to modelers that an element is identified,
but not how it is resolved, and it thus forces the application to have a clean model resolution
implementation in place, which can retrieve models and model elements from numerous sources, such as
local file systems, package repositories, model stores or the web. Secondly, by definition, all URNs
in the meta model itself and as used in the models can follow a well-defined structure that includes
the version (either of the meta model or the model namespace, respectively). This allows frequent
new releases - like actively maintained software - without dilluting semantics when multiple models
with different versions reside in the same graph.

Note that decision is closely related to [ADR-0005](0005-rdf-vocabulary.md) (usage of a closed
vocabulary).

## Considered Options

* Use URNs as identifiers for meta model and model elements.
* Use URNs as identifiers for meta model, use arbitrary URIs for model elements.

## Decision Outcome

Chosen option: "Use URNs as identifiers for meta model and model elements", because the arguments
for the resolution of meta model elements are equally valid for model elements.

### Consequences

* Good, because developers working with the Aspect Meta Model or Aspect Models can rely on the fact
  that the identifiers are only that. This enables models to be hosted in different places, model
  repositories can be moved etc. without breaking the model's identities.
* Bad, because special care must be taken in the implementations if Models refer to externally
  defined vocabularies that use URLs, e.g., RDFS.
