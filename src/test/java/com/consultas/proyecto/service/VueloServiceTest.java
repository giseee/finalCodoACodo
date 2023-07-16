package com.consultas.proyecto.service;

import com.consultas.proyecto.dto.*;
import com.consultas.proyecto.exception.NotFoundException;
import com.consultas.proyecto.model.*;
import com.consultas.proyecto.repository.VueloRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class VueloServiceTest {

    @Mock
    VueloRepository vueloRepository;

    @InjectMocks
    VueloService vueloService;



    @Test
    @DisplayName("US0001-Camino feliz - Muestra un vuelo.")
    void mostrarVueloOKTest(){

        //ARRANGE
        Long id= 1L;
        List<AsientoVueloDTO> asientoVueloDTO = new ArrayList<>();


        Aerolinea aerolinea= new Aerolinea( "Aerolinea");
        Avion avion= new Avion("AA", aerolinea);
        List<Asiento> asientos= new ArrayList<>();
        asientos.add(new Asiento("A", "B", true, avion));
        Vuelo vuelo=new Vuelo(1L, new Date(),new Date(),
               "Origen A","Destino A",false);

        Asiento asiento= new Asiento("A", "A", true, avion);
        AsientoVuelo asientoVuelo = new AsientoVuelo(asiento, vuelo, true, 2000.00, 1500.00 );
        List<AsientoVuelo> asientosVuelo = new ArrayList<>();
        asientosVuelo.add(asientoVuelo);
        vuelo.setAsientosVuelo(asientosVuelo);
        Optional<Vuelo> vueloObject = Optional.of(vuelo);

        asientoVueloDTO.add(new AsientoVueloDTO( new AsientoDTO ("A","A"),true, 1500.00));

        VueloDTO expected = new VueloDTO( 1L,new Date(),new Date(),
                          "Origen A","Destino A",false,1L,
                            asientoVueloDTO,
                           new AvionDTO("AA", new AerolineaDTO("Aerolinea")));
        when(vueloRepository.findById(1L)).thenReturn(vueloObject);

        //ACT
        VueloDTO result = vueloService.mostrarVuelo(1L);

        //ASSERT
        System.out.println(expected);
        System.out.println(result);
        assertAll(
                ()->assertEquals(expected.getAsientosVuelo(), result.getAsientosVuelo()),
                ()->assertEquals(expected.getDestino(), result.getDestino())
        );
    }


    @Test
    @DisplayName("US0002-Recibe Optional nulo o vacio")
    void mostrarUnVueloPorIdOptNuloTest(){
        Long id = 5L;

        Optional<Vuelo> emptyObject = Optional.empty();

        when(vueloRepository.findById(id)).thenReturn(emptyObject);

        assertThrows(NotFoundException.class,()->vueloService.mostrarVuelo(id));
    }

    @Test
    @DisplayName("US0001-Recibe Lista Vacia")
    void mostrarVuelosListaNulaTest() {
        //arrange
        List<Vuelo> listaMock = new ArrayList<>();

        when(vueloRepository.findAll()).thenReturn(listaMock);
        //act

        List<VueloDTO> result = vueloService.mostrarVuelos();

        //assert
        assertTrue(result.isEmpty());
    }
}
