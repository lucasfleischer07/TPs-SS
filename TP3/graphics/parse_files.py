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
