package gbrusa.cse331_17au_campus_paths;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import hw7.Edge;
import hw8.CampusPoint;

/**
 * <b>AndroidParser</b> is a parser utility that allows the user to load the
 * campus data set.
 */

public class AndroidParser {

    /**
     * Reads the Campus Buildings data set. Each line of the input file contains a
     * building's short name, its long name and its pixel coordinates separated by
     * tab characters.
     *
     * @requires context, shortToPoint != null;
     * @param context
     *            The application context so that resources can be accessed from
     *            within the parser
     * @param shortToPoint
     *            A map that allows the conversion from a building's short name to
     *            its pixel coordinates
     *
     * @modifies shortToPoint
     * @effects fills shortToPoint with a map from each short name to its associated
     *          pixel point
     */
    public static void parseBuildings(Context context, Map<String, CampusPoint> shortToPoint) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new
                    InputStreamReader(context.getResources().openRawResource(R.raw.campus_buildings)));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {

                // Parse the data splitting along tabs to retrieve each argument
                String[] tokens = inputLine.split("\t");

                String shortName = tokens[0];
                String longName = tokens[1];
                double x = new Double(tokens[2]) * 0.22;
                double y = new Double(tokens[3]) * 0.22;

                // Adds the parsed short names and coordinates to list and map
                shortToPoint.put(shortName, new CampusPoint(x, y));
            }

        } catch (Exception e) {
            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
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
     * @param context
     *            The application context so that resources can be accessed from
     *            within the parser
     * @param paths
     *            A list containing all the paths in the file as Edge objects
     * @modifies paths
     * @effects fills paths with a list of Edges connecting two points via a
     *          weighted path
     */
    public static void parsePaths(Context context, List<Edge<CampusPoint, Double>> paths) {
        BufferedReader reader = null;
        try  {
            reader = new BufferedReader(new
                    InputStreamReader(context.getResources().openRawResource(R.raw.campus_paths)));
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
        } catch (Exception e) {
            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    }

    /**
     * @param str
     *            The coordinates as a whole string separated by a single comma and
     *            no spaces<br>
     *            E.g. 123.45,678.91
     * @return a CampusPoint representing the string with x as the first coordinate
     *         and y as the second coordinate
     */
    private static CampusPoint toPoint(String str) {
        String[] coordinates = str.split(",");
        return new CampusPoint(new Double(coordinates[0]) * 0.22,
                new Double(coordinates[1]) * 0.22);
    }
}
