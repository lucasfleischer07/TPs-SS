import numpy as np

# va es el parametro de orden
# iterations es una lista de listas.
# tiene la forma: [[List(PProperties)], [List p2], [listPart3], ...], [idem pero iter2], [idem pero iter3], [...]
# i va desde nuestra iteracion estacionaria hasta la ultima iteracion.
# j va de 0 a N en cuanto N particulas

# forma: [[v1, v2, v3], [List particle2], [listPart3], ...], [idem pero iteracion2], [idem pero iteracion3], [...]

# va = 1/N*v |sum(1, N) vi| con |norma|


def calculate_va_mean_and_std(particle_velocities, particle_theta, va_stationary_t, n, amount_of_iterations):
    v_a_values = []

    for i in range(va_stationary_t, amount_of_iterations):
        total_vx = 0.0
        total_vy = 0.0

        for j in range(n):
            vx = particle_velocities[i][j] * np.cos(particle_theta[i][j])  # Componente x de la velocidad
            vy = particle_velocities[i][j] * np.sin(particle_theta[i][j])  # Componente y de la velocidad

            total_vx += vx
            total_vy += vy

        v_total = np.sqrt(total_vx**2 + total_vy**2)  # Magnitud de la velocidad total

        # Formula de va
        v_a_values.append((1 / (n * particle_velocities[i][0])) * v_total)

    mean_va = np.mean(v_a_values)
    error_va = np.std(v_a_values)

    print(f"Mean v_a: {mean_va}")
    print(f"Error v_a: {error_va}")

    return mean_va, error_va