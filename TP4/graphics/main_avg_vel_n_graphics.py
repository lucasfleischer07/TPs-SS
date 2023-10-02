import matplotlib.pyplot as plt

from graphics.parse_files import parse_config_json, parse_output_file


def main():
    N_values = [5, 10, 15, 20, 25, 30]
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'
    dt_values = [1.0E-4]
    n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
    phi_dt_difference = {}
    color_list = ['b', 'g', 'r', 'c', 'y', 'm']

    for dt in dt_values:
        index = 0
        for N in N_values:
            if dt == (1.0E-4):
                current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-4" + ".txt")
            elif dt == (1.0E-5):
                current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-5" + ".txt")
            else:
                current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_" + str(dt) + ".txt")

            # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
            aux_vels = []
            for i in current_dt_particle_data.keys():
                if i < 120.00:
                    continue
                vel_difference = 0

                for current_particle in current_dt_particle_data[i]:
                    # print("Current particle id = " + str(current_particle['id']) + ", Next particle id = " + str(next_particle['id']))
                    vel_difference += current_particle['vx']

                aux_vels.append(vel_difference/N)

            phi_dt_difference[N] = aux_vels  # Usamos N como clave en lugar de un índice
            index += 1

    # Calcular el promedio para cada valor de N en phi_dt_difference
    promedio_vel_dict = {N: sum(vels) / len(vels) for N, vels in phi_dt_difference.items()}

    # Crear listas separadas para el eje X y el eje Y
    N_list = list(promedio_vel_dict.keys())
    promedio_vel_list = list(promedio_vel_dict.values())

    aux_prom = []
    for i in range(len(N_values)):
        if i == 2:
            aux_prom.append(promedio_vel_list[4])
        elif i == 4:
            aux_prom.append(promedio_vel_list[2])
        else:
            aux_prom.append(promedio_vel_list[i])

    # Dibujar un punto por cada valor de N en el gráfico de dispersión
    plt.scatter(N_list, aux_prom, marker='o', color='b', label='Puntos')

    # Unir los puntos con una línea
    plt.plot(N_list, aux_prom, linestyle='-', color='g', label='Línea de Unión')

    plt.xlabel('N')
    plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.grid(True)
    plt.savefig('graphs/ej_2_2_1_prom_vel.png')
    plt.show()


if __name__ == "__main__":
    main()