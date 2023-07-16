package com.consultas.proyecto.controller;


import com.consultas.proyecto.dto.DetalleAsientosPedidosDTO;
import com.consultas.proyecto.dto.HistorialReservasDTO;
import com.consultas.proyecto.dto.ReservaDTO;
import com.consultas.proyecto.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(ReservaController.RESERVA_RESOURCE)
public class ReservaController {

    public static final String RESERVA_RESOURCE = "/api/reservas";
    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<List<DetalleAsientosPedidosDTO>> guardarReserva(@RequestBody ReservaDTO reservaDTO) throws Exception {
        return new ResponseEntity<>(reservaService.crearReserva(reservaDTO), HttpStatus.OK);
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<HistorialReservasDTO> getReservas(@PathVariable("idUsuario") Long idUsuario) throws Exception {
        return new ResponseEntity<>(this.reservaService.verReservasDeUnUsuario(idUsuario),HttpStatus.OK);
    }
}
