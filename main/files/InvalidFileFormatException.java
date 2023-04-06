/* Class: InvalidFileFormatException
 * Author: Christian Torres
 * Date: 4/6/2023
 *
 * Purpose: Custom error for when doing file IO
 *
 * Attributes:
 *
 * Methods: *Constructor*
 */
package main.files;

public class InvalidFileFormatException extends Exception {
    public InvalidFileFormatException(String errorMessage) {
        super(errorMessage);
    }
}
