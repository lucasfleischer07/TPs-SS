from parse_files import parse_config_json, parse_static_file, parse_output_file
from utils import calculate_va_in_each_iteration
from plots import plot_va_time_noise

def main():
    config_json_path = "../config.json"
    N, L, amount_of_iterations, va_stationary_t = parse_config_json(config_json_path)
    stats = []

    for i in range(0, 12):
        i = i*0.5
        if i == 0.0 or i == 1.5 or i == 3.5:
            static_file_path_eta = f"../src/main/resources/statisticsTime/staticEta{float(i)}N{N}L{int(L)}.txt"
            output_file_path_eta = f"../src/main/resources/statisticsTime/outputEta{i}N{N}L{int(L)}.txt"

            initial_state, velocity_initial_state, theta_initial_state = parse_static_file(static_file_path_eta)
            output_iterations, particle_velocities, particle_theta = parse_output_file(output_file_path_eta, initial_state, velocity_initial_state, theta_initial_state, N)
            va = calculate_va_in_each_iteration(particle_velocities, particle_theta, N, amount_of_iterations)
            stats.append(va)

    plot_va_time_noise(stats, N, L)


if __name__ == "__main__":
    main()