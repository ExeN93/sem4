// Funkcja 8.
package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class zad1 {
    public static void main(String[] args) {
        int f = 1000;
        int fs = 8000;
        int Tc = 2;
        double fi = Math.PI / 2;
        int N = Tc * fs;

        XYSeries plot = new XYSeries("x(t)");

        for (int n = 0; n < N; n++) {
            double t = (double) n / fs;
            double xt = (1-t) * Math.sin(2 * Math.PI * f * t + fi) * Math.cos(4 * Math.PI * t);

            plot.add(t, xt);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(plot);
        JFreeChart jfreeChart =  ChartFactory.createXYLineChart("x(t)", "", "", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        JFrame frame = new JFrame("x(t)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(jfreeChart);
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}