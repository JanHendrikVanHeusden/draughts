# Draughts
Draughts board game (aka checkers).

The code implements the "*International draughts*" variant (Dutch: "*dammen*"), so with a 10 by 10 checkered board.
> All values in the code, like board sizes, number of pieces, positions etc. are set by constants, so these could be
> changed easily.
> However, no tests are provided or promises given on the proper functioning of the code when these values are changed.

### Notation
According to draughts conventions for the common "*International draughts*" variant,
the playable squares on a regular Draughts board are numbered 1 to 50 like shown in the diagram below.
> Note that only the dark-colored squares can have a piece on it, and only those are numbered.
> The light-colored squares are not assigned numbers.

![Notation of draughts squares](Draughts numbering.jpg)

### Language
Both the code names (variables, classes etc.) and the informative feedback from the game are in English.
These feedback strings (prompts, messages, errors etc.) are hard coded (String literals).
The time available for this code challenge was short, so I decided to hard code these to save time. 
