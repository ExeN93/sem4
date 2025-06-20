// Funkcja 8.
package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class zad3 {
    public static void main(String[] args) {
        int f = 1000;
        int fs = 8000;
        int Tc = 2;
        double fi = Math.PI / 2;
        int N = Tc * fs;

        XYSeries uPlot = new XYSeries("u(t)");

        for (int n = 0; n < N; n++) {
            double t = (double) n / fs;
            double ut = 0;

            if (t >= 0 && t < 0.91) {
                ut = -t * Math.sin(7 * Math.PI * (t - 0.8)) * Math.cos(25 * Math.PI * (t - 0.2)) + 0.8;
            } else if (t >= 0.91 && t < 2.3) {
                ut = 1 / (Math.sin(2 * Math.PI * t) + 1.1);
            } else if (t >= 2.3 && t < 3) {
                ut = (0.5 * (t - 2.3) * Math.cos(12 * Math.PI * (t - 0.7))) + 0.48;
            }

            uPlot.add(t, ut);
        }
        displayChart(uPlot, "u(t)");
    }

    static void displayChart(XYSeries series, String title) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, "", "", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}