// Andrzej Żwirko 55575 214A

package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class lab11 {

    public static void main(String[] args) {
        String text = "CSGO2TEST"; // 72
        int[] bits = asciiToBits(text);
        System.out.println("oryg: " + Arrays.toString(bits));

        int[] encoded = encodeHamming15_11(bits);
        System.out.println("hamming(15,11): " + Arrays.toString(encoded));

        int[] decoded1 = decodeHamming15_11(encoded);
        System.out.println("decoded1 (bez błędów): " + Arrays.toString(decoded1));

        int[] encodedWithErrors = Arrays.copyOf(encoded, encoded.length);
        for (int i = 0; i < encodedWithErrors.length; i += 15) {
            int randBit = i + (int)(Math.random() * 15);
            if (randBit < encodedWithErrors.length) {
                int index = randBit;
                if (encodedWithErrors[index] == 1) {
                    encodedWithErrors[index] = 0;
                } else {
                    encodedWithErrors[index] = 1;
                }
            }
        }

        int[] decoded2 = decodeHamming15_11(encodedWithErrors);
        System.out.println("decoded2 (z błędami 1-bit): " + Arrays.toString(decoded2));
    }

    public static int[] asciiToBits(String text) {
        int[] bits = new int[text.length() * 8];
        for (int i = 0; i < text.length(); i++) {
            int val = text.charAt(i);

            String binary = String.format("%8s", Integer.toBinaryString(val)).replace(' ', '0');
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = binary.charAt(j) - '0';
            }
        }
        return bits;
    }

    // pomoc LLM'a
    public static int[] encodeHamming15_11(int[] bits) {
        ArrayList<Integer> encoded = new ArrayList<>();
        for (int i = 0; i < bits.length; i += 11) {
            int[] block = new int[11];

            for (int j = 0; j < 11; j++) {
                block[j] = (i + j < bits.length) ? bits[i + j] : 0;
            }

            int[] codeword = new int[15];
            int[] dataIndexes = {2, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14};

            for (int j = 0; j < 11; j++) {
                codeword[dataIndexes[j]] = block[j];
            }

            codeword[0] = codeword[2] ^ codeword[4] ^ codeword[6] ^ codeword[8] ^ codeword[10] ^ codeword[12] ^ codeword[14];
            codeword[1] = codeword[2] ^ codeword[5] ^ codeword[6] ^ codeword[9] ^ codeword[10] ^ codeword[13] ^ codeword[14];
            codeword[3] = codeword[4] ^ codeword[5] ^ codeword[6] ^ codeword[11] ^ codeword[12] ^ codeword[13] ^ codeword[14];
            codeword[7] = codeword[8] ^ codeword[9] ^ codeword[10] ^ codeword[11] ^ codeword[12] ^ codeword[13] ^ codeword[14];


            // LLM bo nie wiedziałem jak się iteruje po liście
            for (int bit : codeword) {
                encoded.add(bit);
            }
        }

        int[] result = new int[encoded.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = encoded.get(i);
        }
        return result;
    }

    // pomoc LLM'a
    public static int[] decodeHamming15_11(int[] encoded) {
        ArrayList<Integer> decoded = new ArrayList<>();
        for (int i = 0; i < encoded.length; i += 15) {
            int[] word = new int[15];
            for (int j = 0; j < 15; j++) {
                word[j] = (i + j < encoded.length) ? encoded[i + j] : 0;
            }

            int s1 = word[0] ^ word[2] ^ word[4] ^ word[6] ^ word[8] ^ word[10] ^ word[12] ^ word[14];
            int s2 = word[1] ^ word[2] ^ word[5] ^ word[6] ^ word[9] ^ word[10] ^ word[13] ^ word[14];
            int s4 = word[3] ^ word[4] ^ word[5] ^ word[6] ^ word[11] ^ word[12] ^ word[13] ^ word[14];
            int s8 = word[7] ^ word[8] ^ word[9] ^ word[10] ^ word[11] ^ word[12] ^ word[13] ^ word[14];

            int errorPos = (s8 << 3) | (s4 << 2) | (s2 << 1) | s1;
            if (errorPos > 0 && errorPos <= 15) {
                if (word[errorPos - 1] == 0) {
                    word[errorPos - 1] = 1;
                } else {
                    word[errorPos - 1] = 0;
                }
            }

            int[] dataIndexes = {2, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14};
            for (int j : dataIndexes) {
                decoded.add(word[j]);
            }
        }

        int[] result = new int[decoded.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = decoded.get(i);
        }
        return result;
    }

    public static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] result = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public static int[][] identity(int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size; i++) {
            result[i][i] = 1;
        }
        return result;
    }

    public static int[][] generate1() {
        int[][] Ik = identity(11);
        int[][] P = {
                {0, 0, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 1},
                {0, 1, 0, 0},
                {0, 1, 0, 1},
                {0, 1, 1, 0},
                {0, 1, 1, 1},
                {1, 0, 0, 0},
                {1, 0, 0, 1},
                {1, 0, 1, 0},
                {1, 0, 1, 1}
        };

        int[][] G = new int[11][15];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                G[i][j] = Ik[i][j];
            }
            for (int j = 0; j < 4; j++) {
                G[i][11 + j] = P[i][j];
            }
        }
        return G;
    }

    public static int[][] generate2() {
        int[][] P = {
                {0, 0, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 1},
                {0, 1, 0, 0},
                {0, 1, 0, 1},
                {0, 1, 1, 0},
                {0, 1, 1, 1},
                {1, 0, 0, 0},
                {1, 0, 0, 1},
                {1, 0, 1, 0},
                {1, 0, 1, 1}
        };

        int[][] PT = transpose(P);
        int[][] I4 = identity(4);
        int[][] H = new int[4][15];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                H[i][j] = I4[i][j];
            }
            for (int j = 0; j < 11; j++) {
                H[i][4 + j] = PT[i][j];
            }
        }

        return H;
    }
}