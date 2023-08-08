def read_neighbors(output_file):
    data = {}
    with open(output_file, 'r') as file:
        for line in file:
            parts = line.strip().split('\t')  # parts[0]=id parts[1]=list_neighbors
            key = int(parts[0])
            values = [int(val) for val in parts[2].split(',') if val.strip()]
            data[key] = values
    return data


def read_dynamic(dynamic_file):
    data = {}
    with open(dynamic_file, 'r') as file:
        file.readline()
        for i, line in enumerate(file):
            parts = line.strip().split('\t')  # parts[0]=x parts[1]=y
            key = i
            values = [parts[0], parts[1]]
            data[key] = values
    return data

def read_static(static_file):
    n = None
    l = None
    radius_and_prop_map = {}
    with open(static_file, 'r') as file:
        n = file.readline()
        l = file.readline()
        for i, line in enumerate(file):
            parts = line.strip().split('\t')  # parts[0]=x parts[1]=y
            key = i
            values = [parts[0], parts[1]]
            radius_and_prop_map[key] = values

    print(radius_and_prop_map)
    return n, l, radius_and_prop_map


def read_time(time_file):
    data = None
    with open(time_file, 'r') as file:
        line = file.readline().strip().split("=")
        data = line[1].strip()

    return data
