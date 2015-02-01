ExpenseMaster
=============
## Description
ExpenseMaster is a simple expense claim tracker and manager for Android.
It is designed to be simple, attractive, and easy-to-use, supporting the following features:
* add/edit/delete individual expense items
* add/edit/delete individual claims
* email a selected claim and its constituent expense items
* denote a claim as submitted (no further edits allowed to it)
* denote a submitted claim as returned (further edits allowed to it)
* denote a submitted claim as approved (no further edits allowed to it)
* list all the claims, indicating status (i.e., in progress, submitted, returned, approved), ordered by start date
* show total currency amounts for a claim when it is listed
* list the expense items for a claim
* show total currency amounts for the expenses of a claim

This project was made for CMPUT301 at the University of Alberta for the W15 term.

## Requirements
The following libraries and components are needed to run the application. If a static reference to
a library or component is included in the project, they are indicated by `(Included *location*)`
marked at the end of the line, with `location` indicating the directory that it is contained within
relative to the root.
* Google-Gson `(Included /libs/)`

## Installation
**An unsigned, compiled .apk file is available in the /bin/ folder as ExpenseMaster.apk.**

This project was built in Eclipse Luna, compliant with JDK 1.8, and targets the Android SDK Level 17 (Android 4.2.2). See **Requirements** above for information about libraries and components that might not be included in the repository.

## License
Please see the file called LICENSE.

