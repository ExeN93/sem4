// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.util.Arrays;

public class zad2 {
    public static void main(String[] args) {
        double f1 = 300;
        double f2 = 100;
        double fs = 2000;
        double Tc = 1;
        int N = (int)(Tc * fs);

        double[] x = new double[N];
        double[] y = new double[N];
        double[] z = new double[N];

        XYSeries xt = new XYSeries("xt");
        XYSeries yt = new XYSeries("yt");
        XYSeries zt = new XYSeries("zt");

        float alpha = 3;
        float beta = 5;

        for (int n = 0; n < N; n++) {
            double t = n / fs;

            x[n] = 0.5 * Math.sin(2 * Math.PI * f1 * t);
            y[n] = Math.sin(2 * Math.PI * f2 * t) + 0.7 * Math.sin(2 * Math.PI* f1 * t);
            z[n] = alpha * x[n] + beta * y[n];

            xt.add(t, x[n]);
            yt.add(t, y[n]);
            zt.add(t, z[n]);
        }

        displayChart(xt, "xt", "czas", "amplituda");
        displayChart(yt, "yt", "czas", "amplituda");
        displayChart(zt, "zt", "czas", "amplituda");

        double[] Mx = computeMagnitudeSpectrum(x);
        double[] My = computeMagnitudeSpectrum(y);
        double[] Mz = computeMagnitudeSpectrum(z);

        // dla szacowanego
        double[] Mzz = new double[Mx.length];
        for (int i = 0; i < Mx.length; i++) {
            Mzz[i] = alpha * Mx[i] + beta * My[i];
        }

        displaySpectrum(Mx, fs, "Mx");
        displaySpectrum(My, fs, "My");
        displaySpectrum(Mz, fs, "Mz");
        displaySpectrum(Mzz, fs, "Mzz");
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

    static void displaySpectrum(double[] signal, double fs, String title) {
        int N = signal.length;
        double[] real = Arrays.copyOf(signal, N);
        double[] imag = new double[N];

        fft(real, imag);

        XYSeries spectrum = new XYSeries(title);
        for (int i = 0; i < N / 2; i++) {
            double frequency = i * fs / N;
            double magnitude = Math.sqrt(real[i]*real[i] + imag[i]*imag[i]) / N;
            spectrum.add(frequency, magnitude);
        }
        displayChart(spectrum, title, "częstotliwość", "amplituda");
    }

    static void fft(double[] real, double[] imag) {
        int n = real.length;

        if (n == 1) return;
        double[] evenReal = new double[n/2];
        double[] evenImag = new double[n/2];
        double[] oddReal = new double[n/2];
        double[] oddImag = new double[n/2];

        for (int i = 0; i < n/2; i++) {
            evenReal[i] = real[2 * i];
            evenImag[i] = imag[2 * i];
            oddReal[i] = real[2 * i + 1];
            oddImag[i] = imag[2 * i + 1];
        }

        fft(evenReal, evenImag);
        fft(oddReal, oddImag);

        for (int k = 0; k < n/2; k++) {
            double angle = (-2 * Math.PI * k / n);

            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            double tReal = cos * oddReal[k] - sin * oddImag[k];
            double tImag = sin * oddReal[k] + cos * oddImag[k];

            real[k + n/2] = evenReal[k] - tReal;
            imag[k + n/2] = evenImag[k] - tImag;

            real[k] = evenReal[k] + tReal;
            imag[k] = evenImag[k] + tImag;
        }
    }

    // widmo za pomocą FFT
    static double[] computeMagnitudeSpectrum(double[] signal) {
        int N = signal.length;
        double[] real = Arrays.copyOf(signal, N);
        double[] imag = new double[N];

        fft(real, imag);

        double[] magnitude = new double[N / 2];
        for (int i = 0; i < N / 2; i++) {
            magnitude[i] = Math.sqrt(real[i] * real[i] + imag[i] * imag[i]) /N;
        }
        return magnitude;
    }
}
