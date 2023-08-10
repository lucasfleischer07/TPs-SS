from scan_files import read_neighbors, read_dynamic, read_static, json_scan
from particle import Particle
import matplotlib.pyplot as plt


def draw_particles(particles, target_particle_id, l, m, rc):
    fig, ax = plt.subplots()
    neighbors_list = particles[target_particle_id].get_neighbors()

    for particle in particles:
        x = float(particle.get_pos_x())
        y = float(particle.get_pos_y())
        radius = float(particle.get_radius())

        if int(particle.id) == target_particle_id:
            color = 'green'
        elif particle.id in neighbors_list:
            color = 'blue'
        else:
            color = 'red'

        circle = plt.Circle((x, y), radius, fill=True, color=color)
        ax.add_artist(circle)
        ax.text(x + radius, y, str(particle.id), fontsize=8, verticalalignment='center')  

    # Dibujar el círculo centrado en las coordenadas de target_particle_id
    target_particle = particles[target_particle_id]
    circle = plt.Circle((float(target_particle.get_pos_x()), float(target_particle.get_pos_y())), rc + float(target_particle.get_radius()), fill=False, color='black', linestyle='dashed')
    ax.add_artist(circle)

    ax.set_xticks(range(0, l+1, int(l/m)))  # Configuración de los ticks en el eje X
    ax.set_yticks(range(0, l+1, int(l/m)))  # Configuración de los ticks en el eje Y
    ax.grid(True)  # Habilitar la grilla

    ax.set_aspect('equal', adjustable='box')
    ax.set_xlabel('Coordinate X')
    ax.set_ylabel('Coordinate Y')
    ax.set_title("Neighbors Particle " + str(target_particle_id) + " Visualization")

    ax.set_xlim(0, l)
    ax.set_ylim(0, l)

    plt.show()


def draw_times(plot_labels, n_values, time_values_cim, time_errors_cim, time_values_brute, time_errors_brute, l, rc, r, brute_force):

    plt.figure(figsize=(10, 6))  # Ajustar el tamaño del gráfico si es necesario

    # plt.plot(n_values, time_values_cim, marker='o', linestyle='-', color='b', label='CIM')
    plt.errorbar(n_values, time_values_cim, yerr=time_errors_cim, marker='o', linestyle='-', color='b', label='CIM')
    if brute_force:
        # plt.plot(n_values, time_values_brute, marker='s', linestyle='--', color='r', label='Brute')
        plt.errorbar(n_values, time_values_brute, yerr=time_errors_brute, marker='s', linestyle='-', color='r', label='Brute')

    plt.xlabel(plot_labels[0])
    plt.ylabel(plot_labels[1])
    plt.title(plot_labels[2])
    plt.legend()

    plt.text(1.02, 0.5, f'L = {l}', transform=plt.gca().transAxes, fontsize=10, verticalalignment='center')
    plt.text(1.02, 0.4, f'rc = {rc}', transform=plt.gca().transAxes, fontsize=10, verticalalignment='center')
    plt.text(1.02, 0.3, f'r = {r}', transform=plt.gca().transAxes, fontsize=10, verticalalignment='center')

    plt.show()


def main():
    target_particle_id = 23
    output_file_name = "../src/main/resources/output.txt"
    dynamic_file_name = "../src/main/resources/dynamic.txt"
    static_file_name = "../src/main/resources/static.txt"

    neighbors_map = read_neighbors(output_file_name)
    positions_map = read_dynamic(dynamic_file_name)
    n, l_str, radius_and_prop_map = read_static(static_file_name)
    l = int(float(l_str))
    m, rc = json_scan("../config.json")

    all_neighbors_particles = []
    for particle_id, particle_neighbors in neighbors_map.items():
        particle = Particle(particle_id, particle_neighbors)
        all_neighbors_particles.append(particle)

    particles = []
    for i in range(int(n)):
        particle_id = i
        existing_particle = None
        for neighbor_particle in all_neighbors_particles:
            if neighbor_particle.get_id() == particle_id:
                existing_particle = neighbor_particle
                break

        if existing_particle:
            particles.append(existing_particle)
        else:
            new_particle = Particle(particle_id, None)
            particles.append(new_particle)

    for particle in particles:
        current_id = particle.get_id()
        current_pos_values = positions_map.get(current_id)
        current_radius_and_prop_values = radius_and_prop_map.get(current_id)

        particle.set_pos(current_pos_values[0], current_pos_values[1])
        particle.set_radius(current_radius_and_prop_values[0])
        particle.set_prop(current_radius_and_prop_values[1])

    draw_particles(particles, target_particle_id, l, m, rc)

    plot_labels = ['Number of particle (N)', 'Time in ms', 'Graphic of time in function of number of particles']
    n_values = [0, 50, 100, 150, 200, 250, 500, 750, 1000]
    time_values_cim = [0, 1, 3, 5, 6, 7, 9, 19, 24]
    time_errors_cim = [1, 2, 3, 4, 5, 4, 3, 2, 1]
    time_values_brute = [0.0, 1.0, 3.0, 6.0, 8.0, 12.0, 19.0, 33.0, 60.0]
    time_errors_brute = [0.5, 1, 1.5, 2, 2.5, 3.5, 4, 4.5, 5]
    draw_times(plot_labels, n_values, time_values_cim, time_errors_cim, time_values_brute, time_errors_brute, l, rc, 0.25, True)

    plot_labels = ['Number of cells (M)', 'Time in ms', 'Graphic of time in function of number of cells']
    m_values = [1, 2, 3, 4, 9, 13]
    time_values_cim = [18, 12, 11, 10, 6, 5]
    time_values_brute = [0, 0, 0, 0, 0, 0]     # No deberia estar este
    time_errors_cim = [1, 2, 3, 4, 5, 4]
    time_errors_brute = [0.5, 1, 1.5, 2, 2.5, 3.5]

    # time_values_brute = [16, 14, 13, 12, 11, 9]     # No deberia estar este
    draw_times(plot_labels, m_values, time_values_cim, time_errors_cim, time_values_brute, time_errors_brute, l, rc, 0.25, False)


if __name__ == "__main__":
    main()
