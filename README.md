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

For the IDE, in the terminal enter h to see options and their functions

For the GUI, you can see the board, pieces, and arrow keys. In addition, you 
can see other buttons such as reset, reload, and hint.

### Lunar Landing
There is an astronaut in space, and you have to help them get back to
their spaceship. They can go in 4 directions: up, down, left, and right however 
they have to go all the way in that direction and only if there is a robot in that 
direction. If there is no robot the astronaut can get lost in space, making it an 
illegal move. The robots follow the same rules as the astronaut.

On the GUI you can choose which piece you want to move by clicking on either the 
astronaut or the robots, and to move that piece you can use the arrow keys on the 
interface. Only legal moves will update the board. Once solved the board UI will 
display "You Won". You can choose different puzzle layouts by clicking on reload,
you can reset the current puzzle with the reset button, or if you're stuck click
hint and the board will update the current board 1 piece 1 move at a time.

### Tip over
Help create a path for the tipper man to move from the start position to the red crate.
You can create a path by tipping crates along the grid. The man can travel along the 
fallen crates to get to the red create. The man cannot move crates where there is not a 
connected path of adjacent crates. Additionally, jumping diagonally, jumping over gaps,
and putting the tipper man on the board floor is not allowed.

On the GUI you can control the Tipper man by using the arrow buttons on the right side of 
the UI. The tipper man can go over paths where the numbered tiles are 1 or greater. 
You can tip crates that are greater than 1 by making the tipper man go in the direction
of the crate and the crate will fall in that direction. A fallen crate will create a path
that's equal to the size of the crate. The tipper man can go across those paths to lead them
to the end represented by a star. You can choose different layouts with reload, reset the 
current board with reset, and move the tipper 1 grid cell in the right direction.

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
Some challenges during the project was understanding how BFS works, how to apply it to the 
puzzle, and how the model-view-controller works. At first glance the concept of BFS is simple enough to 
understand, as starting at the root node the algorithm visits each child node from the root, and 
once done it will go back to the 1st child and visit all of its children. But applying it to the puzzle was 
difficult as I did not understand what was representing the child or root node. 

## Contributers 
Lucie Lim - Lunar Landing Model-View-Controller
Darian Cheung - BFS solver, Tip Over Model-View-Controller

