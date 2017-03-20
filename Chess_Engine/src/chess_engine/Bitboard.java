/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess_engine;

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
            System.out.print(row + "\n");
        }
        System.out.print("\n");
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
        if (bitboard == 0) {
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

    /**
     * This method returns the bitboard containing all the possible vertical
     * moves for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @param bitboards This is the bitboard constraints, i.e. the bitboard
     * representing the cases which the piece cannot move to.
     * @return
     */
    public static long getVerticalMoves(Piece piece, long bitboards) {
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
    public static long getHorizontalMoves(Piece piece, long bitboards) {
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
    public static long[] getDiagonalMoves(Piece piece, long bitboards) {
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
    public static long getKnightMoves(Piece piece) {
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
    public static long getCastlingMoves(Piece piece, long bitboards, Piece[] rooks) {
        long bitboard = piece.getBitboard();
        long mask = bitboard;
        if (piece.isMoved() == false) {
            if (piece.getTeam() == false) {
                if (rooks.length > 0) {
                    Piece rook = rooks[0];
                    if (rook.isMoved() == false) {
                        if ((bitboards & SetupConstants.CASTLING_WHITE_KING_1) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_WHITE_MASK_1)) {
                            mask = mask | SetupConstants.CASTLING_WHITE_KING_1;
                        }
                        if ((bitboards & SetupConstants.CASTLING_WHITE_KING_2) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_WHITE_MASK_2)) {
                            mask = mask | SetupConstants.CASTLING_WHITE_KING_2;
                        }

                    }

                }
                if (rooks.length > 1) {
                    Piece rook = rooks[1];
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
                if (rooks.length > 0) {
                    Piece rook = rooks[0];
                    if (rook.isMoved() == false) {
                        if ((bitboards & SetupConstants.CASTLING_BLACK_KING_1) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_BLACK_MASK_1)) {
                            mask = mask | SetupConstants.CASTLING_BLACK_KING_1;
                        }
                        if ((bitboards & SetupConstants.CASTLING_BLACK_KING_2) == ((bitboard | rook.getBitboard()) & SetupConstants.CASTLING_BLACK_MASK_2)) {
                            mask = mask | SetupConstants.CASTLING_BLACK_KING_2;
                        }

                    }

                }
                if (rooks.length > 1) {
                    Piece rook = rooks[1];
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
        return 10;
    }

    /**
     * This method returns the bitboards containing all the possible king moves
     * for the piece with the current bitboards.
     *
     * @param piece This is the piece which we generate moves for.
     * @return return bitboard
     */
    public static long getKingMoves(Piece piece) {
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
    public static long getBlackPawnOneStepMoves(Piece piece) {
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
    public static long getBlackPawnTwoStepMoves(Piece piece) {
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
    public static long getWhitePawnOneStepMoves(Piece piece) {
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
    public static long getWhitePawnTwoStepMoves(Piece piece) {
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
    public static long getWhitePawnRightDiagMoves(Piece piece) {
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
    public static long getWhitePawnLeftDiagMoves(Piece piece) {
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
    public static long getBlackPawnRightDiagMoves(Piece piece) {
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
    public static long getBlackPawnLeftDiagMoves(Piece piece) {
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

}
