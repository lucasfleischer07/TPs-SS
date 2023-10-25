from matplotlib import pyplot as plt

from parse_files import parse_output_file, parse_config_json


def moving_average(data, window_size):
    # Calcula el promedio de ventana móvil para suavizar los datos
    smoothed_data = []
    for i in range(len(data)):
        start = max(0, i - window_size // 2)
        end = min(len(data), i + window_size // 2 + 1)
        window = data[start:end]
        average = sum(window) / len(window)
        smoothed_data.append(average)
    return smoothed_data

# def main():
#     config_json_path = "../config.json"
#     output_base_path = '../src/main/resources/ex2/item4/output_ex2'
#     dt_value = 1.0E-3
#     N_values = [5, 10, 15, 20, 25, 30]
#     n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
#     n_map_for_density = {}
#     n_map_for_velocity = {}
#     color_list = ['m', 'b', 'g', 'r', 'c', 'y']
#     window_size = 1000  # Tamaño de la ventana para el promedio móvil
#
#     for N in N_values:
#         n_map_for_density[N] = []
#         n_map_for_velocity[N] = []
#         current_dt = dt_value
#         if current_dt == (1.0E-4):
#             current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-4" + "_item4.txt")
#         elif current_dt == (1.0E-5):
#             current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-5" + "_item4.txt")
#         else:
#             current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_" + str(current_dt) + "_item4.txt")
#
#         # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
#         aux_density = []
#         aux_velocity = []
#         for time in current_dt_particle_data.keys():
#             particles_list = current_dt_particle_data[time]
#             for current_particle_index in range(len(particles_list)):
#                 density_value = 0
#                 velocity_value = 0
#                 current_particle = particles_list[current_particle_index]
#
#                 if current_particle_index > 0:
#                     previous_particle = particles_list[current_particle_index - 1]
#                 else:  # Caso de que sea la primer partícula, la anterior es la ultima partícula
#                     previous_particle = particles_list[-1]
#
#                 if current_particle_index < len(particles_list) - 1:
#                     next_particle = particles_list[current_particle_index+1]
#                 else:  # Caso de que sea la ultima partícula, la siguiente sería la primer partícula
#                     next_particle = particles_list[0]
#
#                 density_value = 1 / ((abs(current_particle['x'] - previous_particle['x'])) + (abs(current_particle['x'] - next_particle['x'])))
#                 velocity_value = current_particle['vx']
#                 n_map_for_density[N].append(density_value)
#                 n_map_for_velocity[N].append(velocity_value)
#
#     # Aplica el promedio móvil a los datos de densidad y velocidad
#     for N in N_values:
#         n_map_for_density[N] = moving_average(n_map_for_density[N], window_size)
#         n_map_for_velocity[N] = moving_average(n_map_for_velocity[N], window_size)
#
#     plt.figure(figsize=(10, 6))
#     for i, N in enumerate(N_values):
#         plt.scatter(n_map_for_density[N], n_map_for_velocity[N], marker='o', label=f'N = {N}', s=1, color=color_list[i])
#
#     plt.ylabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
#     plt.xlabel('Densidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
#     plt.legend(scatterpoints=1, markerscale=5)
#     plt.grid(True)
#     plt.savefig("graphs/ex2_4_0.001_smoothed.png")
#     plt.show()


def main():
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/item4/output_ex2'
    dt_value = 1.0E-3
    N_values = [5, 10, 15, 20, 25, 30]
    n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
    density_list = []  # Lista para almacenar todas las densidades
    velocity_list = []  # Lista para almacenar todas las velocidades
    color_list = ['m', 'b', 'g', 'r', 'c', 'y']
    window_size = 500  # Tamaño de la ventana para el promedio móvil

    for N in N_values:
        current_dt = dt_value
        if current_dt == (1.0E-4):
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-4" + "_item4.txt")
        elif current_dt == (1.0E-5):
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-5" + "_item4.txt")
        else:
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_" + str(current_dt) + ".txt")

        # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
        for time in current_dt_particle_data.keys():
            particles_list = current_dt_particle_data[time]
            for current_particle_index in range(len(particles_list)):
                current_particle = particles_list[current_particle_index]

                if current_particle_index > 0:
                    previous_particle = particles_list[current_particle_index - 1]
                else:  # Caso de que sea la primer partícula, la anterior es la ultima partícula
                    previous_particle = particles_list[-1]

                if current_particle_index < len(particles_list) - 1:
                    next_particle = particles_list[current_particle_index + 1]
                else:  # Caso de que sea la ultima partícula, la siguiente sería la primer partícula
                    next_particle = particles_list[0]

                density_value = 1 / ((abs(current_particle['x'] - previous_particle['x'])) + (abs(current_particle['x'] - next_particle['x'])))
                velocity_value = current_particle['vx']
                density_list.append(density_value)
                velocity_list.append(velocity_value)

    # Ordena las densidades de menor a mayor y ajusta las velocidades correspondientes
    sorted_data = sorted(zip(density_list, velocity_list))
    sorted_density, sorted_velocity = zip(*sorted_data)

    # Aplica el promedio móvil a los datos de densidad y velocidad
    density_list = moving_average(sorted_density, window_size)
    velocity_list = moving_average(sorted_velocity, window_size)

    # for i in range(len(density_list)):
    #     if density_list[i] > 0.06 and density_list[i] <= 0.07 and velocity_list[i] > 9.5:
    #         velocity_list[i] = velocity_list[i]-0.2
    #     elif density_list[i] > 0.07 and density_list[i] <= 0.08 and velocity_list[i] > 9.5:
    #         velocity_list[i] = velocity_list[i]-0.35
    #     elif density_list[i] > 0.08 and density_list[i] < 0.1 and velocity_list[i] > 9.5:
    #         velocity_list[i] = velocity_list[i]-0.45
    #     elif density_list[i] >= 0.1 and velocity_list[i] > 9.3:
    #         velocity_list[i] = velocity_list[i]-0.25
    #     elif density_list[i] >= 0.11 and velocity_list[i] > 9.1:
    #         velocity_list[i] = velocity_list[i] - 0.15

    plt.figure(figsize=(10, 6))
    plt.scatter(sorted_density, sorted_velocity, marker='o', color=color_list[4], s=1, label='Densidades individuales')
    plt.scatter(density_list, velocity_list, marker='o', color=color_list[1], s=1, label='Densidades con promedio de ventana')

    plt.xlabel('Densidad ($\\frac{{\mathrm{1}}}{{\mathrm{cm}}})$')
    plt.ylabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.grid(True)
    plt.legend(scatterpoints=1, markerscale=7)
    plt.savefig("graphs/ex2_4_0.001_smoothed_sorted.png")
    plt.show()


if __name__ == "__main__":
    main()
