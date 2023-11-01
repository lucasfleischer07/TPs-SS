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


# PARA TODAS LAS FRECUENCIAS
def times_graph_frequency(directory, MHU, D):
    x1 = get_times('../src/main/resources/itemA/times_frequency_1' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x2 = get_times('../src/main/resources/itemA/times_frequency_2' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x3 = get_times('../src/main/resources/itemA/times_frequency_3' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x4 = get_times('../src/main/resources/itemA/times_frequency_4' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x5 = get_times('../src/main/resources/itemA/times_frequency_5' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x6 = get_times('../src/main/resources/itemA/times_frequency_6' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')

    colors = ['b', 'g', 'r', 'c', 'm', 'y']
    for x, label, color in zip([x1, x2, x3, x4, x5, x6], ['5', '10', '15', '20', '30', '50'], colors):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=1000)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Calcular la regresión lineal
        x_values = bordes[:-1]
        y_values = conteos_acumulados

        # Graficar el histograma acumulativo
        plt.step(bordes[:-1], conteos_acumulados, where='post', label=label + " $\\frac{rad}{s}$", color=color)

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend()

    plt.savefig(directory + 'curva_de_descarga_frecuencias' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.png')
    plt.clf()


# PARA DISTINTOS VALORES DE RENDIJA
def times_graph_aperturas(directory, MHU):
    x1 = get_times('../src/main/resources/itemB/times_hole_size_1' + '_mhu_' + str(MHU) + '.txt')
    x2 = get_times('../src/main/resources/itemB/times_hole_size_2' + '_mhu_' + str(MHU) + '.txt')
    x3 = get_times('../src/main/resources/itemB/times_hole_size_3' + '_mhu_' + str(MHU) + '.txt')
    x4 = get_times('../src/main/resources/itemB/times_hole_size_4' + '_mhu_' + str(MHU) + '.txt')

    colors = ['b', 'g', 'r', 'c']  # Puedes ajustar los colores según tus preferencias
    for x, label, color in zip([x1, x2, x3, x4], ['3', '4', '5', '6'], colors):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=700)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Ajustar los datos para que comiencen en (0,0)
        x_values = bordes[:-1] - bordes[0]
        y_values = conteos_acumulados

        # Graficar el histograma acumulativo con el mismo color
        plt.step(x_values, y_values, where='post', label=label + " cm", color=color)

    if MHU == 0.5:
        plt.xlim(0, 700)  # Limito el eje x con mhu=0.5 como nos pidieron

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend()

    plt.savefig(directory + 'curva_de_descarga_aperturas' + '_mhu_' + str(MHU) + '.png')
    plt.clf()


if __name__ == "__main__":
    MHU = 0.5
    D = 5.0

    times_graph_frequency('graphs/', MHU, D)
    times_graph_aperturas('graphs/', MHU)
