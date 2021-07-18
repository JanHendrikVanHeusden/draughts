# Draughts
**Draughts board game (aka checkers)**

The code implements the "*International draughts*" variant (Dutch: "*dammen*"), so with a 10 by 10 checkered board.
> All values in the code, like board sizes, number of pieces, positions etc. are set by constants, so these could be
> changed easily.
> However, no tests are provided or promises given on the proper functioning of the code when these values are changed.

## How to build & run it!
#### Prerequisites
* This is a Kotlin Maven project, so you have to have Java and Maven correctly installed and working,
  including environment variables like `JAVA_HOME` and `MAVEN_HOME`.
  And you should be familiar with building Java (or Kotlin) projects.
* The project is hosted in GitHub, so you probably will need `git` as well.

#### Build it
To build the project, check the repository out. Then in the root directory, run `mvn clean install` to build it.
In the target directory, you will find 2 .jar-files:
 * `draughts-1.0-SNAPSHOT.jar`
 * `draughts-1.0-SNAPSHOT-jar-with-dependencies.jar`

You need the latter one (the `...-jar-with-dependencies.jar`).

#### Run it
You can then run it from the command line like this:
> ``java -cp "< path to jar file > \draughts\target\draughts-1.0-SNAPSHOT-jar-with-dependencies.jar" nl.jhvh.draughts.player.Player``

Make your command window larger when output is scrambled.

Alternatively, you can run the application in your IDE (preferably IntelliJ). I leave this to your own preference.
The class to run is `nl.jhvh.draughts.player.Player`

## Playing draughts
### Notation
According to draughts conventions for the common "*International draughts*" variant,
the playable squares on a regular Draughts board are numbered 1 to 50 like shown in the diagram below.
> Note that only the dark-colored squares can have a piece on it, and only those are numbered.
> The light-colored squares are not assigned numbers.

![Notation of draughts squares](Draughts numbering.jpg)

This draughts game uses these position numbers to specify moves.

The game is self explanatory; you get helpful feedback on how to play, and also when doing things like impossible
or not-allowed moves.

### Features
#### What's in it
* Moving "normal" (uncrowned) pieces, according to draughts rules
* Toggle turns (white - black - white - black etc.)
* Jumps (captures), including multi-jump and backward jump
   * Including prevention of endless loops in circular jumps
* Enforcement of the rule that you must play the piece with the highest capture count
   * Including display of which moves *are* allowed, in case of an illegal move
* Simple representation (by letter-like symbols) of the board with pieces
* A service providing the required external facade
* A `Player` main class which can be run from the command line
* Unit tests for some of the classes (code coverage ~58%)
   * mainly for the basics of piece movement, capturing etc.,
     as I wanted to properly verify these before building more upon it

#### What's NOT in it
* The game never ends, and no winner (or draw) will be declared
   * There is no detection implemented (yet) of a player reaching the point where no more moves are possible,
     nor  detection that a player has no more pieces.
* Crowning of pieces reaching the opposite side of the board is not implemented; neither an algorithm to determine
  the possible moves of a crowned piece.
  
#### Suggestions / wishes
* Missing features (win / draw; crowned pieces)
* TODO's / FIXME's
   * Especially the very inefficient way of retrieving piece information for each square
* Let players specify names, so it can say *It's John's turn* instead of *black is in turn*
* Undo / redo

Well, and maybe nice but time-consuming stuff
* Saving / re-loading an ongoing game
* Web interface with REST communication
* Nice frontend
* Build it directly in web (maybe php?)

### Language: English
Both the code names (variables, classes etc.) and all informative feedback from the game are in English.
These feedback strings (prompts, messages, errors etc.) are hard coded (String literals) directly within the code.
The time available for this code challenge was short, so I decided to hard code these to save time.

### TODO's / FIXME's
Several `TODO`'s and `FIXME`s are left in the code, these should be handled when a production-strength application
were required, or simply about (hopefully not too dirty) workarounds.
