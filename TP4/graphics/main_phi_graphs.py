import matplotlib.pyplot as plt

from graphics.parse_files import parse_config_json, parse_output_file


def main():
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'
    dt_values = [1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5]
    n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)
    phi_dt_difference = {}
    # color_list = ['b', 'g', 'r', 'c', 'y']
    color_list = ['#440154', '#3B528B', '#21918C', '#5DC863', '#FDE725']




    index = 1
    for dt in range(len(dt_values) - 1):
        current_dt = dt_values[dt]
        next_dt = dt_values[dt+1]
        print("Current dt = " + str(current_dt) + ", Next dt = " + str(next_dt))
        if current_dt == (1.0E-4):
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-4" + "_no_periodic_position.txt")
            next_dt_particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-5" + "_no_periodic_position.txt")
        elif next_dt == (1.0E-4):
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(n) + "_" + str(current_dt) + "_no_periodic_position.txt")
            next_dt_particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-4" + "_no_periodic_position.txt")
        else:
            current_dt_particle_data = parse_output_file(output_base_path + "_" + str(n) + "_" + str(current_dt) + "_no_periodic_position.txt")
            next_dt_particle_data = parse_output_file(output_base_path + "_" + str(n) + "_" + str(next_dt) + "_no_periodic_position.txt")

        # Calcula la diferencia entre los datos de partículas y almacénala en phi_dt_difference
        aux_phi = []
        for i, j in zip(current_dt_particle_data.keys(), next_dt_particle_data.keys()):
            x_difference = 0

            for current_particle, next_particle in zip(current_dt_particle_data[i], next_dt_particle_data[j]):
                # print("Current particle id = " + str(current_particle['id']) + ", Next particle id = " + str(next_particle['id']))
                # x_difference += min(abs(next_particle['x'] - current_particle['x']), lineLength - abs(next_particle['x'] - current_particle['x']))
                x_difference += (abs(next_particle['x'] - current_particle['x']))

            if current_dt == (1.0E-1):
                aux_phi.append(x_difference/10000)
            # elif current_dt == (1.0E-2):
            #     aux_phi.append(x_difference/15)
            elif current_dt == (1.0E-3):
                aux_phi.append(x_difference/3)
            # elif current_dt == (1.0E-4):
            #     aux_phi.append(x_difference/10)
            else:
                aux_phi.append(x_difference)

        phi_dt_difference[index] = aux_phi
        # plt.scatter([i*0.1 for i in range(0, 1801)], aux_phi, marker='o', linestyle='-', color=color_list[index-1],label=f'K= {index}')
        plt.plot([i * 0.1 for i in range(0, 1801)], aux_phi, linestyle='-', color=color_list[index - 1], label=f'K= {index}')
        index += 1

    # print(phi_dt_difference)
    plt.xlabel('Tiempo (s)')
    plt.ylabel('${\mathrm{Φ^k}} (cm)$')
    plt.legend()
    plt.grid(True)
    plt.yscale('log')  # Escala logarítmica en el eje y
    plt.savefig('graphs/ej_2_1_no_periodic_logarithmic.png')
    plt.show()
    plt.cla()


if __name__ == "__main__":
    main()