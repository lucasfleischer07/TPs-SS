import matplotlib.pyplot as plt
import numpy as np
import threading
from matplotlib import animation
from matplotlib.animation import FuncAnimation
from matplotlib.lines import Line2D
from matplotlib.patches import Circle, Rectangle

from parse_files import parse_config_json, parse_output_file


def update_particle_positions(frame, particle_data, ax, L, W, D, dt, frequency):
    ax.clear()

    particles = particle_data[frame]
    # limits = limits_data[frame]

    # Dibuja un rectángulo
    rect = Rectangle((0, 0), W, L + L / 10, color='b', fill=False)
    ax.add_patch(rect)

    # Dibuja una línea horizontal con espacio en el medio
    ax.add_line(plt.Line2D([0, W/2 - D / 2], [L/10, L/10], color='b', linewidth=2))
    ax.add_line(plt.Line2D([D / 2 + W/2, W], [L / 10, L / 10], color='b', linewidth=2))

    # Configura los límites de los ejes
    ax.set_xlim(0, W)
    ax.set_ylim(0, L + L / 10)

    ax.set_aspect('equal', adjustable='datalim')


    for p in particles:
        color = tuple(int(c) / 255 for c in p['color'].split())
        ax.add_patch(Circle((p['x'], p['y']), p['radius'], color=color, fill=True))

    # print(f"Thread {threading.current_thread().name} - Running frequency = {frequency} - Iteration = {frame:.3f}")
    print(f"Running frequency = {frequency} - Iteration = {frame:.3f}")
    ax.set_title('Time = ' + "{:.3f}".format(frame) + 's')


# def run_simulation(frequency):
#     config_json_path = "../config.json"
#     output_base_path = '../src/main/resources/output_frequency_' + str(frequency) + '.txt'
#
#     # Crea la figura y el eje
#     fig, ax = plt.subplots()
#
#     N, W, L, D, N, mass, dt, iterations, A = parse_config_json(config_json_path)
#     particle_data = parse_output_file(output_base_path)
#
#     anim = animation.FuncAnimation(fig, update_particle_positions, fargs=(particle_data, ax, L, W, D, dt, frequency), frames=list(particle_data.keys()), repeat=False, interval=1)
#
#     # Save animation as mp4
#     Writer = animation.writers['ffmpeg']
#     writer = Writer(fps=20, metadata=dict(artist='Me'), bitrate=1800)
#     anim.save('animations/animation_D_' + str(D) + '_dt_ ' + str(dt) + '_frequency_' + str(frequency) + '.mp4', writer=writer)


# def main():
#     threads = []
#
#     for frequency in [5.0, 10.0, 15.0, 20.0, 30.0, 50.0]:
#         thread = threading.Thread(target=run_simulation, args=(frequency,))
#         threads.append(thread)
#         thread.start()
#
#     for thread in threads:
#         thread.join()
#
#
# if __name__ == "__main__":
#     main()


def main():
    config_json_path = "../config.json"
    D = 5.0
    MHU = 0.1

    for i in [5.0, 10.0, 15.0, 20.0, 30.0, 50.0]:
        output_base_path = '../src/main/resources/itemA/output_frequency_' + str(i) + '_D_' + str(D) + '_mhu_' + str(MHU) + '.txt'
        # Crea la figura y el eje
        fig, ax = plt.subplots()  # Esto crea tanto la figura como el eje

        N, W, L, D, N, mass, dt, iterations, A = parse_config_json(config_json_path)

        particle_data = parse_output_file(output_base_path)

        # Llama a la función de actualización de la trama
        anim = animation.FuncAnimation(fig, update_particle_positions, fargs=(particle_data, ax, L, W, D, dt, i), frames=list(particle_data.keys()), repeat=False, interval=1)

        # Save animation as mp4
        Writer = animation.writers['ffmpeg']
        writer = Writer(fps=20, metadata=dict(artist='Me'), bitrate=1800)
        anim.save('animations/animation_D_' + str(D) + '_dt_ ' + str(dt) + '_frequency_' + str(i) + '.mp4', writer=writer)


if __name__ == "__main__":
    main()