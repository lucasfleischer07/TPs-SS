from parse_files import parse_config_json, parse_static_file, parse_output_file
from utils import calculate_va_mean_and_std
from plots import plot_va_rho

def main():
    config_json_path = "../config.json"
    N, L, amount_of_iterations, va_stationary_t = parse_config_json(config_json_path)
    N_values = [150, 300, 450, 750, 900, 1200, 2400, 3200, 4000]

    stats = []

    for N_value in N_values:
        static_file_path_eta = f"../src/main/resources/statisticsRho/staticRho{int(N_value/(L*L))}N{N_value}L{int(L)}.txt"
        output_file_path_eta = f"../src/main/resources/statisticsRho/outputRho{int(N_value/(L*L))}N{N_value}L{int(L)}.txt"

        initial_state, velocity_initial_state, theta_initial_state = parse_static_file(static_file_path_eta)
        output_iterations, particle_velocities, particle_theta = parse_output_file(output_file_path_eta, initial_state, velocity_initial_state, theta_initial_state, N_value)
        aux_mean, aux_error = calculate_va_mean_and_std(particle_velocities, particle_theta, va_stationary_t, N_value, amount_of_iterations)
        stats.append([aux_mean, aux_error])

    plot_va_rho(stats, N_values, L, amount_of_iterations)


if __name__ == "__main__":
    main()