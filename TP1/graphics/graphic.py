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
        ax.text(x + radius, y, str(particle.id), fontsize=8, verticalalignment='center')  # Agregar etiqueta


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


def main():
    target_particle_id = 27
    output_file_name = "../src/main/resources/output.txt"
    dynamic_file_name = "../src/main/resources/dynamic.txt"
    static_file_name = "../src/main/resources/static.txt"

    neighbors_map = read_neighbors(output_file_name)
    positions_map = read_dynamic(dynamic_file_name)
    n, l_str, radius_and_prop_map = read_static(static_file_name)
    l = int(float(l_str))
    m, rc = json_scan("../config.json")

    particles = []
    for particle_id, particle_neighbors in neighbors_map.items():
        particle = Particle(particle_id, particle_neighbors)
        particles.append(particle)

    for particle in particles:
        current_id = particle.get_id()
        current_pos_values = positions_map.get(current_id)
        current_radius_and_prop_values = radius_and_prop_map.get(current_id)

        particle.set_pos(current_pos_values[0], current_pos_values[1])
        particle.set_radius(current_radius_and_prop_values[0])
        particle.set_prop(current_radius_and_prop_values[1])

    draw_particles(particles, target_particle_id, l, m, rc)


if __name__ == "__main__":
    main()
