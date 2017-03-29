/*
 *  Created by Jonathan Fiorentini 2017.
 * Each line should be prefixed with  * 
 */
package chess_engine;

/**
 *
 * @author jonathan
 */
public class BoardException extends Exception {

    BoardException() {
        super();
    }

    BoardException(String message) {
        super(message);
    }
}
