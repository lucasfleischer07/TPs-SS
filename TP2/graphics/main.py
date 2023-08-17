import math
import matplotlib.pyplot as plt
import matplotlib.animation as animation

from parse_files import parse_config_json, parse_static_file, parse_output_file


def update_particle_for_simulation(step, iterations, N, L):
    x = [iterations[step][i][0] for i in range(N)]
    y = [iterations[step][i][1] for i in range(N)]

    thetas = [iterations[step][i][3] for i in range(N)]

    x_vel = [iterations[step][i][2] * math.cos(thetas[i]) for i in range(N)]
    y_vel = [iterations[step][i][2] * math.sin(thetas[i]) for i in range(N)]

    plt.clf()
    plt.gcf().text(0.02, 0.95, "Iteration = {}".format(math.floor(step / 10) * 10))

    plt.quiver(x, y, x_vel, y_vel, thetas, cmap='hsv')

    plt.xlim(0, L)
    plt.ylim(0, L)


def main():
    config_json_path = "../config.json"
    static_file_path = "../src/main/resources/static.txt"
    output_file_path = "../src/main/resources/output.txt"

    N, L, amount_of_iterations = parse_config_json(config_json_path)
    initial_state = parse_static_file(static_file_path)
    output_iterations = parse_output_file(output_file_path, initial_state, N)
    animation.FuncAnimation(plt.gcf(), update_particle_for_simulation, fargs=(output_iterations, N, L), interval=30, frames=amount_of_iterations, repeat=False).save("simulation.gif")


if __name__ == "__main__":
    main()