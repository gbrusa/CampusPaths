package hw7;

import java.util.List;
import java.util.Map;
import java.util.Set;

import hw5.Graph;

/**
 * <b>MarvelPaths2</b> allows a user to call Dijkstra's search algorithm while
 * maintaining the formatting used by the previous MarvelPaths.<br>
 * This class is more specific than Dijkstra.java since it requires that all
 * nodes be of type String and does not implement generics.
 */
public class MarvelPaths2 {

	// Not an ADT

	/**
	 * Creates a new Graph with edges weighted as the multiplicative inverse of the
	 * number of times two characters appear.<br>
	 * E.g. char1 and char2 appear in 5 books together. Therefore the returned Graph
	 * has 1 edge between char1 and char2 which is weighted at 1/5 (the
	 * multiplicative inverse of 5).<br>
	 * Note: despite a character appearing in multiple books with itself, the weight
	 * from a character to itself will always be zero.
	 * 
	 * @param characters
	 *            A set of all the character nodes to be added to the graph
	 * @param books
	 *            A map containing all the characters that appear in specific marvel
	 *            comic books
	 * @requires characters, books != null
	 * @return a new Graph with edges of type Double, where edges are weighted as
	 *         the multiplicative inverse of the number of times two characters
	 *         appear in the same book.
	 */
	public static Graph<String, Double> createGraph(Set<String> characters, Map<String, List<String>> books) {
		return Dijkstra.createGraph(characters, books);
	}

	/**
	 * Allows the user to call Dijkstra's search algorithm to find the least-cost path
	 * between two character nodes and returns this path as a list of Edges.
	 * 
	 * @param start
	 *            The character from which the search begins
	 * @param dest
	 *            The character at which the search ends
	 * @param graph
	 *            The graph in which the search is taking place
	 * @requires start, dest, graph != null
	 * @return a list of edges representing the least-cost path between start and
	 *         dest.<br>
	 *         Returns an empty list if start == dest.<br>
	 *         Returns null if no path exists between start and dest.
	 */
	public static List<Edge<String, Double>> search(String start, String dest, Graph<String, Double> graph) {
		return Dijkstra.search(start, dest, graph);
	}
}
