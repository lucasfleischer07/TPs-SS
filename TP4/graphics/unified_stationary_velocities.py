import matplotlib.pyplot as plt
import numpy as np
import math


def main():
    stationary = 120
    filenames = ['../src/main/resources/ex2/output_ex2_10_0.001.txt',
                 '../src/main/resources/ex2/output_ex2_20_0.001.txt',
                 '../src/main/resources/ex2/output_ex2_30_0.001.txt']
    velocities_dict = {}

    i = 0
    for filename in filenames:
        i += 10
        velocities_dict[i] = []
        with open(filename, 'r') as archive:
            dt = archive.readline()
            while float(dt) < stationary:
                for j in range(i):
                    archive.readline()
                dt = archive.readline()

            for line in archive:
                cols = line.split()

                if len(cols) != 1:
                    velocities_dict[i].append(float(cols[3]))

        archive.close()

    plt.figure(figsize=(8, 6))  # Tamaño del gráfico
    lim = None
    for i, velocities in velocities_dict.items():
        num_particles = len(velocities)
        bins = int(math.log2(num_particles)) + 1
        p, x = np.histogram(velocities, bins)
        x = x[:-1] + (x[1] - x[0]) / 2
        if i == 10:
            lim = x
        plt.plot(x, [a / ((x[1] - x[0]) * num_particles) for a in p], linestyle='-', label=f'N={i}')

    x_recta = np.linspace(9, max(lim), 100)
    y_recta = np.full_like(x_recta, 1 / 3)
    plt.plot(x_recta, y_recta, 'r--', label='Initial distribution')

    plt.legend()  # Mostrar leyendas para cada conjunto de datos
    plt.xlabel('Velocidad ($\\frac{{\mathrm{cm}}}{{\mathrm{s}}})$')
    plt.ylabel('Densidad de probabilidad ($\\frac{{\mathrm{1}}}{{\mathrm{cm/s}}})$')
    plt.savefig(f'./graphs/unified.png')


if __name__ == "__main__":
    main()
