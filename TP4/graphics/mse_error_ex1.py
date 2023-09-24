import matplotlib.pyplot as plt

def main():
    # Abre el archivo de texto para lectura
    with open("../src/main/resources/mseDtTimesEx1.txt", "r") as file:
        lines = file.readlines()

    # Inicializa listas para almacenar los valores
    mse_verlet = []
    mse_beeman = []
    mse_gear = []

    # Itera a través de las líneas del archivo y procesa los valores
    for line in lines:
        values = line.strip().split('\t')
        if len(values) == 3:
            mse_verlet.append(float(values[0]))
            mse_beeman.append(float(values[1]))
            mse_gear.append(float(values[2]))

    # Graficar los datos
    t = [10 ** -6, 10 ** -5, 10 ** -4, 10 ** -3, 10 ** -2]

    plt.loglog(t, mse_verlet, linestyle='-', marker='o', label='Verlet')
    plt.loglog(t, mse_beeman, linestyle='-', marker='o', label='Beeman')
    plt.loglog(t, mse_gear, linestyle='-', marker='o', label='Gear Corrector Predictor')

    plt.xlabel("Tiempo (s)", fontsize=15)
    plt.ylabel("MSE", fontsize=15)

    plt.legend()
    plt.tight_layout()
    plt.show()


if __name__ == '__main__':
    main()
