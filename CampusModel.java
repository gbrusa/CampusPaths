package hw8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hw5.Graph;
import hw7.Dijkstra;
import hw7.Edge;

/**
 * <b>CampusModel</b> represents the main bulk of the Model for
 * CampusPaths.java. It can perform a Dijkstra search to find the least-cost
 * path between two buildings on the UW-Seattle Campus. It also stores holds a
 * reference to all the valid buildings.
 */
public class CampusModel {

	/**
	 * Stores a building's short name and its long name in an easy-to-read fashion
	 */
	private static List<String> fullBuildingNames = new ArrayList<String>();

	/** A directory that maps a building's short name to its long name */
	private static Map<String, String> shortToLong = new HashMap<String, String>();

	/** A directory that maps a building's short name to its pixel coordinate */
	private static Map<String, CampusPoint> shortToPoint = new HashMap<String, CampusPoint>();

	/** A graph representing all the paths between buildings on the UW campus */
	private static Graph<CampusPoint, Double> campus = new Graph<CampusPoint, Double>();

	// Abstraction Function:
	// Stores information for the CampusPaths.java class. fullBuildingNames stores
	// all the names of the buildings in the dataset as "shortName: longName". This
	// is pulled by the View whenever it needs to print it out. shortToLong acts as
	// a directory so that abbreviations can be looked up and their long names can
	// be printed out. shortToPoint allows the Model to find the pixel coordinates
	// of each building in the dataset. campus is a reference to the graph that
	// stores the paths between each building. Currently, these are all stored as
	// points. Note that the graph has many points which are not buildings, since
	// these are intermediate points necessary to find paths between the buildings.
	//
	// Representation Invariant:
	// * fullBuildingNames, shortToLong, shortToPoint, campus != null;
	// * fullBuildingNames has no null entries
	// * shortToLong has no null keys and values
	// * shortToPoint has no null keys and values

	/** Boolean value that toggles extensive checkRep */
	private static final boolean TESTING_MODE = false;

	/**
	 * Initializer: Creates a graph representing the data given in
	 * campus_buildings.dat and campus_paths.dat
	 */
	static {
		// First parse campus_buildings
		CampusParser.parseBuildings("src/hw8/data/campus_buildings.dat", fullBuildingNames, shortToLong, shortToPoint);

		// Next parse campus_paths
		List<Edge<CampusPoint, Double>> paths = new ArrayList<Edge<CampusPoint, Double>>();
		CampusParser.parsePaths("src/hw8/data/campus_paths.dat", paths);

		// Finally add all the nodes and edges to the create the graph
		for (Edge<CampusPoint, Double> e : paths) {
			if (!campus.containsNode(e.getParent()))
				campus.addNode(e.getParent());
			if (!campus.containsNode(e.getChild()))
				campus.addNode(e.getChild());
			campus.addEdge(e.getParent(), e.getChild(), e.getLabel());
		}

		checkRep();
	}

	/**
	 * Performs a Dijkstra search to find the least-cost path between two buildings
	 * 
	 * @param start
	 *            The building at which the search begins
	 * @param end
	 *            The building at which the search ends
	 */
	public static List<String> search(String start, String end) {
		List<Edge<CampusPoint, Double>> searchResult = Dijkstra.search(shortToPoint.get(start), shortToPoint.get(end),
				campus);

		// format the data so that it can be simply printed by the view
		List<String> formattedResult = new ArrayList<String>();
		formattedResult.add("Path from " + shortToLong.get(start) + " to " + shortToLong.get(end) + ":");
		double total = 0;
		for (Edge<CampusPoint, Double> e : searchResult) {
			CampusPoint child = e.getChild();
			String dest = String.format("(%.0f, %.0f)", child.getX(), child.getY());
			String direction = e.getParent().getDirection(child);
			formattedResult.add(String.format("\tWalk %.0f feet " + direction + " to " + dest, e.getLabel()));
			total += e.getLabel();
		}
		formattedResult.add(String.format("Total distance: %.0f feet", total));
		return formattedResult;
	}

	/**
	 * @return a list of all buildings stored as "shortName: longName" sorted by
	 *         short name only
	 */
	public static List<String> pullBuildings() {
		return Collections.unmodifiableList(fullBuildingNames);
	}

	/**
	 * @param str
	 *            The building that is being looked up
	 * @return true if str is a valid building, false otherwise
	 */
	public static boolean contains(String str) {
		return campus.containsNode(shortToPoint.get(str));
	}

	/**
	 * Checks that the representation invariant holds
	 */
	private static void checkRep() {
		assert (fullBuildingNames != null);
		assert (shortToLong != null);
		assert (shortToPoint != null);
		assert (campus != null);
		if (TESTING_MODE) {
			for (String str : fullBuildingNames) {
				assert (str != null) : "there cannot be a null entry in fullBuildingNames";
			}

			for (String str : shortToLong.keySet()) {
				assert (str != null) : "shortToLong cannot contain a null key";
				assert (shortToLong.get(str) != null) : "shortToLong cannot contain a null value";
			}

			for (String str : shortToPoint.keySet()) {
				assert (str != null) : "shortToPoint cannot contain a null key";
				assert (shortToPoint.get(str) != null) : "shortToPoint cannot contain a null value";
			}
		}
	}
}
