def parse_output_file(output_path, integration_methods):
    # Devuelvo un diccionario donde la clave es el metodo de integracion y el valor es otro diccionario donde tengo
    # la posicion para cada metodo de integracion

    with open(output_path, 'r') as file:
        lines = file.readlines()

        method_idx = -1

        positions = {}
        time = None

        for line in lines:
            data = line.split()

            if len(data) == 1:
                time = float(data[0])

                if time == 0:
                    method_idx += 1
                    positions[integration_methods[method_idx]] = {}
            else:
                # velocity = float(data[1])
                positions[integration_methods[method_idx]][time] = float(data[0])

    return positions

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