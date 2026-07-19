INSERT INTO usuario (nombre, email, telefono, password_hash, rol)
VALUES
('Juan Camilo', 'montero@gmail.com', '604141943', 'camilo123', 'operario'),
('Juan Carlos', 'carlos@gmail.com', '666554433', 'carlos123', 'coordinador'),
('Sofia martin', 'sofia@gmail.com', '3334445566', 'sofia123', 'encargado'),
('Josep lus', 'josep@gmail.com', '777654433', 'josep123', 'operario');

INSERT INTO empresa (nombre, email, telefono, direccion, tipo)
VALUES
('Distribuciones Levante S.L.', 'contacto@dislevante.com', '961234567', 'C/ Comercio 15, Valencia', 'Proveedor'),
('Higiene Plus S.L.', 'info@higieneplus.com', '962345678', 'Av. de la Industria 42, Valencia', 'Proveedor'),
('Congelados Mediterráneo S.A.', 'ventas@congeladosmediterraneo.com', '963456789', 'Polígono Norte 8, Paterna', 'Proveedor'),
('Oficina Express S.L.', 'pedidos@oficinaexpress.com', '964567890', 'C/ Papelería 12, Valencia', 'Proveedor'),
('Logística Valencia S.L.', 'logistica@logvalencia.com', '965678901', 'Polígono Fuente del Jarro, Paterna', 'Transporte');

INSERT INTO categoria (nombre)
VALUES
('Alimentación'),
('Bebidas'),
('Limpieza'),
('Higiene'),
('Congelados'),
('Material de oficina'),
('Conservas'),
('Lácteos');

INSERT INTO ubicacion (codigo, descripcion)
VALUES
('A-01', 'Pasillo A - Estantería 1'),
('A-02', 'Pasillo A - Estantería 2'),
('A-03', 'Pasillo A - Estantería 3'),
('B-01', 'Pasillo B - Estantería 1'),
('B-02', 'Pasillo B - Estantería 2'),
('C-01', 'Cámara frigorífica'),
('D-01', 'Zona de recepción'),
('E-01', 'Zona de expedición');

INSERT INTO producto (
    nombre,
    descripcion,
    stock,
    fecha_produccion,
    fecha_caducidad,
    codigo_barras,
    id_categoria,
    id_empresa,
    id_ubicacion
)
VALUES
(
    'Arroz redondo 1 kg',
    'Paquete de arroz redondo de un kilogramo',
    120,
    '2026-05-10',
    '2028-05-10',
    '8410000000011',
    1,
    1,
    1
),
(
    'Galletas María 800 g',
    'Paquete familiar de galletas tipo María',
    80,
    '2026-06-01',
    '2027-06-01',
    '8410000000028',
    1,
    1,
    1
),
(
    'Coca-Cola 330 ml',
    'Lata de refresco de cola de 330 mililitros',
    200,
    '2026-06-15',
    '2027-06-15',
    '8410000000035',
    2,
    1,
    2
),
(
    'Agua mineral 1,5 L',
    'Botella de agua mineral de un litro y medio',
    160,
    '2026-06-20',
    '2028-06-20',
    '8410000000042',
    2,
    1,
    2
),
(
    'Atún en aceite de oliva',
    'Lata de atún en aceite de oliva de 80 gramos',
    90,
    '2026-04-05',
    '2030-04-05',
    '8410000000059',
    7,
    1,
    3
),
(
    'Leche entera 1 L',
    'Brik de leche entera de un litro',
    75,
    '2026-07-01',
    '2026-10-01',
    '8410000000066',
    8,
    1,
    6
),
(
    'Detergente líquido 3 L',
    'Detergente líquido para lavadora',
    45,
    '2026-03-12',
    '2029-03-12',
    '8410000000073',
    3,
    2,
    4
),
(
    'Lejía 2 L',
    'Botella de lejía apta para limpieza doméstica',
    60,
    '2026-05-18',
    '2028-05-18',
    '8410000000080',
    3,
    2,
    4
),
(
    'Papel higiénico 12 rollos',
    'Paquete de papel higiénico de doce rollos',
    50,
    '2026-06-02',
    '2031-06-02',
    '8410000000097',
    4,
    2,
    5
),
(
    'Gel de ducha 750 ml',
    'Gel de ducha familiar de 750 mililitros',
    55,
    '2026-05-22',
    '2029-05-22',
    '8410000000103',
    4,
    2,
    5
),
(
    'Pizza margarita congelada',
    'Pizza margarita ultracongelada de 400 gramos',
    40,
    '2026-06-25',
    '2027-06-25',
    '8410000000110',
    5,
    3,
    6
),
(
    'Verduras congeladas 1 kg',
    'Bolsa de mezcla de verduras ultracongeladas',
    35,
    '2026-06-10',
    '2027-12-10',
    '8410000000127',
    5,
    3,
    6
),
(
    'Papel A4 500 hojas',
    'Paquete de quinientas hojas de papel blanco A4',
    70,
    '2026-01-15',
    '2031-01-15',
    '8410000000134',
    6,
    4,
    3
),
(
    'Bolígrafos azules 10 unidades',
    'Paquete de diez bolígrafos de tinta azul',
    65,
    '2026-02-10',
    '2031-02-10',
    '8410000000141',
    6,
    4,
    3
);

INSERT INTO pedido (
    fecha_pedido,
    estado,
    observaciones,
    id_empresa,
    id_usuario
)
VALUES
(
    '2026-07-01',
    'Pendiente',
    'Reposición de productos de alimentación y bebidas',
    1,
    2
),
(
    '2026-07-03',
    'Recibido',
    'Pedido de productos de limpieza',
    2,
    2
),
(
    '2026-07-05',
    'En tránsito',
    'Reposición de productos congelados',
    3,
    2
),
(
    '2026-07-08',
    'Pendiente',
    'Compra de material de oficina',
    4,
    2
);

INSERT INTO detalle_pedido (
    cantidad_solicitada,
    cantidad_preparada,
    id_pedido,
    id_producto
)
VALUES
-- Pedido 1: Distribuciones Levante
(50, 0, 1, 1),
(40, 0, 1, 2),
(100, 0, 1, 3),
(80, 0, 1, 4),
(30, 0, 1, 5),
(60, 0, 1, 6),

-- Pedido 2: Higiene Plus
(25, 25, 2, 7),
(30, 30, 2, 8),
(20, 20, 2, 9),
(25, 25, 2, 10),

-- Pedido 3: Congelados Mediterráneo
(35, 20, 3, 11),
(40, 25, 3, 12),

-- Pedido 4: Oficina Express
(30, 0, 4, 13),
(40, 0, 4, 14);

INSERT INTO recepcion (
    fecha_recepcion,
    estado,
    observaciones,
    id_empresa,
    id_usuario
)
VALUES
(
    '2026-07-03',
    'recibida',
    'Recepción completa del pedido de productos de limpieza.',
    2,
    2
),
(
    '2026-07-06',
    'parcial',
    'Solo se recibió parte del pedido de congelados.',
    3,
    2
);

INSERT INTO detalle_recepcion (
    cantidad_esperada,
    cantidad_recibida,
    id_recepcion,
    id_producto
)
VALUES
-- Recepción 1: Higiene Plus, completa
(25, 25, 1, 7),
(30, 30, 1, 8),
(20, 20, 1, 9),
(25, 25, 1, 10),

-- Recepción 2: Congelados Mediterráneo, parcial
(35, 20, 2, 11),
(40, 25, 2, 12);

INSERT INTO movimiento_inventario (
    tipo_movimiento,
    cantidad,
    fecha_movimiento,
    descripcion,
    id_producto,
    id_usuario,
    id_recepcion,
    id_pedido
)
VALUES
-- Recepción 1: productos de Higiene Plus
(
    'ENTRADA',
    25,
    '2026-07-03',
    'Entrada por recepción completa de detergente líquido.',
    7,
    2,
    1,
    2
),
(
    'ENTRADA',
    30,
    '2026-07-03',
    'Entrada por recepción completa de lejía.',
    8,
    2,
    1,
    2
),
(
    'ENTRADA',
    20,
    '2026-07-03',
    'Entrada por recepción completa de papel higiénico.',
    9,
    2,
    1,
    2
),
(
    'ENTRADA',
    25,
    '2026-07-03',
    'Entrada por recepción completa de gel de ducha.',
    10,
    2,
    1,
    2
),

-- Recepción 2: productos congelados recibidos parcialmente
(
    'ENTRADA',
    20,
    '2026-07-06',
    'Entrada parcial de pizzas margarita congeladas.',
    11,
    2,
    2,
    3
),
(
    'ENTRADA',
    25,
    '2026-07-06',
    'Entrada parcial de bolsas de verduras congeladas.',
    12,
    2,
    2,
    3
),-- Salida de material para uso interno
(
    'SALIDA',
    5,
    '2026-07-07',
    'Salida de paquetes de papel A4 para uso administrativo.',
    13,
    1,
    NULL,
    NULL
),
-- Ajuste por unidades dañadas
(
    'AJUSTE_NEGATIVO',
    2,
    '2026-07-08',
    'Ajuste de stock por dos paquetes de galletas dañados.',
    2,
    3,
    NULL,
    NULL
);

INSERT INTO incidencia (
    tipo_incidencia,
    descripcion,
    fecha_incidencia,
    estado,
    id_usuario,
    id_producto,
    id_pedido,
    id_recepcion
)
VALUES
(
    'Recepcion',
    'La recepción de pizzas margarita fue parcial: se esperaban 35 unidades y solo se recibieron 20.',
    '2026-07-06',
    'en_proceso',
    2,
    11,
    3,
    2
),
(
    'Recepcion',
    'La recepción de verduras congeladas fue parcial: se esperaban 40 unidades y solo se recibieron 25.',
    '2026-07-06',
    'en_proceso',
    2,
    12,
    3,
    2
),
(
    'Producto',
    'Se detectaron dos paquetes de galletas dañados durante una revisión del almacén.',
    '2026-07-08',
    'resuelta',
    3,
    2,
    NULL,
    NULL
),
(
    'Inventario',
    'El stock físico de papel A4 no coincide con el stock registrado en el sistema.',
    '2026-07-09',
    'pendiente',
    1,
    13,
    NULL,
    NULL
),
(
    'Pedido',
    'El proveedor no ha confirmado todavía la fecha de entrega del pedido de alimentación y bebidas.',
    '2026-07-04',
    'pendiente',
    2,
    1,
    1,
    NULL
);

INSERT INTO tarea (
    tipo_tarea,
    descripcion,
    fecha_tarea,
    estado,
    id_usuario,
    id_pedido,
    id_recepcion
)
VALUES
(
    'Recepcion',
    'Recepcionar el pedido de productos de alimentación y bebidas.',
    '2026-07-02',
    'pendiente',
    1,
    1,
    NULL
),
(
    'Revision',
    'Revisar la mercancía recibida de Higiene Plus.',
    '2026-07-03',
    'cerrada',
    3,
    2,
    1
),
(
    'Inventario',
    'Realizar recuento del stock de productos congelados.',
    '2026-07-06',
    'en_proceso',
    1,
    3,
    2
),
(
    'Reposicion',
    'Reubicar los productos congelados en la cámara frigorífica.',
    '2026-07-06',
    'resuelta',
    1,
    NULL,
    2
),
(
    'Pedido',
    'Contactar con el proveedor para conocer el estado del pedido pendiente.',
    '2026-07-04',
    'pendiente',
    2,
    1,
    NULL
),
(
    'Inventario',
    'Comprobar la diferencia de stock detectada en el papel A4.',
    '2026-07-09',
    'pendiente',
    3,
    NULL,
    NULL
);

INSERT INTO auditoria (
    tipo_accion,
    fecha_accion,
    id_usuario
)
VALUES
(
    'Creación del pedido 1 de alimentación y bebidas',
    '2026-07-01',
    2
),
(
    'Creación del pedido 2 de productos de limpieza',
    '2026-07-03',
    2
),
(
    'Registro de la recepción 1 de Higiene Plus',
    '2026-07-03',
    2
),
(
    'Revisión de la mercancía recibida en la recepción 1',
    '2026-07-03',
    3
),
(
    'Registro de recepción parcial de productos congelados',
    '2026-07-06',
    2
),
(
    'Registro de movimientos de entrada de inventario',
    '2026-07-06',
    2
),
(
    'Registro de incidencia por productos congelados pendientes',
    '2026-07-06',
    2
),
(
    'Registro de ajuste negativo por galletas dañadas',
    '2026-07-08',
    3
),
(
    'Creación de tarea de comprobación de stock de papel A4',
    '2026-07-09',
    3
),
(
    'Consulta del inventario general del almacén',
    '2026-07-09',
    1
);