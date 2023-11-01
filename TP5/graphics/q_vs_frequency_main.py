import matplotlib.pyplot as plt
import numpy as np
from scipy.stats import linregress


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

# Para todas las frecuencias
def times_graph_frequency(directory):
    D = 5.0
    MHU = 0.7

    x1 = get_times('../src/main/resources/itemA/times_frequency_1' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x2 = get_times('../src/main/resources/itemA/times_frequency_2' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x3 = get_times('../src/main/resources/itemA/times_frequency_3' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x4 = get_times('../src/main/resources/itemA/times_frequency_4' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x5 = get_times('../src/main/resources/itemA/times_frequency_5' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x6 = get_times('../src/main/resources/itemA/times_frequency_6' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')

    labels = ['5', '10', '15', '20', '30', '50']

    plt.xlabel('Frecuencia $\\frac{rads}{s}$')
    plt.ylabel('Caudal')

    values = []
    errors = []
    for x, label in zip([x1, x2, x3, x4, x5, x6], labels):
        Q = (len(x)) / (x[-1] - x[0])
        f = []
        x_mean = np.mean(x)
        for i in range(len(x)):
            f.append(Q * x[i])

        values.append(Q)
        S = np.sqrt(np.sum((x - f) ** 2) / (len(x) - 2))
        Sxx = np.sum((x - x_mean) ** 2)
        error = S / np.sqrt(Sxx)
        errors.append(error)


    # Unir los puntos con una línea recta
    plt.plot(labels, values, linestyle='-', marker='o', color='b')
    plt.errorbar(labels, values, yerr=errors, fmt='o')

    # Establece los valores específicos en el eje x (5 en 5)
    plt.xticks(labels)
    plt.savefig(directory + 'q_vs_frequency' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.png')
    plt.clf()


# PARA DISTINTOS VALORES DE RENDIJA
def times_graph_aperturas(directory):
    MHU = 0.7

    x1 = get_times('../src/main/resources/itemB/times_hole_size_1' + '_mhu_' + str(MHU) + '.txt')
    x2 = get_times('../src/main/resources/itemB/times_hole_size_2' + '_mhu_' + str(MHU) + '.txt')
    x3 = get_times('../src/main/resources/itemB/times_hole_size_3' + '_mhu_' + str(MHU) + '.txt')
    x4 = get_times('../src/main/resources/itemB/times_hole_size_4' + '_mhu_' + str(MHU) + '.txt')

    labels = ['3', '4', '5', '6']

    plt.xlabel('Ancho de apertura de rendija (cm)')
    plt.ylabel('Caudal')

    values = []
    errors = []
    for x, label in zip([x1, x2, x3, x4], labels):
        Q = (len(x)) / (x[-1] - x[0])
        f = []
        x_mean = np.mean(x)
        for i in range(len(x)):
            f.append(Q * x[i])

        values.append(Q)
        S = np.sqrt(np.sum((x - f) ** 2) / (len(x) - 2))
        Sxx = np.sum((x - x_mean) ** 2)
        error = S / np.sqrt(Sxx)
        errors.append(error)

    # Unir los puntos con una línea recta
    plt.plot(labels, values, linestyle='-', marker='o', color='b')
    plt.errorbar(labels, values, yerr=errors, fmt='o')

    # Establece los valores específicos en el eje x (5 en 5)
    plt.xticks(labels)
    plt.savefig(directory + 'q_vs_rendija' + '_mhu_' + str(MHU) + '.png')
    plt.clf()


if __name__ == "__main__":
    times_graph_frequency('graphs/')
    times_graph_aperturas('graphs/')