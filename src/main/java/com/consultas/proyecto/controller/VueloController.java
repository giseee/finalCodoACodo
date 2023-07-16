package com.consultas.proyecto.controller;


import com.consultas.proyecto.dto.VueloDTO;
import com.consultas.proyecto.service.VueloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(VueloController.VUELOS_RESOURCE)
public class VueloController {


    public static final String VUELOS_RESOURCE = "/api/vuelos";

    private final VueloService vueloService;

    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @GetMapping
    public ResponseEntity<List<VueloDTO>> mostrarVuelos() {
        return new ResponseEntity<>(vueloService.mostrarVuelos(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VueloDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(vueloService.mostrarVuelo(id), HttpStatus.OK);
    }

}
