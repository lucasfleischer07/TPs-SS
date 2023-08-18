import numpy as np

#va es el parametro de orden

# iterations es una lista de listas.
# tiene la forma: [[List(ParticleProperties)], [List particle2], [listPart3], ...], [idem pero iteracion2], [idem pero iteracion3], [...]
# i va desde nuestra iteracion estacionaria hasta la ultima iteracion.
# j va de 0 a N en cuanto N particulas

# tiene la forma: [[v1, v2, v3], [List particle2], [listPart3], ...], [idem pero iteracion2], [idem pero iteracion3], [...]



# def calculate_va_mean_and_std(N, amount_of_iterations, iterations, va_stationary_t):
#     # v_a inicializado en cero para guardar valores desde el punto estacionario hasta las 1000 iteraciones
#     v_a = np.zeros(amount_of_iterations - va_stationary_t)
#
#     # Compute v_a for each time step
#     for i in range(va_stationary_t, amount_of_iterations):
#         # Inicializamos un array para acumular la suma de todas las velocidades (del eje x e y)
#         v = np.array([0.0, 0.0])
#         # Itero sobre cada particula
#         for j in range(N):
#             aux_v = iterations[i][j][2]     # Agarro la velocidad de dicha particula
#             theta = iterations[i][j][3]     # Agarro el theta de dicha particula
#             v += np.array([aux_v * np.cos(theta), aux_v * np.sin(theta)])   # Calculo y almaceno la velocidades en el eje x e y
#
#         v = np.linalg.norm(v)  # Calculo de la norma
#
#         v_a[i - va_stationary_t] = (1 / (N * aux_v)) * v
#
#     mean_va = np.mean(v_a)      # Calculo de la media
#     std_va = np.std(v_a)        # Calculo del desvio
#
#     print(f"Mean v_a: {mean_va}")
#     print(f"Std v_a: {std_va}")

import numpy as np

def calculate_va_mean_and_std(N, amount_of_iterations, iterations, va_stationary_t):
    v_a_values = []

    for i in range(va_stationary_t, amount_of_iterations):
        total_vx = 0.0
        total_vy = 0.0

        for j in range(N):
            vx = iterations[i][j][2] * np.cos(iterations[i][j][3])  # Componente x de la velocidad
            vy = iterations[i][j][2] * np.sin(iterations[i][j][3])  # Componente y de la velocidad

            total_vx += vx
            total_vy += vy

        avg_vx = total_vx / N
        avg_vy = total_vy / N

        v_a = np.sqrt(avg_vx**2 + avg_vy**2)  # Magnitud de la velocidad promedio

        v_a_values.append(v_a)

    mean_va = np.mean(v_a_values)
    error_va = np.std(v_a_values)

    print(f"Mean v_a: {mean_va}")
    print(f"Error v_a: {error_va}")
