def parse_output_file(output_path, integration_methods):
    # Devuelvo un diccionario donde la clave es el metodo de integracion y el valor es otro diccionario donde tengo
    # la posicion para cada metodo de integracion

    integration_method_positions = {}
    current_method = None

    with open(output_path, 'r') as file:
        for line in file:
            parts = line.split()

            if len(parts) == 1:
                current_method = integration_methods.pop(0)
                integration_method_positions[current_method] = {}
            else:
                time, current_method_position = map(float, parts)
                integration_method_positions[current_method][time] = current_method_position

    return integration_method_positions

