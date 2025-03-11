import numpy as np
import pandas as pd
import scipy.stats
import scipy
import matplotlib.pyplot as plt
import random


#zadanie 1

df = pd.DataFrame({"x": [1, 2, 3, 4, 5], 'y': ['a', 'b', 'a', 'b', 'b']})
srednia_po_y = df.groupby(['y']).mean('x')
print(srednia_po_y)

#zadanie 2

licznosc_atrybutow = df.value_counts('y')
print(licznosc_atrybutow)

#zadanie 3

plik = pd.read_csv('autos.csv')
plik2 = np.loadtxt('autos.csv', delimiter=',', dtype=str)

#zadanie 4

sr_paliwa_po_marce = plik[['make', 'city-mpg', 'highway-mpg']].groupby(['make']).mean('city-mpg', 'highway-mpg')
srednia_paliwa = (sr_paliwa_po_marce['city-mpg'].values + sr_paliwa_po_marce['highway-mpg'].values)/2
sr_paliwa_po_marce['srednia_paliwa'] = srednia_paliwa
print(sr_paliwa_po_marce['srednia_paliwa'])

#zadanie 5

licznosc_fueltype_po_marce = plik.value_counts(['make','fuel-type'])
print(licznosc_fueltype_po_marce)

#zadanie 6

w1 = np.polyfit(plik['length'], plik['city-mpg'], 1)
w2 = np.polyfit(plik['length'], plik['city-mpg'], 2)
#zadanie 7

korelacja = scipy.stats.mstats.pearsonr(plik['city-mpg'], plik['length'])
print(korelacja)

#zadanie 8

plt.plot(plik['length'], plik['city-mpg'], 'ro', label='city-mpg')
plt.plot(plik['length'], np.polyval(w1, plik['length']), label='linear polynominal of best fit')
plt.legend()
plt.show()

# zadanie 9

x1 = plik['length']
y1 = scipy.stats.gaussian_kde(x1)
x2 = np.linspace(x1.min(), x1.max(), 100)

plt.plot(x1, y1(x1), 'bo', label='Próbki')
plt.plot(x2, y1(x2), 'r', label='Estymator Gaussa')
plt.legend()
plt.show()

# zadanie 10

x1 = plik['length']
y1 = scipy.stats.gaussian_kde(x1)
x2 = np.linspace(x1.min(), x1.max(), 100)
x3 = plik['width']
y2 = scipy.stats.gaussian_kde(x3)
x4 = np.linspace(x3.min(), x3.max(), 100)

plt.subplot(2, 1, 1)
plt.plot(x1, y1(x1), 'bo')
plt.plot(x2, y1(x2), 'r')
plt.subplot(2, 1, 2)
plt.plot(x3, y2(x3), 'co')
plt.plot(x4, y2(x4), 'g')
plt.show()

#zadanie 11
