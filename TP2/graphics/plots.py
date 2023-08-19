import matplotlib.pyplot as plt
import numpy as np


def plot_va_eta(stats_500, stats_400, N_values, L_values, amount_of_iterations):
    # Desempaquetar los datos en arreglos separados
    values_500 = np.array([entry[0] for entry in stats_500])
    errors_500 = np.array([entry[1] for entry in stats_500])

    # Desempaquetar los datos en arreglos separados para stats_400
    values_400 = np.array([entry[0] for entry in stats_400])
    errors_400 = np.array([entry[1] for entry in stats_400])

    # Crear la figura y el eje
    fig, ax = plt.subplots()

    # Graficar los puntos con barras de error para stats_500
    ax.errorbar(np.arange(len(values_500)), values_500, yerr=errors_500, fmt='o', capsize=5, label=f'N = {N_values[1]}, L = {L_values[1]}, Iterations = {amount_of_iterations}')

    # Graficar los puntos con barras de error para stats_400
    ax.errorbar(np.arange(len(values_400)), values_400, yerr=errors_400, fmt='x', capsize=5, label=f'N = {N_values[0]}, L = {L_values[0]}, Iterations = {amount_of_iterations}')

    # Agregar una línea que une los puntos para stats_500
    ax.plot(np.arange(len(values_500)), values_500, linestyle='-', marker='o', markersize=4, color='blue')

    # Agregar una línea que une los puntos para stats_400
    ax.plot(np.arange(len(values_400)), values_400, linestyle='-', marker='x', markersize=4, color='orange')

    # Etiquetas de los ejes y título
    ax.set_xlabel('ruido')
    ax.set_ylabel('va')
    ax.set_title('Gráfico de va en funcion del ruido')

    # Agregar una leyenda
    ax.legend()

    # Agregar la grilla de fondo
    ax.grid(True)

    # Mostrar el gráfico
    plt.show()


def plot_va_rho(stats, N_values, L, amount_of_iterations):

    # Desempaquetar los datos en arreglos separados para stats_400
    values = np.array([entry[0] for entry in stats])
    errors = np.array([entry[1] for entry in stats])

    # Crear la figura y el eje
    fig, ax = plt.subplots()

    # Graficar los puntos con barras de error para stats
    ax.errorbar(np.arange(1, len(values) + 1), values, yerr=errors, fmt='o', capsize=6, label=f'L = {L}, Iterations = {amount_of_iterations}')

    # Agregar una línea que une los puntos para stats
    ax.plot(np.arange(1, len(values) + 1), values, linestyle='-', marker='o', markersize=4, color='blue')

    # Etiquetas de los ejes y título
    ax.set_xlabel('densidad')
    ax.set_ylabel('va')
    ax.set_title('Gráfico de va en funcion de la densidad')

    # Agregar una leyenda
    ax.legend()

    # Agregar la grilla de fondo
    ax.grid(True)

    # Mostrar el gráfico
    plt.show()
