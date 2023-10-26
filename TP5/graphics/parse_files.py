import json


def parse_config_json(file_path):
    with open(file_path, 'r') as file:
        config = json.load(file)

        W = config["W"]
        L = config["L"]
        D = config["D"]
        N = int(config["N"])
        mass = config["mass"]
        dt = config["dt"]
        iterations = config["iterations"]
        A = config["A"]

        return N, W, L, D, N, mass, dt, iterations, A


def parse_output_file(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    particles_data = {}
    time = None
    limits = []

    for line in lines:
        data = line.split("\t")

        if len(data) == 3:
            time = float(data[0])
            particles_data[time] = []
            limits.append([float(data[1]), float(data[2])])
        else:
            particle = {
                'id': float(data[0]),
                'x': float(data[1]),
                'y': float(data[2]),
                'vx': float(data[3]),
                'vy': float(data[4]),
                'fx': float(data[5]),
                'fy': float(data[6]),
                'radius': float(data[7]),
                'color': data[8]
            }

            particles_data[time].append(particle)

    return particles_data, limits
