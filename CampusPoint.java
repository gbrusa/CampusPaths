package hw8;

/**
 * <b>CampusPoint</b> represents the pixel coordinates of buildings in the
 * Campus dataset. Note that Pixel (0,0) will be in the upper-left corner of the
 * 'plane' and increasing x values will move right while increasing y values
 * will move down.
 */
public final class CampusPoint implements Comparable<CampusPoint> {

	/** Stores the x-coordinate of the point */
	private final double x;

	/** Stores the y-coordinate of the point */
	private final double y;

	// Abstraction Function:
	// The pixel point is represented by x and y coordinates,
	// where x is the measure of how far right the pixel is with
	// respect to Pixel (0,0) and y is the measure of how far down
	// the pixel is with respect to Pixel (0,0).
	//
	// Representation Invariant:
	// * x and y must be non-negative

	/**
	 * Constructs a new CampusPoint with x and y as its coordinates.
	 * 
	 * @param x
	 *            The x-coordinate of the point
	 * @param y
	 *            The y-coordinate of the point
	 * @requires x, y >= 0
	 */
	public CampusPoint(double x, double y) {
		this.x = x;
		this.y = y;
		checkRep();
	}

	/**
	 * @return the x-coordinate of this point
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y-coordinate of this point
	 */
	public double getY() {
		return y;
	}

	/**
	 * Calculates the direction of travel should one travel from this point to the
	 * end point
	 * 
	 * @param end
	 *            The end point of the trajectory
	 * @requires end != null
	 * @return A string representation of the direction of the trajectory between
	 *         this and end
	 */
	public String getDirection(CampusPoint end) {
		double xDiff = end.x - this.x;
		double yDiff = -(end.y - this.y);

		double theta = Math.atan2(yDiff, xDiff);

		double increment = Math.PI / 8;

		if (theta >= -1 * increment && theta <= increment) {
			return "E";
		} else if (theta > increment && theta < 3 * increment) {
			return "NE";
		} else if (theta >= 3 * increment && theta <= 5 * increment) {
			return "N";
		} else if (theta > 5 * increment && theta < 7 * increment) {
			return "NW";
		} else if ((theta >= 7 * increment && theta <= Math.PI) ||
				(theta >= -1 * Math.PI && theta <= -7 * increment)) {
			return "W";
		} else if (theta > -7 * increment && theta < -5 * increment) {
			return "SW";
		} else if (theta >= -5 * increment && theta <= -3 * increment) {
			return "S";
		} // if (theta > -3 * increment && theta < -1 * increment)
		return "SE";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CampusPoint) {
			CampusPoint cp = (CampusPoint) obj;
			return (this.x == cp.x && this.y == cp.y);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) ((x * 3) + (y * 5));
	}

	@Override
	public int compareTo(CampusPoint o) {
		// Takes differences of each coordinate
		double xDelta = this.x - o.x;
		double yDelta = this.y - o.y;

		// Compares the differences starting with x. If x coordinates
		// are the same then it compares y coordinates
		if (xDelta > 0 || (xDelta == 0 && yDelta > 0)) {
			return 1;
		} else if (xDelta < 0 || (xDelta == 0 && yDelta < 0)) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * Checks that the Representation Invariant holds
	 */
	private void checkRep() {
		assert (x >= 0) : "x cannot be negative";
		assert (y >= 0) : "y cannot be negative";
	}
}
