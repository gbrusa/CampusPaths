package hw6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import hw5.Graph;

/**
 *  <b>MarvelPaths</b> allows a user to create a graph of strings using the MarvelParser class
 *  as well as using the BFS algorithm to find paths between nodes.
 *
 */
public class MarvelPaths {
	
	// Not an ADT
	
	public static void main(String[] args) {
		Map<String, Set<String>> books = new HashMap<String, Set<String>>();
		Set<String> characters = new HashSet<String>();
		
		Scanner console = new Scanner(System.in);
		
		System.out.print("Which file would you like to construct a file from? ");
		String fileName = console.next();
		try {
			MarvelParser.parseData("src/hw6/data/" + fileName,
					characters, books);
		} catch (Exception e) {
			System.out.println("File not found. Please end program and try again, making sure you typed that correctly.");
		}
		
		Graph<String, String> g = createGraph(characters, books);
		
		System.out.println("You can now search for paths between characters using BFS.");
		String start = getInput(console, "Start");
		String dest = getInput(console, "Destination");
		
		printResults(start, dest, g);
		
	}
	
	/**
	 * @param characters
	 * 			Set of nodes to be added to the graph
	 * @param books
	 * 			Set of the edges to be added to the graph
	 * @return a graph constructed using data from characters and books
	 */
	public static Graph<String, String> createGraph(Set<String> characters,
			Map<String, Set<String>> books) {
		Graph<String, String> g = new Graph<String, String>();
		Set<String> edges = books.keySet();
		for (String character : characters) {
			g.addNode(character);
		}
		for (String book : edges) {
			for (String char1 : books.get(book)) {
				for (String char2 : books.get(book)) {
					if (!char1.equals(char2) && !g.containsEdge(char1, char2, book)) {
						g.addEdge(char1, char2, book);
					}
				}
			}
		}
		return g;
	}
	
	/**
	 * Searches for a path from a start node to a destination
	 * node via breadth-first search (BFS).
	 * 
	 * @param start
	 * 			The start node in the search
	 * @param dest
	 * 			The destination node in the search
	 * @param graph
	 * 			The graph which is being searched
	 * @return a map with edges as the keys and a list of nodes as the values. Index 0 of each
	 * 		   list represents the parent and index 1 represents the child. The map represents
	 * 		   the path from start to dest.<br>
	 * 		   Note: returns null if no path exists.
	 */
	public static Map<String, List<String>> search(String start, String dest, Graph<String, String> graph) {
		Queue<String> nodeQueue = new LinkedList<String>();
		Map<String, Map<String, List<String>>> paths = new HashMap<String, Map<String, List<String>>>();
		
		nodeQueue.add(start);
		paths.put(start, new HashMap<String, List<String>>());
		
		while (!nodeQueue.isEmpty()) {
			String next = nodeQueue.remove();
			if (next.equals(dest)) {
				return paths.get(next);
			}
			for (String child : graph.getChildren(next)) {
				if (!paths.containsKey(child)) {
					
					// creates a copy of the path to parent of child
					Map<String, List<String>> newPath = new HashMap<String, List<String>>();
					Map<String, List<String>> edges = paths.get(next);
					for (String edge : edges.keySet()) {
						newPath.put(edge, edges.get(edge));
					}
					
					// creates a parent/child pair to add to the map of edges
					List<String> edgeValue = new ArrayList<String>();
					edgeValue.add(next);
					edgeValue.add(child);
					
					// adds the lexicographically least title as the edge and
					// previous parent/child pair as the values to show a new
					// edge in the path
					newPath.put(graph.getEdges(next, child).get(0), edgeValue);
					
					// updates paths to represent the path to child
					paths.put(child, newPath);
					nodeQueue.add(child);
				}
			}
		}
		
		// if no path exists returns null
		return null;
	}
	
	/**
	 * 
	 * @param console
	 * 			Scanner that allows the acquisition of user input
	 * @param pos
	 * 			The position of the node in respect to BFS algorithm (start or destination)
	 * @return The user input
	 */
	private static String getInput(Scanner console, String pos) {
		System.out.print(pos + " node: ");
		return console.next();
	}
	
	/**
	 * Prints out results of BFS
	 * 
	 * @param start
	 * 			The node at which the BFS begins
	 * @param dest
	 * 			The node at which the BFS will end
	 * @param g
	 * 			The graph that is being searched
	 */
	private static void printResults(String start, String dest, Graph<String, String> g) {
		if (!g.containsNode(start) || !g.containsNode(dest)) {
			// if start was not in the original dataset
			if (!g.containsNode(start)) {
				System.out.println("unknown character " + start);
			}
			// if dest was not in the original dataset
			if (!g.containsNode(dest)) {
				System.out.println("unknown character " + dest);
			}
		
		// if start and dest are the same character
		} else if (start.equals(dest)) {
			System.out.println("path from " + start + " to " + dest + ":");
		} else {
			// only do the heavy work if necessary
			Map<String, List<String>> path = MarvelPaths.search(start, dest, g);
			System.out.println("path from " + start + " to " + dest + ":");
			if (path == null) {
			System.out.println("No path found");
			} else {
				for (String edge : path.keySet()) {
					List<String> value = path.get(edge);
					value.set(0, value.get(0).replace("_", " "));
					value.set(1, value.get(1).replace("_", " "));
					System.out.println(value.get(0) + " to " + value.get(1) + " via " + edge);
				}
			}
		}
	}
}
