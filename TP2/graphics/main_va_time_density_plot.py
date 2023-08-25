from parse_files import parse_config_json, parse_static_file, parse_output_file
from utils import calculate_va_in_each_iteration
from plots import plot_va_time_density

def main():
    config_json_path = "../config.json"
    N, L, amount_of_iterations, va_stationary_t = parse_config_json(config_json_path)
    N_values = [100, 400, 800]
    stats = []
    eta = 2.0

    for n in N_values:
        static_file_path_eta = f"../src/main/resources/statisticsTime/staticEta{eta}N{n}L{int(L)}.txt"
        output_file_path_eta = f"../src/main/resources/statisticsTime/outputEta{eta}N{n}L{int(L)}.txt"

        initial_state, velocity_initial_state, theta_initial_state = parse_static_file(static_file_path_eta)
        output_iterations, particle_velocities, particle_theta = parse_output_file(output_file_path_eta, initial_state, velocity_initial_state, theta_initial_state, n)
        va = calculate_va_in_each_iteration(particle_velocities, particle_theta, n, amount_of_iterations)
        stats.append(va)

    plot_va_time_density(stats, N_values, L, eta)


if __name__ == "__main__":
    main()