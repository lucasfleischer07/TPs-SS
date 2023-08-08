class Particle:
    def __init__(self, particle_id, neighbors):
        self.id = particle_id
        self.neighbors = neighbors
        self.pos_x = None
        self.pos_y = None
        self.radius = None
        self.prop = None

    def get_id(self):
        return self.id

    def get_neighbors(self):
        return self.neighbors

    def get_pos_x(self):
        return self.pos_x

    def get_pos_y(self):
        return self.pos_y

    def get_radius(self):
        return self.radius

    def get_prop(self):
        return self.prop

    def set_pos(self, x, y):
        self.pos_x = x
        self.pos_y = y

    def set_radius(self, radius):
        self.radius = radius

    def set_prop(self, prop):
        self.prop = prop
