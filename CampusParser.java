package hw8;

import java.io.*;
import java.util.*;
import hw7.Edge;

/**
 * <b>CampusParser</b> is a parser utility that allows the user to load the
 * campus dataset.
 */

public class CampusParser {

	/**
	 * Reads the Campus Buildings dataset. Each line of the input file contains a
	 * building's short name, its long name and its pixel coordinates separated by
	 * tab characters.
	 * 
	 * @requires fileName is a valid file path && fullBuildingNames, shortToLong,
	 *           shortToPoint != null
	 * @param fileName
	 *            The file containing all the building information
	 * @param fullBuildingNames
	 *            A list containing each short name followed by its associated long
	 *            name
	 * @param shortToLong
	 *            A map that allows the conversion from a building's short name to
	 *            its long name
	 * @param shortToPoint
	 *            A map that allows the conversion from a building's short name to
	 *            its pixel coordinates
	 * 
	 * @modifies fullBuildingNames, shortToLong, shortToPoint
	 * @effects fills fillBuildingNames with a sorted list of all the short names
	 *          followed by the associated long name
	 * @effects fills shortToLong with a map from each short name to its associated
	 *          long name
	 * @effects fills shortToPoint with a map from each short name to its associated
	 *          pixel point
	 */
	public static void parseBuildings(String fileName, List<String> fullBuildingNames, Map<String, String> shortToLong,
			Map<String, CampusPoint> shortToPoint) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				// Parse the data splitting along tabs to retrieve each argument
				String[] tokens = inputLine.split("\t");

				String shortName = tokens[0];
				String longName = tokens[1];
				double x = new Double(tokens[2]);
				double y = new Double(tokens[3]);

				// Adds the parsed short names and coordinates to list and map
				fullBuildingNames.add(shortName + ": " + longName);
				shortToLong.put(shortName, longName);
				shortToPoint.put(shortName, new CampusPoint(x, y));
			}

			Collections.sort(fullBuildingNames, new BuildingComparator()); // sorts the list so it matches the view spec
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e);
					e.printStackTrace(System.err);
				}
			}
		}
	}

	/**
	 * Readings the Campus Paths dataset. The input file is split into sections with
	 * the first line of each section representing the coordinates of the start of
	 * the path, separated by a comma. Each subsequent line of that section starts
	 * with a tab character and contains the end coordinates of the path, separated
	 * by a comma, followed by a colon, a space and the weight of the path between
	 * the two points.
	 * 
	 * @requires fileName is a valid file path
	 * @param fileName
	 *            The file containing all the information about paths between the
	 *            buildings
	 * @param paths
	 *            A list containing all the paths in the file as Edge objects
	 * @modifies paths
	 * @effects fills paths with a list of Edges connecting two points via a
	 *          weighted path
	 */
	public static void parsePaths(String fileName, List<Edge<CampusPoint, Double>> paths) {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String inputLine;
			CampusPoint parent = null;
			while ((inputLine = reader.readLine()) != null) {

				// Due to format of file, parent is guaranteed to be given a value on the first
				// iteration of loop
				if (inputLine.charAt(0) != '\t') {
					parent = toPoint(inputLine);
				} else {
					// Separates the coordinates from the edge weight and then adds them as Edges
					// to paths
					String[] tokens = inputLine.split(": ");
					Double weight = new Double(tokens[1]);
					Edge<CampusPoint, Double> e = new Edge<CampusPoint, Double>(weight, parent, toPoint(tokens[0]));
					paths.add(e);
				}
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * @param str
	 *            The coordinates as a whole string separated by a single comma and
	 *            no spaces E.g. 123.45,678.91
	 * @return a CampusPoint representing the string with x as the first coordinate
	 *         and y as the second coordinate
	 */
	private static CampusPoint toPoint(String str) {
		String[] coordinates = str.split(",");
		return new CampusPoint(new Double(coordinates[0]), new Double(coordinates[1]));
	}

	/**
	 * Comparator utility so that buildings can be sorted according to their short
	 * name
	 */
	private static class BuildingComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			String[] str1 = o1.split(": ");
			String[] str2 = o2.split(": ");

			return str1[0].compareTo(str2[0]);
		}

	}
}
