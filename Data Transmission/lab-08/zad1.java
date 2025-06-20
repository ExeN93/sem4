// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.util.Arrays;

public class zad1 {
    public static void main(String[] args) {
        String text = "A";
        int[] bits = asciiToBits(text);
        int B = bits.length;
        double Tc = 1;
        double Tb = Tc / B;
        int W = 10;
        double fn = W / Tb;
        double fs = 1000;
        int N = (int)(Tc * fs);
        double A1 = 1, A2 = 2;

        double A = 1;
        double[] time = new double[N];
        double[] zASK = new double[N];
        double[] zPSK = new double[N];

        for (int n = 0; n < N; n++) {
            double t = n / fs;
            time[n] = t;
            int bitIndex = (int)(t / Tb);

            int bit;
            if(bitIndex < B) {
                bit = bits[bitIndex];
            }
            else {
                bit = 0;
            }

            double c = 2 * Math.PI * t;
            if (bit == 0) {
                zASK[n] = Math.sin(c * fn) * A1;
                zPSK[n] = Math.sin(c * fn);
            }
            else {
                zASK[n] = Math.sin(c * fn) * A2;
                zPSK[n] = Math.sin(c * fn + Math.PI);
            }
        }

        double[] xASK = new double[N];
        double[] xPSK = new double[N];
        double[] pASK = new double[N];
        double[] pPSK = new double[N];
        double[] cASK = new double[N];
        double[] cPSK = new double[N];

        for (int n = 0; n < N; n++) {
            double t = n / fs;
            double ref = A * Math.sin(2 * Math.PI * fn * t);
            xASK[n] = zASK[n] * A * Math.sin(2 * Math.PI * fn * t + 0);
            xPSK[n] = zPSK[n] * A * Math.sin(2 * Math.PI * fn * t + Math.PI);
        }

        int Top = (int)(Tb * fs);
        int iASK = 0, iPSK = 0;

        // zapozyczone z tablicy 3 najwazniejsze rzeczy na laboratoriach od doktora Mąki
        int size = xASK.length / Top;
        for (int b = 0; b < size; b++) {
            double s = 0;
            for (int n = b * Top; n < (b+1) * Top; n++) {
                s = s + xASK[n];
                pASK[iASK++] = s;
            }
        }

        size = pPSK.length / Top;
        for (int b = 0; b < size; b++) {
            double s = 0;
            for (int n = b * Top; n < (b+1) * Top; n++) {
                s = s + xPSK[n];
                pPSK[iPSK++] = s;
            }
        }

        double h = 0;
        for (int n = 0; n < N; n++) {
            cASK[n] = pASK[n] > 60 ? 1 : 0;
            cPSK[n] = pPSK[n] > h ? 1 : 0;
        }

        int[] decodedASK = averageDecision(cASK, Top, B);
        int[] decodedPSK = averageDecision(cPSK, Top, B);

        System.out.println("Bity oryginalne: " + Arrays.toString(bits));
        System.out.println("ASK dekodowane: " + Arrays.toString(decodedASK));
        System.out.println("PSK dekodowane: " + Arrays.toString(decodedPSK));

        plot(time, zASK, "zASK(t)");
        plot(time, xASK, "xASK(t)");
        plot(time, pASK, "pASK(t)");
        plot(time, cASK, "cASK(t)");

        plot(time, zPSK, "zPSK(t)");
        plot(time, xPSK, "xPSK(t)");
        plot(time, pPSK, "pPSK(t)");
        plot(time, cPSK, "cPSK(t)");

        plotBitsComparison(bits, decodedASK, "Porównanie bitów ASK");
        plotBitsComparison(bits, decodedPSK, "Porównanie bitów PSK");

    }

    public static int[] asciiToBits(String text) {
        int[] bits = new int[text.length() * 8];
        for (int i = 0; i < text.length(); i++) {
            int val = text.charAt(i);
            for (int j = 7; j >= 0; j--) {
                bits[i * 8 + (7 - j)] = (val >> j) & 1;
            }
        }
        return bits;
    }

    public static int[] averageDecision(double[] c, int Tbp, int B) {
        int[] bits = new int[B];
        for (int b = 0; b < B; b++) {
            double m = 0;
            for (int n = 0; n < Tbp; n++) {
                int idx = b * Tbp + n;
                if (idx < c.length) {
                    m = m + c[idx];
                }
            }
            m = m / Tbp;
            bits[b] = m > 0.5 ? 1 : 0;
        }
        return bits;
    }

    // poprzednie laby
    public static void plot(double[] x, double[] y, String title) {
        XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
        JFrame frame = new JFrame(title);
        frame.setContentPane(new ChartPanel(chart));
        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    // LLM do zrobienia porównania uzyskanych ciągów bitów, tak jak wymaga tego zadanie
    public static void plotBitsComparison(int[] original, int[] decoded, String title) {
        XYSeries originalSeries = new XYSeries("Oryginalne");
        XYSeries decodedSeries = new XYSeries("Zdemodulowane");

        for (int i = 0; i < original.length; i++) {
            originalSeries.add(i, original[i]);
            originalSeries.add(i + 1, original[i]);

            decodedSeries.add(i, decoded[i]);
            decodedSeries.add(i + 1, decoded[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalSeries);
        dataset.addSeries(decodedSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Indeks bitu",
                "Wartość",
                dataset,
                PlotOrientation.VERTICAL,
                true, false, false
        );

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ChartPanel(chart));
        frame.setSize(800, 400);
        frame.setVisible(true);
    }
}