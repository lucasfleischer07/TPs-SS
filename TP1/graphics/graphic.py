from scan_files import read_neighbors
from scan_files import read_dynamic
from scan_files import read_static
from particle import Particle
from manim import *

output_file_name = "../src/main/resources/output.txt"
dynamic_file_name = "../src/main/resources/dynamic.txt"
static_file_name = "../src/main/resources/static.txt"

neighbors_map = read_neighbors(output_file_name)
positions_map = read_dynamic(dynamic_file_name)
n, l, radius_and_prop_map = read_static(static_file_name)

particles = []
for particle_id, particle_neighbors in neighbors_map.items():
    particle = Particle(particle_id, particle_neighbors)
    particles.append(particle)

# for particle_id, pos in positions_map.items():
#     particles[particle_id].set_pos(pos[0], pos[1])

for particle in particles:
    current_id = particle.get_id()
    current_pos_values = positions_map.get(current_id)
    current_radius_and_prop_values = radius_and_prop_map.get(current_id)

    particle.set_pos(current_pos_values[0], current_pos_values[1])
    particle.set_radius(current_radius_and_prop_values[0])
    particle.set_prop(current_radius_and_prop_values[1])

    # Descomentar este print para ver las instancias de las particulas con sus datos
    print(f"ID: {particle.get_id()}, Vecinos: {particle.get_neighbors()}, "
          f"PosX: {particle.get_pos_x()}, Posy: {particle.get_pos_y()}"
          f" Radius: {particle.get_radius()}")


class Graphic(Scene):
    def construct(self):
        number_plane = NumberPlane(
            background_line_style={
                "stroke_color": WHITE,
                "stroke_width": 4,
                "stroke_opacity": 0.6
            }
        )
        self.add(number_plane)
