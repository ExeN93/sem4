// Andrzej Żwirko 55575 214A

package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class lab10 {

    public static void main(String[] args) {
        String text = "CSGO2"; // 40

        int[] bits = asciiToBits(text);
        System.out.println("oryg: " + Arrays.toString(bits));

        int[] encoded = stream(bits);
        System.out.println("hammingus: " + Arrays.toString(encoded));

        // bez błędów
        int[] decoded1 = decodeStream(encoded);
        System.out.println("decoded1 (bez błędów): " + Arrays.toString(decoded1));

        // 1 losowy bit negowany w każdym słowie
        int[] encodedWithErrors = Arrays.copyOf(encoded, encoded.length);
        for (int i = 0; i < encodedWithErrors.length; i += 7) {
            int randBit = i + (int)(Math.random() * 7);
            if (randBit < encodedWithErrors.length) {
                encodedWithErrors[randBit] = (encodedWithErrors[randBit] == 0) ? 1 : 0;
            }
        }

        int[] decoded2 = decodeStream(encodedWithErrors);
        System.out.println("decoded2 (z błędami): " + Arrays.toString(decoded2));
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

    // pomoc llm
    public static int[] encoding(int[] data4) {
        int d1 = data4[0];
        int d2 = data4[1];
        int d3 = data4[2];
        int d4 = data4[3];

        int p1 = d1 ^ d2 ^ d4;
        int p2 = d1 ^ d3 ^ d4;
        int p3 = d2 ^ d3 ^ d4;

        return new int[] {p1, p2, d1, p3, d2, d3, d4};
    }

    public static int[] stream(int[] bits) {
        ArrayList<Integer> encoded = new ArrayList<>();
        for (int i = 0; i < bits.length; i += 4) {
            int[] block = new int[4];
            for (int j = 0; j < 4; j++) {
                block[j] = (i + j < bits.length) ? bits[i + j] : 0;
            }
            int[] encodedBlock = encoding(block);
            for (int val : encodedBlock) {
                encoded.add(val);
            }
        }

        int[] result = new int[encoded.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = encoded.get(i);
        }
        return result;
    }

    // pomoc llm
    public static int[] decoding(int[] codeword) {
        int p1 = codeword[0];
        int p2 = codeword[1];
        int d1 = codeword[2];
        int p3 = codeword[3];
        int d2 = codeword[4];
        int d3 = codeword[5];
        int d4 = codeword[6];

        int s1 = p1 ^ d1 ^ d2 ^ d4;
        int s2 = p2 ^ d1 ^ d3 ^ d4;
        int s3 = p3 ^ d2 ^ d3 ^ d4;

        int errorPos = (s3 << 2) | (s2 << 1) | s1; // indeks 1–7
        if (errorPos >= 1 && errorPos <= 7) {
            int index = errorPos - 1;
            if (codeword[index] == 1) {
                codeword[index] = 0;
            } else {
                codeword[index] = 1;
            }
        }

        return new int[] {codeword[2], codeword[4], codeword[5], codeword[6]};
    }

    public static int[] decodeStream(int[] encoded) {
        ArrayList<Integer> decoded = new ArrayList<>();
        for (int i = 0; i < encoded.length; i += 7) {
            int[] word = new int[7];
            for (int j = 0; j < 7; j++) {
                word[j] = (i + j < encoded.length) ? encoded[i + j] : 0;
            }
            int[] data = decoding(word);
            for (int bit : data) {
                decoded.add(bit);
            }
        }

        int[] result = new int[decoded.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = decoded.get(i);
        }
        return result;
    }
}