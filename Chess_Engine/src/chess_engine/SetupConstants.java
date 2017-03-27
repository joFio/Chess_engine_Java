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
public class SetupConstants {

    /**
     * These constants are used for the original board setup.
     */
    public static final long CASTLING_BLACK_MASK_1 = Long.MIN_VALUE + 8646911284551352320L;
    public static final long CASTLING_BLACK_MASK_2 = 1080863910568919040L;

    public static final long CASTLING_WHITE_MASK_1 = 15L;
    public static final long CASTLING_WHITE_MASK_2 = 248L;

    public static final long CASTLING_WHITE_KING_1 = 2L;
    public static final long CASTLING_WHITE_KING_2 = 32L;
    public static final long CASTLING_BLACK_KING_1 = 2305843009213693952L;
    public static final long CASTLING_BLACK_KING_2 = 144115188075855872L;

    public static final long PROMOTION_WHITE = Long.MIN_VALUE + 9151314442816847872L;
    public static final long PROMOTION_BLACK = 255L;

    // White piece positions
    public static final long WHITE_ROOK_QUEEN = 128L;
    public static final long WHITE_KNIGHT_QUEEN = 64L;
    public static final long WHITE_BISHOP_QUEEN = 32L;
    public static final long WHITE_QUEEN = 16L;
    public static final long WHITE_KING = 8L;
    public static final long WHITE_BISHOP_KING = 4L;
    public static final long WHITE_KNIGHT_KING = 2L;
    public static final long WHITE_ROOK_KING = 1L;
    public static final long[] WHITE_PAWNS = new long[]{256L, 512L, 1024L, 2048L, 4096L, 8192L, 16384L, 32768L};

    // Black piece positions
    public static final long BLACK_ROOK_QUEEN = Long.MIN_VALUE;
    public static final long BLACK_KNIGHT_QUEEN = 4611686018427387904L;
    public static final long BLACK_BISHOP_QUEEN = 2305843009213693952L;
    public static final long BLACK_QUEEN = 1152921504606846976L;
    public static final long BLACK_KING = 576460752303423488L;
    public static final long BLACK_BISHOP_KING = 288230376151711744L;
    public static final long BLACK_KNIGHT_KING = 144115188075855872L;
    public static final long BLACK_ROOK_KING = 72057594037927936L;
    public static final long[] BLACK_PAWNS = new long[]{281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L};
}
