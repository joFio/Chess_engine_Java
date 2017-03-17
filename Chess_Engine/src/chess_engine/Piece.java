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

    final private PieceType pieceType;
    final private boolean team; // 0 = white, black = true

    private boolean moved;
    private long bitboard;
    private int position;

    Piece(long bitboard, PieceType pieceType, boolean team) {
        this.pieceType = pieceType;
        this.team = team;
        this.bitboard = bitboard;
        this.moved = false;
        this.position = Bitboard.searchPosition(bitboard);
    }

    public PieceType getType() {
        return this.pieceType;
    }

    public boolean getTeam() {
        return this.team;
    }

    public boolean isMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public long getBitboard() {
        return this.bitboard;
    }

    public void setBitboard(long bitboard) {
        this.bitboard = bitboard;
        this.position = Bitboard.searchPosition(bitboard);
    }

    public int getPosition() {
        return this.position;
    }
}
