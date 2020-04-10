package search_engine.astar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import rush_hour.Constants;
import rush_hour.Movement;
import rush_hour.RawPuzzleObject;
import rush_hour.Vehicle;

/**
 * This class implements the search node in our game, Search node construct from
 * raw board data which read from an input file and each node knows how to build
 * it's valid successors
 */
public class AStarSearchNode {
	protected AStarSearchNode parent;
	protected int depthInGraph;
	protected double heuristicValue;
	protected int numberOfMoves;
	protected double evaluationFunc;
	protected Collection<Point> emptySpots;
	protected HashMap<Character, Vehicle> vehicles;
	final protected int puzzleID;
	protected final int successorIndex;
	protected final int boardIdentifier;
	private Movement movement;

	public AStarSearchNode(RawPuzzleObject obj) {
		this(obj.getEmptySpots(), obj.getVehiclesMapping(), obj.getPuzzleId(), 0, 0, 0);
	}

	public AStarSearchNode(Collection<Point> emptySpots, HashMap<Character, Vehicle> vehicles, int puzzleID,
			int successorIndex, int numberOfMoves, int depthInGraph) {
		this.emptySpots = emptySpots;
		this.vehicles = vehicles;
		this.puzzleID = puzzleID;
		this.parent = null;
		this.successorIndex = successorIndex;
		heuristicValue = HeuristicFunCalculator.getCalculatedHeuristicValueForState(this.vehicles.values());
		this.numberOfMoves = numberOfMoves;
		this.evaluationFunc = this.heuristicValue + this.numberOfMoves;
		this.depthInGraph = depthInGraph;
		this.boardIdentifier = this.generateStateIdentifier();
	}

	private AStarSearchNode(Collection<Point> emptySpots, HashMap<Character, Vehicle> vehicles, int puzzleID,
			AStarSearchNode parentNode, int successorIndex, Movement movementLeadToThisState) {
		this(emptySpots, vehicles, puzzleID, successorIndex, parentNode.numberOfMoves + 1, parentNode.depthInGraph + 1);
		this.parent = parentNode;
		movement = movementLeadToThisState;

	}

	public boolean isGoalNode() {
		for (int targetVehicleEndPos = (int) vehicles.get(Constants.TARGET_VEHICLE_IDENTIFIER).getEndPos()
				.getY(); targetVehicleEndPos < Constants.BOARD_SIZE; targetVehicleEndPos++) {
			Character identifierAtPosition = getCarIdentifier(new Point(Constants.EXIT_RAW, targetVehicleEndPos));
			if (identifierAtPosition != Constants.UKNOWN_IDENTIFIER
					&& identifierAtPosition != Constants.TARGET_VEHICLE_IDENTIFIER)
				return false;
		}
		return true;
	}

	public Set<AStarSearchNode> getSuccessors() {
		Set<AStarSearchNode> successors = new HashSet<>();
		int successorIndex = 0;
		for (Point emptySpot : emptySpots)
			for (Character carIdentifier : getNeighbors(emptySpot))
				if (canNeighborMoveHere(emptySpot, carIdentifier))
					successors.add(getNextState(carIdentifier, emptySpot, ++successorIndex));
		return successors;
	}

	public static <T> Map<Integer, List<T>> deepCopyStreamWorkAround(Map<Integer, List<T>> original) {
		return original.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, valueMapper -> new ArrayList<>(valueMapper.getValue())));
	}

	public AStarSearchNode getNextState(Character carToMoveID, Point destenationToMove, int successorIndex) {
		Set<Point> newEmptySpots = emptySpots.stream().collect(Collectors.toSet());
		Map<Character, Vehicle> newVehicleMap = vehicles.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, valueMapper -> new Vehicle(valueMapper.getValue())));

		Vehicle updatedVehicle = newVehicleMap.get(carToMoveID);
		// Here you do know which vehicle should go to where!
		Movement movement = new Movement(new Point(updatedVehicle.getStartPos()), new Point(destenationToMove),
				carToMoveID, updatedVehicle.getOrientation());
		Collection<Point> newEmptyPoints = updatedVehicle.moveVehicle(destenationToMove);
		newEmptySpots.addAll(newEmptyPoints);
		newEmptySpots.removeAll(updatedVehicle.getLocations());
		return new AStarSearchNode(newEmptySpots, (HashMap<Character, Vehicle>) newVehicleMap, puzzleID, this,
				successorIndex, movement);
	}

	private Set<Character> getNeighbors(Point currentEmptySpot) {
		Set<Character> toReturn = new HashSet<>();
		toReturn.add(getNorthestNeighbor(currentEmptySpot));
		toReturn.add(getSouthestNeighbor(currentEmptySpot));
		toReturn.add(getEastestNeighbor(currentEmptySpot));
		toReturn.add(getWestestNeighbor(currentEmptySpot));
		return toReturn.stream().filter(c -> c != Constants.UKNOWN_IDENTIFIER).collect(Collectors.toSet());
	}

	public Character getCarIdentifier(Point currentEmptySpot) {
		return vehicles.keySet().stream().filter(v -> vehicles.get(v).isPointIntersectsWithMe(currentEmptySpot))
				.findAny().orElse(Constants.UKNOWN_IDENTIFIER);
	}

	public Character getNorthestNeighbor(Point currentEmptySpot) {
		Character carIdentifier = getCarIdentifier(currentEmptySpot);
		if (currentEmptySpot.getX() <= 0 || carIdentifier != Constants.UKNOWN_IDENTIFIER)
			return carIdentifier;
		return getNorthestNeighbor(new Point((int) currentEmptySpot.getX() - 1, (int) currentEmptySpot.getY()));
	}

	public Character getSouthestNeighbor(Point currentEmptySpot) {
		Character carIdentifier = getCarIdentifier(currentEmptySpot);
		if (currentEmptySpot.getX() > Constants.BOARD_SIZE || carIdentifier != Constants.UKNOWN_IDENTIFIER)
			return carIdentifier;
		return getSouthestNeighbor(new Point((int) currentEmptySpot.getX() + 1, (int) currentEmptySpot.getY()));
	}

	public Character getEastestNeighbor(Point currentEmptySpot) {
		Character carIdentifier = getCarIdentifier(currentEmptySpot);
		if (currentEmptySpot.getY() >= Constants.BOARD_SIZE || carIdentifier != Constants.UKNOWN_IDENTIFIER)
			return carIdentifier;
		return getEastestNeighbor(new Point((int) currentEmptySpot.getX(), (int) currentEmptySpot.getY() + 1));
	}

	public Character getWestestNeighbor(Point currentEmptySpot) {
		Character carIdentifier = getCarIdentifier(currentEmptySpot);
		if (currentEmptySpot.getY() <= 0 || carIdentifier != Constants.UKNOWN_IDENTIFIER)
			return carIdentifier;
		return getWestestNeighbor(new Point((int) currentEmptySpot.getX(), (int) currentEmptySpot.getY() - 1));
	}

	public boolean canNeighborMoveHere(Point emptySpot, Character carIdentifier) {
		Vehicle neighborVehicle = vehicles.get(carIdentifier);
		if (neighborVehicle.getOrientation() == Constants.HORIZONTAL)
			return (emptySpot.getX() == neighborVehicle.getStartPos().getX()
					&& (emptySpot.getX() == neighborVehicle.getEndPos().getX()))
					&& (0 <= (int) emptySpot.getY() && (int) emptySpot.getY() < Constants.BOARD_SIZE);
		else
			return (emptySpot.getY() == neighborVehicle.getStartPos().getY()
					&& (emptySpot.getY() == neighborVehicle.getEndPos().getY()))
					&& (0 <= (int) emptySpot.getX() && (int) emptySpot.getX() < Constants.BOARD_SIZE);
	}

	public double getEvaluationFunc() {
		return evaluationFunc;
	}

	public int getUUID() {
		return boardIdentifier;
	}

	public double getHeuristicsValue() {
		return heuristicValue;
	}

	public int getDepth() {
		return depthInGraph;
	}

	public int getPuzzleID() {
		return puzzleID;
	}

	@Override
	public boolean equals(Object o) {
		return this.depthInGraph == ((AStarSearchNode) o).depthInGraph
				&& this.heuristicValue == ((AStarSearchNode) o).heuristicValue
				&& this.numberOfMoves == ((AStarSearchNode) o).numberOfMoves
				&& this.evaluationFunc == ((AStarSearchNode) o).evaluationFunc
				&& this.emptySpots.equals(((AStarSearchNode) o).emptySpots)
				&& this.vehicles.equals(((AStarSearchNode) o).vehicles)
				&& this.puzzleID == ((AStarSearchNode) o).puzzleID
				&& this.successorIndex == ((AStarSearchNode) o).successorIndex
				&& ((this.parent != null && ((AStarSearchNode) o).parent != null)
						? this.parent.equals(((AStarSearchNode) o).parent)
						: true);
	}

	public int getSuccessorIndex() {
		return successorIndex;
	}

	private int generateStateIdentifier() {
		List<Integer> evaluationLst = new ArrayList<>();

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				Point currentPoint = new Point(i, j);
				Character identifier = getCarIdentifier(currentPoint);
				int val = (int) Constants.STATIC_POINT_TO_VALUE.get(currentPoint);
				int multiplyFactor = ((identifier + 1) - 'A');
				evaluationLst.add(val * multiplyFactor);
			}
		}
		return evaluationLst.stream().collect(Collectors.summingInt(Integer::intValue));
	}

	// remove me
	public Collection<Point> getEmptySpots() {
		return emptySpots;
	}

	public Vehicle getVehicleByID(Character id) {
		return vehicles.get(id);
	}

	public int getNumberOfPassedMoves() {
		return this.numberOfMoves;
	}

	private void setWholeSolution(ArrayList<AStarSearchNode> wholeSolution) {
		AStarSearchNode currentParent = this.parent;
		while (currentParent != null) {
			wholeSolution.add(currentParent);
			currentParent = currentParent.parent;
		}
		Collections.reverse(wholeSolution);
	}

	public String getPuzzleSolution() {
		// First of all, need to get all board from the beggining to the end
		String puzzleSolution = "";
		ArrayList<AStarSearchNode> wholeSolution = new ArrayList<AStarSearchNode>();
		setWholeSolution(wholeSolution);
		for (AStarSearchNode node : wholeSolution)
			puzzleSolution += (node.movement != null) ? node.movement : "";
		puzzleSolution += " END";
		return puzzleSolution;
	}

//	/*
//	 * this method is for printing the solution moves, by going up to the parent
//	 * each time.
//	 * 
//	 * @start - the father / not neccessary can be done untill null found.
//	 * 
//	 * @bestGoal - the best board solution
//	 */
//
//	public void printSolution(Node start, Node bestGoal) {
//		Node currentw = new Node(bestGoal);
//		Node parent;
//		ArrayList<String> moves = new ArrayList<String>();
//		while (currentw.getId() != start.getId()) {
//
//			StringBuilder sb = new StringBuilder();
//			int index = 0;
//			char carSymbol;
//			char direction;
//			int carOrient;
//			int steps = 0;
//			String move;
//			parent = currentw.getParent();
//			if (parent == null)
//				break;
//			for (int i = 0; i < currentw.getPuz().getNumOfCars(); i++) {
//
//				if (currentw.getPuz().getcPosition()[i] != parent.getPuz().getcPosition()[i]) {
//					index = i;
//					steps = Math.abs(currentw.getPuz().getcPosition()[i] - parent.getPuz().getcPosition()[i]);
//
//				}
//			}
//			carSymbol = currentw.getPuz().getCarSymbol()[index];
//			carOrient = currentw.getPuz().getCarOrient()[index];
//
//			if (carOrient == 1) {
//				if (currentw.getPuz().getcPosition()[index] > parent.getPuz().getcPosition()[index])
//					direction = 'D';
//				else
//					direction = 'U';
//
//			} else {
//				if (currentw.getPuz().getcPosition()[index] > parent.getPuz().getcPosition()[index])
//					direction = 'R';
//				else
//					direction = 'L';
//			}
//
//			sb.append(carSymbol);
//			sb.append(direction);
//			sb.append(steps);
//			sb.append(' ');
//			move = sb.toString();
//
//			moves.add(move);
//			currentw = parent;
//		}
//		int countX = 0;
//		while ((bestGoal.getPuz().getcPosition()[0] + countX) < bestGoal.getPuz().getSize()) {
//			countX++;
//		}
//
//		for (int i = moves.size() - 1; 0 <= i; i--) {
//			System.out.print(moves.get(i));
//
//		}
//
//		System.out.print("XR" + countX);
//
//		System.out.println();
//	}
}
