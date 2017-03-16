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
public class Piece {
    
    PieceType pieceType;
    boolean team; // 0 = white, black = true
    boolean moved;    
    long bitboard;
    
    private int position;
    
    Piece(long bitboard, PieceType pieceType, boolean team){
        this.pieceType = pieceType;
        this.team  = team;        
        this.bitboard = bitboard;
        this.moved = false;
        this.position = Bitboard.searchPosition(bitboard);
    
    }
    
    
    
}
