import matplotlib.pyplot as plt
import numpy as np
from matplotlib import animation
from matplotlib.animation import FuncAnimation
from matplotlib.lines import Line2D
from matplotlib.patches import Circle, Rectangle

from parse_files import read_lines, parse_config_json, parse_output_file


# Original que anda
# def update_particle_positions(frame, skip_iteration, n, radius, file_lines, ax):
#     ax.clear()
#
#     # first_file_line = skip_iteration * frame * (n+2)
#     first_file_line = 0
#     time = float(file_lines[first_file_line].split()[0])
#
#     particles_data = []
#     for i in range(n):
#         particles_data.append(file_lines[first_file_line + 1 + i].split())
#
#     particles_data = np.array(particles_data, dtype=float)
#     ax.scatter(particles_data[:, 0], particles_data[:, 1], s=75, c='b')
#
#     # Dibuja el círculo hueco
#     circle = Circle((0, 0), radius, fill=False, color='b', linestyle='--', linewidth=2)
#     ax.add_patch(circle)
#
#     ax.set_title('Time = ' + "{:.3f}".format(time) + 's')
#
# def generate_animation(l, skip_iteration, n, iterations, circleRadius, output_base_path, particleRadius):
#     # file_lines = read_lines(output_base_path + str(l) + '.txt')
#     file_lines = read_lines(output_base_path)
#
#     fig, ax = plt.subplots(figsize=(10, 10))
#     ax.set_aspect('equal', adjustable='box')
#
#     animation = FuncAnimation(fig, frames=int(iterations/skip_iteration), func=update_particle_positions, fargs=(skip_iteration, n, circleRadius, file_lines, ax), interval=100)
#     plt.tight_layout()
#     animation.save('animations/animation_' + str(l) + '.gif')
#     return animation
#
#
# def main():
#     config_path = "../config.json"
#     output_base_path = '../src/main/resources/static_ex2.txt'
#     # output_base_path = '../src/main/resources/output'
#
#     n, particleRadius, circleRadius, iterations = parse_config_json(config_path)
#
#     # TODO: Si hay que skipear, cambiar este 1 por la cantidad que queramos
#     skip_iteration = 1
#
#     generate_animation(10, skip_iteration, n, iterations, circleRadius, output_base_path, particleRadius)
#
#     # El l ahora deberia ser particleAmount
#     # for i in [0.03, 0.05, 0.07, 0.09]:
#     #     generate_animation(i, skip_iteration, n, iterations, circleRadius, output_base_path, particleRadius)


# Para dibujar la simulacion con el circulo
# def update_particle_positions(frame, particle_data, ax, circleRadius, particleRadius):
#     ax.clear()
#
#     time = list(particle_data.keys())[frame]
#     particles = particle_data[time]
#
#     # Dibuja el círculo hueco
#     circle = Circle((0, 0), circleRadius, fill=False, color='b', linestyle='--', linewidth=2)
#     ax.add_patch(circle)
#
#     for p in particles:
#         cir = plt.Circle((p['x'], p['y']), p['radius'], color='r', fill=True)
#         ax.set_aspect('equal', adjustable='datalim')
#         ax.add_patch(cir)
#
#     # Configura los límites de los ejes
#     ax.set_xlim(-circleRadius - 2*particleRadius, circleRadius + 2*particleRadius)
#     ax.set_ylim(-circleRadius - 2*particleRadius, circleRadius + 2*particleRadius)
#     ax.set_aspect('equal', adjustable='datalim')
#
#     ax.set_title('Time = ' + "{:.3f}".format(time) + 's')


# Para dibujar la simulacion con la linea
def update_particle_positions(frame, particle_data, ax, lineLength, particleRadius):
    ax.clear()

    time = list(particle_data.keys())[frame]
    particles = particle_data[time]

    # Dibuja la línea como una línea
    line = Line2D([0, lineLength], [0, 0], color='b', linestyle='--', linewidth=2)
    ax.add_line(line)

    for p in particles:
        # Calcula la posición periódica de la partícula en x
        x = p['x'] % lineLength

        # Verifica si la partícula ha cruzado los límites
        if x + p['radius'] > lineLength:
            x -= lineLength  # Mueve la partícula al otro lado
            ax.add_patch(Circle((x, 0), p['radius'], color='r', fill=True))
            ax.add_patch(Circle((x + lineLength, 0), p['radius'], color='r', fill=True))
        elif x - p['radius'] < 0:
            x += lineLength  # Mueve la partícula al otro lado
            ax.add_patch(Circle((x, 0), p['radius'], color='r', fill=True))
            ax.add_patch(Circle((x - lineLength, 0), p['radius'], color='r', fill=True))
        else:
            ax.add_patch(Circle((x, 0), p['radius'], color='r', fill=True))

    # Configura los límites de los ejes
    ax.set_xlim(0, lineLength)
    ax.set_ylim(-particleRadius, particleRadius)
    ax.set_aspect('equal', adjustable='datalim')

    ax.set_title('Time = ' + "{:.3f}".format(time) + 's')


def main():
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'

    for dt in [1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5]:
        # Crea la figura y el eje
        fig, ax = plt.subplots()  # Esto crea tanto la figura como el eje

        n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)

        if dt == (1.0E-4):
            particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-4" + ".txt")
        elif dt == (1.0E-4):
            particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-5" + ".txt")
        else:
            particle_data = parse_output_file(output_base_path + "_" + str(n) + "_" + str(dt) + ".txt")

        # Llama a la función de actualización de la trama
        anim = animation.FuncAnimation(fig, update_particle_positions, fargs=(particle_data, ax, lineLength, particleRadius), frames=len(particle_data))

        # Save animation as mp4
        Writer = animation.writers['ffmpeg']
        writer = Writer(fps=20, metadata=dict(artist='Me'), bitrate=1800)
        anim.save('animations/animation_' + str(n) + '_' + str(dt) + '.mp4', writer=writer)



if __name__ == "__main__":
    main()