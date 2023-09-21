import numpy as np
import matplotlib.pyplot as plt

# Genera datos de ejemplo para el MSE
x = np.linspace(0.00001, 0.0001, 100)  # Valores del eje x de 0.00001 a 0.0001
y = 1000000 * (x - 0.0000299) ** 2 + 0.006  # Función cuadrática con mínimo en (0.00003, 0.1)

# Crea la gráfica del MSE
plt.figure(figsize=(8, 6))
plt.plot(x, y, color='blue')
plt.xlabel('Coeficiente de difusión (m^2 / s)')
plt.ylabel('Error')
plt.grid(True)

# Configura el rango del eje x para mostrar la forma deseada
plt.xlim(0.00001, 0.0001)

# Configura el eje y para comenzar en 0.002 y luego en intervalos de 0.003
plt.yticks(np.arange(0.002, 0.0201, step=0.0015))

# Marca el mínimo en el gráfico
plt.scatter(0.0000299, 0.006, color='red', marker='o', label='Mínimo')

# Muestra la leyenda
plt.legend()

# Guarda la gráfica en un archivo o la muestra en pantalla
plt.savefig('images/mse_curve.png')  # Guarda la gráfica en un archivo
plt.show()  # Muestra la gráfica en pantalla
