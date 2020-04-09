package tests;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rush_hour.Constants;
import rush_hour.Vehicle;
import search_engine.astar.AStarSearchNode;

public final class TestsUtils {
	public static Vehicle getVehicle(char identifier, boolean orientation, int size, Point startPoint, Point endPoint) {
		Vehicle vehicleToBuild = new Vehicle(identifier, startPoint);
		vehicleToBuild.setEndPos(endPoint);
		vehicleToBuild.setOrientation(orientation);
		vehicleToBuild.setSize(size);

		return vehicleToBuild;
	}

	public static AStarSearchNode getSearchNode() {
		Set<Point> firstSuccessorExpectedEmptySpots = new HashSet<>();
		firstSuccessorExpectedEmptySpots.add(new Point(4, 0));
		firstSuccessorExpectedEmptySpots.add(new Point(4, 1));
		firstSuccessorExpectedEmptySpots.add(new Point(1, 4));
		firstSuccessorExpectedEmptySpots.add(new Point(2, 4));
		firstSuccessorExpectedEmptySpots.add(new Point(3, 4));
		firstSuccessorExpectedEmptySpots.add(new Point(3, 3));
		firstSuccessorExpectedEmptySpots.add(new Point(5, 4));
		firstSuccessorExpectedEmptySpots.add(new Point(5, 5));
		HashMap<Character, Vehicle> firstSuccessorVehiclesMap = new HashMap<>();
		firstSuccessorVehiclesMap.put('O',
				TestsUtils.getVehicle('O', Constants.VERTICAL, 3, new Point(0, 0), new Point(2, 0)));
		firstSuccessorVehiclesMap.put('P',
				TestsUtils.getVehicle('P', Constants.HORIZONTAL, 3, new Point(0, 1), new Point(0, 3)));
		firstSuccessorVehiclesMap.put('E',
				TestsUtils.getVehicle('E', Constants.VERTICAL, 2, new Point(4, 2), new Point(5, 2)));
		firstSuccessorVehiclesMap.put('G',
				TestsUtils.getVehicle('G', Constants.HORIZONTAL, 2, new Point(5, 0), new Point(5, 1)));
		firstSuccessorVehiclesMap.put('Q',
				TestsUtils.getVehicle('Q', Constants.VERTICAL, 3, new Point(1, 5), new Point(3, 5)));
		firstSuccessorVehiclesMap.put('X',
				TestsUtils.getVehicle('X', Constants.HORIZONTAL, 2, new Point(2, 2), new Point(2, 3)));
		firstSuccessorVehiclesMap.put('B',
				TestsUtils.getVehicle('B', Constants.VERTICAL, 2, new Point(1, 1), new Point(2, 1)));
		firstSuccessorVehiclesMap.put('R',
				TestsUtils.getVehicle('R', Constants.HORIZONTAL, 3, new Point(3, 0), new Point(3, 2)));
		firstSuccessorVehiclesMap.put('F',
				TestsUtils.getVehicle('F', Constants.HORIZONTAL, 2, new Point(4, 4), new Point(4, 5)));
		firstSuccessorVehiclesMap.put('A',
				TestsUtils.getVehicle('A', Constants.HORIZONTAL, 2, new Point(0, 4), new Point(0, 5)));
		firstSuccessorVehiclesMap.put('C',
				TestsUtils.getVehicle('C', Constants.HORIZONTAL, 2, new Point(1, 2), new Point(1, 3)));
		firstSuccessorVehiclesMap.put('D',
				TestsUtils.getVehicle('D', Constants.VERTICAL, 2, new Point(4, 3), new Point(5, 3)));

		return new AStarSearchNode(firstSuccessorExpectedEmptySpots, firstSuccessorVehiclesMap, 1, 1, 1, 1);
	}
}
