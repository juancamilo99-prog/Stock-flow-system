package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.Incidencia;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.service.IncidenciaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    public IncidenciaController(IncidenciaService incidenciaService) {
        this.incidenciaService = incidenciaService;
    }

    @GetMapping
    public List<Incidencia> getIncidencias() {
        return incidenciaService.obtenerIncidencias();
    }

    //incidencias por id de usuario
    @GetMapping(path = "/{idUsuario}/usuario")
    public List<Incidencia> obtenerIncidenciasByUsuarioId(@PathVariable Long idUsuario) {
        return incidenciaService.obtenerIncidenciasByUsuarioId(idUsuario);
    }

    //incidencia por id de producto
    @GetMapping(path = "/{idProducto}/producto")
    public List<Incidencia> obtenerIncidenciasByProductoId(@PathVariable Long idProducto) {
        return incidenciaService.obtenerIncidenciasByProductoId(idProducto);
    }

    @GetMapping(path = "/{idPedido}/pedido")
    public List<Incidencia> obtenerIncidenciasByPedidoId(@PathVariable Long idPedido) {
        return incidenciaService.obtenerIncidenciasByPedidoId(idPedido);
    }

    @GetMapping(path = "/{idRecepcion}/recepcion")
    public List<Incidencia> obtenerIncidenciasByRecepcion(@PathVariable Long idRecepcion) {
        return incidenciaService.obtenerIncidenciasByRecepcionId(idRecepcion);
    }


}
