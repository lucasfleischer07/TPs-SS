import matplotlib.pyplot as plt
import numpy as np
from scipy.stats import linregress


def get_times(path):
    # Leer el archivo de tiempos
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))  # Corrección aquí: append en lugar de asignar

    data = np.array(tiempos)

    return data


# PARA TODAS LAS FRECUENCIAS
def times_graph_frequency(directory):
    D = 3.0
    MHU = 0.7

    x1 = get_times('../src/main/resources/itemA/v5/times_frequency_1' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x2 = get_times('../src/main/resources/itemA/v5/times_frequency_2' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x3 = get_times('../src/main/resources/itemA/v5/times_frequency_3' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x4 = get_times('../src/main/resources/itemA/v5/times_frequency_4' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x5 = get_times('../src/main/resources/itemA/v5/times_frequency_5' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    x6 = get_times('../src/main/resources/itemA/v5/times_frequency_6' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt')
    colors = ['b', 'g', 'r', 'c', 'm', 'y']
    for x, label, color in zip([x1, x2, x3, x4, x5, x6], ['5', '10', '15', '20', '30', '50'], colors):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=1000)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Calcular la regresión lineal
        x_values = bordes[:-1]
        y_values = conteos_acumulados
        slope, intercept, r_value, p_value, std_err = linregress(x_values, y_values)

        # Graficar el histograma acumulativo
        plt.step(bordes[:-1], conteos_acumulados, where='post', label=label + " Hz", color=color)

        # Graficar la línea de regresión
        # intercept=0
        # plt.plot(x_values, intercept + slope * x_values, label=f'Regresión (R²={r_value**2:.2f})', linestyle='--',  color=color)

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend()

    plt.savefig(directory + 'curva_de_descarga_frecuencias' + '_D_' + str(D) + '_mhu_' + str(MHU) + '.png')
    plt.clf()


# PARA DISTINTOS VALORES DE RENDIJA
def times_graph_aperturas(directory):
    MHU = 0.7

    x1 = get_times('../src/main/resources/itemB/times_hole_size_1' + '_mhu_' + str(MHU) + '.txt')
    x2 = get_times('../src/main/resources/itemB/times_hole_size_2' + '_mhu_' + str(MHU) + '.txt')
    x3 = get_times('../src/main/resources/itemB/times_hole_size_3' + '_mhu_' + str(MHU) + '.txt')
    x4 = get_times('../src/main/resources/itemB/times_hole_size_4' + '_mhu_' + str(MHU) + '.txt')

    colors = ['b', 'g', 'r', 'c']  # Puedes ajustar los colores según tus preferencias

    for x, label, color in zip([x1, x2, x3, x4], ['3', '4', '5', '6'], colors):
        # Calcular los conteos de eventos en cada intervalo
        conteos, bordes = np.histogram(x, bins=1000)

        # Calcular los conteos acumulativos
        conteos_acumulados = np.cumsum(conteos)

        # Ajustar los datos para que comiencen en (0,0)
        x_values = bordes[:-1] - bordes[0]
        y_values = conteos_acumulados

        # Calcular la regresión lineal
        slope, intercept, r_value, p_value, std_err = linregress(x_values, y_values)

        # Graficar el histograma acumulativo con el mismo color
        plt.step(x_values, conteos_acumulados, where='post', label=label + " cm", color=color)

        # intercept = 0
        # # Graficar la línea de regresión con el mismo color
        # plt.plot(x_values, intercept + slope * x_values, label=f'Regresión para D = ' + label, linestyle='--', color=color)

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend()

    plt.savefig(directory + 'curva_de_descarga_aperturas' + '_mhu_' + str(MHU) + '.png')
    plt.clf()





if __name__ == "__main__":
    times_graph_frequency('graphs/')
    times_graph_aperturas('graphs/')
