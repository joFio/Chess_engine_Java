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
        Bitboard.outWithChequerBoard(board);
        Scanner userInput = new Scanner(System.in);
        Boolean team = false;

        while (!board.isCheckmate() & !board.isStalemate()) {
            System.out.println("Enter cases:");
            char[] coordinates = userInput.next().toCharArray();            
            while ((coordinates.length != 4)) {
                System.out.println("Enter cases:");
                coordinates = userInput.next().toCharArray();
            }          
            int from_col = 8 - (Character.getNumericValue(coordinates[0]) - 9);
            int from_row = Character.getNumericValue(coordinates[1]) - 1;
            int to_col = 8 - (Character.getNumericValue(coordinates[2]) - 9);
            int to_row = Character.getNumericValue(coordinates[3]) - 1;
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
                Bitboard.outWithChequerBoard(board);
                team = !team;
                if(board.isCheck()){
                    System.out.println("Check");
                }
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
