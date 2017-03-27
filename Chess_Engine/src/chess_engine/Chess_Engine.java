/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author jonathan
 */
public class Chess_Engine {

    /**
     * @param the command line arguments
     *
     */
    public static void main(String[] args) {            
        Board board = new Board();
        int t = 1;                        
        Bitboard.out(board.getTeamKnights(true).get(t));
        Piece piece =  board.getTeamKnights(true).get(t);
        Bitboard.out(piece);                
        Bitboard.out(Bitboard.getMoves(piece, board));
    }

}
