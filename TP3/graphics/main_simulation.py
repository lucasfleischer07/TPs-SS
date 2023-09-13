import matplotlib.pyplot as plt
from matplotlib import animation
from matplotlib.collections import EllipseCollection

from graphics.parse_files import parse_config_json, parse_output_file


def update_particle_for_simulation(frame, particle_data, ax2d, main_height, main_width, minor_height, minor_width):
    # Esta línea obtiene una lista de todas las claves del diccionario events, convierte esa lista en una lista ordenada, y luego selecciona la clave en la posición frame. Esto significa que t ahora contiene la clave correspondiente al paso en la animación que se debe representar en el gráfico.
    t = list(particle_data.keys())[frame]
    particles = particle_data[t]

    ax2d.clear()

    ax2d.plot([0, 0], [0, main_height], color='b')  # 0
    ax2d.plot([0, main_width], [0, 0], color='b')  # 1
    ax2d.plot([main_width, main_width], [0, (main_height - minor_height) / 2], color='b')  # 2
    ax2d.plot([main_width, main_width + minor_width], [(main_height - minor_height) / 2, (main_height - minor_height) / 2], color='b')  # 3
    ax2d.plot([main_width + minor_width, main_width + minor_width], [(main_height - minor_height) / 2, (main_height - minor_height) / 2 + minor_height], color='b')  # 4
    ax2d.plot([main_width + minor_width, main_width], [(main_height - minor_height) / 2 + minor_height, (main_height - minor_height) / 2 + minor_height], color='b')  # 5
    ax2d.plot([main_width, main_width], [(main_height - minor_height) / 2 + minor_height, main_height], color='b')  # 6
    ax2d.plot([main_width, 0], [main_height, main_height], color='b')  # 7

    ax2d.set_xticks([0, main_width, main_width + minor_width])
    ax2d.set_yticks([0, (main_height - minor_height) / 2, (main_height - minor_height) / 2 + minor_height, main_height])

    for p in particles:
        cir = plt.Circle((p['x'], p['y']), p['radius'], color='r', fill=True)
        ax2d.set_aspect('equal', adjustable='datalim')
        ax2d.add_patch(cir)

    # ax2d.scatter(([p['x'] for p in particles]), ([p['y'] for p in particles]), s=75, c='b')

    ax2d.set_xlabel('Position X')
    ax2d.set_ylabel('Position Y')
    ax2d.set_title(f'Time: ' + str(t))



def main():
    config_json_path = "../config.json"
    static_file_path = "../src/main/resources/static.txt"
    output_file_path = "../src/main/resources/output.txt"
    # output_file_path_aux = "output.txt"

    # Crea la figura y el eje
    fig = plt.figure()
    ax2d = fig.add_subplot(111)

    N, particleRadius, enclosure1X, enclosure1Y, enclosure2X, L = parse_config_json(config_json_path)
    particle_data = parse_output_file(output_file_path)

    # Llama a la función de actualización de la trama
    anim = animation.FuncAnimation(fig, update_particle_for_simulation, fargs=(particle_data, ax2d, enclosure1Y, enclosure1X, L, enclosure2X), frames=len(particle_data))

    # Save animation as mp4
    Writer = animation.writers['ffmpeg']
    writer = Writer(fps=20, metadata=dict(artist='Me'), bitrate=1800)

    anim.save('animation.mp4', writer=writer)


if __name__ == "__main__":
    main()
