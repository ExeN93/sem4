// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.util.Arrays;

public class zad1 {
    public static void main(String[] args) {
        String text = "CS";

        int[] bits = asciiToBits(text);
        int B = bits.length;

        double Tc = 2;
        double Tb = Tc / B;
        int W = 2;
        double fn = W / Tb;

        double fs = 1000;
        int N = (int)(Tc * fs);

        double fn1 = (W + 1) / Tb;
        double fn2 = (W + 2) / Tb;
        double A1 = 1, A2 = 2;

        double[] time = new double[N];
        double[] ask = new double[N];
        double[] fsk = new double[N];
        double[] psk = new double[N];

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
                ask[n] = Math.sin(c * fn) * A1;
                fsk[n] = Math.sin(c * fn1);
                psk[n] = Math.sin(c * fn);
            }
            else {
                ask[n] = Math.sin(c * fn) * A2;
                fsk[n] = Math.sin(c * fn2);
                psk[n] = Math.sin(c * fn + Math.PI);
            }
        }

        System.out.println(Arrays.toString(asciiToBits("CS")));
        displayChart(time, ask, "ask", "czas", "amplituda");
        displayChart(time, fsk, "fsk", "czas", "amplituda");
        displayChart(time, psk, "psk", "czas", "amplituda");
    }

    static int[] asciiToBits(String text) {
        int n = text.length();
        int[] bits = new int[n * 7];
        int index = 0;

        for (int i = 0; i < n; i++) {
            int character = text.charAt(i);
            for (int b = 6; b >= 0; b--) {
                int bit = (character >> b) & 1;
                bits[index] = bit;
                index++;
            }
        }
        return bits;
    }

    // copy-paste z poprzednich laboratoriów (rysowanie wykresu)
    static void displayChart(double[] x, double[] y, String title, String xLabel, String yLabel) {
        XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++) {
            series.add(x[i], y[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}