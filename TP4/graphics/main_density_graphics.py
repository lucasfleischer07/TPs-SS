from matplotlib import pyplot as plt

from parse_files import parse_output_file, parse_config_json


def main():
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'
    dt_value = 1.0E-3
    N_values = [5,10,15,20,25,30]
    n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
    n_map_for_density = {}
    n_map_for_velocity = {}
    color_list = ['m', 'b', 'g', 'r', 'c', 'y']

    for N in N_values:
        n_map_for_density[N] = []
        n_map_for_velocity[N] = []
        current_dt = dt_value
        if current_dt == (1.0E-4):
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-4" + "_item4.txt")
        elif current_dt == (1.0E-5):
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-5" + "_item4.txt")
        else:
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_" + str(current_dt) + "_item4.txt")

        # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
        aux_density = []
        aux_velocity = []
        for time in current_dt_particle_data.keys():
            particles_list = current_dt_particle_data[time]
            for current_particle_index in range(len(particles_list)):
                density_value = 0
                velocity_value = 0
                current_particle = particles_list[current_particle_index]

                if current_particle_index > 0:
                    previous_particle = particles_list[current_particle_index - 1]
                else:  # Caso de que sea la primer particula, la anterior es la ultima particula
                    previous_particle = particles_list[-1]

                if current_particle_index < len(particles_list) - 1:
                    next_particle = particles_list[current_particle_index+1]
                else:  # Caso de que sea la ultima particula, la sigueinte seria la primer particula
                    next_particle = particles_list[0]

                density_value = 1/((abs(current_particle['x'] - previous_particle['x'])) + (abs(current_particle['x'] - next_particle['x'])))
                velocity_value = current_particle['vx']
                n_map_for_density[N].append(density_value)
                n_map_for_velocity[N].append(velocity_value)

    plt.figure(figsize=(10, 6))
    for i, N in enumerate(N_values):
        plt.scatter(n_map_for_velocity[N], n_map_for_density[N], marker='o', label=f'N = {N}', s=1, color=color_list[i])

    plt.xlabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.ylabel('Densidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.legend(scatterpoints=1, markerscale=5)
    plt.grid(True)
    plt.savefig("graphs/ex2_4_0.001.png")
    plt.show()


if __name__ == "__main__":
    main()