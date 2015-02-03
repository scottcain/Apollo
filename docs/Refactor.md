RefactorClient goals:

- Modular code
- Testable code
- Avoid ad-hoc fixes, do the right thing
- Modern javascript approaches
- Configurable and extensible

RefactorClient changes:

- Remove DraggableHTMLFeatures inheritence  from SequenceTrack
- Make SequenceTrack inherit from jbrowse Sequence track
- Add CodonTable.js change in JBrowse to allow multiple CodonTracks
- Removed all the webapollo Sequencetrack code to make room for inheriting jbrowse code
- Login using AJAX. Many "ajax" requests we have are using synchronous XHR instead.
- Remove maxPxPerBp limit to allow zooming in farther into the sequence
- Remove “BrowserZoomFix” (unnecessary after Sequencetrack refactor).
- Make the JSON requests use actual JSON instead of string building
- Use the quickHelp/aboutThisBrowser config options
- The title now says webapollo instead of jbrowse (was implemented but fixed now)
- Make Tool menu appear before help (was previously implemented but fixed now)
- Fixed resizing of features at any scale (even highly zoomed in)
- Completely removed the "DraggableResultFeatures" because it was unused. It offered the option to "Promote all features" to the annotation track. If this is desired, we need to revisit it, but deleted for now.
- Refactored InformationEditor into a new module WebApollo/View/InformationEditor that is a mixin for AnnotTrack (1400 lines removed from Annottrack.js)
- Convert all synchronous XHR in information editor to use AJAX

Casualties of the refactoring process so far:

- The sequence displaying inside the feature when zoomed in (should this be reimplemented?)
- Color by CDS doesn’t highlight frame (temporarily)
- The highlight doing both top and bottom (temporarily)
- Creating new insertions and deletions (temporarily)



Future:

- Simple highlighting bases when we have insertion or deletion
- Remove “login” code from annottrack, place in main plugin or different module
- Remove information editor code from annottrack, place in new module


Screenshot:
![Refactor](http://i.imgur.com/2QnCnJP.png)