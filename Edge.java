package hw7;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Edge</b> represents an immutable unidirectional edge between two nodes of type N.
 * It also stores a value of type E which identifies the edge.
 * 
 * @param <N>
 * 			The object type of parent and child. N must extend Comparable
 * @param <E>
 * 			The object type of the edge label. E must extend Comparable
 */

public final class Edge<N extends Comparable<N>, E extends Comparable<E>> {
	/** Stores the label that refers to this edge */
	private final E label;
	
	/** Stores the parent node of this edge */
	private final N parent;
	
	/** stores the child node of this edge */
	private final N child;
	
	// Abstraction Function:
	// This edge is a unidirectional connection between
	// nodes.get(0) and nodes.get(1). It can be identified by label.
	// Note that this is not a unique identifier, since other edges
	// can also have the same label.
	//
	// Representation Invariant:
	// * label, parent and child cannot be null
	//
	// **** Please note that the implementation of this Edge class is completely separate from my ****
	// **** Graph implementation despite the reuse of terminology. ****
	
	/**
	 * Creates a new edge
	 * 
	 * @param label
	 * 			The label referring to the edge
	 * @param parent
	 * 			The node from which the edge originates
	 * @param child
	 * 			The node at which the edge terminates
	 */
	public Edge(E label, N parent, N child) {
		this.label = label;
		this.parent = parent;
		this.child = child;
		checkRep();
	}
	/**
	 * @return the unique label referring to this edge
	 */
	public E getLabel() {
		return label;
	}
	
	/**
	 * @return the node from which this edge originates
	 */
	public N getParent() {
		return parent;
	}
	/**
	 * @return the node at which this edge terminates
	 */
	public N getChild() {
		return child;
	}
	
	/** 
	 * Checks that the representation invariant holds
	 */
	private void checkRep() {
		assert (label != null) : "val is null";
		assert (parent != null) : "parent is null";
		assert (child != null) : "child is null";
	}
}
