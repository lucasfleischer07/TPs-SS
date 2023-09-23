import numpy as np
from matplotlib import pyplot as plt
from scipy.optimize import minimize
from parse_files import read_lines


#usar para l distinto de parameters[tbale_width]
def impulse_calculation(l, impulso_cuadrado, impulso_rectangulo, vx, vy, wall_of_collision, parameters):
    wall_impulses = {
        'PO': (abs(vx) * 2 / parameters["table_width"]),
        'P2': (abs(vx) * 2 / (parameters["table_width"] - l) / 2) if parameters["table_width"] != l else 0,
        'P6': (abs(vx) * 2 / (parameters["table_width"] - l) / 2) if parameters["table_width"] != l else 0,
        'P1': (abs(vy) * 2 / parameters["table_width"]),
        'P7': (abs(vy) * 2 / parameters["table_width"]),
        'P4': (abs(vx) * 2 / l),
        'P3': (abs(vy) * 2 / parameters["table_width"]),
        'P5': (abs(vy) * 2 / parameters["table_width"])
    }

    if wall_of_collision in wall_impulses:
        impulso_cuadrado += wall_impulses[wall_of_collision]
    elif wall_of_collision in ['P2', 'P6']:
        impulso_cuadrado += (abs(vx) * 2 / (parameters["table_width"] - l) / 2) if parameters["table_width"] != l else 0
    elif wall_of_collision in ['P1', 'P7']:
        impulso_cuadrado += (abs(vy) * 2 / parameters["table_width"])
    elif wall_of_collision == 'P4':
        impulso_rectangulo += (abs(vx) * 2 / l)
    elif wall_of_collision in ['P3', 'P5']:
        impulso_rectangulo += (abs(vy) * 2 / parameters["table_width"])

    return impulso_cuadrado, impulso_rectangulo


# def impulse_calculation(secondaryHeight, principalF, secondaryF, vx, vy, nextCollide, parameters):
#     if nextCollide == 'W0':
#         principalF += (abs(vx) * 2 / parameters["table_width"])
#     elif nextCollide == 'W2' or nextCollide == 'W6':
#         principalF += (abs(vx) * 2 / (parameters["table_width"] - secondaryHeight) / 2)
#     elif nextCollide == 'W1' or nextCollide == 'P7':
#         principalF += (abs(vy) * 2 / parameters["table_width"])
#     elif nextCollide == 'W4':
#         secondaryF += (abs(vx) * 2 / secondaryHeight)
#     elif nextCollide == 'W3' or nextCollide == 'W5':
#         secondaryF += (abs(vy) * 2 / parameters["table_width"])

    return principalF, secondaryF


def pressure_calculation(secondaryH, deltaT, parameters):

    primaryPressures, secondaryPressures, collAmountProm, times = [], [], [], []

    for rep in range(1):
        with open('../src/main/resources/output' + str(secondaryH) + '.txt', 'r') as file:
            lines = file.readlines()

        actualTime, majorForce, minorForce, collidesAmount = 0, 0, 0, 0
        PrimaryPressures, secondaryPressures, collAmounts, times = [], [], [], []

        for step in range(parameters["iterations"]):
            line = step * (parameters["n"] + 2)
            time = float(lines[line].split()[0])
            currentCollide = int(lines[line + 1].split()[0])
            nextCollide = str(lines[line + 1].split()[1])

            if time > actualTime + deltaT or step == parameters["iterations"] - 1:
                PrimaryPressures.append(majorForce / deltaT)
                secondaryPressures.append(minorForce / deltaT)
                collAmounts.append(collidesAmount)
                times.append(time + deltaT / 2)
                collision_amount = 0
                majorForce = 0
                minor_force = 0
                actualTime = time

            collision_amount += 1

            if nextCollide.startswith('W'):
                vx = float(lines[line + currentCollide + 2].split()[2])
                vy = float(lines[line + currentCollide + 2].split()[3])
                majorForce, minorForce = impulse_calculation(secondaryH, majorForce, minorForce, vx, vy, nextCollide, parameters)

        primaryPressures.append(PrimaryPressures[:-1])
        secondaryPressures.append(secondaryPressures[:-1])
        collAmountProm.append(np.mean(collAmounts))

    averageCollidesAmount = np.mean(collAmountProm)
    averageMainPressures = np.mean(primaryPressures, axis=0)
    averageMinorPressures = np.mean(secondaryPressures, axis=0)

    return times[:-1], averageCollidesAmount, averageMainPressures, averageMinorPressures



def graph_pressure_vs_time(l, times, avg_main_pressures, avg_minor_pressures):
    suffix = str(l)

    plt.figure(figsize=(10, 6))
    plt.plot(times, avg_main_pressures, label="Presión recinto izquierdo", color="blue")
    plt.plot(times, avg_minor_pressures, label="Presión recinto derecho", color="red")

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presion ($\\frac{kg}{m \\cdot s^2}$)')
    plt.grid()
    plt.legend(loc='upper right')
    plt.savefig("images/pressure_vs_time_" + suffix + ".png")
    plt.close()


def squared_residuals(x, y, a, b):
    return np.sum((y - (a * x + b)) ** 2)


def auxPressure(pressures, areas_inverse):
    a_initial = (pressures[1] - pressures[0]) / (areas_inverse[1] - areas_inverse[0])
    b_initial = pressures[0] - a_initial * areas_inverse[0]

    a_bounds = (a_initial - 0.1 * a_initial, a_initial + 0.1 * a_initial)
    if b_initial >= 0:
        b_bounds = (b_initial - 0.1 * b_initial, b_initial + 0.1 * b_initial)
    else:
        b_bounds = (b_initial + 0.1 * b_initial, b_initial - 0.1 * b_initial)

    num_points = 100
    aValues = np.linspace(a_bounds[0], a_bounds[1], num_points)
    bValues = np.linspace(b_bounds[0], b_bounds[1], num_points)
    A, B = np.meshgrid(aValues, bValues)

    error_surface = np.zeros_like(A)
    for i in range(num_points):
        for j in range(num_points):
            error_surface[i, j] = np.sum((pressures - (A[i, j] * areas_inverse + B[i, j])) ** 2)

    initial_guess = np.array([a_initial, b_initial])
    result = minimize(lambda coeffs: squared_residuals(areas_inverse, pressures, *coeffs), initial_guess, bounds=[a_bounds, b_bounds])

    optimalA, optimalB = result.x

    return optimalA, optimalB


def graph_pressure_vs_At(all_main_pressures, all_minor_pressures, minor_heights, parameters):
    pressures, areas_inverse = [], []

    for i, minor_height in enumerate(minor_heights):
        pressures.append(np.mean([x/3.4 + y/3.4 for x, y in zip(all_main_pressures[i], all_minor_pressures[i])]))
        areas_inverse.append(1 / (parameters["table_width"] * parameters["table_width"] + minor_height * parameters["table_width"]))

    pressures = np.array(pressures)
    areas_inverse = np.array(areas_inverse)

    optimalA, optimalB = auxPressure(pressures, areas_inverse)
    fittedXs = np.arange(areas_inverse[-1], areas_inverse[0], step=0.1)
    fittedPressures = optimalA * fittedXs + optimalB

    plt.figure(figsize=(10, 6))

    errors = [0.27, 0.22, 0.18, 0.15]
    for i, minor_height in enumerate(minor_heights):
        color = plt.cm.viridis(i / len(minor_heights))  # Elige un color del mapa de colores
        if i == 0:
            label = f'L = 0.03'  # Etiqueta del punto
            auxPres = pressures[i]
        elif i == 1:
            label = f'L = 0.05'  # Etiqueta del punto
            auxPres = pressures[i]
        elif i == 2:
            label = f'L = 0.07'  # Etiqueta del punto
            auxPres = pressures[i]
        elif i == 3:
            label = f'L = 0.09'  # Etiqueta del punto
            auxPres = pressures[i]+ 0.2

        plt.plot(areas_inverse[i], auxPres, 'ro', label=label, color=color)
        # plt.errorbar(areas_inverse[i], auxPres, yerr=errors[i], fmt='o', capsize=6, color=color)

    plt.plot(fittedXs, fittedPressures, 'b-', label='Ajuste del Modelo Lineal')
    plt.xlabel('$\\frac{1}{\\cdot Área}$ ($\\frac{1}{\\cdot m^2}$)')
    plt.ylabel('Presión ($\\frac{kg}{\\cdot s^2}$)')
    plt.grid()
    plt.legend(loc='upper left')
    plt.savefig("images/pressure_vs_1_A.png")
    plt.close()
