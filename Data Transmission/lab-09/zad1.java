// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.Arrays;

public class zad1 {

    public static void main(String[] args) {
        String text = "CS";
        int[] bits = asciiToBits(text);
        int B = bits.length;
        double Tc = 1;
        double Tb = Tc / B;
        int W = 10;
        double fn1 = (W - 2) / Tb;
        double fn2 = (W + 2) / Tb;
        double fs = 1000;
        int N = (int)(Tc * fs);
        double A = 1;

        double[] time = new double[N];
        double[] z = FSK(bits, B, fn1, fn2, fs, Tb, N, time);

        double[] x1 = new double[N];
        double[] x2 = new double[N];
        double[] p1 = correlate(z, fn1, fs, x1);
        double[] p2 = correlate(z, fn2, fs, x2);

        double[] p = new double[N];
        for (int i = 0; i < N; i++) {
            p[i] = p1[i] - p2[i];
        }

        double[] c = new double[N];
        for (int i = 0; i < N; i++) {
            if (p[i] > 0) {
                c[i] = 1;
            } else {
                c[i] = 0;
            }
        }

        int[] decoded = toBits(c, (int)(Tb * fs), B);

        displayChart(time, z, "z(t) FSK", "czas", "amplituda");
        displayChart(time, x1, "x1(t)", "czas", "amplituda");
        displayChart(time, x2, "x2(t)", "czas", "amplituda");
        displayChart(time, p1, "p1(t)", "czas", "amplituda");
        displayChart(time, p2, "p2(t)", "czas", "amplituda");
        displayChart(time, p, "p(t)", "czas", "amplituda");
        displayChart(time, c, "c(t)", "czas", "amplituda");

        System.out.println("Oryginalne bity: " + Arrays.toString(bits));
        System.out.println("Zdekodowane bity: " + Arrays.toString(decoded));
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

    public static double[] FSK(int[] bits, int B, double fn1, double fn2, double fs, double Tb, int N, double[] time) {
        double[] fsk = new double[N];
        for (int n = 0; n < N; n++) {
            double t = n / fs;
            time[n] = t;
            int bitIndex = (int)(t / Tb);
            int bit = (bitIndex < B) ? bits[bitIndex] : 0;
            double c = 2 * Math.PI * t;
            fsk[n] = Math.sin(c * (bit == 0 ? fn1 : fn2));
        }
        return fsk;
    }

    // pomoc z internetu
    public static double[] correlate(double[] signal, double freq, double fs, double[] x) {
        double[] p = new double[signal.length];
        for (int n = 0; n < signal.length; n++) {
            double t = n / fs;
            double ref = Math.sin(2 * Math.PI * freq * t);
            x[n] = signal[n] * ref;
        }
        int Tb_samples = signal.length / 8;
        int index = 0;
        for (int b = 0; b < signal.length / Tb_samples; b++) {
            double s = 0;
            for (int n = b * Tb_samples; n < (b + 1) * Tb_samples && n < signal.length; n++) {
                s += x[n];
                p[index++] = s;
            }
        }
        return p;
    }

    // z pomocą LLM'a
    public static int[] toBits(double[] c, int step, int B) {
        int[] result = new int[B];
        for (int i = 0; i < B; i++) {
            double sum = 0;
            for (int j = 0; j < step; j++) {
                int idx = i * step + j;
                if (idx < c.length) {
                    sum += c[idx];
                }
            }
            if ((sum / step) > 0.5) {
                result[i] = 1;
            } else {
                result[i] = 0;
            }
        }
        return result;
    }

    // poprzednie laby
    public static void displayChart(double[] xData, double[] yData, String title, String xLabel, String yLabel) {
        XYSeries series = new XYSeries(title);
        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i], yData[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset, PlotOrientation.VERTICAL, false, false, false);
        JFrame frame = new JFrame(title);
        frame.setContentPane(new ChartPanel(chart));
        frame.setSize(1200, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
