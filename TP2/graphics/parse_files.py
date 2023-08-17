import json


def parse_config_json(file_path):
    with open(file_path, 'r') as file:
        config = json.load(file)

        N = int(config["N"])
        L = float(config["L"])
        iterations = int(config["iterations"])

        # va_stationary_t = int(config["vaStationaryT"])

        return N, L, iterations


# Me agarra el valor inicial de velocidad, posiciones y theta de cada particula y la guardo en un diccionario de diciconarios
def parse_static_file(static_file_path):
    # Creo un diccionario de diccionarios donde la clave del primero es la iteracion y el valor es otro diccionario
    # donde la clave es el id de las particulas y el valor, la velocidad, posicion y theta
    # iteration = {0: {id: {posX, posY, vel, theta}}}
    iterations = {0: {}}

    with open(static_file_path, 'r') as static_file:
        static_file.readline().rsplit("\n")  # Me salteo la primer lunea del archivo que es el rc que no lo uso ahora

        particle_id = 0             # Lo uso como indice para la clave del segundo diccionario (que es el id de cada particula)
        for line in static_file:    # Itero sobre cada linea del archivo
            iterations[0][particle_id] = [float(particle_info) for particle_info in line.rstrip("\n").split("\t")[1:]]
            particle_id += 1

    return iterations


def parse_output_file(output_file_path, iterations, N):
    with open(output_file_path, "r") as output_file:
        amount_of_particles = 0
        iteration = 0
        skip = True     # Lo uso para skipear la linea que indica la iteracion

        for line in output_file:    # Itero por cada linea del archivo
            if amount_of_particles % N == 0 and skip:   # Si la linea indica el numero de iteracion, la salteo ya que no contiene info de las particulas
                iteration += 1
                iterations[iteration] = {}  # Creo un nuevo diccionario para almacenar la info que va a venir despues
                skip = False
                continue

            particle_data = [float(x) for x in line.rstrip("\n").split("\t")]
            iterations[iteration][particle_data[0]] = particle_data[1:]

            amount_of_particles += 1
            skip = True

    return iterations
