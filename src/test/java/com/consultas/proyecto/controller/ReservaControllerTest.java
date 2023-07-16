package com.consultas.proyecto.controller;

import com.consultas.proyecto.dto.*;
import com.consultas.proyecto.enums.EMetodosDePago;
import com.consultas.proyecto.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservaControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    /*
     * URL request: http://localhost:8080/api/reservas
     * Metodo POST
     *
     * Guardar una reserva en el sistema, verificar que la respuesta sea el esperado
     *
     *
     */
    @Test
    public void testGuardarReservaPostOk() throws Exception {

        List<AsientoDTO> asientosDTO = new ArrayList<>();
        AsientoDTO asientoDTO1 = new AsientoDTO("2","1");
        asientosDTO.add(asientoDTO1);

        ReservaDTO reservaDTO = new ReservaDTO(1L,asientosDTO, EMetodosDePago.TARJETA_DE_CREDITO);

        String mensaje = String.format("El asiento con columna: %s, fila: %s y vuelo: %d fue reservado por usted", asientoDTO1.getColumna(), asientoDTO1.getFila(), 1L);

        List<DetalleAsientosPedidosDTO> detalleAsientosPedidosDTOS = new ArrayList<>();
        detalleAsientosPedidosDTOS.add(new DetalleAsientosPedidosDTO(asientoDTO1, 1L, true, mensaje));

        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        String jsonPayload = writer.writeValueAsString(reservaDTO);
        String jsonResponse = writer.writeValueAsString(detalleAsientosPedidosDTOS);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = User.withUsername("matias@gmail.com")
                .password("12d21")
                .authorities(authorities)
                .build();
        String jwt = this.jwtTokenUtil.generateToken(userDetails);

        MvcResult mvcResult = mockMvc.perform(post(ReservaController.RESERVA_RESOURCE)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        assertEquals(jsonResponse, mvcResult.getResponse().getContentAsString());
    }




    /*
     * URL request: http://localhost:8080/api/reservas/usuarios/1
     * Metodo POST
     *
     * Testear las reservas de un usuario, solo un agente de ventas puede hacerlo
     *
     *
     */
    @Test
    public void testVerReservaDeUnUsuarioPostOk() throws Exception {
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .writer();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = User.withUsername("matias@gmail.com")
                .password("12d21")
                .authorities(authorities)
                .build();
        String jwt = this.jwtTokenUtil.generateToken(userDetails);

        List<AsientoDTO> asientosDTO = new ArrayList<>();
        AsientoDTO asientoDTO1 = new AsientoDTO("1","1");
        asientosDTO.add(asientoDTO1);

        ReservaDTO reservaDTO = new ReservaDTO(1L,asientosDTO, EMetodosDePago.TARJETA_DE_CREDITO);

        String jsonPayload = writer.writeValueAsString(reservaDTO);
        mockMvc.perform(post(ReservaController.RESERVA_RESOURCE)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andDo(print());

        AerolineaDTO aerolineaDTO = new AerolineaDTO("Aerolineas Imaginarias");
        AvionDTO avionDTO = new AvionDTO("avion 1", aerolineaDTO);
        VueloReservaDTO vueloReservaDTO = new VueloReservaDTO(1L, new Date(), new Date(), "Origen A",
                "Destino A", false, avionDTO, "TARJETA_DE_CREDITO");

        List<VueloReservaDTO> vuelosReservaDTO = new ArrayList<>(List.of(vueloReservaDTO));

        HistorialReservasDTO historialReservasDTO = new HistorialReservasDTO("Matias", "Mati", "matias@gmail.com", "33-4444-5555-6666",vuelosReservaDTO);

        String responseJson = writer.writeValueAsString(historialReservasDTO);

        MvcResult mvcResult = mockMvc.perform(get(ReservaController.RESERVA_RESOURCE + "/usuarios/{idUsuario}", 3L)
                        .header("Authorization", "Bearer " + jwt))
                .andDo(print())
                .andExpect(jsonPath("$.nombre").value(historialReservasDTO.getNombre()))
                .andExpect(jsonPath("$.apellido").value(historialReservasDTO.getApellido()))
                .andExpect(jsonPath("$.vuelos[0].idVuelo").value(historialReservasDTO.getVuelos().get(0).getIdVuelo()))
                .andReturn();

        System.out.println(responseJson);
        System.out.println(mvcResult.getResponse().getContentAsString());

    }


}
