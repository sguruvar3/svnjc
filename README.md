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
