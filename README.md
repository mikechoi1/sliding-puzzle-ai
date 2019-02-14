## Sliding Puzzle AI
This is an ai built using the A* Searching Algorithm and it solves the sliding puzzle in ascending order from any solvable user-inputted starting position.

### How this works:
1. make sure that the initial state is solvable (inversion count must be even)
2. if the current state is not the goal state, find successors(possible next moves) and calculate the heuristic for each successor
3. put all nodes in a priority queue ordered based on how good the heuristic is (node is a container that holds the state, depth, and other data)
4. take out the best node from the queue and set its state as current state
5. put nodes with "used" states in a set to prevent expanding the same states again
5. repeat 2-5 until current state is goal state
6. now, the current state is the goal state, but it wasn't solved in order of legal moves. To explain, the program just checks the best move and expands, but the next state the program checks could be a totally different state that isn't the current's successors because which state the program checks next is solely based on best heuristic
7. each node stores its parent's node so starting from the final node, we can see which state the current state came from and repeat until we have the starting state. We then print in reverse order to properly display the moves from start to finish

### Some things to note:
- "0" is considered the blank space, so a different number and 0 will switch places for every move
- 0  1  2
  3  4  5
  6  7  8
  is considered the final state and to change what the final state is, user has to manually go in the source code of SlidingPuzzle.java and change "gState" in main method
- To run the program using user-inputted initial state, type numbers 0-8 in any order with no spaces inbetween
  ex: 012345678, 102837456, 582017364
- This program has two heuristics implemented. Heuristic 2 solves significantly faster. Here's a snippet:

```
public int calcHeuristic(String state) {
		int counter = 0;
		//h1
		if(whichHeuristic == 1) {
			for(int i = 0; i < state.length(); i++) {
				if(state.charAt(i) != '0' && state.charAt(i) != gState.charAt(i))
					counter++;
			}
		}
		//h2
		else {
			int row, col;
			for(int i = 0; i < state.length(); i++) {
				if(state.charAt(i) != '0') {
					row = i / 3;
					col = i % 3;
					//only works for goal state of 012345678
					counter += Math.abs((Character.getNumericValue(state.charAt(i)) / 3 - row)) + Math.abs((Character.getNumericValue(state.charAt(i)) % 3 - col));
				}
			}
		}
		return counter;
	}
```
Heuristic 1 only calculates how many numbers are not in their correct final positions
Heuristic 2 calculates how many spaces each number is away from its correct final position
