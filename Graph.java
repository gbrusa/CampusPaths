package hw5;

import java.util.*;

/**
 * <b>Graph<N,E></b> represents a generic mutable graph with nodes of type N and
 * edges of type E connecting them.<br>
 * Each edge is unidirectional so an edge from n1 to n2 is not equivalent to an
 * edge from n2 to n1.<br>
 * Each graph cannot store duplicate nodes. It does however support edges from
 * a node to itself and multiple, duplicate edges between nodes.
 * <p>
 * 
 * @param <N>
 * 			The object type of each node in the graph. N must extend Comparable
 * @param <E>
 * 			The object type of each node in the graph. E must extend Comparable
 */

public class Graph<N extends Comparable<N>, E extends Comparable<E>> {

	/** Stores all the nodes in the graph */
	private Map<N, GraphNode> nodes;

	// Abstraction function:
	// Graph g, represents a graph with nodes stored in 'nodes'.
	//
	// Representation Invariant for every Graph g:
	// * The nodes field must point to a useable object
	// * Nodes that are added to the graph cannot be null

	/** A boolean constant used to reduce checkRep time if not in testing mode */
	private static final boolean TESTING_MODE = false;

	/**
	 * @effects Constructs a new empty graph.
	 */
	public Graph() {
		nodes = new HashMap<N, GraphNode>();
		checkRep();
	}

	/**
	 * Adds node to the graph
	 * 
	 * @param node
	 *            The node that is being added
	 * @requires node != null
	 * @modifies this
	 * @effects adds a node to the graph
	 * @return true if node was added to graph, false otherwise i.e. node already
	 * 			exists
	 */
	public boolean addNode(N node) {
		if (containsNode(node)) {
			return false;
		}
		nodes.put(node, new GraphNode(node));
		checkRep();
		return true;
	}

	/**
	 * Removes node from the graph
	 * 
	 * @param node
	 *            The node that is being removed
	 * @requires node != null
	 * @modifies this
	 * @effects removes node from the graph
	 * @return true if removed, false otherwise i.e. node not in graph
	 */
	public boolean removeNode(N node) {
		if (!containsNode(node)) {
			return false;
		}
		nodes.remove(node);
		return true;
	}

	/**
	 * Adds an edge to the graph. Adds parent and child if they are not already
	 * present.
	 * 
	 * @param parent
	 *            The node from which the edge originates
	 * @param child
	 *            The node at which the edge terminates
	 * @param edgeLabel
	 *            The label for the edge that is being added to the graph
	 * @requires parent, child, edgeLabel != null && graph.contains(parent)
	 * 			 && graph.contains(child)
	 * @modifies this
	 * @effects attempts to add edge between parent and child. Returns whether
	 * 			it was successful
	 * @return true if edge was added to graph, false otherwise (for example,
	 * 			parent and child are not in the graph
	 */
	public boolean addEdge(N parent, N child, E edgeLabel) {
		boolean added = nodes.get(parent).addEdge(child, edgeLabel);
		checkRep();
		return added;
	}

	/**
	 * Removes an edge from the graph
	 * 
	 * @param parent
	 *            The node from which the edge originated
	 * @param child
	 *            The node at which the edge terminated
	 * @param edgeLabel
	 *            The label of the edge being removed
	 * @requires parent, child, edgeLabel != null
	 * @modifies this
	 * @effects removes the edge between parent and child from the graph
	 * @return true if removed, false otherwise i.e. no such edge exists
	 */
	public boolean removeEdge(N parent, N child, E edgeLabel) {
		return nodes.get(parent).removeEdge(child, edgeLabel);
	}

	/**
	 * @return a sorted list containing the string representation of all
	 * 			the nodes stored in the graph
	 */
	public List<String> listNodes() {
		List<String> lst = new ArrayList<String>();
		for (N gn : nodes.keySet()) {
			lst.add(gn.toString());
		}
		Collections.sort(lst);
		return lst;
	}

	/**
	 * Returns a list containing all the child nodes of the given node
	 * and their edges
	 * 
	 * @param node
	 *            The node that is being looked up
	 * @requires node != null
	 * @return a sorted list containing the string representation of all
	 * 			the child nodes of the given node followed by the edge that
	 * 			connects them.<br>
	 *			Example: n1(e1), n2(e2), n2(e5), n3(e23), n4(e10)<br>
	 *         	Note: If node is not in graph, returns an empty list.
	 */
	public List<String> listChildren(N node) {
		if (!containsNode(node))
			return new ArrayList<String>();
		return nodes.get(node).listChildren();
	}

	/**
	 * Returns a list containing all the child nodes of the given node
	 * 
	 * @param node
	 *            The node that is being looked up
	 * @requires node != null
	 * @return a sorted list containing all the child nodes of the given node.<br>
	 *         Example: n1, n2, n3, n4<br>
	 *         Note: If node is not in graph, returns an empty list.
	 */
	public List<N> getChildren(N node) {
		if (!containsNode(node))
			return new ArrayList<N>();
		return nodes.get(node).getChildren();
	}
	
	/**
	 * Returns a list containing all the edges between parent and child
	 * 
	 * @param parent
	 * 			The node from which the edges originate
	 * @param child
	 * 			The node at which the edges terminate
	 * @requires parent, child != null
	 * @return a sorted list containing all the edges between parent and child.
	 * 		   Returns an empty list if either parent or child are not in the graph
	 */
	public List<E> getEdges(N parent, N child) {
		if (!containsNode(parent) || !containsNode(child))
			return new ArrayList<E>();
		return nodes.get(parent).getEdges(child);
	}

	/**
	 * Checks if node is stored in the graph
	 * 
	 * @param node
	 *            The node that is being looked up
	 * @requires node != null
	 * @return true if the graph contains this node, false otherwise
	 */
	public boolean containsNode(N node) {
		return nodes.containsKey(node);
	}
	
	/**
	 * Checks if edge is stored in the graph
	 * 
	 * @param parent
	 *          The node from which the edge originates
	 * @param child
	 * 			The node at which the edge terminates
	 * @param edgeLabel
	 * 			The label of the edge
	 * @requires parent, child, edgeLabel != null
	 * @return true if the graph contains this edge, false otherwise
	 */
	public boolean containsEdge(N parent, N child, E edgeLabel) {
		return nodes.get(parent).containsEdge(child, edgeLabel);
	}
	
	/**
	 * @return true if there are no entries in this graph, false otherwise
	 */
	public boolean isEmpty() {
		return nodes.size() == 0;
	}

	/**
	 * Checks that the representation invariant holds
	 */
	private void checkRep() {
		assert (nodes != null);
		if (TESTING_MODE) {
			for (N key : nodes.keySet()) {
				assert (nodes.get(key) != null) : "null node";
			}
		}
	}

	/**
	 * Used only for testing purposes!
	 * 
	 * @return the nodes field
	 */
	public Map<N, GraphNode> getNodesForTesting() {
		return nodes;
	}

	/**
	 * <b>GraphNode</b> represents a mutable graph node which stores references to
	 * its child nodes and all edges between them.<br>
	 * Note: edges are unidirectional so an edge from n1 --> n2 is not equivalent
	 * to n2 --> n1.
	 * <p>
	 */
	private class GraphNode {
		/** Stores the nodes unique value */
		N val;

		/** Stores all the values of this node's children and all the edges between them */
		Map<N, List<E>> edges;

		// Abstraction Function:
		// GraphNode, gn, represents a single node in a graph with
		// its value stored in val and its children and all the edges
		// between this node and each child stored in edges.
		//
		// Representation Invariant:
		// * No child can have an empty list of edges
		// * val and edges cannot be null
		// * Child nodes and edges added to edges cannot be null

		/**
		 * Constructs a new empty node with the value of val
		 * 
		 * @param val
		 *            The value stored in the node
		 * @requires val != null
		 */
		GraphNode(N val) {
			this.val = val;
			edges = new HashMap<N, List<E>>();
			checkRep();
		}

		/**
		 * Adds an edge between this and child
		 * 
		 * @param child
		 *            The node at which the edge will terminate
		 * @param edgeLabel
		 *            The label for the edge
		 * @requires child, edgeLabel != null
		 * @modifies this
		 * @effects adds a new edge between this and child. Duplicates are allowed
		 * @return true if edge was added, false otherwise
		 */
		boolean addEdge(N child, E edgeLabel) {
			if (!edges.containsKey(child))
				edges.put(child, new ArrayList<E>());
			boolean added = edges.get(child).add(edgeLabel);
			checkRep();
			return added;
		}

		/**
		 * Removes an edge name edgeLabel between this and child
		 * 
		 * @param child
		 *            The node at which the edge terminated
		 * @param edgeLabel
		 *            The label for the edge
		 * @requires child, edgeLabel != null && edges.contains(child)
		 * @modifies this
		 * @effects removes an edge between this and child
		 * @return true if edge was removed, false otherwise i.e. no such edge exists
		 */
		boolean removeEdge(N child, E edgeLabel) {
			boolean removed = edges.get(child).remove(edgeLabel);
			if (edges.get(child).isEmpty())
				edges.remove(child);
			checkRep();
			return removed;
		}

		/**
		 * @return a sorted list of the string representation of the
		 * 			children of this node followed by the edge that
		 * 			connects them
		 */
		List<String> listChildren() {
			List<String> lst = new ArrayList<String>();
			for (N gn : edges.keySet()) {
				for (E e : edges.get(gn)) {
					if (e instanceof Double) {
						lst.add(gn.toString() + String.format("(%.3f)", e));
					} else {
						lst.add(gn.toString() + "(" + e.toString() + ")");
					}
				}
			}
			Collections.sort(lst);
			return lst;
		}

		/**
		 * @return a sorted list of all the children of this
		 */
		List<N> getChildren() {
			List<N> lst = new ArrayList<N>();
			for (N gn : edges.keySet()) {
				lst.add(gn);
			}
			Collections.sort(lst);
			return lst;
		}
		
		/**
		 * @param child
		 * 			The node at which each edge terminates
		 * @requires child != null
		 * @return a sorted list of edges between this and child
		 */
		List<E> getEdges(N child) {
			List<E> lst = new ArrayList<E>();
			for (E e : edges.get(child)) {
				lst.add(e);
			}
			Collections.sort(lst);
			return lst;
		}
		
		/**
		 * Checks if edge is stored in the graph
		 * 
		 * @param child
		 * 			The node at which the edge terminates
		 * @param edgeLabel
		 * 			The label of the edge
		 * @requires child, edgeLabel != null
		 * @return true if the graph contains this edge, false otherwise
		 */
		public boolean containsEdge(N child, E edgeLabel) {
			if (!edges.containsKey(child))
				return false;
			return edges.get(child).contains(edgeLabel);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Graph.GraphNode) {
				GraphNode gn = (GraphNode) obj;
				return this.val.equals(gn.val);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.val.hashCode();
		}

		/**
		 * Checks that the representation invariant holds
		 */
		private void checkRep() {
			assert (edges != null) : "edges cannot be null";
			assert (val != null) : "val cannot be null";

			if (TESTING_MODE) {
				for (N gn : edges.keySet()) {
					assert (gn != null) : "child node cannot be null";
					assert (!edges.get(gn).isEmpty()) : "cannot be a child node if share no edges";
					for (E edge : edges.get(gn)) {
						assert (edge != null) : "cannot have a null edge";
					}
				}
			}
		}
	}
}