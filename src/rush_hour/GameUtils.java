package rush_hour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class GameUtils {

	public static Logger LOGGER = Logger.getLogger(Main.class.getName());

	private GameUtils() {
	}

	public static int getTimeLimitFromCommandLine(String[] args) {
		int defaultTimeLimit = 1000;
		if (args.length > 0) {
			try {
				defaultTimeLimit = Integer.parseInt(args[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.warning("You didn't pass time to solve puzzles, using default value..");
		}
		LOGGER.info("Time to solve single puzzle: " + defaultTimeLimit);
		return defaultTimeLimit;
	}

	public static String getInputFilePath(String[] args) {
		String inputFilePath = "./rh.txt";
		if (args.length > 0) {
			try {
				inputFilePath = args[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.warning(
					"You didn't pass input file for this application, assuming you would like me to use default input file");
		}
		LOGGER.info("Puzzle input file path is: " + inputFilePath);
		return inputFilePath;
	}

	public static ArrayList<String> getRawPuzzlesFromInputFile(String[] args) {
		String inputFilePath = GameUtils.getInputFilePath(args);

		ArrayList<String> rawPuzzles = new ArrayList<String>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(inputFilePath));
			String rawPuzzle = reader.readLine();
			while (rawPuzzle != null) {
				rawPuzzles.add(rawPuzzle);
				rawPuzzle = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.severe("IOException had catched, exiting the application!");
			System.exit(-1);
		}
		parsePuzzlesToCsv(rawPuzzles);
		return rawPuzzles;
	}

	public static String getPuzzleRawCsvPath(int puzzleNumber) {
		return Constants.RAW_CSV_PUZZELS_OUTPUT_PATH + puzzleNumber + ".csv";
	}

	public static void parsePuzzlesToCsv(ArrayList<String> rawPuzzlesAs) {
		int puzzleNumber = 1;

		// Check if output directory exists
		File directory = new File(Constants.RAW_CSV_PUZZELS_OUTPUT_PATH);
		if (!directory.exists()) {
			directory.mkdir();
		}

		for (String puzzle : rawPuzzlesAs) {
			String fileHeaderAndFooter = ",0,1,2,3,4,5\n";
			try {
				FileWriter myWriter = new FileWriter(getPuzzleRawCsvPath(puzzleNumber));
				myWriter.write(fileHeaderAndFooter);
				int rawIndex = 0;
				for (String puzzleRaw : puzzle.split("(?<=\\G.{6})")) {
					String lineToPrint = rawIndex + "|,";
					for (char cell : puzzleRaw.toCharArray())
						lineToPrint += cell + ",";
					if (rawIndex == Constants.EXIT_RAW)
						lineToPrint += "EXIT,";
					else
						lineToPrint += "|";
					lineToPrint += "\n";
					myWriter.write(lineToPrint);
					rawIndex += 1;
				}
				myWriter.write(fileHeaderAndFooter);
				myWriter.close();

			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			puzzleNumber += 1;
		}
	}

	public static String getOutputFileName(String[] args) {
		String outputFileName = "rh";
		if (args.length > 0) {
			try {
				String inputFileNameWithSuffix = args[0];
				outputFileName = inputFileNameWithSuffix.substring(0, inputFileNameWithSuffix.lastIndexOf('.'));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.warning(
					"You didn't pass input file for this application, assuming you would like me to use default input file");
		}
		LOGGER.info("Puzzles statistics output file name is: " + outputFileName);
		return outputFileName;
	}
}
