package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.XYPlot;

public class zad2 {
    public static void main(String[] args) {
        double fs = 2000;
        double Tc = 1;
        int N = (int)(Tc * fs);

        XYSeries signal = new XYSeries("x(t)");
        double f1 = 10;
        double f2 = fs/2 - f1;
        double f3 = f1/2;
        for(int n = 0; n < N; n++) {
            double t = n/fs;
            signal.add(t, Math.sin(2*Math.PI*f1*t) + Math.sin(2*Math.PI*f2*t) + Math.sin(2*Math.PI*f3*t));
        }

        XYSeries spectrumLinear = new XYSeries("liniowa");
        XYSeries spectrumLog = new XYSeries("logarytmiczna");
        XYSeries spectrumDB = new XYSeries("decybele");

        for(int k = 0; k < N/2; k++) {
            double re = 0, im = 0;
            for(int n = 0; n < N; n++) {
                double angle = (-2 * Math.PI * k * n / N);
                re += signal.getY(n).doubleValue() * Math.cos(angle);
                im += signal.getY(n).doubleValue() * Math.sin(angle);
            }
            double M = Math.sqrt(re*re + im*im) / (N/2);
            double frequency = k * (fs/N);

            spectrumLinear.add(frequency, M);

            if (k > 0 && frequency > 0 && M > 0) {
                spectrumLog.add(frequency, M);
                spectrumDB.add(Math.log10(frequency), 10 * Math.log10(M));
            }
        }

        displayChart(spectrumDB, "wartość skali decybelowej", "częstotliwość", "amplituda");
        displayLogChart(spectrumLog, "Widmo amplitudowe logarytmicznie", "częstotliwość", "M(k)");
        displayChart(spectrumLinear, "Widmo amplitudowe liniowo", "częstotliwość", "M(k)");
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

    static void displayLogChart(XYSeries series, String title, String xLabel, String yLabel) {
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(title, xLabel, yLabel, dataset);

        XYPlot plot = (XYPlot) chart.getPlot();

        LogarithmicAxis xAxis = new LogarithmicAxis(xLabel);
        xAxis.setStrictValuesFlag(false); // Pozwala na małe wartości dodatnie
        xAxis.setAutoRangeIncludesZero(false); // Wyklucza zero z zakresu
        plot.setDomainAxis(xAxis);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}