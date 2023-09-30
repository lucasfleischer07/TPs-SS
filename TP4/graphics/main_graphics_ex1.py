import math

import matplotlib.pyplot as plt
import numpy as np


#si llega a fallar puede ser esta funcion. Si es asi, descomentar la de aca abajo
from graphics.utils import calculate_analytical_positions


# from utils import calculate_analytical_positions
# from parse_files import parse_output_file

def main():
    output_file_path = "../src/main/resources/ex1/output_ex1.txt"

    methods_to_use = ["Verlet", "Beeman", "Gear Predictor Corrector"]

    positions = parse_output(output_file_path, methods_to_use)

    t = list(positions["Verlet"].keys())  #mismo lugar en cada uno de los metodos

    # Ejercicio 1.1 oscilator
    plt.plot(t, calculate_analytical_positions(np.array(t)), label="Analítica", linestyle='-')

    for method in methods_to_use:
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


def parse_output(outputFilePath: str, methods: list[str]) -> dict[str, dict[float, float]]:

    with open(outputFilePath, 'r') as file:
        lines = file.readlines()

    method_aux_idx = -1

    positions = {}
    time = None

    for line in lines:
        data = line.split()

        if len(data) == 1:
            time = float(data[0])

            if time == 0:
                method_aux_idx += 1
                positions[methods[method_aux_idx]] = {}
        else:
            positions[methods[method_aux_idx]][time] = float(data[0])

    return positions


# def analytical_results(t: np.array) -> list[float]:
#     # Retorna posicion para tiempo dado. Usamos parametros de consigna
#     A = 1.0  # m
#     gamma = 100.0  # kg/s
#     k = 10 ** 4  # N/m
#     m = 70.0  # kg
#
#     #Formula en teorica
#     return A * np.exp(-gamma * t / (2 * m)) * np.cos(math.sqrt(k / m - gamma ** 2 / (4 * m ** 2)) * t)


if __name__ == '__main__':
    main()
    

