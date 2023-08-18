from parse_files import parse_config_json, parse_static_file, parse_output_file
from utils import calculate_va_mean_and_std
from plots import plot_va_eta

def main():
    config_json_path = "../config.json"
    N, L, amount_of_iterations, va_stationary_t = parse_config_json(config_json_path)
    N_values = [400, 500]
    L_values = [10, 15]

    stats_500 = []
    stats_400 = []

    for N_value, L_value in zip(N_values, L_values):
        stats = []

        for i in range(0, 6):
            static_file_path_eta = f"../src/main/resources/statistics/staticEta{i}N{N_value}L{L_value}.txt"
            output_file_path_eta = f"../src/main/resources/statistics/outputEta{i}N{N_value}L{L_value}.txt"

            initial_state, velocity_initial_state, theta_initial_state = parse_static_file(static_file_path_eta)
            output_iterations, particle_velocities, particle_theta = parse_output_file(output_file_path_eta, initial_state, velocity_initial_state, theta_initial_state, N_value)
            aux_mean, aux_error = calculate_va_mean_and_std(particle_velocities, particle_theta, va_stationary_t, N_value, amount_of_iterations)
            stats.append([aux_mean, aux_error])

        if N_value == 500:
            stats_500 = stats
        elif N_value == 400:
            stats_400 = stats

    plot_va_eta(stats_500, stats_400, N_values, L_values, amount_of_iterations)


if __name__ == "__main__":
    main()