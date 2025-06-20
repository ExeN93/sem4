// Andrzej Żwirko 55575 214A

package org.example;

import org.jfree.chart.*;
import org.jfree.data.xy.*;
import javax.swing.*;
import java.util.Arrays;

public class zad1 {
    public static void main(String[] args) {
        double f = 10;
        double fs = 800;
        double Tc = 1;
        int H = 10;
        int N = (int)(Tc * fs);

        double[]x = new double[N];
        double[]y = new double[N];
        double[]z = new double[N];

        XYSeries xt = new XYSeries("xt");
        XYSeries yt = new XYSeries("yt");
        XYSeries zt = new XYSeries("zt");

        int countx = 0;
        int ind = 1;
        double alfa = ind * f;
        while (alfa < fs / 2) {
            countx++;
            ind++;
            alfa = ind * f;
        }

        int county = 0;
        ind = 1;
        alfa = (2 * ind - 1) * f;
        while (alfa < fs / 2) {
            county++;
            ind++;
            alfa = (2 * ind - 1) * f;
        }

        System.out.println("prązki dla x: " + countx);
        System.out.println("prązki dla y: " + county);
        System.out.println("prązki dla z: " + county);

        for (int n = 0; n < N; n++) {
            double t = n / fs;
            // piłokształtny
            double xValue = 0;
            for (int k = 1; k <= H; k++) {
                xValue += Math.pow(-1, k + 1) * Math.sin(2 * Math.PI * k * f * t) / k;
            }
            xValue *= 2 / Math.PI;

            // trójkątny
            double yValue = 0;
            for (int k = 1; k <= H; k++) {
                yValue += Math.pow(-1, k - 1) * Math.sin(2 * Math.PI * (2 * k - 1) * f * t) / Math.pow((2 * k - 1), 2);
            }
            yValue *= 8 / Math.pow(Math.PI, 2);

            // prostokątny
            double zValue = 0;
            for (int k = 1; k <= H; k++) {
                zValue += Math.sin(2 * Math.PI * (2 * k - 1) * f * t) / (2 * k - 1);
            }
            zValue *= 4 / Math.PI;

            xt.add(t, xValue);
            yt.add(t, yValue);
            zt.add(t, zValue);

            x[n] = xValue;
            y[n] = yValue;
            z[n] = zValue;
        }

        displayChart(xt, "x(t)", "czas", "amplituda");
        displayChart(yt, "y(t)", "czas", "amplituda");
        displayChart(zt, "z(t)", "czas", "amplituda");

        displaySpectrum(x, fs, "Mx");
        displaySpectrum(y, fs, "My");
        displaySpectrum(z, fs, "Mz");
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
}
