/*
 *  Created by Jonathan Fiorentini 2017.
 * Each line should be prefixed with  * 
 */
package chess_engine;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 *
 * @author jonathan
 */
public class Board {

    private List<Piece> pieces;
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;

    private Piece whiteKing;
    private Piece blackKing;

    /**
     * The fields below can be populated again when pieces are promoted ->
     * change of PieceType. We use fields for each piece category to improve
     * efficiency when accessing them.
     */
    private boolean teamPlay;
    private List<Piece> whitePawns;
    private List<Piece> whiteRooks;
    private List<Piece> whiteBishops;
    private List<Piece> whiteKnights;
    private List<Piece> whiteQueens;
    private List<Piece> blackPawns;
    private List<Piece> blackRooks;
    private List<Piece> blackKnights;
    private List<Piece> blackBishops;
    private List<Piece> blackQueens;

    Board() {
        whiteKing = new Piece(SetupConstants.WHITE_KING, PieceType.KING, false);
        pieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        whitePawns = new ArrayList<>();
        whiteRooks = new ArrayList<>();
        whiteKnights = new ArrayList<>();
        whiteBishops = new ArrayList<>();
        whiteQueens = new ArrayList<>();
        for (int i = 0; i < SetupConstants.WHITE_PAWNS.length; i++) {
            whitePawns.add(new Piece(SetupConstants.WHITE_PAWNS[i], PieceType.PAWN, false));
        }
        whiteRooks.add(new Piece(SetupConstants.WHITE_ROOK_QUEEN, PieceType.ROOK, false));
        whiteRooks.add(new Piece(SetupConstants.WHITE_ROOK_KING, PieceType.ROOK, false));
        whiteKnights.add(new Piece(SetupConstants.WHITE_KNIGHT_QUEEN, PieceType.KNIGHT, false));
        whiteKnights.add(new Piece(SetupConstants.WHITE_KNIGHT_KING, PieceType.KNIGHT, false));
        whiteBishops.add(new Piece(SetupConstants.WHITE_BISHOP_QUEEN, PieceType.BISHOP, false));
        whiteBishops.add(new Piece(SetupConstants.WHITE_BISHOP_KING, PieceType.BISHOP, false));
        whiteQueens.add(new Piece(SetupConstants.WHITE_QUEEN, PieceType.QUEEN, false));

        whitePieces.addAll(whiteRooks);
        whitePieces.addAll(whiteKnights);
        whitePieces.addAll(whiteBishops);
        whitePieces.addAll(whiteQueens);
        whitePieces.addAll(whitePawns);

        blackKing = new Piece(SetupConstants.BLACK_KING, PieceType.KING, true);
        blackPieces = new ArrayList<>();
        blackPawns = new ArrayList<>();
        blackRooks = new ArrayList<>();
        blackKnights = new ArrayList<>();
        blackBishops = new ArrayList<>();
        blackQueens = new ArrayList<>();
        for (int i = 0; i < SetupConstants.BLACK_PAWNS.length; i++) {
            blackPawns.add(new Piece(SetupConstants.BLACK_PAWNS[i], PieceType.PAWN, true));
        }
        blackRooks.add(new Piece(SetupConstants.BLACK_ROOK_QUEEN, PieceType.ROOK, true));
        blackRooks.add(new Piece(SetupConstants.BLACK_ROOK_KING, PieceType.ROOK, true));
        blackKnights.add(new Piece(SetupConstants.BLACK_KNIGHT_QUEEN, PieceType.KNIGHT, true));
        blackKnights.add(new Piece(SetupConstants.BLACK_KNIGHT_KING, PieceType.KNIGHT, true));
        blackBishops.add(new Piece(SetupConstants.BLACK_BISHOP_QUEEN, PieceType.BISHOP, true));
        blackBishops.add(new Piece(SetupConstants.BLACK_BISHOP_KING, PieceType.BISHOP, true));
        blackQueens.add(new Piece(SetupConstants.BLACK_QUEEN, PieceType.QUEEN, true));
        blackPieces.addAll(blackPawns);
        blackPieces.addAll(blackRooks);
        blackPieces.addAll(blackKnights);
        blackPieces.addAll(blackBishops);
        blackPieces.addAll(blackQueens);
        pieces.addAll(whitePieces);
        pieces.addAll(blackPieces);
    }

    /**
     * This function refreshes the board by populating the fields again. To be
     * used when promoting pieces
     */
    public void refresh() {
        whitePawns.clear();
        blackPawns.clear();
        whiteRooks.clear();
        blackRooks.clear();
        whiteKnights.clear();
        blackKnights.clear();
        whiteBishops.clear();
        blackBishops.clear();
        whiteQueens.clear();
        blackQueens.clear();
        for (Piece piece : whitePieces) {
            switch (piece.getType()) {
                case PAWN:
                    whitePawns.add(piece);
                    break;
                case ROOK:
                    whiteRooks.add(piece);
                    break;
                case KNIGHT:
                    whiteKnights.add(piece);
                    break;
                case BISHOP:
                    whiteBishops.add(piece);
                    break;
                case QUEEN:
                    whiteQueens.add(piece);
                    break;
            }
        }
        for (Piece piece : blackPieces) {
            switch (piece.getType()) {
                case PAWN:
                    blackPawns.add(piece);
                    break;
                case ROOK:
                    blackRooks.add(piece);
                    break;
                case KNIGHT:
                    blackKnights.add(piece);
                    break;
                case BISHOP:
                    blackBishops.add(piece);
                    break;
                case QUEEN:
                    blackQueens.add(piece);
                    break;
            }
        }
    }

    public Boolean getTeamPlay() {
        return teamPlay;
    }

    public List<Piece> getAllPieces() {
        return pieces;
    }

    public List<Piece> getTeamPieces(Boolean team) {
        if (team) {
            return blackPieces;
        } else {
            return whitePieces;
        }
    }

    public List<Piece> getAdversaryPieces(Boolean team) {
        if (team) {
            return whitePieces;
        } else {
            return blackPieces;
        }
    }

    public List<Piece> getTeamRooks(Boolean team) {
        if (team) {
            return blackRooks;
        } else {
            return whiteRooks;
        }
    }

    public List<Piece> getAdversaryRooks(Boolean team) {
        if (team) {
            return whiteRooks;
        } else {
            return blackRooks;
        }
    }

    public List<Piece> getTeamPawns(Boolean team) {
        if (team) {
            return blackPawns;
        } else {
            return whitePawns;
        }
    }

    public List<Piece> getTeamKnights(Boolean team) {
        if (team) {
            return blackKnights;
        } else {
            return whiteKnights;
        }
    }

    public List<Piece> getAdversaryKnights(Boolean team) {
        if (team) {
            return whiteKnights;
        } else {
            return blackKnights;
        }
    }

    public List<Piece> getAdversaryPawns(Boolean team) {
        if (team) {
            return whitePawns;
        } else {
            return blackPawns;
        }
    }

    public Piece getTeamKing(Boolean team) {
        if (team) {
            return blackKing;
        } else {
            return whiteKing;
        }
    }

    public Piece getAdversaryKing(Boolean team) {
        if (team) {
            return whiteKing;
        }
        return blackKing;
    }

    public void flipTurn() {
        teamPlay = !teamPlay;
    }

    public void move(int fromCase, int toCase) {
        long fromCaseBitboard = (long) Math.pow(2, fromCase);
        long toCaseBitboard = (long) Math.pow(2, toCase);
        long teamBitboard = Bitboard.or(this.getTeamPieces(this.teamPlay));
        if ((fromCaseBitboard & toCaseBitboard) != 0) {
            throw new EmptyStackException();
        }
    }

}
