package search_engine.astar;

import java.util.Collection;
import java.util.HashMap;

import search_engine.FibonacciHeap;

public class AStar {

	protected FibonacciHeap<AStarSearchNode> openList;
	protected HashMap<Integer, AStarSearchNode> ndOpenList;
	protected HashMap<Integer, AStarSearchNode> closedList;
	protected AStarSearchNode prev;
	protected AStarSolutionStatisticsData solutionStatisticsData;

	// constructor for AStar and its parameters
	public AStar() {
		openList = new FibonacciHeap<>();
		ndOpenList = new HashMap<>();
		closedList = new HashMap<>();
		prev = null;
	}

	/*
	 * the method for finding the optimal solution for the board given. the way this
	 * solution works is doing the A* each time working the node with the min f
	 * value ( f = h + g ) where g is the num of moves so far and h is the heuristic
	 * which is the evaluated num of moves left to reach goal.
	 * 
	 * each new node we find all its next moves, we save them in the fibHeap .. and
	 * put the node we worked with in the closed list .. and we continue with this
	 * way untill we find the goal. or the openlist is embty.
	 */
	public SolvedAstarPuzzle getBestSolution(AStarSearchNode startNode) {
		solutionStatisticsData = new AStarSolutionStatisticsData(startNode.getPuzzleID());
		openList.enqueue(startNode, startNode.getEvaluationFunc());
		ndOpenList.put(startNode.getUUID(), startNode);

		AStarSearchNode bestGoal = null;
		int nodesCounter = 0;
		while (!openList.isEmpty()) {
			AStarSearchNode currentNode = openList.min().getValue();
			if (ndOpenList.containsKey(currentNode.getUUID()) && shouldNotExpandNode(currentNode)) {
				openList.dequeueMin();
				continue;
			}

			solutionStatisticsData.increaseNumberOfNodes();
			solutionStatisticsData.increaseHeuristicCounter(currentNode.getHeuristicsValue());

			if (currentNode.isGoalNode()) {
				solutionStatisticsData.setBranchingFactor(closedList.size());
				solutionStatisticsData.setPenetrance(closedList.size());

				bestGoal = currentNode;

				while (!openList.isEmpty())
					openList.dequeueMin();
				ndOpenList.clear();
				closedList.clear();
				break;
			}

			if ((prev != null) && (prev.getDepth() >= currentNode.getDepth())) {
				solutionStatisticsData.increaseAvgCounter();
				solutionStatisticsData.increaseAvgSum(prev.getDepth());
				solutionStatisticsData.setMinDepthIfNeeded(prev.getDepth());
				solutionStatisticsData.setMaxDepthIfNeeded(prev.getDepth());
			}

			prev = currentNode;

			openList.dequeueMin();
			ndOpenList.remove(currentNode.getUUID());
			closedList.put(currentNode.getUUID(), currentNode);

			if (currentNode.isGoalNode()) {
				continue;
			}

			// loop for working on hash tables, open and closed lists to work with nodes
			Collection<AStarSearchNode> successors = currentNode.getSuccessors();
			for (AStarSearchNode successorNode : successors) {
				if (closedList.containsKey(successorNode.getUUID())) {
					continue;
				}

				if (!ndOpenList.containsKey(successorNode.getUUID())) {
					openList.enqueue(successorNode, successorNode.getEvaluationFunc());
					ndOpenList.put(successorNode.getUUID(), successorNode);
				} else {
					if (successorNode.getEvaluationFunc() < ndOpenList.get(successorNode.getUUID())
							.getEvaluationFunc()) {
						nodesCounter++;
						openList.enqueue(successorNode, successorNode.getEvaluationFunc());
						ndOpenList.replace(successorNode.getUUID(), successorNode);
					}
				}
			}
		}
		solutionStatisticsData.setNumberOfNodes(solutionStatisticsData.getNumberOfNodes() - nodesCounter);
		solutionStatisticsData.calculateFinalData();

		if (solutionStatisticsData.getTotalRunningTimeForSolution() > 1000) {
			System.out.println("FAILED solving puzzle: " + startNode.getPuzzleID());
			return null;
		} else if (bestGoal == null) {
			System.out.println("No Solution for puzzle: " + startNode.getPuzzleID());
			return null;
		}

		return new SolvedAstarPuzzle(bestGoal, solutionStatisticsData);
	}

	private boolean shouldNotExpandNode(AStarSearchNode currentNode) {
		return ndOpenList.get(currentNode.getUUID()).getEvaluationFunc() < currentNode.getEvaluationFunc();
	}
}
