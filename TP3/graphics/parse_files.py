import json


def parse_config_json(file_path):
    with open(file_path, 'r') as file:
        config = json.load(file)

        N = int(config["N"])
        particleRadius = config["particleRadius"]
        enclosure1X = config["enclosure1X"]
        enclosure1Y = config["enclosure1Y"]
        enclosure2X = config["enclosure2X"]
        enclosure2Y = config["enclosure2Y"]

        return N, particleRadius, enclosure1X, enclosure1Y, enclosure2X, enclosure2Y


def parse_output_file(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    # Inicializa las variables para almacenar temporalmente los datos
    particles_data = {}
    time = None

    for line in lines:
        data = line.split("\t")
        # data = line.split()

        if len(data) == 1:
            time = float(data[0])
            particles_data[time] = []
        else:
            particle = {
                'x': float(data[0]),
                'y': float(data[1]),
                'vx': float(data[2]),
                'vy': float(data[3]),
                'radius': float(data[4]),
                'id': int(data[5])
            }

            particles_data[time].append(particle)

    return particles_data
