import matplotlib.pyplot as plt
import numpy as np
from matplotlib import animation
from matplotlib.animation import FuncAnimation
from matplotlib.lines import Line2D
from matplotlib.patches import Circle, Rectangle

from parse_files import read_lines, parse_config_json, parse_output_file


# Para dibujar la simulacion con la linea
def update_particle_positions(frame, particle_data, ax, lineLength, particleRadius, dt):
    ax.clear()

    particles = particle_data[frame]

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

    print("dt = " + str(dt) + ", Frame = " + str(frame))

    ax.set_title('Time = ' + "{:.3f}".format(frame) + 's')


def main():
    config_json_path = "../config.json"
    output_base_path = '../src/main/resources/ex2/output_ex2'

    for dt in [1.0E-1, 1.0E-2, 1.0E-3, 1.0E-4, 1.0E-5]:
        # Crea la figura y el eje
        fig, ax = plt.subplots()  # Esto crea tanto la figura como el eje

        n, particleRadius, lineLength, iterations = parse_config_json(config_json_path)

        if dt == (1.0E-4):
            particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-4" + ".txt")
        elif dt == (1.0E-5):
            particle_data = parse_output_file(output_base_path + "_" + str(n) + "_1.0E-5" + ".txt")
        else:
            particle_data = parse_output_file(output_base_path + "_" + str(n) + "_" + str(dt) + ".txt")

        # Llama a la función de actualización de la trama
        anim = animation.FuncAnimation(fig, update_particle_positions, fargs=(particle_data, ax, lineLength, particleRadius, dt), frames=list(particle_data.keys()), repeat=False, interval=1)

        # Save animation as mp4
        Writer = animation.writers['ffmpeg']
        writer = Writer(fps=20, metadata=dict(artist='Me'), bitrate=1800)
        anim.save('animations/animation_' + str(n) + '_' + str(dt) + '.mp4', writer=writer)


if __name__ == "__main__":
    main()
