# BFS Solver for 4 different puzzles

## Description
A puzzle solver that utilizes the breadth first search algorithm
to find solutions on 4 different puzzles

## How to play
You have 4 puzzles to choose from: clock, water, lunarlanding, and tipover.
Clock and water puzzles are unplayable puzzles. They are mainly to test the 
BFS solver. 

Lunar landing and tip over are playable puzzles, and you can play it in the 
terminal of the IDE or on the GUI. 

### Lunar Landing

### Tip over


## How it works

### BFS
For each puzzle the bfs solver chooses each possible move from the playable pieces
on the board. Once each move has been chosen, new choices are available and 
the solver will continue checking each option until the solution has been found.

In Lunar Landing the astronaut and the robots on the board
are all playable, you can move the pieces in 4 different directions. The BFS
solver will take 1 of the playable pieces and choose 1 of the 4 directions as long
as its legal. This will continue until the astronaut lands in the spaceship marking
the puzzle as solved.

### MVC 
The puzzles utilize the model view controller architecture. The rules and
guidelines behind the puzzles are upheld in the model, the user can view and interact
with the puzzle in the GUI, and the middle man that connects the view to the model
is the PTUI.

### GUI & PTUI
The GUI is the main component where the user can view and interact with the puzzle. 
The user can select different pieces from the board and choose a direction. They also
have several buttons to reload, reset, or get a hint for the puzzle. The board will update
and the GUI will reflect those changes to the user.

The PTUI is the controller behind the solver, and it acts as a middle man by receiving 
user choices from the GUI and send it to the model to update the board. Any changes from 
the model will be sent back to the PTUI and displayed to the viewer through the GUI. 

## Challenges


## Contributers 

Darian Cheung - BFS solver, tip over