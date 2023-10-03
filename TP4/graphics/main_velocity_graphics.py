import matplotlib.pyplot as plt

from graphics.parse_files import parse_config_json, parse_output_file


def main():
    N_values = [5,10,15,20,25,30]
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'
    dt_values = [1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5]
    n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
    phi_dt_difference = {}
    color_list = ['b', 'g', 'r', 'c', 'y', 'm']
    # color_list = ['navy', 'darkgreen', 'maroon', 'teal', 'gold', 'purple']

    for dt in dt_values:
        index = 0
        for N in N_values:
            if dt == (1.0E-4):
                # current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-4" + ".txt")
                current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-4" + ".txt")
            elif dt == (1.0E-5):
                current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_1.0E-5" + ".txt")
            else:
                current_dt_particle_data = parse_output_file(output_base_path + "_" + str(N) + "_" + str(dt) + ".txt")

            # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
            aux_vels = []
            for i in current_dt_particle_data.keys():
                vel_difference = 0

                for current_particle in current_dt_particle_data[i]:
                    # print("Current particle id = " + str(current_particle['id']) + ", Next particle id = " + str(next_particle['id']))
                    vel_difference += current_particle['vx']

                aux_vels.append(vel_difference/N)

            phi_dt_difference[index] = aux_vels
            # plt.scatter([i*0.1 for i in range(0, 1801)], aux_vels, marker='o', linestyle='-', color=color_list[index-1],label=f'N= {N}')
            plt.plot([i * 0.1 for i in range(0, 1801)], aux_vels, linestyle='-', color=color_list[index - 1],label=f'N= {N}')
            index += 1

        # print(phi_dt_difference)
        plt.xlabel('Tiempo (s)')
        plt.ylabel('Velocidad Promedio ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
        plt.legend()
        plt.grid(True)
        # plt.savefig(f"graphs/ej_2_2_1_n_25_dt_{dt}.png")
        plt.savefig(f"graphs/ej_2_3_n_25_dt_{dt}.png")
        plt.cla()


if __name__ == "__main__":
    main()


