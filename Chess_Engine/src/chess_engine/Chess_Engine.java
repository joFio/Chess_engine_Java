/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_engine;

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
        int position = 56;
        Piece piece = new Piece((long) Math.pow((double)2, (double)position), PieceType.KING, false);
        Bitboard.out(piece.getBitboard());
        Bitboard.out(Bitboard.getVerticalMoves(piece, 0));
        Bitboard.out(Bitboard.getHorizontalMoves(piece, 0));
        Bitboard.out(Bitboard.getDiagonalMoves(piece, 0)[0]);
        Bitboard.out(Bitboard.getDiagonalMoves(piece, 0)[1]);   
        Bitboard.out(Bitboard.getKnightMoves(piece));
        Bitboard.out(Bitboard.getKingMoves(piece));        
        
    }

}
