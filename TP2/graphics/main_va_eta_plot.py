from parse_files import parse_config_json, parse_static_file, parse_output_file
from utils import calculate_va_mean_and_std
from plots import plot_va_eta

def main():
    config_json_path = "../config.json"
    N, L, amount_of_iterations, va_stationary_t = parse_config_json(config_json_path)
    N_values = [100, 400]
    L_values = [5.0, 10.0]

    stats_100 = []
    stats_400 = []

    for N_value, L_value in zip(N_values, L_values):
        stats = []

        for i in range(0, 11):  # Itera de 0 a 5 inclusive (6 iteraciones)
            i_value = i * 0.5  # Calcula el valor correspondiente a la iteración actual
            static_file_path_eta = f"../src/main/resources/statisticsEta/staticEta{i_value}N{N_value}L{L_value}.txt"
            output_file_path_eta = f"../src/main/resources/statisticsEta/outputEta{i_value}N{N_value}L{L_value}.txt"

            initial_state, velocity_initial_state, theta_initial_state = parse_static_file(static_file_path_eta)
            output_iterations, particle_velocities, particle_theta = parse_output_file(output_file_path_eta, initial_state, velocity_initial_state, theta_initial_state, N_value)
            aux_mean, aux_error = calculate_va_mean_and_std(particle_velocities, particle_theta, va_stationary_t, N_value, amount_of_iterations)
            stats.append([aux_mean, aux_error])

        if N_value == 100:
            stats_100 = stats
        elif N_value == 400:
            stats_400 = stats

    plot_va_eta(stats_100, stats_400, N_values, L_values, amount_of_iterations)


if __name__ == "__main__":
    main()