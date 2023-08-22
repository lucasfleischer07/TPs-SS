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

    divided_values = [value / (L ** 2) for value in N_values]

    # Graficar los puntos con barras de error para stats
    ax.errorbar(divided_values, values, yerr=errors, fmt='o', capsize=6, label=f'L = {L}, Iterations = {amount_of_iterations}')

    # Agregar una línea que une los puntos para stats
    ax.plot(divided_values, values, linestyle='-', marker='o', markersize=4, color='blue')

    # Etiquetas de los ejes y título
    ax.set_xlabel('densidad [N/L^2]')
    ax.set_ylabel('va')
    ax.set_title('Gráfico de va en funcion de la densidad')

    # Agregar una leyenda
    ax.legend()

    # Agregar la grilla de fondo
    ax.grid(True)

    ax.set_xlim(0, max(divided_values) + 0.5)


    # Mostrar el gráfico
    plt.show()


def plot_va_time(stats, N, L, amount_of_iterations):
    # Crear una figura y ejes para el gráfico
    fig, ax = plt.subplots()

    # Iterar a través de cada array en va_stats y graficar una línea
    for i, data in enumerate(stats):
        ax.plot(data, label=f'N={N}, L={L}, η={i}')

    # Etiquetas de los ejes y título del gráfico
    ax.set_xlabel('Iteración')
    ax.set_ylabel('vₐ')
    ax.set_title('Evolución de vₐ en función del tiempo')

    # Establecer la escala del eje y en incrementos de 0.1
    ax.set_yticks(np.arange(0, 1.1, 0.1))

    ax.legend()

    # Colocar la leyenda fuera del gráfico
    # Ajustar el diseño para evitar recortes
    # ax.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    # plt.tight_layout()

    # Mostrar el gráfico
    plt.grid()
    plt.show()