package com.consultas.proyecto.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class VueloControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/vuelos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        //.andExpect(MockMvcResultMatchers.jsonPath(""))
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
        @Test
        void mostrarVuelos() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/api/vuelos"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].tieneConexion").value("true"))
                    .andReturn();
            System.out.println(mvcResult.getResponse().getContentAsString());
        }
    }