import numpy as np
import math

def calculate_analytical_positions(time_list: np.array) -> list[float]:
    # Retorna la posicion analitica (es la formula que esta en la filmina 36 del ppt)
    A = 1.0
    k = 10000
    mass = 70.0
    gamma = 100.0

    return A * np.exp(-gamma*time_list/(2*mass)) * np.cos(math.sqrt(k/mass - gamma**2/(4*mass**2))*time_list)
