def parse_output_file(outputPath, integrationMethods) -> dict[str, dict[float, float]]:
    # Devuelvo un diccionario donde la clave es el metodo de integracion y el valor es otro diccionario donde tengo
    # la posicion para cada metodo de integracion

    integration_method_positions = {}
    current_method = None

    with open(outputPath, 'r') as file:
        for line in file:
            parts = line.split()

            if len(parts) == 1:
                current_method = integrationMethods.pop(0)
                integration_method_positions[current_method] = {}
            else:
                time, position = map(float, parts)
                integration_method_positions[current_method][time] = position

    return integration_method_positions