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

    particles_data = {}
    time = None

    for line in lines:
        data = line.split("\t")

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


def read_lines(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    return lines


def get_parameters():
    with open("../src/main/resources/data.txt", 'r') as data_file:
        data_lines = data_file.readlines()

    n = int(data_lines[0].split()[1])
    iterations = int(data_lines[1].split()[1])
    particle_radius = float(data_lines[2].split()[1])
    particle_mass = float(data_lines[3].split()[1])
    particle_initial_vel = float(data_lines[4].split()[1])
    table_width = float(data_lines[5].split()[1])

    parameters = {
        "n": n,
        "iterations": iterations,
        "particle_radius": particle_radius,
        "particle_mass": particle_mass,
        "particle_initial_vel": particle_initial_vel,
        "table_width": table_width
    }

    return parameters