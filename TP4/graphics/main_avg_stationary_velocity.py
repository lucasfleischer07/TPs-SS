import matplotlib.pyplot as plt
import numpy as np


def main():
    n = 20
    stationary = 130
    filename = f'../src/main/resources/ex2/output_ex2_{n}_0.001.txt'

    with open(filename, 'r') as archive:
        velocities = []
        dt = archive.readline()
        while float(dt) < stationary:
            for i in range(n):
                archive.readline()
            dt = archive.readline()

        for line in archive:
            cols = line.split()

            if len(cols) != 1:
                velocities.append(float(cols[3]))

    archive.close()

    hist, bin_edges = np.histogram(velocities, bins=20, density=True)

    bin_centers = 0.5 * (bin_edges[1:] + bin_edges[:-1])

    plt.figure(figsize=(8, 6))
    plt.plot(bin_centers, hist, 'k-', linewidth=2, label='Stationary distribution')

    # Agregar la recta y=1/3 desde el punto en el que la velocidad es 9
    x_recta = np.linspace(9, max(bin_centers), 100)  # Valores de x desde 9 hasta el máximo
    y_recta = np.full_like(x_recta, 1 / 3)  # Array de 1/3 del mismo tamaño que x
    plt.plot(x_recta, y_recta, 'r--', label='Initial distribution')  # Agregar la recta

    plt.grid(True)
    plt.legend()
    plt.savefig(f'./graphs/pdf_{n}.png')


if __name__ == "__main__":
    main()