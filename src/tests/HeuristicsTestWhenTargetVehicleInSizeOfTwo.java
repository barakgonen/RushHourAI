package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import rush_hour.Constants;
import rush_hour.Vehicle;
import search_engine.astar.HeuristicFunCalculator;

public class HeuristicsTestWhenTargetVehicleInSizeOfTwo {

	private ArrayList<Vehicle> vehiclesMap;

	@Before
	public void SetUp() {
		vehiclesMap = new ArrayList<>();
		vehiclesMap.add(TestsUtils.getVehicle(Constants.TARGET_VEHICLE_IDENTIFIER, Constants.HORIZONTAL, 2,
				new Point(2, 0), new Point(2, 1)));
	}

	@Test
	public void testVehicleDistanceHeuristicDistanceFourStepsVehicleSizeTwo() {
		assertEquals(4, HeuristicFunCalculator.targetVehiclDistanceFromExit(TestsUtils.getVehicle(
				Constants.TARGET_VEHICLE_IDENTIFIER, Constants.HORIZONTAL, 2, new Point(2, 0), new Point(2, 1))));
	}

	@Test
	public void testVehicleDistanceHeuristicDistanceZeroStepsVehicleSizeTwo() {
		assertEquals(0, HeuristicFunCalculator.targetVehiclDistanceFromExit(TestsUtils.getVehicle(
				Constants.TARGET_VEHICLE_IDENTIFIER, Constants.HORIZONTAL, 2, new Point(2, 4), new Point(2, 5))));
	}

	@Test
	public void testVehicleDistanceHeuristicDistanceOneStepVehicleSizeTwo() {
		assertEquals(1, HeuristicFunCalculator.targetVehiclDistanceFromExit(TestsUtils.getVehicle(
				Constants.TARGET_VEHICLE_IDENTIFIER, Constants.HORIZONTAL, 2, new Point(2, 3), new Point(2, 4))));
	}

	@Test
	public void testVehicleDistanceHeuristicDistanceTwoStepsVehicleSizeTwo() {
		assertEquals(2, HeuristicFunCalculator.targetVehiclDistanceFromExit(TestsUtils.getVehicle(
				Constants.TARGET_VEHICLE_IDENTIFIER, Constants.HORIZONTAL, 2, new Point(2, 2), new Point(2, 3))));
	}

	@Test
	public void testVehicleDistanceHeuristicDistanceThreeStepsVehicleSizeTwo() {
		assertEquals(3, HeuristicFunCalculator.targetVehiclDistanceFromExit(TestsUtils.getVehicle(
				Constants.TARGET_VEHICLE_IDENTIFIER, Constants.HORIZONTAL, 2, new Point(2, 1), new Point(2, 2))));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithOneVerticalBlocker() {
		vehiclesMap.add(TestsUtils.getVehicle('K', Constants.VERTICAL, 3, new Point(1, 3), new Point(3, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('Q', Constants.VERTICAL, 2, new Point(4, 5), new Point(5, 5)));
		vehiclesMap.add(TestsUtils.getVehicle('M', Constants.HORIZONTAL, 2, new Point(5, 0), new Point(5, 1)));
		assertEquals(1, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithOneHorizontalBlocker() {
		vehiclesMap.add(TestsUtils.getVehicle('A', Constants.VERTICAL, 2, new Point(0, 1), new Point(1, 1)));
		vehiclesMap.add(TestsUtils.getVehicle('N', Constants.VERTICAL, 2, new Point(4, 5), new Point(5, 5)));
		vehiclesMap.add(TestsUtils.getVehicle('R', Constants.HORIZONTAL, 2, new Point(3, 2), new Point(3, 3)));
		assertEquals(0, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithTwoVerticalBlockers() {
		vehiclesMap.add(TestsUtils.getVehicle('L', Constants.VERTICAL, 3, new Point(1, 3), new Point(3, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('S', Constants.VERTICAL, 2, new Point(2, 4), new Point(3, 4)));
		vehiclesMap.add(TestsUtils.getVehicle('C', Constants.HORIZONTAL, 2, new Point(5, 0), new Point(5, 1)));
		assertEquals(2, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithTwoHorizontalBlockers() {
		vehiclesMap.add(TestsUtils.getVehicle('N', Constants.VERTICAL, 2, new Point(0, 3), new Point(1, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('A', Constants.HORIZONTAL, 2, new Point(2, 2), new Point(2, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('B', Constants.HORIZONTAL, 2, new Point(2, 4), new Point(2, 5)));
		vehiclesMap.add(TestsUtils.getVehicle('P', Constants.HORIZONTAL, 3, new Point(4, 1), new Point(4, 3)));
		assertEquals(2, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithThreeVerticalBlockers() {
		vehiclesMap.add(TestsUtils.getVehicle('L', Constants.VERTICAL, 3, new Point(2, 2), new Point(2, 4)));
		vehiclesMap.add(TestsUtils.getVehicle('S', Constants.VERTICAL, 3, new Point(1, 3), new Point(3, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('B', Constants.VERTICAL, 3, new Point(1, 5), new Point(3, 5)));
		vehiclesMap.add(TestsUtils.getVehicle('C', Constants.HORIZONTAL, 3, new Point(5, 3), new Point(5, 5)));
		assertEquals(3, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithOneHorizontalAndTwoVertiaclBlockers() {
		vehiclesMap.add(TestsUtils.getVehicle('B', Constants.HORIZONTAL, 2, new Point(0, 0), new Point(0, 1)));
		vehiclesMap.add(TestsUtils.getVehicle('A', Constants.HORIZONTAL, 2, new Point(2, 2), new Point(2, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('P', Constants.VERTICAL, 2, new Point(2, 4), new Point(3, 4)));
		vehiclesMap.add(TestsUtils.getVehicle('N', Constants.VERTICAL, 3, new Point(0, 5), new Point(2, 5)));
		vehiclesMap.add(TestsUtils.getVehicle('S', Constants.HORIZONTAL, 2, new Point(4, 0), new Point(4, 1)));
		assertEquals(3, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	public void testBlockingVehiclesHeuristicCalculationWithFourVerticalBlockers() {
		vehiclesMap.add(TestsUtils.getVehicle('L', Constants.VERTICAL, 3, new Point(2, 2), new Point(4, 2)));
		vehiclesMap.add(TestsUtils.getVehicle('S', Constants.VERTICAL, 3, new Point(2, 3), new Point(0, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('B', Constants.VERTICAL, 2, new Point(1, 4), new Point(2, 4)));
		vehiclesMap.add(TestsUtils.getVehicle('A', Constants.VERTICAL, 3, new Point(0, 5), new Point(5, 2)));
		vehiclesMap.add(TestsUtils.getVehicle('W', Constants.HORIZONTAL, 2, new Point(0, 1), new Point(0, 2)));
		vehiclesMap.add(TestsUtils.getVehicle('Z', Constants.HORIZONTAL, 2, new Point(5, 1), new Point(5, 2)));
		assertEquals(4, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}

	@Test
	public void testBlockingVehiclesHeuristicCalculationWithThreeHorizontalAndOneVertical() {
		vehiclesMap.add(TestsUtils.getVehicle('B', Constants.HORIZONTAL, 2, new Point(1, 1), new Point(1, 2)));
		vehiclesMap.add(TestsUtils.getVehicle('A', Constants.HORIZONTAL, 2, new Point(2, 2), new Point(2, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('N', Constants.VERTICAL, 3, new Point(5, 3), new Point(3, 3)));
		vehiclesMap.add(TestsUtils.getVehicle('S', Constants.HORIZONTAL, 2, new Point(2, 4), new Point(2, 5)));
		assertEquals(2, HeuristicFunCalculator.numberOfBlockingVehicles(vehiclesMap));
	}
}