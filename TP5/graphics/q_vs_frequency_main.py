import matplotlib.pyplot as plt
import numpy as np


def get_output_data_times(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))

    data = np.array(tiempos)
    return data


def get_frequency_data(MHU, D):
    x1 = get_output_data_times('../src/main/resources/itemA/times_frequency_1' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x2 = get_output_data_times('../src/main/resources/itemA/times_frequency_2' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x3 = get_output_data_times('../src/main/resources/itemA/times_frequency_3' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x4 = get_output_data_times('../src/main/resources/itemA/times_frequency_4' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x5 = get_output_data_times('../src/main/resources/itemA/times_frequency_5' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x6 = get_output_data_times('../src/main/resources/itemA/times_frequency_6' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')

    return x1, x2, x3, x4, x5, x6


def get_hole_size_data(MHU):
    x1 = get_output_data_times('../src/main/resources/itemB/times_hole_size_1' + '_mhu_' + str(MHU) + '.txt')
    x2 = get_output_data_times('../src/main/resources/itemB/times_hole_size_2' + '_mhu_' + str(MHU) + '.txt')
    x3 = get_output_data_times('../src/main/resources/itemB/times_hole_size_3' + '_mhu_' + str(MHU) + '.txt')
    x4 = get_output_data_times('../src/main/resources/itemB/times_hole_size_4' + '_mhu_' + str(MHU) + '.txt')

    return x1, x2, x3, x4


# Para todas las frecuencias
def times_graph_frequency(directory, x_values, y_values, MHU_1, MHU_2, D):
    labels = ['5', '10', '15', '20', '30', '50']
    values_x = []
    errors_x = []
    values_y = []
    errors_y = []

    for x, y, label in zip(x_values, y_values, labels):
        # Procesamiento para el primer conjunto de datos (x)
        Q_x = len(x) / (x[-1] - x[0])
        f_x = []
        x_mean = np.mean(x)

        for i in range(len(x)):
            f_x.append(Q_x * x[i])

        values_x.append(Q_x)
        S_x = np.sqrt(np.sum((x - f_x) ** 2) / (len(x) - 2))
        Sxx_x = np.sum((x - x_mean) ** 2)
        error_x = S_x / np.sqrt(Sxx_x)
        errors_x.append(error_x)

        # Procesamiento para el segundo conjunto de datos (y)
        Q_y = len(y) / (y[-1] - y[0])
        f_y = []
        y_mean = np.mean(y)

        for i in range(len(y)):
            f_y.append(Q_y * y[i])

        values_y.append(Q_y)
        S_y = np.sqrt(np.sum((y - f_y) ** 2) / (len(y) - 2))
        Sxx_y = np.sum((y - y_mean) ** 2)
        error_y = S_y / np.sqrt(Sxx_y)
        errors_y.append(error_y)

    # Graficar ambos conjuntos de datos
    plt.plot(labels, values_x, linestyle='-', marker='o', color='b', label='μ = ' + str(MHU_1))
    plt.errorbar(labels, values_x, yerr=errors_x, fmt='o', color='b')

    plt.plot(labels, values_y, linestyle='-', marker='o', color='r', label='μ = ' + str(MHU_2))
    plt.errorbar(labels, values_y, yerr=errors_y, fmt='o', color='r')

    plt.xlabel('w ($\\frac{rad}{s}$)')
    plt.ylabel('Caudal ($\\frac{particula}{s}$)')
    plt.xticks(labels)
    plt.legend()
    plt.savefig(directory + 'q_vs_frequency' + '_D_' + str(D) + '_mhu_' + str(MHU_1) + '_mhu_' + str(MHU_2) + '.png')
    plt.clf()


# PARA DISTINTOS VALORES DE RENDIJA
def times_graph_aperturas(directory, x_values, y_values, MHU_1, MHU_2, D):
    labels = ['3', '4', '5', '6']
    values_x = []
    errors_x = []
    values_y = []
    errors_y = []

    for x, y, label in zip(x_values, y_values, labels):
        # Procesamiento para el primer conjunto de datos (x)
        Q_x = len(x) / (x[-1] - x[0])
        f_x = []
        x_mean = np.mean(x)

        for i in range(len(x)):
            f_x.append(Q_x * x[i])

        values_x.append(Q_x)
        S_x = np.sqrt(np.sum((x - f_x) ** 2) / (len(x) - 2))
        Sxx_x = np.sum((x - x_mean) ** 2)
        error_x = S_x / np.sqrt(Sxx_x)
        errors_x.append(error_x)

        # Procesamiento para el segundo conjunto de datos (y)
        Q_y = len(y) / (y[-1] - y[0])
        f_y = []
        y_mean = np.mean(y)

        for i in range(len(y)):
            f_y.append(Q_y * y[i])

        values_y.append(Q_y)
        S_y = np.sqrt(np.sum((y - f_y) ** 2) / (len(y) - 2))
        Sxx_y = np.sum((y - y_mean) ** 2)
        error_y = S_y / np.sqrt(Sxx_y)
        errors_y.append(error_y)

    # Graficar ambos conjuntos de datos
    plt.plot(labels, values_x, linestyle='-', marker='o', color='b', label='μ = ' + str(MHU_1))
    plt.errorbar(labels, values_x, yerr=errors_x, fmt='o', color='b')

    plt.plot(labels, values_y, linestyle='-', marker='o', color='r', label='μ = ' + str(MHU_2))
    plt.errorbar(labels, values_y, yerr=errors_y, fmt='o', color='r')

    plt.xlabel('Ancho de apertura de rendija (cm)')
    plt.ylabel('Caudal ($\\frac{particula}{s}$)')
    plt.xticks(labels)
    plt.legend()
    plt.savefig(directory + 'q_vs_rendija' + '_mhu_' + str(MHU_1) + '_mhu_' + str(MHU_2) + '.png')
    plt.clf()


def main():
    D = 5.0
    MHU_1 = 0.7
    MHU_2 = 0.5

    x1, x2, x3, x4, x5, x6 = get_frequency_data(MHU_1, D)
    y1, y2, y3, y4, y5, y6 = get_frequency_data(MHU_2, D)
    times_graph_frequency('graphs/', [x1, x2, x3, x4, x5, x6], [y1, y2, y3, y4, y5, y6], MHU_1, MHU_2, D)

    x1, x2, x3, x4 = get_hole_size_data(MHU_1)
    y1, y2, y3, y4 = get_hole_size_data(MHU_2)
    times_graph_aperturas('graphs/', [x1, x2, x3, x4], [y1, y2, y3, y4], MHU_1, MHU_2, D)


if __name__ == "__main__":
    main()
