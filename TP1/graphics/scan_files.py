def read_data(output_file):
    data = {}
    with open(output_file, 'r') as file:
        for line in file:
            parts = line.strip().split('\t')
            key = int(parts[0])
            values = [int(val) for val in parts[2].split(',') if val.strip()]
            data[key] = values
    return data


def read_time(time_file):
    data = None
    with open(time_file, 'r') as file:
        line = file.readline().strip().split("=")
        data = line[1].strip()

    return data
