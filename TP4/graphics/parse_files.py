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

