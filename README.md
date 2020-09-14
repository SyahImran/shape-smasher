# shape-smasher

Program that allows the user to play the shape smasher game.

The game has 3 shapes:
- Square
  - Worth 1 points
  - The user will have 3 seconds to hit the shape
- Circle
  - Worth 3 points
  - The user will have 2.5 seconds to hit the shape
- Triangle
  - Worth 5 points
  - The user will have 2 seconds to hit the shape
  
When a shape is left clicked, it moves to a new random location and the user is awarded points for it.
When a shape is not pressed, it moves to a new random location and the user receives 1 skull.
The game ends when the user has 3 skulls.
When the program displays game over, the shapes stop moving/creating skulls and clicking shapes no longer awards points.
When the game is over, right clicks reset the game.
