import numpy as np
from matplotlib import pyplot as plt
from scipy.optimize import minimize
from parse_files import read_lines


# def impulse_calculation(l, impulso_cuadrado, impulso_rectangulo, vx, vy, wall_of_collision, parameters):
#     wall_impulses = {
#         'PO': (abs(vx) * 2 / parameters["table_width"]),
#         'P2': (abs(vx) * 2 / (parameters["table_width"] - l) / 2) if parameters["table_width"] != l else 0,
#         'P6': (abs(vx) * 2 / (parameters["table_width"] - l) / 2) if parameters["table_width"] != l else 0,
#         'P1': (abs(vy) * 2 / parameters["table_width"]),
#         'P7': (abs(vy) * 2 / parameters["table_width"]),
#         'P4': (abs(vx) * 2 / l),
#         'P3': (abs(vy) * 2 / parameters["table_width"]),
#         'P5': (abs(vy) * 2 / parameters["table_width"])
#     }
#
#     if wall_of_collision in wall_impulses:
#         impulso_cuadrado += wall_impulses[wall_of_collision]
#     elif wall_of_collision in ['P2', 'P6']:
#         impulso_cuadrado += (abs(vx) * 2 / (parameters["table_width"] - l) / 2) if parameters["table_width"] != l else 0
#     elif wall_of_collision in ['P1', 'P7']:
#         impulso_cuadrado += (abs(vy) * 2 / parameters["table_width"])
#     elif wall_of_collision == 'P4':
#         impulso_rectangulo += (abs(vx) * 2 / l)
#     elif wall_of_collision in ['P3', 'P5']:
#         impulso_rectangulo += (abs(vy) * 2 / parameters["table_width"])
#
#     return impulso_cuadrado, impulso_rectangulo


# def process_system(l, iterations, delta_time, parameters):
#     suma_presiones_cuadrado, suma_presiones_rectangulo, cantidad_colisiones, tiempo_transcurrido = [], [], [], []
#
#     for iteration in range(iterations):
#         adding_suffix = str(l)
#         with open('../src/main/resources/output' + adding_suffix + '.txt', 'r') as file:
#             lines = file.readlines()
#
#         presiones_cuadrado, presiones_rectangulo, cantidad_colisiones_for, times = [], [], [], []
#         local_time, impulso_cuadrado, impulso_rectangulo, collision_amount = 0, 0, 0, 0
#
#         for event in range(parameters["iterations"]):
#             first_line = event * (parameters["n"] + 2) # salteamos 2 posiciones por lo escrito en output
#             time = float(lines[first_line].split()[0]) # lo dividimos en dos y tomamos el tiempo que esta en la posicion 0
#             object_choque1 = int(lines[first_line + 1].split()[0]) # del output tomamos los dos numeros de particulas/ particula-pared que colisionan
#             object_choque2 = str(lines[first_line + 1].split()[1]) # tanto el primero como el segundo
#
#             if time > local_time + delta_time or event == parameters["iterations"] - 1:
#                 presiones_cuadrado.append(impulso_cuadrado / delta_time)
#                 presiones_rectangulo.append(impulso_rectangulo / delta_time)
#                 cantidad_colisiones_for.append(collision_amount)
#                 times.append(time + delta_time / 2)
#                 collision_amount, impulso_cuadrado, impulso_rectangulo = 0, 0, 0
#                 local_time = time
#
#             collision_amount += 1
#
#             # si estoy chocando con una pared
#             if object_choque2.startswith('P'):
#                 vx = float(lines[first_line + object_choque1 + 2].split()[2]) # agarro la velocidad en x del output
#                 vy = float(lines[first_line + object_choque1 + 2].split()[3]) # agarro la velocidad en y del output
#                 impulso_cuadrado, impulso_rectangulo = impulso_transferido(l, impulso_cuadrado, impulso_rectangulo, vx, vy, object_choque2, parameters)
#
#         suma_presiones_cuadrado.append(presiones_cuadrado[:-1])
#         suma_presiones_rectangulo.append(presiones_rectangulo[:-1])
#         cantidad_colisiones.append(np.mean(cantidad_colisiones_for))
#
#     promedio_colisiones = np.mean(cantidad_colisiones)
#     promedio_presion_cuadrado = np.mean(suma_presiones_cuadrado, axis=0)
#     promedio_presion_rectangulo = np.mean(suma_presiones_rectangulo, axis=0)
#
#     return tiempo_transcurrido[:-1], promedio_presion_cuadrado, promedio_presion_rectangulo, promedio_colisiones




#usamos este
def impulse_calculation(minor_height, main_force, minor_force, vx, vy, second_object_colliding, parameters):
    if second_object_colliding == 'W0':
        main_force += (abs(vx) * 2 / parameters["table_width"])
    elif second_object_colliding == 'W2' or second_object_colliding == 'W6':
        main_force += (abs(vx) * 2 / (parameters["table_width"] - minor_height) / 2)
    elif second_object_colliding == 'W1' or second_object_colliding == 'P7':
        main_force += (abs(vy) * 2 / parameters["table_width"])
    elif second_object_colliding == 'W4':
        minor_force += (abs(vx) * 2 / minor_height)
    elif second_object_colliding == 'W3' or second_object_colliding == 'W5':
        minor_force += (abs(vy) * 2 / parameters["table_width"])

    return main_force, minor_force


# def process_system(minor_height, reps, delta_t, parameters):
#     all_main_pressures = []
#     all_minor_pressures = []
#     avg_collision_amounts = []
#     times = []
#
#     for rep in range(reps):
#         with open('../src/main/resources/output' + str(minor_height) + '.txt', 'r') as file:
#             lines = file.readlines()
#
#         current_time = 0
#         main_force = 0
#         minor_force = 0
#         collision_amount = 0
#
#         main_pressures = []
#         minor_pressures = []
#         collision_amounts = []
#         times = []
#
#         for step in range(parameters["iterations"]):
#             first_line = step * (parameters["n"] + 2)  # salteamos 2 posiciones por lo escrito en output
#             time = float(lines[first_line].split()[0])
#             first_object_colliding = int(lines[first_line + 1].split()[0])
#             second_object_colliding = str(lines[first_line + 1].split()[1])
#
#             if time > current_time + delta_t or step == parameters["iterations"] - 1:
#                 main_pressures.append(main_force / delta_t)
#                 minor_pressures.append(minor_force / delta_t)
#                 collision_amounts.append(collision_amount)
#                 times.append(time + delta_t / 2)
#                 collision_amount = 0
#                 main_force = 0
#                 minor_force = 0
#                 current_time = time
#
#             collision_amount += 1
#
#             if second_object_colliding.startswith('P'):
#                 vx = float(lines[first_line + first_object_colliding + 2].split()[2])  # + 3 because of the first 3 lines (step, time, collision)
#                 vy = float(lines[first_line + first_object_colliding + 2].split()[3])
#                 main_force, minor_force = add_force(minor_height, main_force, minor_force, vx, vy, second_object_colliding, parameters)
#
#         all_main_pressures.append(main_pressures[:-1])
#         all_minor_pressures.append(minor_pressures[:-1])
#         avg_collision_amounts.append(np.mean(collision_amounts))
#
#     avg_collision_amount = np.mean(avg_collision_amounts)
#     avg_main_pressures = np.mean(all_main_pressures, axis=0)
#     avg_minor_pressures = np.mean(all_minor_pressures, axis=0)
#
#     return times[:-1], avg_main_pressures, avg_minor_pressures, avg_collision_amount


def pressure_calculation(minor_height, delta_t, parameters):
    all_main_pressures, all_minor_pressures, avg_collision_amounts, times = [], [], [], []

    for rep in range(1):
        output_file_path = f'../src/main/resources/output{minor_height}.txt'

        lines = read_lines(output_file_path)

        current_time, main_force, minor_force, collision_amount = 0, 0, 0, 0
        main_pressures, minor_pressures, collision_amounts, times = [], [], [], []

        for step in range(parameters["iterations"]):
            first_line = step * (parameters["n"] + 2)  # saltamos 2 posiciones por lo escrito en output
            time = float(lines[first_line].split()[0])
            first_object_colliding = int(lines[first_line + 1].split()[0])
            second_object_colliding = str(lines[first_line + 1].split()[1])

            if time > current_time + delta_t or step == parameters["iterations"] - 1:
                main_pressures.append(main_force / delta_t)
                minor_pressures.append(minor_force / delta_t)
                collision_amounts.append(collision_amount)
                times.append(time + delta_t / 2)
                collision_amount, main_force, minor_force = 0, 0, 0
                current_time = time

            collision_amount += 1

            if second_object_colliding.startswith('W'):
                particle_data = lines[first_line + first_object_colliding + 2].split()
                vx = float(particle_data[2])
                vy = float(particle_data[3])
                main_force, minor_force = impulse_calculation(minor_height, main_force, minor_force, vx, vy, second_object_colliding, parameters)

        all_main_pressures.append(main_pressures[:-1])
        all_minor_pressures.append(minor_pressures[:-1])
        avg_collision_amounts.append(np.mean(collision_amounts))

    avg_collision_amount = np.mean(avg_collision_amounts)
    avg_main_pressures = np.mean(all_main_pressures, axis=0)
    avg_minor_pressures = np.mean(all_minor_pressures, axis=0)

    return times[:-1], avg_main_pressures, avg_minor_pressures, avg_collision_amount


def graph_pressure_vs_time(l, times, avg_main_pressures, avg_minor_pressures):
    file_suffix = str(l)
    # Graph main_pressures and minor_pressures in the same graph, having pressure in y-axis and time in x-axis
    plt.figure(figsize=(10, 6))
    plt.plot(times, avg_main_pressures, label="Cuadrado", color="blue")
    plt.plot(times, avg_minor_pressures, label="Rectángulo", color="red")
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Presión (N/m)')
    plt.grid()
    plt.legend(loc='upper right')
    plt.savefig("images/pressureVsTime_" + file_suffix + ".png")
    print("Saved pressureVsTime_" + file_suffix + ".png")
    plt.close()


# def f(x, a, b):
#     return a * x + b


def squared_residuals(x, y, a, b):
    return np.sum((y - (a * x + b)) ** 2)
    # return np.sum((y - f(x, a, b)) ** 2)


def adjust_pressure_vs_at(pressures, areas_inverse):
    a_initial = (pressures[1] - pressures[0]) / (areas_inverse[1] - areas_inverse[0])
    b_initial = pressures[0] - a_initial * areas_inverse[0]

    # Bounds for 'a' and 'b' within 20% of the initial values
    a_bounds = (a_initial - 0.1 * a_initial, a_initial + 0.1 * a_initial)
    if b_initial >= 0:
        b_bounds = (b_initial - 0.1 * b_initial, b_initial + 0.1 * b_initial)
    else:
        b_bounds = (b_initial + 0.1 * b_initial, b_initial - 0.1 * b_initial)

    # Create a grid of 'a' and 'b' values
    num_points = 100
    a_values = np.linspace(a_bounds[0], a_bounds[1], num_points)
    b_values = np.linspace(b_bounds[0], b_bounds[1], num_points)
    A, B = np.meshgrid(a_values, b_values)

    # Calculate the squared residuals for each combination of 'a' and 'b'
    error_surface = np.zeros_like(A)
    for i in range(num_points):
        for j in range(num_points):
            error_surface[i, j] = np.sum((pressures - (A[i, j] * areas_inverse + B[i, j])) ** 2)
            # error_surface[i, j] = squared_residuals(areas_inverse, pressures, A[i, j], B[i, j])

    # Find the minimum of the error surface using minimize
    initial_guess = np.array([a_initial, b_initial])
    result = minimize(lambda coeffs: squared_residuals(areas_inverse, pressures, *coeffs), initial_guess, bounds=[a_bounds, b_bounds])

    # Get the best-fitting coefficients
    best_a, best_b = result.x

    # Plot the 3D error surface
    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')
    surf = ax.plot_surface(A, B, error_surface, cmap='viridis')  # Plot the error surface
    ax.set_xlabel('a')
    ax.set_ylabel('b')
    ax.set_zlabel('Error')

    # Plot the point corresponding to the minimum
    ax.scatter(best_a, best_b, squared_residuals(areas_inverse, pressures, best_a, best_b), color='black', s=50, label='Minimum')
    ax.view_init(elev=20)
    plt.legend()
    plt.savefig("images/adjust_pressureVsAt.png")
    print("Saved adjust_pressureVsAt.png")
    plt.close()

    return best_a, best_b


def graph_pressure_vs_At(all_main_pressures, all_minor_pressures, minor_heights, parameters):
    pressures = []
    areas_inverse = []

    for i, minor_height in enumerate(minor_heights):
        pressures.append(np.mean([x + y for x, y in zip(all_main_pressures[i], all_minor_pressures[i])]))  # TODO: luego del estado estacionario
        areas_inverse.append(1 / (parameters["main_height"] * parameters["main_width"] + minor_height * parameters["minor_width"]))
        print("area: " + str((parameters["main_height"] * parameters["main_width"] + minor_height * parameters["minor_width"])))
    pressures = np.array(pressures)
    areas_inverse = np.array(areas_inverse)

    best_a, best_b = adjust_pressure_vs_at(pressures, areas_inverse)
    fitted_xs = np.arange(areas_inverse[-1], areas_inverse[0], step=0.1)
    fitted_pressures = best_a*fitted_xs + best_b
    # fitted_pressures = f(fitted_xs, best_a, best_b)

    # Grafica los datos originales y la curva de ajuste con el valor óptimo de c
    plt.figure(figsize=(10, 6))
    plt.plot(areas_inverse, pressures, 'ro', label='Datos Originales')
    plt.plot(fitted_xs, fitted_pressures, 'b-', label='Curva de Ajuste Lineal')
    plt.xlabel('1/Área (1/m)')
    plt.ylabel('Presión (N/m)')
    plt.grid()
    plt.legend(loc='upper left')
    plt.savefig("images/pressureVsAt.png")
    print("Saved pressureVsAt.png")
    plt.close()