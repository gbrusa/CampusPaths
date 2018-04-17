package hw7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import hw5.Graph;

/**
 * <b>Dijkstra</b> is a class containing only static methods that allows one to
 * carry out Dijkstra's algorithm on generic graphs.<br>
 * This algorithm requires that the Graph has edge labels of type Double
 * otherwise the algorithm will not be able to accurately return the least-cost
 * path.
 */

public class Dijkstra {

	// Not an ADT

	/**
	 * Creates a new Graph with edges weighted as the multiplicative inverse of the
	 * number of edges between a parent and child node.<br>
	 * E.g. n1 and n2 have 5 edges between them. Therefore the returned Graph has 1
	 * edge between n1 and n2 which is weighted at 1/5 (the multiplicative inverse
	 * of 5).<br>
	 * Note: regardless of how many times a node has an edge to itself, the weight
	 * will always be zero.
	 * 
	 * @param nodes
	 *            A set of all the nodes to be added to the graph
	 * @param edges
	 *            A map containing the number of edges between a parent and child
	 *            node
	 * @requires nodes, edges != null
	 * @return a new Graph with edges of type Double, where edges are weighted as
	 *         the multiplicative inverse of the number of edges they would
	 *         originally have.
	 */
	public static <N extends Comparable<N>, E extends Comparable<E>> Graph<N, Double>
			createGraph(Set<N> nodes, Map<E, List<N>> edges) {
		// formats the data so that it is easier to use
		Map<List<N>, Integer> newEdges = formatData(edges);
		Graph<N, Double> g = new Graph<N, Double>();

		// adds each node to graph
		for (N node : nodes) {
			g.addNode(node);
		}

		// adds each edge to the graph
		for (List<N> lst : newEdges.keySet()) {
			g.addEdge(lst.get(0), lst.get(1), 1.0 / newEdges.get(lst));
		}
		return g;
	}

	/**
	 * Implements Dijkstra's algorithm to find the least-cost path between two nodes
	 * and returns this path as a list of edges.
	 * 
	 * @param start
	 *            The node from which the search begins
	 * @param dest
	 *            The node at which the search ends
	 * @param graph
	 *            The graph in which the search is taking place. Although nodes can
	 *            be of any type, the edges must be of type double or else the
	 *            algorithm will not work effectively
	 * @requires start, dest, graph != null
	 * @return a list of edges representing the least-cost path between start and
	 *         dest.<br>
	 *         Returns an empty list if start == dest.<br>
	 *         Returns null if no path exists between start and dest.
	 */
	public static <N extends Comparable<N>> List<Edge<N, Double>> search(N start, N dest, Graph<N, Double> graph) {
		// initializes variables necessary to carry out Dijkstra's algorithm
		PriorityQueue<Path<N>> active = new PriorityQueue<Path<N>>();
		Set<N> finished = new HashSet<N>();

		// adds start node to the queue, indicated by an empty path and a cost of 0.0
		active.add(new Path<N>(0.0, new ArrayList<Edge<N, Double>>()));

		while (!active.isEmpty()) {
			// allows us to access the cumulative cost of minPath later in algorithm
			Path<N> minPathWrapper = active.remove();

			// unwraps the current minimum path
			List<Edge<N, Double>> minPath = minPathWrapper.path;

			// if there are no entries in minPath it means it must be the path from start to
			// itself, therefore we must have this special case
			N minDest;
			if (minPath.size() == 0) {
				minDest = start;
			} else {
				minDest = minPath.get(minPath.size() - 1).getChild();
			}

			// if this is what we were looking for we can end here
			if (minDest.equals(dest)) {
				return minPath;
			}

			// if not, then we make sure we haven't already visited this node
			if (!finished.contains(minDest)) {
				finished.add(minDest);

				// cycle through all the children and check if each child has yet to be visited
				for (N child : graph.getChildren(minDest)) {
					if (!finished.contains(child)) {

						// copies all the values into a newPath because of Java reference semantics
						List<Edge<N, Double>> newPath = new ArrayList<Edge<N, Double>>();
						for (Edge<N, Double> e : minPath) {
							newPath.add(e);
						}

						// adds this newPath into the queue so it can be check against the others
						double edgeCost = graph.getEdges(minDest, child).get(0);
						active.add(minPathWrapper.add(new Edge<N, Double>(new Double(edgeCost), minDest, child)));
					}
				}
			}
		}
		// this means that no path exists between start and dest
		return null;
	}

	/**
	 * @param edges
	 *            A map storing all the connections between nodes
	 * @requires edges != null;
	 * @return a map containing pairs of nodes as keys and the number of edges
	 *         between these nodes as a value. Note that the edges are
	 *         unidirectional so n1 --> n2 will have the same value as n2 --> n1
	 *         despite having a different key.
	 */
	private static <N extends Comparable<N>, E extends Comparable<E>> Map<List<N>, Integer> formatData(
			Map<E, List<N>> edges) {
		Map<List<N>, Integer> result = new HashMap<List<N>, Integer>();
		for (E edge : edges.keySet()) {
			List<N> nodes = edges.get(edge);
			for (N n1 : nodes) {
				for (N n2 : nodes) {
					if (!n1.equals(n2)) {
						List<N> e = new ArrayList<N>();
						e.add(n1);
						e.add(n2);
						if (!result.containsKey(e)) {
							result.put(e, 0);
						}
						result.put(e, result.get(e) + 1);
					}
				}
			}
		}
		return result;
	}

	/**
	 * <b>Path</b> is a private class that stores a path between nodes as successive
	 * edges and the cost to get from the start to the end.<br>
	 * It is necessary for Dijkstra's algorithm since it allows a more efficient
	 * method to store these values in a PriorityQueue without having to delve into
	 * Maps which can return things in the wrong order since sets have their own
	 * order.
	 * 
	 * @param <N>
	 *            Represents the type of node that is being stored in the edges of
	 *            path
	 */
	private static class Path<N extends Comparable<N>> implements Comparable<Path<N>> {
		/** stores the sum of all the edges in path */
		final double val;

		/** stores all the edges that make up a path from one node to another */
		final List<Edge<N, Double>> path;

		// Abstraction Function:
		// A wrapper class that contains a list of edges, path, as well as a
		// reference to the sum of all the edges in path, val.
		//
		// Representation Invariant:
		// * val is non-negative
		// * path is not null
		// * path cannot contain a null edge

		/** A boolean constant used to reduce checkRep time if not in testing mode */
		private static final boolean TESTING_MODE = true;

		/**
		 * Constructs a new Path object with a specified val and path
		 * 
		 * @param val
		 *            The sum of each edge in path
		 * @param path
		 *            A list of edges which forms a path
		 * @requires path != null && val >= 0
		 */
		Path(double val, List<Edge<N, Double>> path) {
			this.val = val;
			this.path = new ArrayList<Edge<N, Double>>();
			for (Edge<N, Double> e : path) {
				this.path.add(e);
			}
			checkRep();
		}

		/**
		 * Returns a new list representing this + e
		 * 
		 * @param e
		 *            The edge that is being added
		 * @requires e != null
		 * @return a new list which is a copy of this but has e added to the end.
		 */
		Path<N> add(Edge<N, Double> e) {
			List<Edge<N, Double>> newPath = new ArrayList<Edge<N, Double>>();
			for (Edge<N, Double> edge : path) {
				newPath.add(edge);
			}
			newPath.add(e);
			return new Path<N>(val + e.getLabel(), newPath);
		}

		@Override
		public int compareTo(Path<N> other) {
			double delta = this.val - other.val;
			if (delta > 0) {
				return 1;
			} else if (delta < 0) {
				return -1;
			}
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Path) {
				Path<N> other = (Path<N>) obj;
				return compareTo(other) == 0;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return (int) (val * 123);
		}

		/**
		 * Checks that the representation invariant holds
		 */
		private void checkRep() {
			assert (val >= 0) : "a path cannot have a negative value";
			assert (path != null) : "path is null!";
			if (TESTING_MODE) {
				for (Edge<N, Double> e : path) {
					assert (e != null) : "path contains a null edge";
				}
			}
		}
	}
}