import math
import matplotlib.pyplot as plt
import matplotlib.animation as animation

from parse_files import parse_config_json, parse_static_file, parse_output_file


def update_particle_for_simulation(iteration, iterations, N, L):
    # Extraer las posiciones x de las partículas en la iteración 'iteration'
    x = [iterations[iteration][i][0] for i in range(N)]

    # Extraer las posiciones y de las partículas en la iteración 'iteration'
    y = [iterations[iteration][i][1] for i in range(N)]

    # Extraer los ángulos theta de las partículas en la iteración 'iteration'
    thetas = [iterations[iteration][i][3] for i in range(N)]

    # Calcular las velocidades en el eje x (x_vel) y en el eje y (y_vel) de las partículas en la iteración 'iteration'
    x_vel = [iterations[iteration][i][2] * math.cos(thetas[i]) for i in range(N)]
    y_vel = [iterations[iteration][i][2] * math.sin(thetas[i]) for i in range(N)]

    # Borrar la figura actual (limpiar el lienzo)
    plt.clf()

    # Agregar texto a la figura actual indicando el número de iteración
    plt.gcf().text(0.02, 0.95, "Iteration = {}".format(math.floor(iteration / 10) * 10))

    # Crear un gráfico de vectores (flechas) utilizando las posiciones (x, y) y las velocidades (x_vel, y_vel)
    # La dirección de las flechas se determina por los ángulos thetas
    plt.quiver(x, y, x_vel, y_vel, thetas, cmap='winter')

    # Establecer los límites del gráfico en los ejes x e y
    plt.xlim(0, L)
    plt.ylim(0, L)


def main():
    config_json_path = "../config.json"
    static_file_path = "../src/main/resources/static.txt"
    output_file_path = "../src/main/resources/output.txt"

    N, L, amount_of_iterations, va_stationary_t = parse_config_json(config_json_path)
    initial_state, velocity_initial_state, theta_initial_state = parse_static_file(static_file_path)
    output_iterations, particle_velocities, particle_theta = parse_output_file(output_file_path, initial_state, velocity_initial_state, theta_initial_state, N)

    # Hace la animacion (el .gif)
    animation.FuncAnimation(plt.gcf(), update_particle_for_simulation, fargs=(output_iterations, N, L), interval=30, frames=amount_of_iterations, repeat=False).save("simulation.gif")


if __name__ == "__main__":
    main()