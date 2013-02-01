Simple Workflow
=====================
Notes
=====================

First of all, clone the project:

    git clone <need repo location>


Building from Gradle
=====================
This project is built with Maven and Gradle.  Gradle is the preference, however maven is maintain as we will need to tie into the other GBMP projects.
At some point, this will all be built with gradle.  __Gradle is self-installing.__

    ./gradlew jar
    or
    >gradlew.bat ajr

This will install gradle, download all dependencies and build a war file.


Setting up the project in Intellij IDEA or Eclipse
---------------------------------------
To setup for IDEA:
    ./gradlew idea
To setup for Eclipse:
    ./gradlew eclipse
__This has been tested with IDEA__

It may be necessary to:
* Setup Artifact Building (WAR)
* Setup Run Configurations


Configuration
---------------------------------------

### NOTE: [markdown reference](http://support.mashery.com/docs/customizing_your_portal/Markdown_Cheat_Sheet) or [Markdown](http://daringfireball.net/projects/markdown)