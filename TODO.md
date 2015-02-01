Requirements
============
Expenses
- [ ] add individual expense items
  * Adding and removing will be similar to claims.
- [ ] edit individual expense items
  * Saving the changes requires calling updateClaim
- [ ] delete individual expense items

Claims
- [X] add individual claims
- [X] edit individual claims
- [X] delete individual claims

Emails
- [ ] email a selected claim and its constituent expense items
  * Might need to factor out the methods for Json to more general things
    * Interface ClaimExportable
    * Class HTMLExporter implements ClaimExportable
    * Class JsonExporter implements ClaimExportable

Status
- [X] denote a claim as submitted (no further edits allowed to it)
- [X] denote a submitted claim as returned (further edits allowed to it)
- [X] denote a submitted claim as approved (no further edits allowed to it)

Claims Listing
- [X]   indicating name
- [X]   indicating status
- [ ]   ordered by start date
  * List just needs to be sorted before update.
- [X]   show total currency amounts for a claim when it is listed

Expense Listing
- [X] list the expense items for a claim
- [X] show total currency amounts for the expenses of a claim

Currency
- [X] Multiple currencies supported

Persistence
- [X] Data is persistent between states
- [X] Data is not lost when exiting the program
  * Data is saved every time the Save button is pressed
  * How to handle saving of Expenses?
    * If we save every time Expenses>Save is pressed, then the user can't cancel from the Edit Claims view.
    * If we don't handle it this way, data may be lost if the app is closed while editing a claim, even if the expense was saved.
      * This is practical if every usage is a full session, but it's unrealistic.

Consistency
- [ ] App assists consistent data entry
  * Add android:hint to fields
  * Date parsing through SimpleDateFormat is weak at best.
    * Allows dates such as 123/45/67 to be entered validly.
    * Might need a stronger format than yyyy/mm/dd.
      * Date may need a stronger format for de/serialization

Completeness
============
Code base
- [ ] Code runs, covers requirements, is stable and calculates stats
- [ ] Design fits OO paradigm
- [ ] Code is clean and readable
- [ ] Code has good encapsulation and separation of concerns

Documentation
- [ ] Each class indicates its purpose, rationale, and issues
- [ ] Includes UML class diagrams

UML
- [ ] Focuses on important classes
- [ ] Effectively covers the problem domain

Demonstration
- [ ] Two minute maximum video demonstrating required functionality

Submission
==========
- [ ] git URL is properly formatted
- [ ] Repository contains the compiled APK binary in the /bin/ directory
- [ ] Directory and file structure is correctly organized
- [ ] Documentation files appear in the /doc/ directory

Important
=========
- [ ] Only 1 APK appears in the /bin/ directory
- [X] Application displays as ccid-notes
- [ ] Contains a README file
  * Should describe intent of program
  * Should mention external libraries used
- [ ] UML is in the doc directory
- [X] SQLite is not used