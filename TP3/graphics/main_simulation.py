import matplotlib.pyplot as plt
import numpy as np
from matplotlib.animation import FuncAnimation
from parse_files import get_parameters, read_lines


def update_particle_positions(frame, skip_iteration, n, table_width, l, file_lines, ax):
    ax.clear()

    first_file_line = skip_iteration * frame * (n+2)
    time = float(file_lines[first_file_line].split()[0])

    particles_data = []
    for i in range(n):
        particles_data.append(file_lines[first_file_line + 2 + i].split())

    particles_data = np.array(particles_data, dtype=float)
    ax.scatter(particles_data[:, 0], particles_data[:, 1], s=75, c='b')

    # Puntos de inicio y fin para cada línea en un formato más compacto
    lines = [
        ([0, 0], [0, table_width]),
        ([0, table_width], [0, 0]),
        ([table_width, table_width], [0, (table_width - l) / 2]),
        ([table_width, table_width * 2], [(table_width - l) / 2, (table_width - l) / 2]),
        ([table_width * 2, table_width * 2], [(table_width - l) / 2, (table_width - l) / 2 + l]),
        ([table_width * 2, table_width], [(table_width - l) / 2 + l, (table_width - l) / 2 + l]),
        ([table_width, table_width], [(table_width - l) / 2 + l, table_width]),
        ([table_width, 0], [table_width, table_width]),
    ]

    for start, end in lines:
        ax.plot(start, end, color='b')

    ax.set_xticks([0, table_width, table_width + table_width])
    ax.set_yticks([0, (table_width - l) / 2, (table_width - l) / 2 + l, table_width])

    ax.set_title('Time = ' + "{:.3f}".format(time) + 's')


def generate_animation(l, skip_iteration, n, iterations, table_width, output_base_path):
    file_lines = read_lines(output_base_path + str(l) + '.txt')

    fig, ax = plt.subplots(figsize=(10, 10))
    ax.set_aspect('equal', adjustable='box')

    animation = FuncAnimation(fig, frames=int(iterations/skip_iteration), func=update_particle_positions, fargs=(skip_iteration, n, table_width, l, file_lines, ax), interval=100)
    plt.tight_layout()
    animation.save('animations/animation_' + str(l) + '.gif')
    return animation


def main():
    output_base_path = '../src/main/resources/output'
    parameters = get_parameters()

    n = parameters["n"]
    iterations = parameters["iterations"]
    table_width = parameters["table_width"]
    skip_iteration = 100

    for i in [0.03, 0.05, 0.07, 0.09]:
        generate_animation(i, skip_iteration, n, iterations, table_width, output_base_path)


if __name__ == "__main__":
    main()