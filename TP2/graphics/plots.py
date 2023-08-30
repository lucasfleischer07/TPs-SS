import matplotlib.pyplot as plt
import numpy as np


def plot_va_eta(stats_100, stats_400, N_values, L_values, amount_of_iterations):
    # Desempaquetar los datos en arreglos separados
    values_100 = np.array([entry[0] for entry in stats_100])
    errors_100 = np.array([entry[1] for entry in stats_100])

    # Desempaquetar los datos en arreglos separados para stats_400
    values_400 = np.array([entry[0] for entry in stats_400])
    errors_400 = np.array([entry[1] for entry in stats_400])

    eta_values = [0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5]

    # Crear la figura y el eje
    fig, ax = plt.subplots()

    # Graficar los puntos con barras de error para stats_100
    ax.errorbar(eta_values, values_100, yerr=errors_100, fmt='o', capsize=5, label=f'N = {N_values[0]}, L = {L_values[0]}')

    # Graficar los puntos con barras de error para stats_400
    ax.errorbar(eta_values, values_400, yerr=errors_400, fmt='o', capsize=5, label=f'N = {N_values[1]}, L = {L_values[1]}')

    # Agregar una línea que une los puntos para stats_100
    ax.plot(eta_values, values_100, linestyle='-', marker='o', markersize=4, color='blue')

    # Agregar una línea que une los puntos para stats_400
    ax.plot(eta_values, values_400, linestyle='-', marker='o', markersize=4, color='orange')

    # Etiquetas de los ejes y título
    ax.set_xlabel('ruido')
    ax.set_ylabel('vₐ')
    ax.set_title('Gráfico de va en función del ruido')

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
    ax.errorbar(divided_values, values, yerr=errors, fmt='o', capsize=6)

    # Agregar una línea que une los puntos para stats
    ax.plot(divided_values, values, linestyle='-', marker='o', markersize=4, color='blue')

    # Etiquetas de los ejes y título
    ax.set_xlabel('Densidad [$\\frac{N}{{L^2}}$]')
    ax.set_ylabel('Parámetro de orden (vₐ)')
    ax.set_title('Gráfico de va en función de la densidad')

    # Agregar una leyenda
    # ax.legend()

    # Agregar la grilla de fondo
    ax.grid(True)

    ax.set_xlim(0, max(divided_values) + 0.5)


    # Mostrar el gráfico
    plt.show()


def plot_va_time_noise(stats, N, L):
    # Crear una figura y ejes para el gráfico
    fig, ax = plt.subplots()

    # Iterar a través de cada array en va_stats y graficar una línea
    for i, data in enumerate(stats):
        if i == 0:
            ax.plot(data, label=f'η={0}')
        elif i == 1:
            ax.plot(data, label=f'η={1.5}')
        elif i == 2:
            ax.plot(data, label=f'η={3.5}')

    # Etiquetas de los ejes y título del gráfico
    ax.set_xlabel('Iteraciones')
    ax.set_ylabel('Parámetro de orden (vₐ)')
    ax.set_title('Evolución de vₐ en función del tiempo')

    # Establecer la escala del eje y en incrementos de 0.1
    ax.set_yticks(np.arange(0, 1.1, 0.1))

    ax.legend()

    # Mostrar el gráfico
    plt.grid()
    plt.show()


def plot_va_time_density(stats, N_values, L, eta):
    # Crear una figura y ejes para el gráfico
    fig, ax = plt.subplots()

    # Iterar a través de cada array en va_stats y graficar una línea
    for i, data in enumerate(stats):
        ax.plot(data, label=f'ρ={(N_values[i])/(L*L)}, N={N_values[i]}, L={L}, η={eta}')

    # Etiquetas de los ejes y título del gráfico
    ax.set_xlabel('tiempo')
    ax.set_ylabel('vₐ')
    ax.set_title('Evolución de vₐ en función del tiempo')

    # Establecer la escala del eje y en incrementos de 0.1
    ax.set_yticks(np.arange(0, 1.1, 0.1))

    ax.legend()

    # Mostrar el gráfico
    plt.grid()
    plt.show()