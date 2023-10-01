import json


def parse_config_json(file_path):
    with open(file_path, 'r') as file:
        config = json.load(file)

        N = int(config["N"])
        particleRadius = config["particleRadius"]
        lineLength = config["lineLength"]
        iterations = config["iterations"]

        return N, particleRadius, lineLength, iterations


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
                'id': float(data[0]),
                'x': float(data[1]),
                'y': float(data[2]),
                'vx': float(data[3]),
                'vy': float(data[4]),
                'radius': float(data[7]),
            }

            particles_data[time].append(particle)

    return particles_data

def read_mse_txt_file(filename, integration_methods):
    data = {}
    current_method = None

    with open(filename, 'r') as file:
        lines = file.readlines()

    for line in lines:
        parts = line.strip().split()

        if len(parts) == 1:
            current_method = float(parts[0])
            data[current_method] = {}
        elif current_method is not None and len(parts) == 3:
            values = [float(part) for part in parts]
            data[current_method][values[0]] = values[1:]

    return data


def read_lines(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    return lines
