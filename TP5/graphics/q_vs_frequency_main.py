import matplotlib.pyplot as plt
import numpy as np


def get_times(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))

    data = np.array(tiempos)

    return data


def get_qs(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))  # Corrección aquí: append en lugar de asignar

    # data = np.array(tiempos)
    return tiempos


def times_graph(directory):
    x1 = get_times('../src/main/resources/times_frequency_1.txt')
    x2 = get_times('../src/main/resources/times_frequency_2.txt')
    x3 = get_times('../src/main/resources/times_frequency_3.txt')
    x4 = get_times('../src/main/resources/times_frequency_4.txt')
    x5 = get_times('../src/main/resources/times_frequency_5.txt')
    x6 = get_times('../src/main/resources/times_frequency_6.txt')

    error_list = []

    plt.xlabel('Frecuencia (Hz)')
    plt.ylabel('Caudal')

    for x, label in zip([x1, x2, x3, x4, x5, x6], ['5', '10', '15', '20', '30', '50']):
        Q = (len(x))/(x[-1]-x[0])
        f = []
        x_mean = np.mean(x)
        for i in range(len(x)):
            f.append(Q*x[i])  ## dudoso

        S = np.sqrt(np.sum((x - f) ** 2) / (len(x) - 2))

        Sxx = np.sum((x - x_mean) ** 2)

        error = S / np.sqrt(Sxx)
        error_list.append(error)
        plt.scatter(int(label), Q)
        plt.errorbar(int(label), Q, yerr=error, label="w = " + label)

    plt.savefig(directory + 'q_vs_frequency.png')
    plt.clf()


if __name__ == "__main__":
    times_graph('graphs/')