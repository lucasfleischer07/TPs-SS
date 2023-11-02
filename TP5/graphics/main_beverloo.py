import matplotlib.pyplot as plt
import numpy as np

radius = 1
density = 1/3   # La cuenta es (#de particulas)/(volumen) = (200)/(20*70)
g = 5 ** 0.5


def beverloo_equation(x, c):
    aux = x - c * radius
    return density * g * (aux ** 1.5)


def beverloo_error_equation(Qs, Ds, c):
    result = 0
    for q, d in zip(Qs, Ds):
        b = beverloo_equation(d, c)
        result = result + (q - b) ** 2
    return result


def get_qs(path):
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))

    return tiempos


def main():
    MHU_1 = 0.7
    MHU_2 = 0.5

    Cs_1 = [num / 100.0 for num in range(0, 200, 1)]
    Cs_2 = [num / 100.0 for num in range(0, 200, 1)]
    Qs_1 = get_qs('../src/main/resources/itemB/caudals_hole_size' + '_mhu_' + str(MHU_1) + '.txt')
    Qs_2 = get_qs('../src/main/resources/itemB/caudals_hole_size' + '_mhu_' + str(MHU_2) + '.txt')

    beverloo_err1 = [beverloo_error_equation(Qs_1, [3, 4, 5, 6], c) for c in Cs_1]
    beverloo_err2 = [beverloo_error_equation(Qs_2, [3, 4, 5, 6], c) for c in Cs_2]

    plt.plot(Cs_1, beverloo_err1, color='blue')
    plt.plot(Cs_2, beverloo_err2, color='red')

    c1 = Cs_1[np.argmin(beverloo_err1)]
    c2 = Cs_2[np.argmin(beverloo_err2)]

    print("Valor de c para μ = " + str(MHU_1) + " es igual a: " + str(c1))
    print("E(c) para μ = " + str(MHU_1) + " es igual a: " + str(beverloo_err1[np.argmin(beverloo_err1)]))
    print()
    print("Valor de c para μ = " + str(MHU_2) + " es igual a: " + str(c2))
    print("E(c) para μ = " + str(MHU_2) + " es igual a: " + str(beverloo_err2[np.argmin(beverloo_err2)]))

    plt.scatter(c1, beverloo_err1[np.argmin(beverloo_err1)], color='blue', label='μ = ' + str(MHU_1))
    plt.scatter(c2, beverloo_err2[np.argmin(beverloo_err2)], color='red', label='μ = ' + str(MHU_2))

    plt.xlabel('Parámetro libre c')
    plt.ylabel('Error')
    plt.legend()

    # Esto hace el recuadro del zoom en el grafico
    # ax_zoom = plt.axes([0.6, 0.4, 0.25, 0.25])  # Posición y tamaño del recuadro
    # ax_zoom.plot(Cs_1, beverloo_err1, color='blue')
    # ax_zoom.plot(Cs_2, beverloo_err2, color='red')
    # ax_zoom.scatter(c1, beverloo_err1[np.argmin(beverloo_err1)], color='blue', label='μ = ' + str(MHU_1))
    # ax_zoom.scatter(c2, beverloo_err2[np.argmin(beverloo_err2)], color='red', label='μ = ' + str(MHU_2))
    # ax_zoom.set_xlim(1.30, 1.85)
    # ax_zoom.set_ylim(0.19, 0.30)

    plt.savefig("graphs/" + 'beverloo_punto_minimo_' + '_mhu_' + str(MHU_1) + '_mhu_' + str(MHU_2) + '.png')
    plt.show()

    # Aca ya arranca para el otro grafico que se hace
    x_1 = [0.1 * i for i in range(20, 100)]
    y_1 = [beverloo_equation(x_i, c1) for x_i in x_1]

    x_2 = [0.1 * i for i in range(20, 100)]
    y_2 = [beverloo_equation(x_i, c2) for x_i in x_2]

    plt.plot(x_1, y_1, color='blue', label='Beverloo ' + 'μ = ' + str(MHU_1))
    plt.plot(x_2, y_2, color='red', label='Beverloo ' + 'μ = ' + str(MHU_2))
    plt.scatter([3, 4, 5, 6], Qs_1, color='blue', label='Resultados ' + 'μ = ' + str(MHU_1))
    plt.scatter([3, 4, 5, 6], Qs_2, color='red', label='Resultados ' + 'μ = ' + str(MHU_2))
    plt.ylabel('Caudal')
    plt.xlabel('Apertura (cm)')
    plt.legend()
    plt.savefig("graphs/" + 'beverloo' + '_mhu_' + str(MHU_1) + '_mhu_' + str(MHU_2) + '.png')
    plt.show()
    plt.clf()


if __name__ == "__main__":
    main()