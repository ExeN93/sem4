package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;

public class zad1 {
    public static void main(String[] args) {
        double A = 1;
        double f = 1000;
        double fs = 16000;
        double Tc = 0.02;
        int N = (int)(Tc * fs);

        XYSeries signal = new XYSeries("x(t)");
        for(int n = 0; n < N; n++) {
            double t = n/fs;
            signal.add(t, A * Math.sin(2 * Math.PI * f * t));
        }
        displayChart(signal, "x(t)", "czas", "amplituda");

        XYSeries spectrum = new XYSeries("M(k)");
        for(int k = 0; k < N/2; k++) {
            double re = 0, im = 0;
            for(int n = 0; n < N; n++) {
                double angle = -2 * Math.PI * k * n / N;
                re += signal.getY(n).doubleValue() * Math.cos(angle);
                im += signal.getY(n).doubleValue() * Math.sin(angle);
            }
            spectrum.add(k * fs/N, Math.sqrt(re*re + im*im) / (N/2));
        }
        displayChart(spectrum, "widmo amplitudowe", "częstotliwość", "amplituda");
    }

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