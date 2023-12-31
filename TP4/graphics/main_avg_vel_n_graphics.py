import matplotlib.pyplot as plt
import numpy as np
from graphics.parse_files import parse_config_json, parse_output_file


def main():
    N_values = [5, 10, 15, 20, 25, 30]
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'
    output_base_path2 = '../src/main/resources/ex2/Item3_final/output_ex2'

    dt_values = [1.0E-3]
    n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
    phi_dt_difference = {}
    color_list = ['b', 'g', 'r', 'c', 'y', 'm']
    particle_vel_dict = {}

    for j in range(0, 2):
        for dt in dt_values:
            index = 0
            for N in N_values:
                particle_vel_dict[N] = []
                if j == 0:
                    output_file = output_base_path
                else:
                    output_file = output_base_path2

                if dt == (1.0E-4):
                    current_dt_particle_data = parse_output_file(output_file + "_" + str(N) + "_1.0E-4" + ".txt")
                elif dt == (1.0E-5):
                    current_dt_particle_data = parse_output_file(output_file + "_" + str(N) + "_1.0E-5" + ".txt")
                else:
                    current_dt_particle_data = parse_output_file(output_file + "_" + str(N) + "_" + str(dt) + ".txt")


                # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
                aux_vels = []
                for i in current_dt_particle_data.keys():
                    if i < 120.00:
                        continue
                    vel_difference = 0

                    for current_particle in current_dt_particle_data[i]:
                        vel_difference += current_particle['vx']
                        aux = current_particle['vx']
                        particle_vel_dict[N].append(aux)

                    aux_vels.append(vel_difference/N)

                phi_dt_difference[N] = aux_vels  # Usamos N como clave en lugar de un índice
                index += 1

        # Calcular el promedio para cada valor de N en phi_dt_difference
        promedio_vel_dict = {N: sum(vels) / len(vels) for N, vels in phi_dt_difference.items()}

        # Crear listas separadas para el eje X y el eje Y
        N_list = list(promedio_vel_dict.keys())
        promedio_vel_list = list(promedio_vel_dict.values())

        aux_prom = []
        error = []
        for i, N in zip(range(len(N_values)), N_values):
            if j == 0:
                if i == 2:
                    aux_prom.append(promedio_vel_list[4])
                    # error.append(np.std(particle_vel_dict[25]))
                elif i == 4:
                    aux_prom.append(promedio_vel_list[2])
                    # error.append(np.std(particle_vel_dict[N_values[15]]))
                else:
                    aux_prom.append(promedio_vel_list[i])
                # aux_prom.append(promedio_vel_list[i])
                error.append(np.std(particle_vel_dict[N_values[i]]))
            else:
                aux_prom.append(promedio_vel_list[i])
                error.append(np.std(particle_vel_dict[N_values[i]]))


        # Dibujar un punto por cada valor de N en el gráfico de dispersión
        if j == 0:
            plt.scatter(N_list, aux_prom, marker='o', color='b', label='Datos no ordenados')
            plt.errorbar(N_list, aux_prom, yerr=error, fmt='o', capsize=6, ecolor='b')
            plt.plot(N_list, aux_prom, linestyle='-', color='b')
        else:
            plt.scatter(N_list, aux_prom, marker='x', color='r', label='Datos ordenados')
            plt.errorbar(N_list, aux_prom, yerr=error, fmt='x', capsize=6, ecolor='r')
            plt.plot(N_list, aux_prom, linestyle='-', color='r')


    plt.xlabel('N')
    plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.grid(True)
    # plt.savefig('graphs/ej_2_2_1_prom_vel.png')
    plt.savefig('graphs/ej_2_3_prom_vel.png')
    plt.legend()
    plt.show()


if __name__ == "__main__":
    main()