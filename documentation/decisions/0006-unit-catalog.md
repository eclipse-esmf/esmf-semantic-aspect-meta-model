# Unit Catalog: Use UNECE Recommendation 20

## Context and Problem Statement

In order to be able to specify the physical unit a certain Property refers to, the Aspect Meta Model
should provide a catalog of commonly used physical units. The unit catalog should be based on some
well established standard or de-facto-standard to ensure adequate completeness; there is no need to
invest time into building one from scratch. The catalog should be suitable for the type of
applications that might be built using Aspect Models. It might be necessary to transform the format
a given catalog is provided in, though.

## Decision Drivers

* Unit catalog should be based on established standard.
* Units must be referrable by a unique identifier (i.e., the symbol is not sufficient; e.g., `g`
  might mean gram or gravitational acceleration).

## Considered Options

* [UNECE Recommendation 20](https://tfig.unece.org/contents/recommendation-20.htm): The
  [UNECE](https://en.wikipedia.org/wiki/United_Nations_Economic_Commission_for_Europe) (United
  Nations Economic Commission for Europe) has specified a catalog of around 1,800 units which is in
  wide use since it is for example used in [UN/CEFACT](https://unece.org/trade/uncefact). Notably,
  for the usage context of Aspect Models, the [OPC Unified
  Architecture](https://en.wikipedia.org/wiki/OPC_Unified_Architecture) (OPC-UA) _Part 8: Data
  Acess_ (also known as IEC 62541-8) [builds on UNECE Recommendation
  20](https://reference.opcfoundation.org/Core/Part8/v104/docs/5.6.3) for measurement units.
* [QUDT](https://qudt.org/) (Quantities, Units, Dimensions, Types): QUDT is an ontology and catalog
  for the description of units. The unit catalog in QUDT 1.1 consists of roughly 800 units.
* [UO](https://www.ebi.ac.uk/ols/ontologies/uo) the _Units of measurements ontology_: Comparable to
  QUDT, UO provides an ontology and a catalog of units. It consists of roughly 270 units.

## Decision Outcome

Chosen option: "UNECE Recommendation 20", because it has a certain relevance, in particular, by
being used by OPC-UA.  With 1,800 units, it also already has a considerable extent.

### Consequences

* Good, because the unit catalog is established and due to its size, manual defintions of units
  should not be necessary very often.
* Bad, because the catalog is only provided as an Excel file, which, after preliminary inspection,
  contains numerous inconsistencies, typos etc., so additional work on cleaning up must be done.

