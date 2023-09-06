# BFS Solver for 4 different puzzles

## Description
A puzzle solver that utilizes the breadth first search algorithm
to find solutions on 4 different puzzles.

## How to play
You have 4 puzzles to choose from: clock, water, lunar landing, and tip over.
Clock and water puzzles are unplayable puzzles. They are mainly to test the 
BFS solver. 

Lunar landing and tip over are playable puzzles, and you can play it in the 
terminal of the IDE or on a GUI.

For the IDE, in the terminal enter h to see options and their functions.

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
with the puzzle in the GUI and the PTUI. Certain functions act as the controller 
such as updating the board after a piece has been moved.

### GUI & PTUI
The GUI is the main component where the user can view and interact with the puzzle. 
The user can select different pieces from the board and choose a direction. They also
have several buttons to reload, reset, or get a hint for the puzzle. The board will update
and the GUI will reflect those changes to the user. The PTUI is another main component of 
viewing the puzzle but the board is represented in the terminal instead of an interactive 
popup. The user can see the board but they cannot directly click on a piece and move it, they 
would have to type in options and what piece they would like to move.

## Challenges
Some challenges during the project was understanding how to apply BFS to the 
puzzle, and how the model-view-controller works. 

At first glance the concept of BFS is simple enough to 
understand, as starting at the root node the algorithm visits each child node from the root, and 
once done it will go back to the 1st child and visit all of its children. But applying it to the puzzle was 
difficult as I did not understand what was representing the child or root node. With assistance and
clarification I was able to understand that the initial puzzle board was the root node and each child node 
was a configuration of 1 of the pieces moving in 1 of the directions. Essentially every parent node is a
configuration and each child of the parent was another configuration of the board with 1 of the pieces moving in 
all possible directions. 

The model-view-controller was a tough concept to grasp and create. I understood that the model
was the logic behind the puzzle, so it's clear what functions I would have to create
such as moving a piece, resetting the board, loading a puzzle, and getting a hint. However, the 
controller aspect was tough to grasp. I understood it was to connect the model to the view however
I expected it to have its own file or class in the project. I was not aware that in this project
its certain functions in the model and gui file. Such functions are the update and move pieces 
functions as the user can update the model and the model reflects those changes back to the viewer.

Creating the GUI and PTUI was a unique experience for me because there's a lot of functions in the 
javafx library and I was unsure how to display the updated positions of the figurines
properly. After learning about the setOnAction and setOnGraphic functions I was able to 
understand how to apply it to the project.


## Contributers 
Lucie Lim - Lunar Landing Model-View-Controller
Darian Cheung - BFS solver, Tip Over Model-View-Controller

