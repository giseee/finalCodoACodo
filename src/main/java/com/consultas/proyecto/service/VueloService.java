package com.consultas.proyecto.service;

import com.consultas.proyecto.dto.AvionDTO;
import com.consultas.proyecto.dto.VueloDTO;
import com.consultas.proyecto.exception.NotFoundException;
import com.consultas.proyecto.model.Vuelo;
import com.consultas.proyecto.repository.VueloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VueloService {

    private VueloRepository vueloRepository;

    public VueloService(VueloRepository vueloRepository) {
        this.vueloRepository = vueloRepository;
    }

    public List<VueloDTO> mostrarVuelos() {
        ModelMapper mapper = new ModelMapper();
        List<Vuelo> vuelos = vueloRepository.findAll();

        return vuelos.stream().map(vuelo -> {
            Long cantidadDisponible = vuelo.getAsientosVuelo().stream().filter(asiento -> asiento.getEstaLibre()).count();
            VueloDTO vueloDTO = mapper.map(vuelo, VueloDTO.class);
            vueloDTO.setCantidadDeAsientosDisponibles(cantidadDisponible);
            vuelo.getAsientosVuelo().stream().findFirst().ifPresent(asientoVuelo -> {
                vueloDTO.setAvion(mapper.map(asientoVuelo.getAsiento().getAvion(), AvionDTO.class));
            });
            return vueloDTO;
        }).toList();
    }
    public VueloDTO mostrarVuelo(Long id) {
        ModelMapper mapper = new ModelMapper();
        Optional<Vuelo> vuelo = vueloRepository.findById(id);

        if (vuelo.isEmpty()) {
            throw new NotFoundException("No existe este vuelo con id: " + id);
        }

        Vuelo vueloBuscado = vuelo.get();
        Long cantidadDisponible = vueloBuscado.getAsientosVuelo().stream().filter(asiento -> asiento.getEstaLibre()).count();
        VueloDTO vueloDTO = mapper.map(vueloBuscado, VueloDTO.class);
        vueloDTO.setCantidadDeAsientosDisponibles(cantidadDisponible);
        vuelo.get().getAsientosVuelo().stream().findFirst().ifPresent(asientoVuelo -> {
            vueloDTO.setAvion(mapper.map(asientoVuelo.getAsiento().getAvion(), AvionDTO.class));
        });

        return vueloDTO;
    }


}
