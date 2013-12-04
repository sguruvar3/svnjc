svnjc - Subversion Java Client
=============

Java based Subversion client. i.e. Typical jar is acting as a subversion client with lot of flexibilities than comparing to normal subversion client

It works on the principle “Lets do SVN operations without SVN client installed” i.e, any SVN commands can be simulated using this tool without SVN client at all, and even in a better way. It works on top of the open source library, SVNKit.

Key Features
======
- Customizable to the user requirements and goes beyond the capability of SVN. For ex., SVN checkin supports only list of filenames/filenames in a flat file. SVN Java Client breaks this rule and even XML files (which contains the list of filenames) can be used to checkin even with some conditions also.

- Since Java based, extensible as UI/plug-in is possible

- Platform neutral and useful for automation since it deals with SVN from the scratch

- Can be re-used anywhere by just copying the jar with no code changes at all.

- Can be useful in places where the installation of SVN client is a pain point

- Integration with other APIs is possible which is a difficult job when using SVN client. For example, authentication module can be separated from SVN and can be dealt by 3rd party API like Cloakware etc.

- All sort of customizations are possible. i.e, “svn add” and “svn checkin” can be combined into a single operation called “add-and-checkin” which is not possible using normal SVN client and the output can be e-mailed to a Reviewer by default. 

API Guidelines
=======
This page lists the various commands that are covered in this svnjc app. including the possible input and output parameters

Add
---
    purpose : to simulate "svn add" command using SVNKit Java Library
    External parameters : (1)WC_Dir (Current Working Directory)
    Outputs
      as retrun value : ---

Checkout
---
    purpose : to simulate "svn co" command using SVNKit Java Library
    External parameters : (1)WC_Dir and (2) SVN_URL
    Outputs
      as retrun value : revision no.  
      returned files : the files are checked out into the WC_Dir

Cleanup
---
    purpose : to simulate "svn cleanup" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : none

Commit
---
    purpose : to simulate "svn ci" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : SVNCommitInfo
      meessage : Same message that we see while checking in using svn client

Copy
---
    purpose : to simulate "svn copy" command using SVNKit Java Library
    External parameters : (1)URL1 (2)Branch_URL and (3)Message to commit
    Outputs
      as retrun value : SVNCommitInfo
      message : Same message that we see while checking in using svn client

Delete
---
    purpose : to simulate "svn del" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : ---

Diff
----
    purpose : to simulate "svn diff" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : ---
      message : Same message that we see while finding diff using svn client

Info
----
    purpose : to simulate "svn info" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs:
      as retrun value : ---
      message : Same message that we see while finding info using svn client

Log
----
    purpose : to simulate "svn log" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs : as retrun value : SVNLogClient
      message : Same message that we see while finding log using svn client

Merge
----
    purpose : to simulate "svn merge" command using SVNKit Java Library
    External parameters : (1)url1 (2)url2 and (3)target_path
    Outputs
      as retrun value : none

Resolved
---
    purpose : to simulate "svn resolved" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : ---

Revert
---
    purpose : to simulate "svn revert" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : ---

Status
---
    purpose : to simulate "svn status" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : ---
      message : Same message that we see while finding status using svn client

Update
---
    purpose : to simulate "svn update" command using SVNKit Java Library
    External parameters : (1)WC_Dir
    Outputs
      as retrun value : updated revision info
      returned files : list of fies updated & the old and latest revision no. 
