from scan_files import read_neighbors
from scan_files import read_dynamic
from particle import Particle

output_file_name = "../src/main/resources/output.txt"
dynamic_file_name = "../src/main/resources/dynamic.txt"

neighbors_map = read_neighbors(output_file_name)
positions_map = read_dynamic(dynamic_file_name)

particles = []
for particle_id, particle_neighbors in neighbors_map.items():
    particle = Particle(particle_id, particle_neighbors)
    particles.append(particle)

# for particle_id, pos in positions_map.items():
#     particles[particle_id].set_pos(pos[0], pos[1])

for particle in particles:
    current_id = particle.get_id()
    current_pos_values = positions_map.get(current_id)
    particle.set_pos(current_pos_values[0], current_pos_values[1])
    print(f"ID: {particle.get_id()}, Vecinos: {particle.get_neighbors()}, PosX: {particle.get_pos_x()}, Posy: {particle.get_pos_y()}")

