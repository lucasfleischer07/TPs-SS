import numpy as np
import matplotlib.pyplot as plt

# Genera datos de ejemplo para el MSE
x = np.linspace(0, 0.2, 100)  # Valores del eje x de 0 a 0.2
y = (x - 0.08) ** 2 + 0.02  # Función cuadrática con mínimo en (0.08, 0.01)

# Crea la gráfica del MSE
plt.figure(figsize=(8, 6))
plt.plot(x, y, label='MSE', color='blue')
plt.xlabel('Pendiente')
plt.ylabel('Error Cuadrático Medio (MSE)')
plt.grid(True)

# Marca el mínimo en el gráfico
plt.scatter(0.08, 0.02, color='red', marker='o', label='Mínimo')

# Muestra la leyenda
plt.legend()

# Guarda la gráfica en un archivo o la muestra en pantalla
plt.savefig('images/mse_curve.png')  # Guarda la gráfica en un archivo
plt.show()  # Muestra la gráfica en pantalla
