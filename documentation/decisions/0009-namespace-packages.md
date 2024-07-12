# Namespace packages

## Context and Problem Statement

Occasionally sets of Aspect Model need to be processed as a units. Uses cases include sending and
receiving Aspects Models using tools such as email or chats or APIs that allow download or upload of
Aspect Models. In order to enable tools to process such sets of Aspect Models in a consistent way,
this document describes a format called _Aspect Model namespace package_, or _namespace package_ for
short. The name is chosen to indicate that such a package contains a set of namespace files (see
[ADR-0007](0007-model-resolution.md) for an explanation of this term).

The goals for the format are:

* Enable simple and straightforward exchange of sets of aspect model files.
* Enable reasonable interoperability with existing tools and conventions.

Non-goals of the format are:

* Establish a generic container format that can contain arbitrary content.
* Establish a self-contained publishing format that contains meta data beyond regular aspect model
  file content (e.g., provenance meta data).
* Include mechanisms for authenticity such as digital signatures or certificates.
* Include mechanisms for content verification such as checksums.

The above points can and should be achieved using different means - depending on the used
distribution channels - and need no reinvention.

## Namespace Package Specification

A namespace package has the following properties:

* It is a file in ZIP format.
* Its standard file extension is `.zip`. This means:
  * When creating a new namespace package file, the file extension should be `.zip`.
  * Reading namespace packages files with other file extensions is allowed. Use cases for this are
    discussed in section [Interoperability](#interoperability).
* A _models root_ is determined for the content of the ZIP file:
  * If a directory `aspect-models` exists in `/` of the ZIP file, it is the models root.
  * Otherwise, the directories in `/` of the ZIP are traversed non-recursively in lexicographically
    sorted order (by en-US locale) and the first subdirectory of any of them that is called
    `aspect-models` is the models root.
  * Otherwise, if no such directory exists, `/` of the ZIP file is the models root.
* The namespace package ZIP file contains aspect model files with `.ttl` file extension that are
  organized in the directory structure _models root_/_namespace main part_/_namespace version
  part_/AspectModelFile.ttl, where _namespace main part_ and _namespace version part_ correspond to
  the namespace of the content of the `.ttl` file. There are no further restrictions, i.e., there can
  be arbitrary many namespace main part directories, arbitrary many namespace version directories
  and arbitrary many, arbitrarily named `.ttl` files inside.
* Any other directory or file in the ZIP file is ignored.

## Interoperability

By not strictly requiring a specific file extension and ignoring unknown contents of the namespace
package file, it is possible to create files that are at the same time valid namespace packages and
valid files according to some other ZIP-based file format.

_The following examples are non-normative._

### Example 1: Overlay Namespace Package with Java .jar file

Since the `.jar` format used to distribute Java artifacts is also based on the ZIP format, it is
possible to create a `.jar` file - which may or may not contain Java class files - that is at the
same time a valid namespace package. Uses for this technique include:
* Distributing namespace packages using existing Java architecture and services such as Maven
  Central or Artifactory.
* Allowing to declare a dependency of a Java project to an Aspect Model namespace package using
  standard Java/Maven tooling.
* Publishing Java-based software that comes with Aspect Models describing its domain; the executable
  `.jar` file can both be executed and opened as a namespace package.

A `.jar` file can contain resources organized in arbitrary directory structures, so a `.jar`-based
namespace package could use either the `.jar`'s root as models root, or use a separate
`aspect-models` directory.

### Example 2: Overlay Namespace Package with Asset Administration Shell .aasx file

The Asset Administration Shell
[.aasx](https://industrialdigitaltwin.org/wp-content/uploads/2023/04/IDTA-01005-3-0_SpecificationAssetAdministrationShell_Part5_AASXPackageFileFormat.pdf)
format is based on [Office Open XML](https://en.wikipedia.org/wiki/Office_Open_XML) (also
standardized as ECMA-376 and ISO/IEC 29500), which is also based on the ZIP format. Apart from
structure and files mandated by OOXML, the directory structure in `.aasx` files always includes an
`aasx` directory, and below that, custom structures of directories and files are allowed that can be
referenced from within `File` submodel elements, as long as corresponding relationships are
described in the OOXML's XML descriptors. This allows creation of an `.aasx` file that bundles
aspect model files which are referenced from within a contained AAS submodel or submodel template
and that is at the same time a valid Aspect Model namespace package. The contents of the `.aasx`
file could therefore look like this:

```
/
├─ _rels
│   └─ .rels
├─ aasx
│   ├─ _rels
│   │   └─ data.xml.rels
│   ├─ aasx-origin
│   ├─ data.xml
│   └─ aspect-models
│       ├─ com.example.ex1
│       │   └─ 1.0.0
│       │       ├─ MyAspect.ttl
│       │       └─ AnotherFile.ttl
│       └─ com.example.ex2
│           └─ 1.0.0
│               └─ MyOtherAspect.ttl
└─ [Content_Types].xml
```

The OOXML file `[Content_Types].xml` could register the MIME type for `.ttl` as follows:

```xml
<?xml version="1.0" encoding="utf-8"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="ttl" ContentType="text/turtle"/>
  <!-- ...other extensions... -->
  <Override PartName="/aasx/aasx-origin" ContentType="text/plain"/>
</Types>
```

The model elements (Asset Administration Shells or Submodels) defined in `data.xml` can then contain
`File` submodel elements that refer to the aspect model files:

```xml
<!-- ... -->
<file>
  <idShort>MyAspect</idShort>
  <semanticId>
    <type>ExternalReference</type>
    <keys>
      <key>
        <type>GlobalReference</type>
        <value>urn:samm:com.example.ex1:1.0.0#MyAspect</value>
      </key>
    </keys>
  </semanticId>
  <value>/aasx/aspect-models/com.example.ex1/1.0.0/MyAspect.ttl</value>
  <contentType>text/turtle</contentType>
</file>
<!-- ... -->
```

With this setup, the `.aasx` file itself is also a valid namespace package.
