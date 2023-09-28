import matplotlib.pyplot as plt
import numpy as np
from matplotlib.animation import FuncAnimation
from matplotlib.patches import Circle

from parse_files import read_lines, parse_config_json


def update_particle_positions(frame, skip_iteration, n, radius, file_lines, ax):
    ax.clear()

    # first_file_line = skip_iteration * frame * (n+2)
    first_file_line = 0
    time = float(file_lines[first_file_line].split()[0])

    particles_data = []
    for i in range(n):
        particles_data.append(file_lines[first_file_line + 1 + i].split())

    particles_data = np.array(particles_data, dtype=float)
    ax.scatter(particles_data[:, 0], particles_data[:, 1], s=75, c='b')

    # Dibuja el círculo hueco
    circle = Circle((0, 0), radius, fill=False, color='b', linestyle='--', linewidth=2)
    ax.add_patch(circle)

    ax.set_title('Time = ' + "{:.3f}".format(time) + 's')


def generate_animation(l, skip_iteration, n, iterations, circleRadius, output_base_path, particleRadius):
    # file_lines = read_lines(output_base_path + str(l) + '.txt')
    file_lines = read_lines(output_base_path)

    fig, ax = plt.subplots(figsize=(10, 10))
    ax.set_aspect('equal', adjustable='box')

    animation = FuncAnimation(fig, frames=int(iterations/skip_iteration), func=update_particle_positions, fargs=(skip_iteration, n, circleRadius, file_lines, ax), interval=100)
    plt.tight_layout()
    animation.save('animations/animation_' + str(l) + '.gif')
    return animation


def main():
    config_path = "../config.json"
    output_base_path = '../src/main/resources/static_ex2.txt'
    # output_base_path = '../src/main/resources/output'

    n, particleRadius, circleRadius, iterations = parse_config_json(config_path)

    # TODO: Si hay que skipear, cambiar este 1 por la cantidad que queramos
    skip_iteration = 1

    generate_animation(10, skip_iteration, n, iterations, circleRadius, output_base_path, particleRadius)

    # El l ahora deberia ser particleAmount
    # for i in [0.03, 0.05, 0.07, 0.09]:
    #     generate_animation(i, skip_iteration, n, iterations, circleRadius, output_base_path, particleRadius)


if __name__ == "__main__":
    main()