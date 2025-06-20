// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;

public class zad1 {
    public static void main(String[] args) {
        double fm = 10;
        double fn = 500;
        double fs = 2000; // próbkowanie
        double Tc = 1; // czas sygnału
        int N = (int)(Tc * fs);

        double[]kA = {0.5, 6, 30};
        double[]kP = {0.5, 2, 10};
        double[]kF = {0.5, 2, 10};

        for (int i = 0; i < 3; i++) {
            generateModulatedSignals(fm, fn, fs, N, kA[i], kP[i], kF[i], i+1);
        }
    }

    static void generateModulatedSignals(double fm, double fn, double fs, int N, double kA, double kP, double kF, int caseNum) {
        XYSeries amSeries = new XYSeries("AM" + caseNum);
        XYSeries pmSeries = new XYSeries("PM" + caseNum);
        XYSeries fmSeries = new XYSeries("FM" + caseNum);

        for (int n = 0; n < N; n++) {
            double t = n / fs;
            double mt = Math.sin(2 * Math.PI * fm * t); // sygnał informacyjny

            double amSignal = (kA * mt + 1) * Math.cos(2 * Math.PI * fn * t);
            double pmSignal = Math.cos(2 * Math.PI * fn * t + kP * mt);
            double fmSignal = Math.cos(2 * Math.PI * fn * t + (kF/fm) * mt);

            amSeries.add(t, amSignal);
            pmSeries.add(t, pmSignal);
            fmSeries.add(t, fmSignal);
        }

        displayChart(amSeries, "AM" + caseNum + " kA = " + kA, "czas", "amplituda");
        displayChart(pmSeries, "PM" + caseNum + " kP = " + kP, "czas", "amplituda");
        displayChart(fmSeries, "FM" + caseNum + " kF = " + kF, "czas", "amplituda");
    }

    // copy-paste z poprzednich laboratoriów (rysowanie wykresu)
    static void displayChart(XYSeries series, String title, String xLabel, String yLabel) {
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}