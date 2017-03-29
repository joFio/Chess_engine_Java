/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_engine;

import java.util.Scanner;

/**
 *
 * @author jonathan
 */
public class Chess_Engine {

    /**
     * @param args
     */
    public static void main(String[] args) {
        startGame();
    }

    // Interface
    public static void startGame() {
        Board board = new Board();
        Bitboard.outWithRulers(Bitboard.or(board.getAllPieces()));
        Scanner userInput = new Scanner(System.in);
        Boolean team = false;

        while (!board.isCheckmate() & !board.isStalemate()) {
            System.out.println("Enter the case of the piece which you would like to move");
            char[] from_char = userInput.next().toCharArray();
            int from_col = 8 - (Character.getNumericValue(from_char[0]) - 9);
            int from_row = Character.getNumericValue(from_char[1]) - 1;

            System.out.println("Enter the case to which you would like to move your piece");
            char[] to_char = userInput.next().toCharArray();
            int to_col = 8 - (Character.getNumericValue(to_char[0]) - 9);
            int to_row = Character.getNumericValue(to_char[1]) - 1;
            int from = from_col + from_row * 8;
            int to = to_col + to_row * 8;
            boolean success = true;
            try {
                board = Board.getBoardForMove(from, to, team, board);
            } catch (BoardException ex) {
                success = false;
                System.out.println(ex.getMessage());
            }
            if (success) {
                Bitboard.outWithRulers(Bitboard.or(board.getAllPieces()));
                team = !team;
            }
        }
        if (board.isStalemate()) {
            System.out.println("Stalemate");
        }
        if (board.isCheckmate()) {
            System.out.println("Checkmate");
        }
    }

}
