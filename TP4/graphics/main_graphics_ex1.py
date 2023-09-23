import math

import matplotlib.pyplot as plt
import numpy as np

# from utils import calculate_analytical_positions
# from parse_files import parse_output_file

def main():
    output_file_path = "../src/main/resources/output_ex1.txt"

    integration_methods = ["Verlet", "Beeman", "Gear Predictor Corrector"]

    positions = parse_output(output_file_path, integration_methods)

    t = list(positions["Verlet"].keys())  # same for all methods

    # Plot analytical solution
    plt.plot(t, get_analytical_positions(np.array(t)), label="Analítica", linestyle='-')

    for method in integration_methods:
        x = list(positions[method].values())

        if method == "Gear Predictor Corrector":
            linestyle = ':'
        elif method == "Beeman":
            linestyle = '--'
        else:
            linestyle = '-.'

        plt.plot(t, x, label=f"{method}", linestyle=linestyle)

    plt.xlabel("Tiempo (s)", fontsize=20)
    plt.ylabel("Posición (m)", fontsize=20)

    plt.tight_layout()
    plt.legend(loc="lower right")

    plt.show()


def parse_output(outputPath: str, methods: list[str]) -> dict[str, dict[float, float]]:
    """
    Parse the output file and return a dictionary with the
    times and the positions for each integration method.
    """
    with open(outputPath, 'r') as file:
        lines = file.readlines()

    method_idx = -1

    positions = {}
    time = None

    for line in lines:
        data = line.split()

        if len(data) == 1:
            time = float(data[0])

            if time == 0:
                method_idx += 1
                positions[methods[method_idx]] = {}
        else:
            # velocity = float(data[1])
            positions[methods[method_idx]][time] = float(data[0])

    return positions


def get_analytical_positions(t: np.array) -> list[float]:
    """
    Return the analytical position for the given time.
    """
    A = 1.0  # m
    gamma = 100.0  # kg/s
    k = 10 ** 4  # N/m
    m = 70.0  # kg
    return A * np.exp(-gamma * t / (2 * m)) * np.cos(math.sqrt(k / m - gamma ** 2 / (4 * m ** 2)) * t)


if __name__ == '__main__':
    main()
    
#     integration_methods = ["Beeman", "Verlet", "Gear Predictor Corrector"]
#
#     # Diccionario que contiene como clave el metodo y como valor otro diccionario con el tiempo y las posiciones
#     integration_method_positions = parse_output_file(output_file_path, integration_methods)
#     methods_time_list = integration_method_positions["Beeman"].keys()
#
#     analytical_positions_list = calculate_analytical_positions(np.array(methods_time_list))
#     plt.plot(methods_time_list, analytical_positions_list, label="Analítica", linestyle='-')
#
#     # TODO: No esta testeado esto
#     # Define un diccionario que mapea métodos a estilos de línea
#     # method_line_styles = {"Gear Predictor Corrector": ':',"Beeman": '--' }
#     # for current_method in integration_methods:
#     #     x = integration_method_positions[current_method].values()
#     #
#     #     # Utiliza el diccionario para obtener el estilo de línea o '-' como valor predeterminado
#     #     linestyle = method_line_styles.get(current_method, '-')
#     #
#     #     plt.plot(methods_time_list, x, label=f"{current_method}", linestyle=linestyle)
#
#     plt.plot(methods_time_list, calculate_analytical_positions(np.array(methods_time_list)), label="Analítica", linestyle='-')
#     for current_method in integration_methods:
#         x = integration_method_positions[current_method].values()
#         if current_method == "Gear Predictor Corrector":
#             linestyle = ':'
#         elif current_method == "Beeman":
#             linestyle = '--'
#         else:
#             linestyle = '-.'
#
#         plt.plot(methods_time_list, x, label=f"{current_method}", linestyle=linestyle)
#
#     plt.xlabel("Tiempo (s)")
#     plt.ylabel("Posición (m)")
#
#     plt.tight_layout()
#     plt.legend()
#     # plt.legend(loc="lower right")
#
#     plt.show()
#
#
# if __name__ == '__main__':
#     main()

