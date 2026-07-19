CREATE TABLE usuario (
    id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR (100) NOT NULL,
    email VARCHAR (100) UNIQUE NOT NULL,
    telefono VARCHAR (20),
    password_hash VARCHAR (255) NOT NULL,
    rol VARCHAR (30) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE empresa (
    id_empresa BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    direccion VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

CREATE TABLE categoria (
    id_categoria BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE ubicacion (
    id_ubicacion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL,
    descripcion VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE producto (
    id_producto BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    stock INTEGER NOT NULL,
    fecha_produccion DATE NOT NULL,
    fecha_caducidad DATE NOT NULL,
    codigo_barras VARCHAR(50) UNIQUE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    id_categoria BIGINT NOT NULL,
    id_empresa BIGINT NOT NULL,
    id_ubicacion BIGINT NOT NULL,

    
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria),

    CONSTRAINT fk_producto_empresa
        FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa),

    CONSTRAINT fk_producto_ubicacion
        FOREIGN KEY (id_ubicacion) REFERENCES ubicacion(id_ubicacion),

    CONSTRAINT chk_fechas_producto
        CHECK ( fecha_caducidad >= fecha_produccion)
);

CREATE TABLE pedido (
    id_pedido BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_pedido DATE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    observaciones TEXT,
    
    id_empresa BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,

    CONSTRAINT fk_pedido_empresa
        FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa),
    
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_pedido (
    id_detalle_pedido BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cantidad_solicitada INTEGER NOT NULL,
    cantidad_preparada INTEGER NOT NULL DEFAULT 0,

    id_pedido BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,

    CONSTRAINT fk_detalle_pedido_pedido
        FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
    
    CONSTRAINT fk_producto_detalle_pedido
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto),

    CONSTRAINT chk_cantidad_solicitada
        CHECK (cantidad_solicitada > 0),
    
    CONSTRAINT chk_cantidad_preparada
        CHECK (cantidad_preparada >= 0),

    CONSTRAINT chk_cantidad_preparada_maxima
        CHECK (cantidad_preparada <= cantidad_solicitada)
);

CREATE TABLE movimiento_inventario (
    id_movimiento_inventario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_movimiento VARCHAR(50) NOT NULL,
    cantidad INTEGER NOT NULL,
    fecha_movimiento DATE NOT NULL,
    descripcion TEXT,

    id_producto BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    id_recepcion BIGINT,
    id_pedido BIGINT,

    CONSTRAINT fk_producto_movimiento_inventario
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto),

    CONSTRAINT fk_usuario_movimiento_inventario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    
    CONSTRAINT fk_recepcion_movimiento_inventario
        FOREIGN KEY (id_recepcion) REFERENCES recepcion(id_recepcion),

    CONSTRAINT fk_pedido_movimiento_inventario
        FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido)

    CONSTRAINT chk_cantidad_movimiento
        CHECK (cantidad > 0)
);

CREATE TABLE incidencia (
    id_incidencia BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_incidencia VARCHAR(20) NOT NULL,
    descripcion TEXT,
    fecha_incidencia DATE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',

    id_usuario BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    id_pedido BIGINT,
    id_recepcion BIGINT,

    CONSTRAINT fk_usuario_incidencia
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    
    CONSTRAINT fk_producto_incidencia
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto),

    CONSTRAINT fk_pedido_incidencia
        FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),

    CONSTRAINT fk_recepcion_incidencia
        FOREIGN KEY (id_recepcion) REFERENCES recepcion(id_recepcion)
    
    CONSTRAINT chk_tipo_incidencia
        CHECK (tipo_incidencia IN ('Producto', 'Pedido', 'Recepcion', 'Inventario')),

    CONSTRAINT chk_estado_incidencia
        CHECK (estado IN ('pendiente', 'en_proceso', 'resuelta', 'cerrada'))
);

CREATE TABLE recepcion (
    id_recepcion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fecha_recepcion DATE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
    observaciones TEXT,

    id_empresa BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,

    CONSTRAINT fk_empresa_recepcion
        FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa),
    
    CONSTRAINT fk_usuario_recepcion
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),

    CONSTRAINT chk_estado_recepcion
        CHECK (estado IN ('pendiente', 'en_proceso', 'recibida', 'parcial' ,'cancelada'))
);

CREATE TABLE detalle_recepcion (
    id_detalle_recepcion BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cantidad_esperada INTEGER NOT NULL,
    cantidad_recibida INTEGER NOT NULL DEFAULT 0,

    id_recepcion BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,

    CONSTRAINT fk_recepcion_detalle_recepcion
        FOREIGN KEY (id_recepcion) REFERENCES recepcion(id_recepcion),
    
    CONSTRAINT fk_producto_detalle_recepcion
        FOREIGN KEY (id_producto) REFERENCES producto(id_producto),

    CONSTRAINT chk_cantidad_recibida
        CHECK (cantidad_recibida >= 0),

    CONSTRAINT chk_cantidad_esperada
        CHECK (cantidad_esperada > 0)

    CONSTRAINT chk_cantidad_recibida_maxima
        CHECK (cantidad_recibida <= cantidad_esperada)
);

CREATE TABLE tarea (
    id_tarea BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_tarea VARCHAR(20) NOT NULL,
    descripcion TEXT,
    fecha_tarea DATE NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',

    id_usuario BIGINT NOT NULL,
    id_pedido BIGINT,
    id_recepcion BIGINT,

    CONSTRAINT fk_usuario_tarea
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),

    CONSTRAINT fk_pedido_tarea
        FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),

    CONSTRAINT fk_recepcion_tarea
        FOREIGN KEY (id_recepcion) REFERENCES recepcion(id_recepcion),

    CONSTRAINT chk_estado_tarea
        CHECK (estado IN ('pendiente', 'en_proceso', 'resuelta', 'cerrada'))
);


CREATE TABLE auditoria (
    id_auditoria BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_accion VARCHAR(100) NOT NULL,
    fecha_accion DATE NOT NULL,

    id_usuario BIGINT NOT NULL,

    CONSTRAINT fk_usuario_auditoria
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);