package gbrusa.cse331_17au_campus_paths;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hw5.Graph;
import hw7.Dijkstra;
import hw7.Edge;
import hw8.CampusPoint;

/**
 * <b>CampusModel</b> represents the main bulk of the Model for
 * CampusPaths.java. It can perform a Dijkstra search to find the least-cost
 * path between two buildings on the UW-Seattle Campus. It also stores a
 * reference to all the valid buildings names.
 */

public class CampusPathsModel {

    /** Maps the building names to their pixel coordinates */
    private static Map<String, CampusPoint> buildingCoordinates;

    /** Represents all the paths between buildings */
    private static Graph<CampusPoint, Double> campus;

    /** Stores all the buildingNames in a sorted list */
    private static List<String> buildingNames;

    // Abstraction Function:
    // Stores information for the CampusPathsMainActivity.java class. buildingNames
    // holds a list of all the building name abbreviations, much like
    // buildingCoordinates.keySet() except in List so that it can be sorted alphabetically.
    // This is then pulled by the main activity when setting up the ListViews.
    // buildingCoordinates allows the Model to find the pixel coordinates of each
    // building in the data set. campus is a reference to the graph that
    // stores the paths between each building. Currently, these are all stored
    // as points. Note that the graph has many points which are not buildings,
    // since these are intermediate points necessary to find paths between the
    // buildings.
    //
    // Representation Invariant:
    // * buildingCoordinates, campus, buildingNames != null;
    // * buildingNames has no null entries
    // * buildingCoordinates has no null keys and values

    /**
     * Creates and sets up the fields in the Model
     * @param context
     *          Can be passed to the associated parser so that resources can be accessed
     */
    public static void createModel(Context context) {
        // parse buildings first
        buildingCoordinates = new HashMap<String, CampusPoint>();
        AndroidParser.parseBuildings(context, buildingCoordinates);
        buildingNames = toSortedList();

        // parse paths and create graph
        List<Edge<CampusPoint, Double>> edges = new ArrayList<Edge<CampusPoint, Double>>();
        AndroidParser.parsePaths(context, edges);

        // adds paths to the graph field
        campus = new Graph<CampusPoint, Double>();
        for (Edge<CampusPoint, Double> e : edges) {
            if (!campus.containsNode(e.getParent()))
                campus.addNode(e.getParent());
            if (!campus.containsNode(e.getChild()))
                campus.addNode(e.getChild());
            campus.addEdge(e.getParent(), e.getChild(), e.getLabel());
        }
    }

    /**
     * @return a sorted List containing all the building name abbreviations in alphabetical order
     */
    private static List<String> toSortedList() {
        List<String> buildings = new ArrayList<String>();
        for (String building : buildingCoordinates.keySet()) {
            buildings.add(building);
        }
        Collections.sort(buildings);
        return buildings;
    }

    /**
     * @return a sorted List containing all the building name abbreviations in alphabetical order
     */
    public static List<String> getBuildingNames() {
        return Collections.unmodifiableList(buildingNames);
    }

    /**
     * @param name
     *          The building name that is being looked up
     * @return the CampusPoint associated with a given building
     */
    public static CampusPoint getPoint(String name) {
        return buildingCoordinates.get(name);
    }

    /**
     * Performs a Dijkstra search to find the least-cost path between two buildings
     *
     * @param start
     *            The building at which the search begins
     * @param dest
     *            The building at which the search ends
     * @return a list containing all the points that make up the path from start to dest
     */
    public static List<CampusPoint> search(String start, String dest) {
        List<Edge<CampusPoint, Double>> searchResult =
                Dijkstra.search(buildingCoordinates.get(start), buildingCoordinates.get(dest),
                        campus);

        // format the data so it's usable by the view
        List<CampusPoint> formattedResult = new ArrayList<CampusPoint>();
        formattedResult.add(buildingCoordinates.get(start));
        for (Edge<CampusPoint, Double> e : searchResult) {
            if (!formattedResult.contains(e.getParent())) {
                formattedResult.add(e.getParent());
            }
            formattedResult.add(e.getChild());
        }
        return formattedResult;
    }
}
