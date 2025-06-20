// Funkcja 5.
package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class zad2 {
    public static void main(String[] args) {
        int f = 1000;
        int fs = 8000;
        int Tc = 2;
        double fi = Math.PI / 2;
        int N = Tc * fs;

        XYSeries yPlot = new XYSeries("y(t)");
        XYSeries zPlot = new XYSeries("z(t)");
        XYSeries vPlot = new XYSeries("v(t)");

        for (int n = 0; n < N; n++) {
            double t = (double) n / fs;
            double xt = (1-t) * Math.sin(2 * Math.PI * f * t + fi) * Math.cos(4 * Math.PI * t);

            double yt = (2 * t * Math.sin(0.5 * t * Math.PI) + 1.5) * Math.cos(9 * Math.PI * t + Math.PI * t);
            double zt = yt * xt + Math.abs(xt + 2) * (Math.pow(yt, 2) + 0.32);
            double vt = Math.sqrt(Math.abs(xt * zt + 10)) * (Math.abs(yt) + 1.2) * Math.sin(2 * Math.PI * t);

            yPlot.add(t, yt);
            zPlot.add(t, zt);
            vPlot.add(t, vt);
        }
        displayChart(yPlot, "y(t)");
        displayChart(zPlot, "z(t)");
        displayChart(vPlot, "v(t)");
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