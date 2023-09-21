import graphics
from graphics import *
from parse_files import get_parameters

def main():
    config_json_path = "../config.json"
    static_file_path = "../src/main/resources/static.txt"
    output_file_path = "../src/main/resources/output.txt"

    times, main_pressures_dict, minor_pressures_dict = [], [], []
    parameters = get_parameters()
    delta_t = 0.45

    for l in [0.03, 0.05, 0.07, 0.09]:
        times, avg_main_pressures, avg_minor_pressures, avg_collision_amount = graphics.pressure_calculation(l, delta_t, parameters)
        main_pressures_dict.append(avg_main_pressures)
        minor_pressures_dict.append(avg_minor_pressures)

        graphics.graph_pressure_vs_time(l, times, avg_main_pressures, avg_minor_pressures)

    graphics.graph_pressure_vs_At(main_pressures_dict, minor_pressures_dict, [0.03, 0.05, 0.07, 0.09], parameters)


if __name__ == "__main__":
    main()