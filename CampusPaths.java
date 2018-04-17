package hw8;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * <b>CampusPaths</b> consists of the View and Controller of a text-based UI
 * that allows the user to search for paths between buildings on the UW-Seattle
 * Campus. It returns these paths as instructions to walk a certain distance in
 * a given direction.
 *
 */
public class CampusPaths {

	/** stores a reference the possible input options */
	private static String menu = "\tr to find a route\n" + "\tb to see a list of all buildings\n" + "\tq to quit";

	/** stores a reference to the scanner that reads in user input */
	private static Scanner console = null; // not initialized yet

	public static void main(String[] args) {
		console = new Scanner(System.in); // initialized now
		printMenu();

		String input = null;
		do {
			System.out.print("Enter an option ('m' to see the menu): ");
			input = console.nextLine();
			
			if (input.startsWith("#") || input.equals(""))
				input = echo(input);

			if (input.equals("m")) {
				printMenu();
			} else if (input.equals("r")) {
				search();
			} else if (input.equals("b")) {
				print(CampusModel.pullBuildings());
				System.out.println(); // print method does not add an empty line
			} else if (!input.equals("q")) {
				unknownOption();
			}
		} while (!input.equals("q"));
	}

	/**
	 * Prints the menu of options
	 */
	private static void printMenu() {
		System.out.println("Menu: ");
		System.out.println(menu);
		System.out.println();
	}

	/**
	 * Echoes all empty lines and comments (line that begin with '#')
	 * 
	 * @param input
	 *            The user input that begins with '#' or is an empty line
	 * @return user input once it is no longer a comment or an empty line
	 */
	private static String echo(String input) {
		do {
			if (input.startsWith("#") || input.equals(""))
				System.out.println(input);
			input = console.nextLine();
		} while (input.startsWith("#") || input.equals(""));
		return input;
	}

	/**
	 * Prompts Model to begin search between two buildings on the UW campus
	 */
	private static void search() {
		String start = inputBuilding("start");
		String end = inputBuilding("end");

		// prints message if buildings are not stored in the Model
		if (!CampusModel.contains(start))
			unknownBuilding(start);
		if (!CampusModel.contains(end))
			unknownBuilding(end);

		// pulls search results from Model
		if (CampusModel.contains(start) && CampusModel.contains(end)) {
			print(CampusModel.search(start, end));
		}
		System.out.println();
	}

	/**
	 * Prints the given list to the console
	 * 
	 * @param lst
	 *            A formatted list that can be printed without further modifications
	 */
	private static void print(List<String> lst) {
		for (String str : lst) {
			System.out.println(str);
		}
	}

	/**
	 * Prompts the user for input regarding which buildings they want to find a path
	 * between
	 * 
	 * @param pos
	 *            Either "start" or "end" indicating whether this is the start or
	 *            end building in the path search
	 * @return the building given by the user
	 */
	private static String inputBuilding(String pos) {
		System.out.print("Abbreviated name of " + pos + "ing building: ");
		return console.nextLine();
	}

	/**
	 * Prints an error message if an unknown option is given
	 */
	private static void unknownOption() {
		System.out.println("Unknown option");
		System.out.println();
	}

	/**
	 * Prints an error message if a building doesn't exist
	 * 
	 * @param building
	 *            The building name that was given as input
	 */
	private static void unknownBuilding(String building) {
		System.out.println("Unknown building: " + building);
	}
}
