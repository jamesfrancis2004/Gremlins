# Gremlins

Gremlins is a game like pacman, where the main character a wizard must navigate to the end of the map symbolised by a door. As the wizard is traversing the map it must aim to avoid the gremlins. Gremlins shoot slime balls that can kill the wizard and if the wizard comes into contact with the gremlins he also loses a life. The number of lives the wizard has is specified in the `config.json` file, where wizard fireball cooldown and gremlin slime cooldownis also specified.
The controls for the game are the following:

- `Up arrow` moves the wizard up one tile on the board.
- `Down arrow` moves the wizard down one tile on the board.
- `Left arrow` moves the wizard left one tile on the board.
- `Right arrow` moves the wizard right one tile on the board.
- `Space bar` shoots a wizard fireball.
- `Q` shoots a special fireball that teleports the wizard to the point where the fireball disintegrates.

To run this games at least `gradle 7.5.1` is required and the game also requires `java 8` or above. To run the game use type `gradle run` into the command line in the directory with the `build.gradle` file located in it.
