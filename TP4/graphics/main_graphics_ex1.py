import matplotlib.pyplot as plt
import numpy as np

from utils import calculate_analytical_positions
from parse_files import parse_output_file


def main():
    output_file_path = "../src/main/resources/output_ex1.txt"
    integration_methods = ["Beeman", "Verlet", "Gear Predictor Corrector"]

    # Diccionario que contiene como clave el metodo y como valor otro diccionario con el tiempo y las posiciones
    integration_method_positions = parse_output_file(output_file_path, integration_methods)
    methods_time_list = integration_method_positions["Beeman"].keys()

    analytical_positions_list = calculate_analytical_positions(np.array(methods_time_list))
    plt.plot(methods_time_list, analytical_positions_list, label="Analítica", linestyle='-')

    # TODO: No esta testeado esto
    # Define un diccionario que mapea métodos a estilos de línea
    method_line_styles = {"Gear Predictor Corrector": ':',"Beeman": '--' }
    for current_method in integration_methods:
        x = integration_method_positions[current_method].values()

        # Utiliza el diccionario para obtener el estilo de línea o '-' como valor predeterminado
        linestyle = method_line_styles.get(current_method, '-')

        plt.plot(methods_time_list, x, label=f"{current_method}", linestyle=linestyle)

    # plt.plot(methods_time_list, calculate_analytical_positions(np.array(methods_time_list)), label="Analítica", linestyle='-')
    # for current_method in integration_methods:
    #     x = integration_method_positions[current_method].values()
    #     if current_method == "Gear Predictor Corrector":
    #         linestyle = ':'
    #     elif current_method == "Beeman":
    #         linestyle = '--'
    #     else:
    #         linestyle = '-.'
    #
    #     plt.plot(methods_time_list, x, label=f"{current_method}", linestyle=linestyle)

    plt.xlabel("Tiempo (s)")
    plt.ylabel("Posición (m)")

    plt.tight_layout()
    plt.legend()
    # plt.legend(loc="lower right")

    plt.show()


if __name__ == '__main__':
    main()