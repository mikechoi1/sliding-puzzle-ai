import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Collections;

public class SlidingPuzzle {

	static int lowestDepth = 31;
	static int highestDepth = 0;
	static double[][] solutionData;
	int whichHeuristic;
	int nodesGenerated;
	String iState;
	String gState;
	PriorityQueue<Node> openSet;
	HashSet<String> closedSet;
	int currentCost;
	int solutionDepth;
	
	
	public SlidingPuzzle(String initialState, String goalState, int heuristic) {
		solutionDepth = 0;
		currentCost = 0;
		whichHeuristic = heuristic;
		iState = initialState;
		gState = goalState;
		openSet = new PriorityQueue<>();
		openSet.add(new Node(null, initialState, 0, calcHeuristic(initialState)));
		nodesGenerated = 1;
		closedSet = new HashSet<>();
	}
	public static boolean isSolvable(String iState) {
		int inversionCount = 0;
		for(int i = 0; i < iState.length(); i++) {
			for(int j = i + 1; j < iState.length(); j++) {
				if(iState.charAt(i) == '0')
					{}
				else if(iState.charAt(j) == '0')
					{}
				else if(iState.charAt(i) > iState.charAt(j))
					inversionCount++;
			}
		}
		return inversionCount % 2 == 0;
	}
	public void start() {
		double startTime = System.nanoTime();
		while(!openSet.isEmpty()) {
			Node temp = openSet.poll();
			while(closedSet.contains(temp.getState()) && !openSet.isEmpty()) {
				temp = openSet.poll();
			}
			closedSet.add(temp.getState());
			String currentState = temp.getState();
			if(currentState.equals(gState)) {
				double endTime = (System.nanoTime() - startTime) / 1000000000;
				int currentCost = temp.getCost();
				solutionData[currentCost][0]++;
				solutionData[currentCost][1] += nodesGenerated;
				solutionData[currentCost][2] += endTime;
				if(currentCost > highestDepth)
					highestDepth = currentCost;
				else if(currentCost < lowestDepth)
					lowestDepth = currentCost;
				printPath(temp);
				System.out.println("Nodes created: " + nodesGenerated + " Depth: " + temp.getCost());
				break;
			}
			else {
				ArrayList<String> tempList = getSuccessors(currentState);
				for(int i = 0; i < tempList.size(); i++) {
					Node newNode = new Node(temp, tempList.get(i), solutionDepth(temp), calcHeuristic(tempList.get(i)));
					nodesGenerated++;
					temp.addChildren(newNode);
					if(!closedSet.contains(newNode.getState())) {
						openSet.add(newNode);
					}
				}
			}
			solutionDepth++;
		}
	}
	public int solutionDepth(Node currentNode) {
		int count = 1;
		while(currentNode.getParent() != null) {
			count++;
			currentNode = currentNode.getParent();
		}
		return count;
	}
	public ArrayList<String> getSuccessors(String currentState) {
		ArrayList<String> tempList = new ArrayList<>();
		int blankPosition = currentState.indexOf('0');
		if(hasNeighbor(currentState, "left")) {
			tempList.add(currentState.replace(currentState.charAt(blankPosition), '*').replace(currentState.charAt(blankPosition - 1), currentState.charAt(blankPosition)).replace('*', currentState.charAt(blankPosition - 1)));
		}
		if(hasNeighbor(currentState, "right")) {
			tempList.add(currentState.replace(currentState.charAt(blankPosition), '*').replace(currentState.charAt(blankPosition + 1), currentState.charAt(blankPosition)).replace('*', currentState.charAt(blankPosition + 1)));
		}
		if(hasNeighbor(currentState, "up")) {
			tempList.add(currentState.replace(currentState.charAt(blankPosition), '*').replace(currentState.charAt(blankPosition - 3), currentState.charAt(blankPosition)).replace('*', currentState.charAt(blankPosition - 3)));
		}
		if(hasNeighbor(currentState, "down")) {
			tempList.add(currentState.replace(currentState.charAt(blankPosition), '*').replace(currentState.charAt(blankPosition + 3), currentState.charAt(blankPosition)).replace('*', currentState.charAt(blankPosition + 3)));
		}
		return tempList;
	}
	public boolean hasNeighbor(String state, String direction) {
		boolean valid = false;
		if(direction.equals("left")) {
			if(state.indexOf('0') % 3 != 0) {
				valid = true;
			}
		}
		else if(direction.equals("right")) {
			if(state.indexOf('0') % 3 != 2) {
				valid = true;
			}
		}
		else if(direction.equals("up")) {
			if(state.indexOf('0') > 2) {
				valid = true;
			}
		}
		else if(direction.equals("down")) {
			if(state.indexOf('0') < 6) {
				valid = true;
			}
		}
		return valid;
		
	}
	public boolean isEmpty() {
		return openSet.size() == 0;
	}
	public int getBlankPos(String state) {
		for(int i = 0; i < state.length(); i++) {
			if(state.charAt(i) == '0')
				return i;
		}
		return -1; //should never reach
	}
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
	public void printPath(Node finalNode) {
		if(finalNode.getParent() == null)
			System.out.println("#" + finalNode.getCost() + "\n" + finalNode);
		else {
			printPath(finalNode.getParent());
			System.out.println("#" + finalNode.getCost() + "\n" + finalNode);
		}
	}
	public static void main(String[] args) {
		int numberOfPuzzles;
		String iState = "";
		String gState = "012345678";
		Scanner kb = new Scanner(System.in);
		boolean loop = true;
		int heuristic;
		SlidingPuzzle test;
		double startTime, endTime;
		while(loop) {
			numberOfPuzzles = 1;
			System.out.println("\n8 Puzzle Solver\n");
			System.out.println("1) Enter initial state manually to solve\n2) Randomly solve n puzzles\n3) Quit");
			solutionData = new double[32][3];
			switch(kb.nextInt()) {
			case 1:
				System.out.println("Enter a state to solve: ");
				kb.nextLine();
				iState = kb.nextLine().replaceAll("\\s+","");
				while(!isSolvable(iState)) {
					System.out.println("Not solvable, choose a different initial state ");
					iState = kb.nextLine();
				}
				System.out.println("heuristic choice: 1 or 2? ");
				heuristic = kb.nextInt();
				test = new SlidingPuzzle(iState, gState, heuristic);
				startTime = System.nanoTime();
				test.start();
				endTime = (System.nanoTime() - startTime) / 1000000000;
				System.out.println("Total time taken: " + endTime + " seconds");
				break;
			case 2:
				System.out.println("Enter number of puzzles to solve: ");
				numberOfPuzzles = kb.nextInt();
				Character[] stateNumbers = {'0','1','2','3','4','5','6','7','8'};
				System.out.println("heuristic choice: 1 or 2? ");
				heuristic = kb.nextInt();
				startTime = System.nanoTime();
				int tempNumber = numberOfPuzzles;
				while(tempNumber-- > 0) {
					do {
						iState= "";
						Collections.shuffle(Arrays.asList(stateNumbers));
						for(Character c : stateNumbers)
						iState += c.toString();
					} while(!isSolvable(iState));
					test = new SlidingPuzzle(iState, gState, heuristic);
					test.start();
				}
				endTime = (System.nanoTime() - startTime) / 1000000000;
				System.out.println("\nTotal time taken: " + endTime + " seconds");
				//uncomment below to output additional data such as average nodes generated at different solution depths
				for(int i = 0; i < solutionData.length; i++) {
					if(solutionData[i][1] != 0) {
						System.out.printf("Average nodes generated for depth %d: %.2f with %.0f case(s) and time: %.5f sec\n",i , solutionData[i][1] / solutionData[i][0], solutionData[i][0], solutionData[i][2] / solutionData[i][0]);
					}
				}
				System.out.println("Highest depth: " + highestDepth);
				System.out.println("Lowest depth: " + lowestDepth);
				break;
			case 3:
				System.out.println("Goodbye");
				loop = false;
				break;
			default:
				System.out.println("Invalid choice");
				break;
			}
		}
		kb.close();
	}
}
