# Sword and Shield
Sword and Shield is a two player game that is part of my SWEN222 Assignment 1. The game is implemented in a pure text interface to be displayed in the text console.

## Getting started
In order to compile and run the game, please navigate to the src folder
```
cd src/
```
To compile, enter the following command
```
javac Main.java model/*.java view/*.java
```
To run the game, 
```
java -cp . Main
```

### Rules
A piece is denoted with a <letter> and have either a sword or a shield or nothing, at all sides:
- or |        represents SWORD
\#             represents SHIELD
<empty space> represents NOTHING

The goal of the game is attack your opponent player's FACE (0 or 1) with SWORD.
