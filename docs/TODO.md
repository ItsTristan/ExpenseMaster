Requirements
============
Expenses
--------
- [X] add individual expense items
  * Adding and removing will be similar to claims.
- [X] edit individual expense items
  * Saving the changes requires calling updateClaim
- [X] delete individual expense items

Claims
------
- [X] add individual claims
- [X] edit individual claims
- [X] delete individual claims

Emails
------
- [X] email a selected claim and its constituent expense items
  * Might need to factor out the methods for Json to more general things
    * Interface ClaimExportable
    * Class HTMLExporter implements ClaimExportable
    * Class JsonExporter implements ClaimExportable

Status
------
- [X] denote a claim as submitted (no further edits allowed to it)
- [X] denote a submitted claim as returned (further edits allowed to it)
- [X] denote a submitted claim as approved (no further edits allowed to it)

Claims Listing
--------------
- [X]   indicating name
- [X]   indicating status
- [X]   ordered by start date
  * List just needs to be sorted before update.
- [X]   show total currency amounts for a claim when it is listed

Expense Listing
---------------
- [X] list the expense items for a claim
- [X] show total currency amounts for the expenses of a claim

Currency
--------
- [X] Multiple currencies supported

Persistence
-----------
- [X] Data is persistent between states
- [X] Data is not lost when exiting the program
  * Data is saved every time the Save button is pressed
  * How to handle saving of Expenses?
    * If we save every time Expenses>Save is pressed, then the user can't cancel from the Edit Claims view.
    * If we don't handle it this way, data may be lost if the app is closed while editing a claim, even if the expense was saved.
      * This is practical if every usage is a full session, but it's unrealistic.

Consistency
-----------
- [X] App assists consistent data entry
  * Add android:hint to fields
  * Date parsing through SimpleDateFormat is weak at best.
    * Allows dates such as 123/45/67 to be entered validly.
     * This can probably be ignored if we include hints.
    * Date may need a stronger format for de/serialization (fixed)

Completeness
============
*Checkmarks shown below indicate completeness up to the limits of my current patience.*
Code base
---------
- [X] Code runs, covers requirements, is stable and calculates stats
- [X] Design fits OO paradigm
- [X] Code is clean and readable (Subjective)
- [X] Code has good encapsulation and separation of concerns

Documentation
-------------
- [X] Each class indicates its purpose, rationale, and issues
- [X] Includes UML class diagrams

UML
---
- [X] Focuses on important classes
  * Classes and functions not shown are likely implicit
- [X] Effectively covers the problem domain

Demonstration
-------------
- [X] Two minute maximum video demonstrating required functionality
  * Need to find a place to submit it (currently in the /docs/ folder as a blob).

Submission
==========
- [ ] git URL is properly formatted
- [X] Repository contains the compiled APK binary in the /bin/ directory
  * /bin/ExpenseMaster.apk
- [ ] Directory and file structure is correctly organized
  * Not sure if the /gen/ folder should go in there
- [X] Documentation files appear in the /doc/ directory

Important
=========
- [X] Only 1 APK appears in the /bin/ directory
- [X] Application displays as ccid-notes
- [X] Contains a README file
  * Should describe intent of program
  * Should mention external libraries used
- [X] UML is in the doc directory
- [X] SQLite is not used
