import matplotlib.pyplot as plt


def main():
    values = [8.147576, 8.048209, 8.155115, 8.156774, 8.204253, 8.207143, 8.209882, 8.126166, 8.126040, 8.956311,
              8.956163, 8.956477, 8.769200, 8.469418, 8.471212, 8.469514, 8.468642, 7.332539, 7.421086, 7.784995]

    minimo = min(values)
    maximo = max(values)

    ancho = (maximo - minimo) / 10

    bins = [minimo + i * ancho for i in range(0, 11)]

    plt.hist(values, bins=bins, edgecolor='black', linewidth=1.2)
    plt.xlabel('Velocity')
    plt.ylabel('Amount of particles')
    plt.savefig('./graphs/hist.png')


if __name__ == "__main__":
    main()
