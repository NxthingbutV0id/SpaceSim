/* Class: Main
 * Author: Christian Torres
 * Created: 2023/3/13
 * Modified:
 *
 * Purpose: Start point for the program
 *
 * Attributes:
 *
 * Methods: +_main(String[]): void - Start of program
 */
package main;

import main.simulation.SimulationManager;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {Application.launch(SimulationManager.class, args);}
}
