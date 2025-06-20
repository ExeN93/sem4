// Funkcja 10.
package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class zad4 {
    public static void main(String[] args) {
        int fs = 22050;
        double Tc = 1;
        int N = (int) Tc * fs;

        int[] H = {2, 4, 8};

        for (int k = 0; k < H.length; k++) {
            XYSeries bkPlot = new XYSeries("b_k(t)" + H[k]);

            for (int n = 0; n < N; n++) {
                double t = (double) n / fs;
                double bk = 0;

                for (int h = 1; h <= H[k]; h++) {
                    bk += Math.sin(Math.PI * t * (Math.pow(h, 2) * Math.sin(h))) / (7 * h);
                }

                bkPlot.add(t, bk);
            }
            displayChart(bkPlot, "b_k(t) h= " + H[k]);
        }
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