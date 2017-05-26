/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_engine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This set of methods perform operations on bitboards. Bitboards are 64-bit
 * integers (long) containing the position of the pieces on the board.
 *
 * @author jonathan
 */
public class Bitboard {

    /**
     * This method prints the binary in a checkerboard-like manner.
     *
     * @param number This is the binary corresponding to the checkerboard.
     */
    public static void out(long number) {
        String bs = Long.toBinaryString(number);
        int count = 64 - bs.length();
        String repeated = new String(new char[count]).replace("\0", "0");
        String binary = repeated.concat(bs);
        for (int i = 0; i < 8; ++i) {
            String row = binary.substring(i * 8, ((i + 1) * 8));
            System.out.println(row);
        }
        System.out.println("");
    }

    public static void outWithChequerBoard(Board board) {
        String[] boardString = new String[64];
        List<Piece> pieces = board.getAllPieces().stream().filter((p) -> !p.isCaptured()).collect(Collectors.toList());
        for (Piece piece : pieces) {
            int position = piece.getPosition();
            String symbol = "";
            switch (piece.getType()) {
                case PAWN:
                    symbol = symbol + " P";
                    break;
                case ROOK:
                    symbol = symbol + " R";
                    break;
                case KNIGHT:
                    symbol = symbol + " N";
                    break;
                case BISHOP:
                    symbol = symbol + " B";
                    break;
                case QUEEN:
                    symbol = symbol + " Q";
                    break;
                case KING:
                    symbol = symbol + " K";
                    break;
            }
            if (piece.getTeam()) {
                symbol = symbol + "+";
            } else {
                symbol = symbol + "-";
            }
            boardString[position] = symbol;

        }
        for (int i = (boardString.length - 1); i >= 0; i--) {

            if (((i + 1) % 8) == 0) {
                System.out.print(((i + 1) / 8) + " | ");
            }
            if (boardString[i] != null) {
                System.out.print(boardString[i]);
            } else {
                System.out.print(" O ");
            }
            if (((i) % 8) == 0) {
                System.out.println("");
            }
        }
        System.out.println("     -  -  -  -  -  -  -  -");
        System.out.println("     A  B  C  D  E  F  G  H");
        System.out.println("");

    }

    public static void outWithRulers(long number) {
        String bs = Long.toBinaryString(number);
        int count = 64 - bs.length();
        String repeated = new String(new char[count]).replace("\0", "0");
        String binary = repeated.concat(bs);
        for (int i = 0; i < 8; ++i) {
            String row = binary.substring(i * 8, ((i + 1) * 8));
            System.out.print((8 - i) + " | ");
            System.out.println(row);
        }

        System.out.println("    --------");
        System.out.println("    ABCDEFGH");
        System.out.println("");
    }

    public static void out(Piece piece) {
        String bs = Long.toBinaryString(piece.getBitboard());
        int count = 64 - bs.length();
        String repeated = new String(new char[count]).replace("\0", "0");
        String binary = repeated.concat(bs);
        for (int i = 0; i < 8; ++i) {
            String row = binary.substring(i * 8, ((i + 1) * 8));
            System.out.println(row);
        }
        System.out.println("");
    }

    /**
     * This method looks for the position of the piece in the board based on the
     * binary.
     *
     * @param bitboard This is the binary (long) representing the position.
     * @return The number associated with the checkerboard case (0 to 63).
     * Numbers go from right to left and bottom to top.
     */
    //TODO throw exception if more than one bit is on
    public static int searchPosition(long bitboard) {
        if (bitboard == Long.MIN_VALUE) {
            return 63;
        }
        if (bitboard == 0L) {
            return 0;
        }
        int bound = 32;
        int scope = bound;
        boolean stop = false;
        while (stop == false) {
            scope = scope / 2;
            if (bitboard == Math.pow((double) 2, (double) bound)) {
                stop = true;
            } else if (bitboard > Math.pow((double) 2, (double) bound)) {
                bound = bound + scope;
            } else {
                bound = bound - scope;
                if (scope == 0) {
                    bound = bound - 1;
                }
            }
            if (scope == 0) {
                stop = true;
            }
        }
        return bound;
    }

    public static boolean isCheck(Board board, Boolean team) {
        List<Piece> adversaryPieces = board.getAdversaryPieces(team);
        Piece teamKing = board.getTeamKing(team);
        return adversaryPieces.stream().map((piece) -> Bitboard.getLegalMoves(piece, board)).anyMatch((threatBitboard) -> ((threatBitboard & teamKing.getBitboard()) != 0));
    }

    public static boolean isMate(Board board, Boolean team) {
        List<Piece> teamPieces = board.getTeamPieces(team);
        long moves = 0L;
        for (Piece piece : teamPieces) {
            long pieceMove = (Bitboard.getLegalMoves(piece, board)) ^ piece.getBitboard();
            moves = moves | pieceMove;
        }
        return moves == 0;
    }

    // Check if component include piece. Do not include mask in return value -> create other method that compiles
    /**
     * Applies "or" operator to all components of an array of long
     *
     * @param bitboards
     * @return
     */
    public static long or(long[] bitboards) {
        int k = bitboards.length;
        long mask = 0L;
        for (int i = 0; i < k; i++) {
            mask = mask | bitboards[i];
        }
        return mask;
    }

    public static long or(List<Piece> pieces) {
        long mask = 0L;
        for (Piece piece : pieces) {
            mask = mask | piece.getBitboard();
        }
        return mask;
    }

    public static long and(long[] bitboards) {
        int k = bitboards.length;
        long mask = 0L;
        for (int i = 0; i < k; i++) {
            mask = mask & bitboards[i];
        }
        return mask;
    }

    public static long and(List<Piece> pieces) {
        long mask = 0L;
        for (Piece piece : pieces) {
            mask = mask & piece.getBitboard();
        }
        return mask;
    }

    public static long getLegalMoves(Piece piece, Board board) {
        Boolean team = piece.getTeam();
        // Remove captured pieces from Lists
        List<Piece> teamPieces = board.getTeamPieces(team).stream().filter((p) -> p.isCaptured() == false).collect(Collectors.toList());
        List<Piece> adversaryPieces = board.getAdversaryPieces(team).stream().filter((p) -> p.isCaptured() == false).collect(Collectors.toList());
        List<Piece> teamRooks = board.getTeamRooks(team).stream().filter((p) -> p.isCaptured() == false).collect(Collectors.toList());
        List<Piece> adversaryRooks = board.getAdversaryRooks(team).stream().filter((p) -> p.isCaptured() == false).collect(Collectors.toList());
        List<Piece> teamPawns = board.getTeamPawns(team).stream().filter((p) -> p.isCaptured() == false).collect(Collectors.toList());
        Piece teamKing = board.getTeamKing(team);

        long pieceBitboard = piece.getBitboard();
        long teamBitboard = Bitboard.or(teamPieces);
        long adversaryBitboard = Bitboard.or(adversaryPieces);
        long teamPawnsBitboard = Bitboard.or(teamPawns);
        long adversaryPawnsBitboard = 0L;
        if (board.getEnPassantPawn() != null) {
            adversaryPawnsBitboard = board.getEnPassantPawn().getBitboard();
        }
        long[] splitMoves = Bitboard.getMoves(piece, teamBitboard, adversaryBitboard, adversaryPawnsBitboard, teamRooks, false);
        long moves = Bitboard.or(splitMoves);
        long teamBitboardWithoutPiece = pieceBitboard ^ teamBitboard;
        for (Piece pce : adversaryPieces) {
            if (piece.getType() == PieceType.KING) {
                long adversaryBitboardWithoutKingAttack = ((~moves) & adversaryBitboard);
                long teamBitboardWithoutKingWithKingAttack = teamBitboardWithoutPiece | (moves & adversaryBitboard);
                long[] splitThreatBitboard = Bitboard.getMoves(pce, adversaryBitboardWithoutKingAttack, teamBitboardWithoutKingWithKingAttack, teamPawnsBitboard, adversaryRooks, true);
                long threatBitboard = Bitboard.or(splitThreatBitboard);
                moves = moves & ~(threatBitboard ^ pce.getBitboard());
                if (!piece.isMoved()) {
                    for (int i = 0; i < splitThreatBitboard.length; i++) {
                        long queenSideCheckMask = pieceBitboard << 1;
                        long kingSideCheckMask = pieceBitboard >> 1;
                        if ((splitThreatBitboard[i] & queenSideCheckMask) != 0) {
                            moves = moves ^ (pieceBitboard << 2);
                            break;
                        }
                        if ((splitThreatBitboard[i] & kingSideCheckMask) != 0) {
                            moves = moves ^ (pieceBitboard >> 2);
                            moves = moves ^ (pieceBitboard << 2);
                            break;
                        }

                    }
                }
            } else {
                long[] splitThreatBitboard = Bitboard.getMoves(pce, adversaryBitboard, teamBitboardWithoutPiece, teamPawnsBitboard, adversaryRooks, true);
                long threatBitboard = Bitboard.or(splitThreatBitboard);
                if ((threatBitboard & teamKing.getBitboard()) != 0) {
                    for (int i = 0; i < splitThreatBitboard.length; i++) {
                        if ((splitThreatBitboard[i] & teamKing.getBitboard()) != 0) {
                            moves = moves & splitThreatBitboard[i];
                        }
                    }
                }

            }
        }
        return moves;
    }

    /**
     * This method returns the bitboard containing all the possible vertical
     * moves for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @param bitboards This is the bitboard constraints, i.e. the bitboard
     * representing the cases which the piece cannot move to.
     * @return
     */
    private static long getVerticalMoves(Piece piece, long bitboards) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        long mask = bitboard;
        int rowdec = row;
        int rowinc = row;
        while (rowdec > 0) {
            rowdec--;
            int offset = 8 * (row - rowdec);
            long bit = bitboard >> offset;
            mask = mask | (bit);
            if ((bit & bitboards) != 0) {
                break;
            }
        }
        while (rowinc < 7) {
            rowinc++;
            int offset = 8 * (rowinc - row);
            long bit = bitboard << offset;
            mask = mask | (bit);
            if ((bit & bitboards) != 0) {
                break;
            }
        }
        return mask;
    }

    /**
     * This method returns the bitboard containing all the possible vertical
     * moves for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @param bitboards This is the bitboard constraints, i.e. the bitboard
     * representing the cases which the piece cannot move to.
     * @return
     */
    private static long getHorizontalMoves(Piece piece, long bitboards) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();

        int col = position % 8;
        long mask = bitboard;
        int coldec = col;
        int colinc = col;
        while (coldec > 0) {
            coldec--;
            int offset = col - coldec;
            long bit = bitboard >> offset;
            mask = mask | bit;
            if ((bit & bitboards) != 0) {
                break;
            }
        }
        while (colinc < 7) {
            colinc++;
            int offset = colinc - col;
            long bit = bitboard << offset;
            mask = mask | bit;
            if ((bit & bitboards) != 0) {
                break;
            }
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible diagonal
     * moves for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @param bitboards This is the bitboard constraints, i.e. the bitboard
     * representing the cases which the piece cannot move to.
     * @return Returns 2 bitboards: left-to-right and right-to-left
     */
    private static long[] getDiagonalMoves(Piece piece, long bitboards) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int col = position % 8;
        int row = position / 8;
        long maskLeftToRight = bitboard;
        long maskRightToLeft = bitboard;
        int coldec = col;
        int colinc = col;
        int rowdec = row;
        int rowinc = row;
        while (colinc < 7 && rowinc < 7) {
            rowinc++;
            colinc++;
            int temp = 8 * (rowinc - row);
            int offset = (colinc - col) + temp;
            long bit = bitboard << offset;
            maskLeftToRight = maskLeftToRight | bit;
            if ((bit & bitboards) != 0) {
                break;
            }
        }
        coldec = col;
        colinc = col;
        rowdec = row;
        rowinc = row;
        while (colinc < 7 && rowdec > 0) {
            rowdec--;
            colinc++;
            int temp = 8 * (row - rowdec);
            int offset = temp - (colinc - col);
            long bit = bitboard >> offset;
            maskRightToLeft = maskRightToLeft | bit;
            if ((bit & bitboards) != 0) {
                break;
            }

        }
        coldec = col;
        colinc = col;
        rowdec = row;
        rowinc = row;
        while (coldec > 0 && rowdec > 0) {
            rowdec--;
            coldec--;
            int temp = 8 * (row - rowdec);
            int offset = temp + (col - coldec);
            long bit = bitboard >> offset;
            maskLeftToRight = maskLeftToRight | bit;
            if ((bit & bitboards) != 0) {
                break;
            }
        }
        coldec = col;
        colinc = col;
        rowdec = row;
        rowinc = row;
        while (coldec > 0 && rowinc < 7) {
            rowinc++;
            coldec--;
            int temp = 8 * (rowinc - row);
            int offset = temp - (col - coldec);
            long bit = bitboard << offset;
            maskRightToLeft = maskRightToLeft | bit;
            if ((bit & bitboards) != 0) {
                break;
            }

        }
        return new long[]{maskLeftToRight, maskRightToLeft};
    }

    /**
     * This method returns the bitboards containing all the possible knight
     * moves for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getKnightMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int col = position % 8;
        int row = position / 8;
        long mask = bitboard;
        if (row < 6 && col > 0) {
            mask = mask | (bitboard << 15);
        }
        if (row < 6 && col < 7) {
            mask = mask | (bitboard << 17);
        }
        if (row < 7 && col > 1) {
            mask = mask | (bitboard << 6);
        }
        if (row < 7 && col < 6) {
            mask = mask | (bitboard << 10);
        }
        if (row > 0 && col < 6) {
            mask = mask | (bitboard >> 6);
        }
        if (row > 0 && col > 1) {
            mask = mask | (bitboard >> 10);
        }
        if (row > 1 && col > 0) {
            mask = mask | (bitboard >> 17);
        }
        if (row > 1 && col < 7) {
            mask = mask | (bitboard >> 15);
        }
        return mask;
    }

    /**
     * This method returns the possible castling moves
     *
     * @param piece Piece which is to castle
     * @param bitboards
     * @param rooks
     * @return
     */
    private static long getCastlingMoves(Piece piece, long bitboards, List<Piece> rooks) {
        long bitboard = piece.getBitboard();
        long mask = bitboard;
        if (piece.isMoved() == false) {
            if (piece.getTeam() == false) {
                for (Piece rook : rooks) {
                    if (rook.isMoved() == false) {
                        if ((bitboards & SetupConstants.CASTLING_WHITE_KING_1) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_WHITE_MASK_1)) {
                            mask = mask | SetupConstants.CASTLING_WHITE_KING_1;
                        }
                        if ((bitboards & SetupConstants.CASTLING_WHITE_KING_2) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_WHITE_MASK_2)) {
                            mask = mask | SetupConstants.CASTLING_WHITE_KING_2;
                        }
                    }
                }
            } else {
                for (Piece rook : rooks) {
                    if (rook.isMoved() == false) {
                        if ((bitboards & SetupConstants.CASTLING_BLACK_KING_1) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_BLACK_MASK_1)) {
                            mask = mask | SetupConstants.CASTLING_BLACK_KING_1;
                        }
                        if ((bitboards & SetupConstants.CASTLING_BLACK_KING_2) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_BLACK_MASK_2)) {
                            mask = mask | SetupConstants.CASTLING_BLACK_KING_2;
                        }

                    }
                }
            }
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible king moves
     * for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getKingMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int col = position % 8;
        int row = position / 8;
        long mask = bitboard;
        if (row < 7 && col > 0) {
            mask = mask | (bitboard << 7);
        }
        if (row > 0 && col < 7) {
            mask = mask | (bitboard >> 7);
        }
        if (row < 7 && col < 7) {
            mask = mask | (bitboard << 9);
        }
        if (row > 0 && col > 0) {
            mask = mask | (bitboard >> 9);
        }
        if (row < 7) {
            mask = mask | (bitboard << 8);
        }
        if (row > 0) {
            mask = mask | (bitboard >> 8);
        }
        if (col > 0) {
            mask = mask | (bitboard >> 1);
        }
        if (col < 7) {
            mask = mask | (bitboard << 1);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible moves for
     * the black pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getBlackPawnOneStepMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        long mask = bitboard;
        if (row < 7) {
            mask = mask | (bitboard << 8);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible moves for
     * the black pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getBlackPawnTwoStepMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        long mask = bitboard;
        if (row < 6) {
            mask = mask | (bitboard << 16);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible moves for
     * the white pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getWhitePawnOneStepMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        long mask = bitboard;
        if (row > 0) {
            mask = mask | (bitboard >> 8);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible moves for
     * the white pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getWhitePawnTwoStepMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        long mask = bitboard;
        if (row > 1) {
            mask = mask | (bitboard >> 16);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible diagonal
     * moves for the white pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getWhitePawnRightDiagMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        int col = position % 8;
        long mask = bitboard;
        if (row > 0 && col > 0) {
            mask = mask | (bitboard >> 9);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible diagonal
     * moves for the white pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getWhitePawnLeftDiagMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        int col = position % 8;
        long mask = bitboard;
        if (row > 0 && col < 7) {
            mask = mask | (bitboard >> 7);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible diagonal
     * moves for the black pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getBlackPawnRightDiagMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        int col = position % 8;
        long mask = bitboard;
        if (row < 7 && col > 0) {
            mask = mask | (bitboard << 7);
        }
        return mask;
    }

    /**
     * This method returns the bitboards containing all the possible diagonal
     * moves for the black pawn with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    private static long getBlackPawnLeftDiagMoves(Piece piece) {
        long bitboard = piece.getBitboard();
        int position = piece.getPosition();
        int row = position / 8;
        int col = position % 8;
        long mask = bitboard;
        if (col < 7 && row < 7) {
            mask = mask | (bitboard << 9);
        }
        return mask;
    }

    private static long[] getMoves(Piece piece, long teamBitboard, long adversaryBitboard, long adversaryPawnsBitboard, List<Piece> teamRooks, boolean attack) {
        boolean moved = piece.isMoved();
        boolean team = piece.getTeam();
        long pieceBitboard = piece.getBitboard();
        long bitboards = teamBitboard | adversaryBitboard;
        long h, v, kn, k, d1, d2, castle;
        long[] d;
        long[] splitMasks;

        switch (piece.getType()) {
            case ROOK:
                h = (Bitboard.getHorizontalMoves(piece, bitboards) & (~teamBitboard)) | pieceBitboard;
                v = (Bitboard.getVerticalMoves(piece, bitboards) & (~teamBitboard)) | pieceBitboard;
                splitMasks = new long[]{h, v};
                break;
            case KNIGHT:
                kn = (Bitboard.getKnightMoves(piece) & (~teamBitboard)) | pieceBitboard;
                splitMasks = new long[]{kn};
                break;
            case BISHOP:
                d = Bitboard.getDiagonalMoves(piece, bitboards);
                d1 = (d[0] & ~teamBitboard) | pieceBitboard;
                d2 = (d[1] & ~teamBitboard) | pieceBitboard;
                splitMasks = new long[]{d1, d2};
                break;
            case QUEEN:
                d = Bitboard.getDiagonalMoves(piece, bitboards);
                d1 = (d[0] & ~teamBitboard) | pieceBitboard;
                d2 = (d[1] & ~teamBitboard) | pieceBitboard;
                h = (Bitboard.getHorizontalMoves(piece, bitboards) & (~teamBitboard)) | pieceBitboard;
                v = (Bitboard.getVerticalMoves(piece, bitboards) & (~teamBitboard)) | pieceBitboard;
                splitMasks = new long[]{v, h, d1, d2, d1, d2};
                break;
            case KING:
                k = (Bitboard.getKingMoves(piece) & ~teamBitboard) | pieceBitboard;
                castle = Bitboard.getCastlingMoves(piece, bitboards, teamRooks) | pieceBitboard;
                splitMasks = new long[]{k, castle};
                break;
            case PAWN:
                if (team == false) {
                    long enpassantMask = ((pieceBitboard << 1) | (pieceBitboard >> 1)) << 8;
                    long enpassant = (((adversaryPawnsBitboard << 8) & enpassantMask) | pieceBitboard);
                    long mtb = (Bitboard.getBlackPawnOneStepMoves(piece) & ~bitboards);
                    long attackMaskLeft = Bitboard.getBlackPawnLeftDiagMoves(piece);
                    long attackMaskRight = Bitboard.getBlackPawnRightDiagMoves(piece);
                    if (moved == false & mtb != 0) {
                        mtb = (mtb | Bitboard.getBlackPawnTwoStepMoves(piece)) & ~bitboards;
                    }
                    if (attack) {
                        mtb = 0;
                    } else {
                        attackMaskLeft = attackMaskLeft & adversaryBitboard;
                        attackMaskRight = attackMaskRight & adversaryBitboard;
                    }
                    attackMaskLeft = attackMaskLeft | pieceBitboard;
                    attackMaskRight = attackMaskRight | pieceBitboard;
                    splitMasks = new long[4];
                    splitMasks[0] = mtb | pieceBitboard;
                    splitMasks[1] = enpassant | pieceBitboard;
                    splitMasks[2] = attackMaskLeft | pieceBitboard;
                    splitMasks[3] = attackMaskRight | pieceBitboard;
                } else {
                    long enpassantMask = ((pieceBitboard << 1) | (pieceBitboard >> 1)) >> 8;
                    long enpassant = (((adversaryPawnsBitboard >> 8) & enpassantMask) | pieceBitboard);
                    long mtw = (Bitboard.getWhitePawnOneStepMoves(piece) & ~bitboards);
                    long attackMaskLeft = Bitboard.getWhitePawnLeftDiagMoves(piece);
                    long attackMaskRight = Bitboard.getWhitePawnRightDiagMoves(piece);
                    if (moved == false & mtw != 0) {
                        mtw = (mtw | Bitboard.getWhitePawnTwoStepMoves(piece)) & ~bitboards;
                    }
                    if (attack) {
                        mtw = 0;
                    } else {
                        attackMaskLeft = attackMaskLeft & adversaryBitboard;
                        attackMaskRight = attackMaskRight & adversaryBitboard;
                    }
                    attackMaskLeft = attackMaskLeft | pieceBitboard;
                    attackMaskRight = attackMaskRight | pieceBitboard;
                    splitMasks = new long[4];
                    splitMasks[0] = mtw | pieceBitboard;
                    splitMasks[1] = enpassant | pieceBitboard;
                    splitMasks[2] = attackMaskLeft | pieceBitboard;
                    splitMasks[3] = attackMaskRight | pieceBitboard;
                }
                break;
            default:
                splitMasks = new long[0];
                break;
        }
        return splitMasks;
    }

    private static long[] getMoves(Piece piece, long teamBitboard, long adversaryBitboard, long adversaryPawnsBitboard, List<Piece> teamRooks) {
        return getMoves(piece, teamBitboard, adversaryBitboard, adversaryPawnsBitboard, teamRooks, false);
    }

    private static int[] getPositionsFromBitboard(long bitboard) {
        int[] positions;
        positions = new int[64];
        Arrays.fill(positions, -1);
        if (bitboard == 0) {
            return positions;
        }
        boolean stop = false;
        int counter = 0;
        int iterator = 0;
        long val = bitboard;

        while (stop == false) {
            long newVal = val / 2;
            long mod = val % 2;
            if (mod == 1) {
                positions[iterator] = counter;
                iterator++;
            }
            val = newVal;
            counter++;
            if (val == 0) {
                stop = true;
            }
        }
        return positions;
    }

}
