# Contribution Guideline BAMM Aspect Meta Model
Thank you for your interest in contributing to the BAMM Aspect Meta Model. Use this repository to contribute to this specification as easy and transparent as possible, whether it is:

* Reporting a bug
* Discussing the current state of the Technical Specifications
* Submitting a fix
* Proposing new features
* other

With the remainder of this document, we want to support interested contributors in bringing in your proposal. 

## OMP SDS and Roles
The BAMM Aspect Meta Model is a result of the OMP SDS WG ("Open Manufacturing Platform - Semantic Data Structuring - Working Group"). More information about the OMP like its goals or members is available under [open-manufacturing.org](https://open-manufacturing.org). The overall goal of the SDS WG within the OMP is to work on a Semantic Data Structuring Layer that addresses the needs to share, join, and reuse heterogeneous data of the manufacturing. 
In that regard, the BAMM Aspect Meta Model acts as a foundation for the modeling of various aspects in a manufacturing environment. 

### Roles
The work on the BAMM Aspect Meta Model is organized within the OMP SDS WG to which this document simply refers as "`working group`" in the following. The `working group` is currently meeting regularly and may decide on the acceptance of Pull Requests (`PRs`) and `Issues`. Before a release of the specification, the `working group` further needs to agree on a state of the specification as a release candidate. 

Besides the `working group`, there is a group of people to which this document refers to as "`maintainers`". `Maintainers` manage this repository and therefore have write access and the right to assign labels to `Issues` and `PR`. 

The working group is led by a `Chair` who facilitates the `working group` meetings and organizes the work in the working group. The `Chair` is also a `maintainer` of this repository. 

# Contributing Source Code and Technical Specifications for this Project (using GitHub)
* We use this GitHub repository for the Technical Specifications related to this project, to track issues and feature requests, as well as discuss and manage all pull requests related to this project.
* Opening `Issues` and `PRs` in GitHub is the preferred way to interact with the community around this repository and the BAMM Aspect Meta Model.

## Branching
We follow the [Git branching guidance](https://docs.microsoft.com/en-us/azure/devops/repos/git/git-branching-guidance?view=azure-devops).

More specifically the repository has the following branches:

name of branch | description
----| ----
`main` | Contains the latest state of the repository
`v{version_number}-RC` | A state on which the working group agreed on as a release candidate but which is missing the approval by the OMP. 
`v{version_number}` | A release of the respective version which is approved by the working group and the OMP. 
`feature/{feature-name}` | Contains the development on a specific feature and is intended to be merged back into the `main` branch as soon as possible. Note, that it is recommended for contributors to create and develop feature branches in a personal fork and not the upstream repository.
`bug/{bug-name}` | Contains the development of (usually smaller) changes in files of the repository that do not introduce new functionality but fix mistakes, errors or inconsistencies. These branches should be merged back into the `main`branch as soon as possible.

## Issues
We use the `Issues` feature of GitHub for tracking all types of work in the repository.

We distinguish between the following types of issues;

Issue Types        |   Description
-------------------| ------------------------------------------------------
`Bug Report`         | This `Issue` is dedicated to reporting a problem.
 `Task` | This `Issue` is used for describing and proposing a new work item (e.g., a new feature)

 If there are issues that link to the same topic, the creator of the issue shall mention those other Tasks in the description. To group tasks that can belong together, one could further create an issue mentioning and describing the overall user story for the referenced tasks.
 
## Pull Requests
Proposals for changes to the content of the repository are managed through Pull Requests (`PRs`). 

### Opening Pull Requests

To open such a `PR`, implement the changes in a new `feature branch`. Each `PR` must reference an issue and follows the naming schema: `<issue-number>-<feature-name>`. 
For a new `PR` the target branch is the `main` branch while the source branch is your `feature branch`
The `feature branch` branch should be developed in a fork of the upstream repository. So before working on your first feature, you need to create such a fork (e.g., by pressing the `Fork` button in the top right corner of the GitHub page)

When opening a `PR` please consider the following topics:

* optional: Rebase your development on the branch to which you plan to create the `PR`.
* Each `PR` must be linked to an `Issue`:
    - Reference the `Issue` number in the name of your `feature branch` and the description of the `PR`.
    - Mention the `Issue` in one of the commit messages associated to the `PR` together with a GitHub keyword like `closes #IssueNumber` or `fixes #IssuesNumber`. For more details visit the [GitHub documentation on linking PR with Issues](https://docs.github.com/en/github/managing-your-work-on-github/linking-a-pull-request-to-an-issue#linking-a-pull-request-to-an-issue-using-a-keyword)
* Each `PR` should only contain changes related to a single work item. If the changes cover more than one work item or feature, then create one `PR` per work item. You may need to create new more specific `Issues` to reference if you split up the work into multiple `feature branches`.
* Commit changes often. A `PR` may contain one or more commits.

## Paperwork and DCO
The OMP is a [JDF project (Joint Developement Foundation)](https://www.jointdevelopment.org/) following the project and working group charters as defined in JDF charter template 4.0.1
 
For source code contribution the project charter requests for non-working group participants the following:
 
```
Non-Working Group Participant Feedback and Participation. Upon the Approval of the Working Group Participants, the Working Group can request feedback from and/or allow Non-Working Group Participant participation in a Working Group, subject to each Non-Working Group Participant executing the Feedback Agreement set forth in Appendix B.
```

``Appendix B`` with the placeholders set to: 
- [Project Name] = ”OMP” 
- [Projects’s Source Code License] = “MPL 2.0”
- [name of deliverable] = ”BAMM Aspect Meta Model”

states:

>### OMP Feedback Agreement
>**
>
>Feedback
> 
>The OMP (“Project”) is developing the BAMM Aspect Meta Model (the “Materials”). Project would like to receive input, suggestions and other feedback (“Feedback”) on the Materials. By signing below, you (on behalf of yourself if you are an individual and your company if you are providing Feedback on behalf of the company) grant the Project under all applicable intellectual property rights owned or controlled by you or your company a non-exclusive, non-transferable, worldwide, perpetual, irrevocable, royalty-free license to use, disclose, copy, publish, license, modify, sublicense or otherwise distribute and exploit Feedback you provide for the purpose of developing and promoting the Materials and in connection with any product that implements and complies with the Materials. You warrant to the best of your knowledge that you have rights to provide this Feedback, and if you are providing Feedback on behalf of a company, you warrant that you have the rights to provide Feedback on behalf of your company. You also acknowledge that the Project is not required to incorporate your Feedback into any version of the Materials. You further agree that you and your company will not disclose it or distribute drafts of the Project Materials to third parties. Unless the parties agree otherwise, this obligation of non-disclosure will expire five (5) years from the date the material was disclosed to you.
>
>Source Code
> 
>Any source code you provide to the Project is subject to the Developer Certificate of Origin version 1.1, available at http://developercertificate.org/ and the MPL 2.0.

This means, before making a pull request or providing an issue please sign the OMP Feedback Agreement for the working group “Semantic Data Structuring”.

## Labeling
After new `Issues` or `PRs` are created, the Chair or one of the maintainers may further assign a `label` to it according to this table:

Label Types        | Description
-------------------| ----------------------------------------
`to be discussed`        | Involvement and Discussion in one of the `working group` meetings are needed.
`request for information`| The `working group` or the `maintainers` are requesting further information from the creator of the `Issue` or `PR`. If for a pre-defined time no information is received, then the `Issue` or `PR` can be closed.
`approved` | Has been discussed and approved in the `working group`. 
`not accepted` | Has been discussed in the `working group` with the decision to close this issue.



### PR or Issue Discussion and Decision

* You can provide comments to any `PR` or `Issue` via the comment function in GitHub
* If no further involvement from the working group is required, a `maintainer` may merge a `PR`. This mostly applies to bug fixes when no substantial changes are performed. A `PR` for a bug fix may reference an issue of type `Bug Report`.
* The `maintainers` may assign the label `to be discussed` to a proposal when further involvement from the `working group` is required. This then triggers the following steps:
1. The `Chair` of the `working group` puts the proposal up for discussion in one of the next `working group` meetings. 
2. The `working group` then uses Consensus Decision-Making with one of the following outcomes:

| Decision | Next Steps |
| ------ | ------- |
|`Approved`| The `Issue` or the `PR` gets the label `approved`. In the case of a `PR`, the `maintainers` merge the respective `PR`. |
| `Discussion`| The `Issue` or the `PR` get the label `request for information`. 
|`Close` | The `Issue` or the `PR` are closed and get the label `not accepted`. |

3. The label `to be discussed` is removed.

* If the `working group` or the `maintainers` feel that further information is required to explain a `PR` or an `Issue`, they may request this information through the comment section of the `PR` or `Issue` and assign the label `request for information`. The `maintainers` may close the issue if no answer is received after a pre-defined time. 

Note, that merging a `PR` leads to the closing of the `Issue` if it is linked in the `PR`. 

### Review Checklist
The following checklist can be seen as a basis for performing reviews on new `PRs`:

- [ ] brief and useful commit messages
- [ ] document style is followed
- [ ] the contribution matches to the linked issue and the description of the PR
- [ ] provide clear documentation of new features (if applicable) 
- [ ] outline added third party dependencies

### Commit Messages 
Separate the subject from the body with a blank line because the subject line is shown in the Git history and should summarize the commit body. Use the body to explain what and why with less focus on the details of the how. This [blog post](https://chris.beams.io/posts/git-commit/#seven-rules) has more tips and details.
Before you push your commits to a repository, you can squash your commits into one or more logical units of work, e.g., if you want to add a new feature solely in a single commit.

## License Headers & Licensing
All files contributed require headers - this will ensure the license and copyright clearing at the end. Also, all contributions must have the same license as the source.
The header should follow the following template:

```
////
Copyright (c) {YEAR} {NAME OF COMPANY X} 
Copyright (c) {YEAR} {NAME OF COMPANY Y} 

See the AUTHORS file(s) distributed with this work for additional information regarding authorship. 

This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
SPDX-License-Identifier: MPL-2.0
////
```
When using the template, one must replace "{NAME OF COMPANY X}" with the name of the involved companies and "{YEAR}" with the year of the contribution. For each involved company you need a new line starting with "Copyright" as outlined in the example. 

This example uses the Commenting Option of Ascii-Doc, so if your file is of another type you may have to adapt the comment syntax accordingly. 

If you use third-party content (e.g., import / include ...), you are required to list each third-party content explicitly with its version number in the documentation and your pull-request comment. Please note that software licensed under GPL, AGPL or, a similar strong copy-left license cannot be approved.

# Release Process
The working group may decide that it reached a stable state for the contents of the repository. To settle an agreement on this and provide downstream users with a stable version of the specification, a release process can be triggered. 

For such a release the `working group` must approve the current state of the `main` branch as agreement. A `maintainer` of the repository then forks the `main` branch into a new branch that follows the naming convention `v{version_number}-RC`. The organization team of the OMP is then asked to `review & approve` the `v{version_number}-RC` branch. If the organization agrees on the approval the OMP steering committee needs to be notified. After that notification, a `maintainer` triggers the release feature from GitHub based on the commit on which the `v{version_number}-RC` branch is based.

## Versioning
We use Semantic Versioning to identify versions of published BAMM Aspect Meta Models. Semantic Versioning is documented [here] (https://semver.org). It proposes to have a versioning number with the following elements:

````
Given a version number MAJOR.MINOR.PATCH, increment the:
- MAJOR version when you make incompatible API changes,
- MINOR version when you add functionality in a backwards compatible manner, and
- PATCH version when you make backwards compatible bug fixes.
Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.
````
 
Whereas the Major version must be incremented if the API has backward-incompatible changes (e.g., has breaking changes), the Minor version must be changed if new backward-compatible features are introduced and, the Patch version must be incremented if backward-compatible bugfixes are introduced.
### Breaking Changes
For the definition of a breaking change, we follow the definition as in the [Microsoft REST API Guidelines](https://github.com/Microsoft/api-guidelines/blob/vNext/Guidelines.md#123-definition-of-a-breaking-change) which are licensed under [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0). This definition states:
````
Changes to the contract of an API are considered a breaking change. Changes that impact the backwards compatibility of an API are a breaking change.
`````

In the context of the BAMM Aspect Meta Model, one may consider the dependencies between the different elements of the BAMM Aspect Meta Model in a wider sense as some kind of API. So to evaluate whether a change is breaking or not, one needs to consider the implications and required changes in the Aspect Models that are based on the BAMM Aspect Meta Model. 

The following table gives examples for breaking and non-breaking changes: 

Examples for non-breaking changes are:
* Adding a new Property
* Making a mandatory Property optional
* Adding a new Characteristic

Examples for breaking changes are:
* Renaming an existing Property
* Making an optional Property mandatory
* Removing an existing Characteristic
* Removing an attribute of an existing class
* Change of the relationship between two classes (inheritance, aggregation, composition, etc.)

### Version Syntax for Specific Environments

Git version tag

vX.Y.Z-[pre-release-identifier]

Examples:

v1.0.0-RC1, v1.0.0

# Resources

## GitHub Resources
* [For a Repo](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request-from-a-fork)
* [Issue Creation](https://help.github.com/en/github/managing-your-work-on-github/creating-an-issue)
* [PR Creation](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/creating-a-pull-request)


