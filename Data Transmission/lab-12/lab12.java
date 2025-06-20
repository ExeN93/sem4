// Andrzej Żwirko 55575 214A

package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

// Całe laby wykonywałem z Andrii Zhupanov, Mikołaj Wróbel oraz Mikołaj Odzeniak ze względu na trudności które napotkałem na drodze.
// Starałem się jak najwięcej importować rzeczy od siebie, ale niekiedy byłem zmuszony uzyskać pomoc od Andriego, który to biegle rozumie.

public class lab12 {
    public static void main(String[] args) {
        zadanie1();
    }

    public static void zadanie1() {
        int[] inputBits = asciiToBits("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        int[] hammingEncodedBits = encodeHamming74(inputBits);

        double A = 1;
        double A1 = 0.5;
        double A2 = 1.0;
        double B = hammingEncodedBits.length;
        double Tc = 6;
        double Tb = Tc / B;
        double W = 2;
        double fn = W / Tb;
        double fs = 2000;
        double fn1 = (W + 1) / Tb;
        double fn2 = (W + 2) / Tb;

        double[] noiseAmplitudes = new double[10];
        for(int i = 0; i < 10; i++) {
            noiseAmplitudes[i] = i * 3.0 / 9.0;
        }

        double[] berAsk74 = new double[10];
        double[] berPsk74 = new double[10];
        double[] berFsk74 = new double[10];

        double[] za = modulateASK(A1, A2, hammingEncodedBits, fn, fs, Tb);
        double[] xt = modulate(za, fn, fs, A, 0);

        for(int i = 0; i < 10; i++) {
            double[] yt = generateAndAddNoise(xt, noiseAmplitudes[i]);
            double[] pt = demodulateASK(yt, fs, Tb);

            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;

            for (double v : pt) {
                if (v < min) min = v;
                if (v > max) max = v;
            }

            if (pt.length == 0) {
                min = 0;
                max = 1;
            }


            double bestBER = 100.0;

            for(int j = 0; j <= 50; j++) {
                double h = min + (max - min) * j / 50.0;
                int[] ct = threshold(pt, h);
                int[] decode = decodeHamming74(ct);
                double ber = calculateBER(inputBits, decode);
                if(ber < bestBER) {
                    bestBER = ber;
                }
            }
            berAsk74[i] = bestBER;
        }

        double[] zp = modulatePSK(hammingEncodedBits, fn, fs, Tb);
        double[] xt1 = modulate(zp, fn, fs, A, Math.PI);

        for(int i = 0; i < 10; i++) {
            double[] yt = generateAndAddNoise(xt1, noiseAmplitudes[i]);
            double[] pt = demodulate(yt, fs, Tb);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming74(ct);
            berPsk74[i] = calculateBER(inputBits, decode);
        }

        double[] zf = modulateFSK(hammingEncodedBits, fn1, fn2, fs, Tb);
        double[] xt2 = modulateFSK1(zf, fn1, fs);
        double[] xt3 = modulateFSK1(zf, fn2, fs);

        for(int i = 0; i < 10; i++) {
            double[] yt1 = generateAndAddNoise(xt2, noiseAmplitudes[i]);
            double[] yt2 = generateAndAddNoise(xt3, noiseAmplitudes[i]);
            double[] pt1 = demodulate(yt1, fs, Tb);
            double[] pt2 = demodulate(yt2, fs, Tb);
            double[] pt = calculateDifferences(pt1, pt2);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming74(ct);
            berFsk74[i] = calculateBER(inputBits, decode);
        }

        int[] encode15_11 = encodeHamming1511(inputBits);
        B = encode15_11.length;
        Tb = Tc / B;
        fn = W / Tb;
        fn1 = (W + 1) / Tb;
        fn2 = (W + 2) / Tb;

        double[] berAsk1511 = new double[10];
        double[] berPsk1511 = new double[10];
        double[] berFsk1511 = new double[10];

        za = modulateASK(A1, A2, encode15_11, fn, fs, Tb);
        xt = modulate(za, fn, fs, A, 0);

        for(int i = 0; i < 10; i++) {
            double[] yt = generateAndAddNoise(xt, noiseAmplitudes[i]);
            double[] pt = demodulateASK(yt, fs, Tb);

            double min = Arrays.stream(pt).min().orElse(0);
            double max = Arrays.stream(pt).max().orElse(1);
            double bestBER = 100.0;

            for(int j = 0; j <= 50; j++) {
                double h = min + (max - min) * j / 50.0;
                int[] ct = threshold(pt, h);
                int[] decode = decodeHamming1511(ct);
                double ber = calculateBER(inputBits, decode);
                if(ber < bestBER) {
                    bestBER = ber;
                }
            }
            berAsk1511[i] = bestBER;
        }

        zp = modulatePSK(encode15_11, fn, fs, Tb);
        xt1 = modulate(zp, fn, fs, A, Math.PI);

        for(int i = 0; i < 10; i++) {
            double[] yt = generateAndAddNoise(xt1, noiseAmplitudes[i]);
            double[] pt = demodulate(yt, fs, Tb);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming1511(ct);
            berPsk1511[i] = calculateBER(inputBits, decode);
        }

        zf = modulateFSK(encode15_11, fn1, fn2, fs, Tb);
        xt2 = modulateFSK1(zf, fn1, fs);
        xt3 = modulateFSK1(zf, fn2, fs);

        for(int i = 0; i < 10; i++) {
            double[] yt1 = generateAndAddNoise(xt2, noiseAmplitudes[i]);
            double[] yt2 = generateAndAddNoise(xt3, noiseAmplitudes[i]);
            double[] pt1 = demodulate(yt1, fs, Tb);
            double[] pt2 = demodulate(yt2, fs, Tb);
            double[] pt = calculateDifferences(pt1, pt2);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming1511(ct);
            berFsk1511[i] = calculateBER(inputBits, decode);
        }

        System.out.println("BER 7,4:");
        System.out.println("alpha\task\tpsk\tfsk");

        for(int i = 0; i < 10; i++) {
            System.out.printf("%.3f\t%.3f\t%.3f\t%.3f%n", noiseAmplitudes[i], berAsk74[i], berPsk74[i], berFsk74[i]);
        }

        System.out.println("\nBER 15,11:");
        System.out.println("alpha\task\tpsk\tfsk");

        for(int i = 0; i < 10; i++) {
            System.out.printf("%.3f\t%.3f\t%.3f\t%.3f%n",
                    noiseAmplitudes[i], berAsk1511[i], berPsk1511[i], berFsk1511[i]);
        }

        createPlot(noiseAmplitudes, berAsk74, berPsk74, berFsk74, "hamming74");
        createPlot(noiseAmplitudes, berAsk1511, berPsk1511, berFsk1511, "hamming1511");
    }

    public static void zadanie2() {
        int[] inputBits = asciiToBits("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        int[] hammingEncodedBits = encodeHamming74(inputBits);

        double A = 1;
        double A1 = 0.5;
        double A2 = 1.0;
        double B = hammingEncodedBits.length;
        double Tc = 6;
        double Tb = Tc / B;
        double W = 2;
        double fn = W / Tb;
        double fs = 2000;
        double fn1 = (W + 1) / Tb;
        double fn2 = (W + 2) / Tb;

        double[] noiseAmplitudes = new double[10];
        for(int i = 0; i < 10; i++) {
            noiseAmplitudes[i] = i * 2.0 / 10.0;
        }

        double[] berAsk74 = new double[10];
        double[] berPsk74 = new double[10];
        double[] berFsk74 = new double[10];

        double[] za = modulateASK(A1, A2, hammingEncodedBits, fn, fs, Tb);
        double[] xt = modulate(za, fn, fs, A, 0);

        for(int i = 0; i < 10; i++) {
            double[] yt = singalAttenuation(xt, noiseAmplitudes[i], Tc);
            double[] pt = demodulateASK(yt, fs, Tb);

            double min = Arrays.stream(pt).min().orElse(0);
            double max = Arrays.stream(pt).max().orElse(1);
            double bestBER = 100.0;

            for(int j = 0; j <= 50; j++) {
                double h = min + (max - min) * j / 50.0;
                int[] ct = threshold(pt, h);
                int[] decode = decodeHamming74(ct);
                double ber = calculateBER(inputBits, decode);
                if(ber < bestBER) {
                    bestBER = ber;
                }
            }
            berAsk74[i] = bestBER;
        }

        double[] zp = modulatePSK(hammingEncodedBits, fn, fs, Tb);
        double[] xt1 = modulate(zp, fn, fs, A, Math.PI);

        for(int i = 0; i < 10; i++) {
            double[] yt = singalAttenuation(xt1, noiseAmplitudes[i], Tc);
            double[] pt = demodulate(yt, fs, Tb);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming74(ct);
            berPsk74[i] = calculateBER(inputBits, decode);
        }

        double[] zf = modulateFSK(hammingEncodedBits, fn1, fn2, fs, Tb);
        double[] xt2 = modulateFSK1(zf, fn1, fs);
        double[] xt3 = modulateFSK1(zf, fn2, fs);

        for(int i = 0; i < 10; i++) {
            double[] yt1 = singalAttenuation(xt2, noiseAmplitudes[i], Tc);
            double[] yt2 = singalAttenuation(xt3, noiseAmplitudes[i], Tc);
            double[] pt1 = demodulate(yt1, fs, Tb);
            double[] pt2 = demodulate(yt2, fs, Tb);
            double[] pt = calculateDifferences(pt1, pt2);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming74(ct);
            berFsk74[i] = calculateBER(inputBits, decode);
        }

        int[] encode15_11 = encodeHamming1511(inputBits);
        B = encode15_11.length;
        Tb = Tc / B;
        fn = W / Tb;
        fn1 = (W + 1) / Tb;
        fn2 = (W + 2) / Tb;

        double[] berAsk1511 = new double[10];
        double[] berPsk1511 = new double[10];
        double[] berFsk1511 = new double[10];

        za = modulateASK(A1, A2, encode15_11, fn, fs, Tb);
        xt = modulate(za, fn, fs, A, 0);

        for(int i = 0; i < 10; i++) {
            double[] yt = singalAttenuation(xt, noiseAmplitudes[i], Tc);
            double[] pt = demodulateASK(yt, fs, Tb);

            double min = Arrays.stream(pt).min().orElse(0);
            double max = Arrays.stream(pt).max().orElse(1);
            double bestBER = 100.0;

            for(int j = 0; j <= 50; j++) {
                double h = min + (max - min) * j / 50.0;
                int[] ct = threshold(pt, h);
                int[] decode = decodeHamming1511(ct);
                double ber = calculateBER(inputBits, decode);
                if(ber < bestBER) {
                    bestBER = ber;
                }
            }
            berAsk1511[i] = bestBER;
        }

        zp = modulatePSK(encode15_11, fn, fs, Tb);
        xt1 = modulate(zp, fn, fs, A, Math.PI);

        for(int i = 0; i < 10; i++) {
            double[] yt = singalAttenuation(xt1, noiseAmplitudes[i], Tc);
            double[] pt = demodulate(yt, fs, Tb);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming1511(ct);
            berPsk1511[i] = calculateBER(inputBits, decode);
        }

        zf = modulateFSK(encode15_11, fn1, fn2, fs, Tb);
        xt2 = modulateFSK1(zf, fn1, fs);
        xt3 = modulateFSK1(zf, fn2, fs);

        for(int i = 0; i < 10; i++) {
            double[] yt1 = singalAttenuation(xt2, noiseAmplitudes[i], Tc);
            double[] yt2 = singalAttenuation(xt3, noiseAmplitudes[i], Tc);
            double[] pt1 = demodulate(yt1, fs, Tb);
            double[] pt2 = demodulate(yt2, fs, Tb);
            double[] pt = calculateDifferences(pt1, pt2);
            int[] ct = threshold(pt, 0);
            int[] decode = decodeHamming1511(ct);
            berFsk1511[i] = calculateBER(inputBits, decode);
        }

        System.out.println("BER 7,4:");
        System.out.println("alpha\task\tpsk\tfsk");
        for(int i = 0; i < 10; i++) {
            System.out.printf("%.3f\t%.3f\t%.3f\t%.3f%n",
                    noiseAmplitudes[i], berAsk74[i], berPsk74[i], berFsk74[i]);
        }

        System.out.println("\nBER 15,11:");
        System.out.println("alpha\task\tpsk\tfsk");
        for(int i = 0; i < 10; i++) {
            System.out.printf("%.3f\t%.3f\t%.3f\t%.3f%n",
                    noiseAmplitudes[i], berAsk1511[i], berPsk1511[i], berFsk1511[i]);
        }

        createPlot(noiseAmplitudes, berAsk74, berPsk74, berFsk74, "hamming74");
        createPlot(noiseAmplitudes, berAsk1511, berPsk1511, berFsk1511, "hamming1511");
    }

    public static double[] singalAttenuation(double[] inputSignal, double beta, double Tc) {
        double t0 = Tc * 0.65;
        double[] attenuatedSignal = new double[inputSignal.length];
        for (int i = 0; i < inputSignal.length; i++) {
            double t = (i * Tc) / inputSignal.length;
            attenuatedSignal[i] = inputSignal[i] * Math.exp(-beta * t) * Math.max(0, 1 - (t / t0));
        }
        return attenuatedSignal;
    }

    public static double[] modulateASK(double A1, double A2, int[] inputBits, double fn, double fs, double Tb) {
        int bitTime = (int)(fs * Tb);
        double[] signal = new double[inputBits.length * bitTime];

        for(int n = 0; n < signal.length; n++) {
            double t = (double) n / fs;
            double f = 0;
            if(inputBits[n / bitTime] == 0) {
                f = A1 * Math.sin(2 * Math.PI * t * fn);
            } else {
                f = A2 * Math.sin(2 * Math.PI * t * fn);
            }
            signal[n] = f;
        }
        return signal;
    }

    public static double[] modulateFSK(int[] inputBits, double fn1, double fn2, double fs, double Tb) {
        int bitTime = (int)(fs * Tb);
        double[] signal = new double[inputBits.length * bitTime];

        for(int n = 0; n < signal.length; n++) {
            double t = (double) n / fs;
            double s;
            if(inputBits[n / bitTime] == 0) {
                s = Math.sin(2 * Math.PI * t * fn1);
            } else {
                s = Math.sin(2 * Math.PI * t * fn2);
            }
            signal[n] = s;
        }
        return signal;
    }

    public static double[] modulatePSK(int[] inputBits, double fn, double fs, double Tb) {
        int bitTime = (int)(fs * Tb);
        double[] signal = new double[inputBits.length * bitTime];

        for(int n = 0; n < signal.length; n++) {
            double t = (double) n / fs;
            double f = 0;
            if(inputBits[n / bitTime] == 0) {
                f = Math.sin(2 * Math.PI * t * fn);
            } else {
                f = Math.sin(2 * Math.PI * t * fn + Math.PI);
            }
            signal[n] = f;
        }
        return signal;
    }

    public static double[] modulate(double[] signal, double fn, double fs, double A, double fi) {
        double[] res = new double[signal.length];

        for(int n = 0; n < signal.length; n++) {
            double t = (double) n / fs;
            res[n] = signal[n] * A * Math.sin(2 * Math.PI * t * fn + fi);
        }
        return res;
    }

    public static double[] modulateFSK1(double[] signal, double fn, double fs) {
        double[] res = new double[signal.length];
        for(int n = 0; n < signal.length; n++) {
            double t = (double) n / fs;
            res[n] = signal[n] * Math.sin(2 * Math.PI * t * fn);
        }
        return res;
    }

    public static double[] demodulate(double[] xt, double fs, double Tb) {
        int bitTime = (int)(fs * Tb);
        int numBits = xt.length / bitTime;
        double[] res = new double[numBits];

        for(int i = 0; i < numBits; i++) {
            double sum = 0;
            for(int y = i * bitTime; y < (i + 1) * bitTime; y++) {
                sum += xt[y];
            }
            res[i] = sum;
        }
        return res;
    }

    public static double[] demodulateASK(double[] xt, double fs, double Tb) {
        return demodulate(xt, fs, Tb);
    }

    public static double[] calculateDifferences(double[] pt1, double[] pt2) {
        double[] inputBits = new double[pt1.length];
        for(int i = 0; i < pt1.length; i++) {
            inputBits[i] = pt2[i] - pt1[i];
        }
        return inputBits;
    }

    public static int[] threshold(double[] pt, double h) {
        int[] ct = new int[pt.length];
        for (int i = 0; i < pt.length; i++) {
            if (pt[i] > h) {
                ct[i] = 1;
            } else {
                ct[i] = 0;
            }
        }
        return ct;
    }

    public static int[] asciiToBits(String word) {
        int length = word.length() * 7;
        int[] result = new int[length];
        for(int i = 0; i < word.length(); i++) {
            int charValue = word.charAt(i);
            for(int j = 0; j < 7; j++) {
                result[i * 7 + (6 - j)] = charValue & 1;
                charValue = charValue >> 1;
            }
        }
        return result;
    }

    public static void createPlot(double[] noiseAmplitudes, double[] askData, double[] pskData, double[] fskData, String title) {
        XYSeries askSeries = new XYSeries("ask");
        XYSeries pskSeries = new XYSeries("psk");
        XYSeries fskSeries = new XYSeries("fsk");

        for (int i = 0; i < noiseAmplitudes.length; i++) {
            askSeries.add(noiseAmplitudes[i], askData[i]);
            pskSeries.add(noiseAmplitudes[i], pskData[i]);
            fskSeries.add(noiseAmplitudes[i], fskData[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(askSeries);
        dataset.addSeries(pskSeries);
        dataset.addSeries(fskSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(title, "alpha", "BER", dataset, PlotOrientation.VERTICAL, false, false, false);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChartPanel(chart));
            frame.setSize(1200, 1000);
            frame.setVisible(true);
        });
    }

    public static double calculateBER(int[] inputBits, int[] demodulation) {
        int error = 0;
        for(int i = 0; i < inputBits.length; i++) {
            if(inputBits[i] != demodulation[i]) {
                error++;
            }
        }
        double ber = ((double) error / (double) inputBits.length) * 100;
        return ber;
    }

    public static double[] generateAndAddNoise(double[] xt, double alfa) {
        double[] res = new double[xt.length];
        Random rand = new Random();
        double[] noise = new double[xt.length];

        for (int i = 0; i < xt.length; i++) {
            noise[i] = rand.nextDouble() * 2 - 1;
        }

        for(int i = 0; i < xt.length; i++) {
            res[i] = xt[i] + (alfa * noise[i]);
        }
        return res;
    }

    public static int[] encodeHamming74Block(int[] data4) {
        int d1 = data4[0];
        int d2 = data4[1];
        int d3 = data4[2];
        int d4 = data4[3];
        int p1 = d1 ^ d2 ^ d4;
        int p2 = d1 ^ d3 ^ d4;
        int p3 = d2 ^ d3 ^ d4;
        return new int[] {p1, p2, d1, p3, d2, d3, d4};
    }

    public static int[] encodeHamming74(int[] inputBits) {
        ArrayList<Integer> encoded = new ArrayList<>();
        for (int i = 0; i < inputBits.length; i += 4) {
            int[] block = new int[4];
            for (int j = 0; j < 4; j++) {
                block[j] = (i + j < inputBits.length) ? inputBits[i + j] : 0;
            }
            int[] encodedBlock = encodeHamming74Block(block);
            for (int val : encodedBlock) {
                encoded.add(val);
            }
        }
        int[] result = new int[encoded.size()];
        for (int i = 0; i < result.length; i++) result[i] = encoded.get(i);
        return result;
    }

    public static int[] decodeHamming74Block(int[] codeword) {
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
        int errorPosition = (s3 << 2) | (s2 << 1) | s1;
        if (errorPosition >= 1 && errorPosition <= 7) {
            int index = errorPosition - 1;
            if (codeword[index] == 0) {
                codeword[index] = 1;
            } else {
                codeword[index] = 0;
            }
        }
        return new int[] {d1, d2, d3, d4};
    }

    public static int[] decodeHamming74(int[] encoded) {
        ArrayList<Integer> decoded = new ArrayList<>();
        for (int i = 0; i < encoded.length; i += 7) {
            int[] word = new int[7];
            for (int j = 0; j < 7; j++) {
                word[j] = (i + j < encoded.length) ? encoded[i + j] : 0;
            }
            int[] data = decodeHamming74Block(word);
            for (int bit : data) decoded.add(bit);
        }
        int[] result = new int[decoded.size()];
        for (int i = 0; i < result.length; i++) result[i] = decoded.get(i);
        return result;
    }

    public static int[] encodeHamming1511(int[] inputBits) {
        ArrayList<Integer> encoded = new ArrayList<>();
        for (int i = 0; i < inputBits.length; i += 11) {
            int[] block = new int[11];
            for (int j = 0; j < 11; j++) {
                block[j] = (i + j < inputBits.length) ? inputBits[i + j] : 0;
            }

            int[] codeword = new int[15];
            int[] dataIndex = {2, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14};
            for (int j = 0; j < 11; j++) {
                codeword[dataIndex[j]] = block[j];
            }

            codeword[0] = codeword[2] ^ codeword[4] ^ codeword[6] ^ codeword[8] ^ codeword[10] ^ codeword[12] ^ codeword[14];
            codeword[1] = codeword[2] ^ codeword[5] ^ codeword[6] ^ codeword[9] ^ codeword[10] ^ codeword[13] ^ codeword[14];
            codeword[3] = codeword[4] ^ codeword[5] ^ codeword[6] ^ codeword[11] ^ codeword[12] ^ codeword[13] ^ codeword[14];
            codeword[7] = codeword[8] ^ codeword[9] ^ codeword[10] ^ codeword[11] ^ codeword[12] ^ codeword[13] ^ codeword[14];

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

    public static int[] decodeHamming1511(int[] encoded) {
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

            int errorPosition = (s8 << 3) | (s4 << 2) | (s2 << 1) | s1;
            if (errorPosition > 0 && errorPosition <= 15) {
                if (word[errorPosition - 1] == 0) {
                    word[errorPosition - 1] = 1;
                } else {
                    word[errorPosition - 1] = 0;
                }
            }

            int[] dataIndex = {2, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14};
            for (int j : dataIndex) {
                decoded.add(word[j]);
            }
        }

        int[] result = new int[decoded.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = decoded.get(i);
        }
        return result;
    }
}