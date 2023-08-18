import json


def parse_config_json(file_path):
    with open(file_path, 'r') as file:
        config = json.load(file)

        N = int(config["N"])
        L = float(config["L"])
        iterations = int(config["iterations"])

        # va_stationary_t = int(config["vaStationaryT"])

        return N, L, iterations


def parse_static_file(static_file_path):
    iterations = [[]]
    particle_velocities = [[]]  # Lista de listas para almacenar las velocidades de las partículas en cada iteración
    particle_theta = [[]]       # Lista de listas para almacenar las thetas de las partículas en cada iteración

    with open(static_file_path, 'r') as static_file:
        static_file.readline()  # Me salteo la primer línea del archivo que es el rc que no lo uso ahora

        for line in static_file:    # Itero sobre cada línea del archivo
            particle_info = [float(info) for info in line.rstrip("\n").split("\t")[1:]]
            iterations[-1].append(particle_info)
            particle_velocities[-1].append(particle_info[2])  # Agrego la velocidad de la partícula a la lista de velocidades
            particle_theta[-1].append(particle_info[3])  # Agrego la velocidad de la partícula a la lista de velocidades

    return iterations, particle_velocities, particle_theta


def parse_output_file(output_file_path, iterations, particle_velocities, particle_theta, N):
    with open(output_file_path, "r") as output_file:
        amount_of_particles, iteration = 0, 0
        skip = True                 # Lo uso para skipear la linea que indica la iteracion

        for line in output_file:    # Itero por cada linea del archivo
            if amount_of_particles % N == 0 and skip:   # Si la linea indica el numero de iteracion, la salteo ya que no contiene info de las particulas
                iteration += 1
                iterations.append([])           # Creo una nueva lista para almacenar la info que va a venir después
                particle_velocities.append([])  # Creo una nueva lista para las velocidades en esta iteración
                particle_theta.append([])       # Creo una nueva lista para las thetas en esta iteración
                skip = False
                continue

            particle_data = [float(x) for x in line.rstrip("\n").split("\t")]
            iterations[-1].append(particle_data[1:])            # Agrego la información de la partícula a la última iteración
            particle_velocities[-1].append(particle_data[3])    # Agrego la velocidad de la partícula a la lista de velocidades
            particle_theta[-1].append(particle_data[4])         # Agrego la velocidad de la partícula a la lista de velocidades

            amount_of_particles += 1
            skip = True

    return iterations, particle_velocities, particle_theta


