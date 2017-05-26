/*
 *  Created by Jonathan Fiorentini 2017.
 * Each line should be prefixed with  * 
 */
package chess_game_state_evaluation;
import chess_engine.*;

/**
 *
 * @author jonathan
 */
public class Heuristic {               
    /**
     * This method returns the sum of the value of each piece for the current game
     * state. TODO: Implement time-varying piece value.
     * @param board
     * @return score value
     */
    public static int getScoreForMaterial(Board board){            
        int white_queens = (int) board.getTeamQueens(false).stream().filter((p)-> !p.isCaptured()).count();
        int black_queens = (int) board.getTeamQueens(true).stream().filter((p)-> !p.isCaptured()).count();
        int queen_score = (white_queens-black_queens)*PieceValueConstants.QUEEN;
        int white_bishops = (int) board.getTeamQueens(false).stream().filter((p)-> !p.isCaptured()).count();
        int black_bishops = (int) board.getTeamQueens(true).stream().filter((p)-> !p.isCaptured()).count();
        int white_bishop_score = (white_bishops == 2) ? (white_bishops*PieceValueConstants.BISHOP + PieceValueConstants.BISHOP_PAIR_BONUS) : white_bishops*PieceValueConstants.BISHOP;
        int black_bishop_score = (black_bishops == 2) ? (black_bishops*PieceValueConstants.BISHOP + PieceValueConstants.BISHOP_PAIR_BONUS) : black_bishops*PieceValueConstants.BISHOP;
        int bishop_score = (white_bishop_score-black_bishop_score);
        int white_knights = (int) board.getTeamKnights(false).stream().filter((p)-> !p.isCaptured()).count();
        int black_knights = (int) board.getTeamKnights(true).stream().filter((p)-> !p.isCaptured()).count();
        int knight_score = (white_knights-black_knights)*PieceValueConstants.KNIGHT;
        int white_rooks = (int) board.getTeamRooks(false).stream().filter((p)-> !p.isCaptured()).count();
        int black_rooks = (int) board.getTeamRooks(true).stream().filter((p)-> !p.isCaptured()).count();
        int rook_score = (white_rooks-black_rooks)*PieceValueConstants.ROOK;
        int white_pawns = (int) board.getTeamPawns(false).stream().filter((p)-> !p.isCaptured()).count();
        int black_pawns = (int) board.getTeamPawns(true).stream().filter((p)-> !p.isCaptured()).count();
        int pawn_score = (white_pawns-black_pawns)*PieceValueConstants.PAWN;        
        return queen_score + bishop_score + knight_score + rook_score + pawn_score;
    }       
    public static int getScoreForPosition(Board board){   
        
        int white_queens = (int) board.getTeamQueens(false).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.QUEEN_SQUARE[p.getPosition()]).mapToInt(Integer::valueOf).sum();                
        int black_queens = ((int) board.getTeamQueens(true).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.QUEEN_SQUARE[63-p.getPosition()]).mapToInt(Integer::valueOf).sum());
        
        int white_bishops = ((int) board.getTeamBishops(false).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.BISHOP_SQUARE[p.getPosition()]).mapToInt(Integer::valueOf).sum());
        int black_bishops = ((int) board.getTeamBishops(true).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.BISHOP_SQUARE[63-p.getPosition()]).mapToInt(Integer::valueOf).sum());
        
        int white_knights = ((int) board.getTeamKnights(false).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.KNIGHT_SQUARE[p.getPosition()]).mapToInt(Integer::valueOf).sum());
        int black_knights = ((int) board.getTeamKnights(true).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.KNIGHT_SQUARE[63-p.getPosition()]).mapToInt(Integer::valueOf).sum());
        
        int white_rooks = ((int) board.getTeamRooks(false).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.ROOK_SQUARE[p.getPosition()]).mapToInt(Integer::valueOf).sum());
        int black_rooks = ((int) board.getTeamRooks(true).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.ROOK_SQUARE[63-p.getPosition()]).mapToInt(Integer::valueOf).sum());
        
        int white_pawns = ((int) board.getTeamPawns(false).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.PAWN_SQUARE[p.getPosition()]).mapToInt(Integer::valueOf).sum());
        int black_pawns = ((int) board.getTeamPawns(true).stream().filter((p)-> !p.isCaptured()).map(p -> PieceValueConstants.PAWN_SQUARE[63-p.getPosition()]).mapToInt(Integer::valueOf).sum());
                       
        int white_king = (int) PieceValueConstants.KING_SQUARE[board.getTeamKing(false).getPosition()];
        int black_king = (int) PieceValueConstants.KING_SQUARE[63-board.getTeamKing(true).getPosition()];
        int score = (white_queens-black_queens) + (white_bishops-black_bishops) +(white_knights-black_knights) + (white_rooks-black_rooks) + (white_pawns-black_pawns) + (white_king-black_king);        
        return score;
        
    }
}
