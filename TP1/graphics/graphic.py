from scan_files import read_neighbors, read_dynamic, read_static, json_scan
from particle import Particle
from manim import *
import matplotlib.pyplot as plt


def draw_particles(particles, target_particle_id, l, m, rc):
    fig, ax = plt.subplots()
    scale = 1
    neighbors_list = particles[target_particle_id-1].get_neighbors()

    for particle in particles:
        color = 'red'  # Las partículas no vecinas se dibujarán en rojo

        if int(particle.id) == target_particle_id:
            color = 'green'  # La partícula objetivo se dibujará en verde
            circle = plt.Circle((float(particle.get_pos_x()), float(particle.get_pos_y())), float(particle.get_radius()) * scale, fill=True, color=color)
            ax.add_artist(circle)

        # Dibujar las partículas vecinas en rojo
        if particle.id in neighbors_list and int(particle.id) != target_particle_id:
            color = 'blue'  # La partícula vecinas se dibujará en azul
            circle = plt.Circle((float(particle.get_pos_x()), float(particle.get_pos_y())), float(particle.get_radius()) * scale, fill=True, color=color)
            ax.add_artist(circle)
        else:
            circle = plt.Circle((float(particle.get_pos_x()), float(particle.get_pos_y())), float(particle.get_radius()) * scale, fill=True, color=color)
            ax.add_artist(circle)

    # Dibujar el círculo centrado en las coordenadas de target_particle_id
    target_particle = particles[target_particle_id - 1]
    circle = plt.Circle((float(target_particle.get_pos_x()), float(target_particle.get_pos_y())), rc, fill=False, color='black', linestyle='dashed', linewidth=2)
    ax.add_artist(circle)

    # Configuración de la grilla
    grid_size = int(l / m)
    ax.set_xticks(range(0, l+1, grid_size))
    ax.set_yticks(range(0, l+1, grid_size))
    ax.grid(True)

    ax.set_aspect('equal', adjustable='datalim')
    ax.set_xlabel('Coordinate X')
    ax.set_ylabel('Coordinate Y')
    ax.set_title("Neighbors Particle " + str(target_particle_id) + " Visualization")

    ax.set_xlim(0, l)
    ax.set_ylim(0, l)

    plt.show()


def main():
    target_particle_id = 8
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




# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------
# ------------------------------------------------------------------------------------------------------------------------------------------


# output_file_name = "../src/main/resources/output.txt"
# dynamic_file_name = "../src/main/resources/dynamic.txt"
# static_file_name = "../src/main/resources/static.txt"
#
# neighbors_map = read_neighbors(output_file_name)
# positions_map = read_dynamic(dynamic_file_name)
# n, l_str, radius_and_prop_map = read_static(static_file_name)
# l = float(l_str)
# m = json_scan("../config.json")
#
# particles = []
# for particle_id, particle_neighbors in neighbors_map.items():
#     particle = Particle(particle_id, particle_neighbors)
#     particles.append(particle)
#
# for particle in particles:
#     current_id = particle.get_id()
#     current_pos_values = positions_map.get(current_id)
#     current_radius_and_prop_values = radius_and_prop_map.get(current_id)
#
#     particle.set_pos(current_pos_values[0], current_pos_values[1])
#     particle.set_radius(current_radius_and_prop_values[0])
#     particle.set_prop(current_radius_and_prop_values[1])

    # Descomentar este print para ver las instancias de las particulas con sus datos
    # print(f"ID: {particle.get_id()}, Vecinos: {particle.get_neighbors()}, "
    #       f"PosX: {particle.get_pos_x()}, Posy: {particle.get_pos_y()}"
    #       f" Radius: {particle.get_radius()}")



# class Graphic(Scene):
#     def construct(self):
#         target_particle = 8  # Cambia esto al número de partícula que quieras resaltar
#         target_neighbors = particles[target_particle - 1].get_neighbors()
#         scale = 0.5
#
#         axes = Axes(
#             x_range=[-1, 100],
#             y_range=[-1, 100],
#             axis_config={"color": BLUE},
#             x_length=16,
#             y_length=12,
#             tips=True,
#             x_axis_config={"unit_size": scale},
#             y_axis_config={"unit_size": scale}
#         )
#
#         # Dibujar ejes cartesianos
#         self.add(axes)
#
#         for particleGrid in particles:
#             x = float(particleGrid.get_pos_x()) * scale
#             y = float(particleGrid.get_pos_y()) * scale
#             radius = float(particleGrid.get_radius()) * scale
#             if particleGrid.id in target_neighbors:
#                 neighbor_color = BLUE  # Color para las partículas vecinas
#                 neighbor_dot = Dot(point=axes.c2p(x, y), radius=radius, color=neighbor_color)
#                 self.add(neighbor_dot)
#             else:
#                 color = RED if int(particleGrid.id) == target_particle else GREEN  # Cambiar el color si es la partícula objetivo
#                 dot = Dot(point=axes.c2p(x, y), radius=radius, color=color)
#                 self.add(dot)