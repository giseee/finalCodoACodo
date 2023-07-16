package com.consultas.proyecto.service;

import com.consultas.proyecto.dto.*;
import com.consultas.proyecto.enums.EMetodosDePago;
import com.consultas.proyecto.exception.NotFoundException;
import com.consultas.proyecto.model.*;
import com.consultas.proyecto.repository.MetodoDePagoRepository;
import com.consultas.proyecto.repository.ReservaRepository;
import com.consultas.proyecto.repository.UsuarioRepository;
import com.consultas.proyecto.repository.VueloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReservaServiceTest {

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    MetodoDePagoRepository metodoDePagoRepository;

    @Mock
    VueloRepository vueloRepository;

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    ReservaService reservaService;




    private List<Reserva> reservasMock;
    private MetodoDePago metodoDePagoMock;
    private Usuario usuarioMock;
    private Aerolinea aerolineaMock;
    private Avion avionMock;
    private Vuelo vueloMock;
    private List<Asiento> asientosAvionMock;
    private List<AsientoVuelo> asientosVueloMock;
    private List<AsientoVuelo> asientosReservadosMock;


    @BeforeEach
    void init() {
        this.initVariable();
    }

    private void initVariable() {

        Aerolinea aerolinea = new Aerolinea();
        aerolinea.setNombre("Aerolineas Imaginarias");

        Avion avion = new Avion("avion 1", aerolinea);

        List<Asiento> asientosAvion = new ArrayList<>();
        asientosAvion.add(new Asiento("1", "1", true, avion));
        asientosAvion.add(new Asiento("2", "1", true, avion));
        asientosAvion.add(new Asiento("3", "1", true, avion));
        asientosAvion.add(new Asiento("4", "1", true, avion));
        asientosAvion.add(new Asiento("5", "1", true, avion));
        asientosAvion.add(new Asiento("1", "2", false, avion));
        asientosAvion.add(new Asiento("2", "2", false, avion));
        asientosAvion.add(new Asiento("3", "2", true, avion));

        avion.setAsientos(asientosAvion);

        Vuelo vuelo = new Vuelo(new Date(), new Date(), "Origen A", "Destino A", false);

        List<AsientoVuelo> asientosVuelo = new ArrayList<>();
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(0), vuelo, asientosAvion.get(0).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(1), vuelo, asientosAvion.get(1).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(2), vuelo, asientosAvion.get(2).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(3), vuelo, asientosAvion.get(3).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(4), vuelo, asientosAvion.get(4).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(5), vuelo, asientosAvion.get(5).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(6), vuelo, asientosAvion.get(6).getEstaApto(), null, 9000D));
        asientosVuelo.add(new AsientoVuelo(asientosAvion.get(7), vuelo, asientosAvion.get(7).getEstaApto(), null, 9000D));

        vuelo.setAsientosVuelo(asientosVuelo);
        vuelo.setIdVuelo(1L);

        MetodoDePago metodoDePago = new MetodoDePago();
        metodoDePago.setIdMetodoDePago(1L);
        metodoDePago.setDescuento(20D);
        metodoDePago.setNombreMetodoDePago(EMetodosDePago.TARJETA_DE_CREDITO);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("Matias");
        usuario.setApellido("Torrez");
        usuario.setTelefono("11-2222-3333");
        usuario.setMail("matias@gmail.com");

        Reserva reserva = new Reserva();
        reserva.setIdReserva(1L);
        reserva.setPrecioTotal(200D);
        reserva.setMetodoDePago(metodoDePago);
        reserva.setUsuario(usuario);

        List<AsientoVuelo> asientosReservados = new ArrayList<>();
        AsientoVuelo asientoReservado1 = asientosVuelo.get(0);
        asientoReservado1.setReserva(reserva); //quitamos y no encuentra el asientoVuelo, la reserva vinculada
        asientosReservados.add(asientoReservado1);
        AsientoVuelo asientoReservado2 = asientosVuelo.get(1);
        asientoReservado2.setReserva(reserva);
        asientosReservados.add(asientoReservado2);

        reserva.setAsientosVuelos(asientosReservados); // quitamos y no encuentra el asientos vuelo

        List<Reserva> reservas = new ArrayList<>();
        reservas.add(reserva);

        usuario.setReservas(reservas);
        metodoDePago.setReservas(reservas);


        this.reservasMock = reservas;
        this.usuarioMock = usuario;
        this.aerolineaMock = aerolinea;
        this.avionMock = avion;
        this.vueloMock = vuelo;
        this.asientosAvionMock = asientosAvion;
        this.asientosVueloMock = asientosVuelo;
        this.metodoDePagoMock = metodoDePago;
        this.asientosReservadosMock = asientosReservados;
    }


    @Test
    @DisplayName("US0002-Camino Feliz :D")
    void crearReservaTodoOk() {
        //arrange
        UserDetails userDetailsExpected = User.withUsername("matias@gmail.com")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsExpected, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long idVuelo = 1L;
        AsientoDTO primerAsientoQueSeQuiereReservarMock = new AsientoDTO("1", "1");
        AsientoDTO segundoAsientoQueSeQuiereReservarMock = new AsientoDTO("1", "2");
        List<AsientoDTO> asientosQueSeQuierenReservarMock = new ArrayList<>();
        asientosQueSeQuierenReservarMock.add(primerAsientoQueSeQuiereReservarMock);
        asientosQueSeQuierenReservarMock.add(segundoAsientoQueSeQuiereReservarMock);


        //expected

        String mensajeExitoso = String.format("El asiento con columna: %s, fila: %s y vuelo: %d fue reservado por usted",
                primerAsientoQueSeQuiereReservarMock.getColumna(), primerAsientoQueSeQuiereReservarMock.getFila(), idVuelo);
        String mensajeFallido = String.format("El asiento con columna: %s, fila: %s y vuelo: %d ya fue reservado por alguien más, lo lamento",
                segundoAsientoQueSeQuiereReservarMock.getColumna(), segundoAsientoQueSeQuiereReservarMock.getFila(), idVuelo);

        List<DetalleAsientosPedidosDTO> expected = new ArrayList<>();
        expected.add(new DetalleAsientosPedidosDTO(new AsientoDTO(primerAsientoQueSeQuiereReservarMock.getFila(), primerAsientoQueSeQuiereReservarMock.getColumna()), idVuelo, true, mensajeExitoso));
        expected.add(new DetalleAsientosPedidosDTO(new AsientoDTO(segundoAsientoQueSeQuiereReservarMock.getFila(), segundoAsientoQueSeQuiereReservarMock.getColumna()), idVuelo, false, mensajeFallido));

        ReservaDTO reservaDTO = new ReservaDTO(idVuelo, asientosQueSeQuierenReservarMock, EMetodosDePago.TARJETA_DE_CREDITO);

        when(usuarioRepository.findByMail(anyString())).thenReturn(Optional.of(usuarioMock));
        when(vueloRepository.findById(anyLong())).thenReturn(Optional.of(vueloMock));
        when(metodoDePagoRepository.findByNombreMetodoDePago(any(EMetodosDePago.class))).thenReturn(Optional.of(metodoDePagoMock));

        //act
        List<DetalleAsientosPedidosDTO> result = reservaService.crearReserva(reservaDTO);

        //assert
        assertEquals(expected, result);

    }

    @Test
    @DisplayName("US0002-Asientos que se quiere estan ocupados")
    void crearReservaConNingunAsientoDisponibleTest() {
        //arrange

        UserDetails userDetailsExpected = User.withUsername("matias@gmail.com")
                .password("asdasd")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsExpected, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //expected
        ReservaDTO reservaDTO = new ReservaDTO(1L, new ArrayList<>(List.of(new AsientoDTO("1", "2"))), EMetodosDePago.TARJETA_DE_CREDITO);

        when(usuarioRepository.findByMail(anyString())).thenReturn(Optional.of(usuarioMock));
        when(vueloRepository.findById(anyLong())).thenReturn(Optional.of(vueloMock));
        when(metodoDePagoRepository.findByNombreMetodoDePago(any(EMetodosDePago.class))).thenReturn(Optional.of(metodoDePagoMock));

        //act
        //assert
        assertThrows(NotFoundException.class, () -> reservaService.crearReserva(reservaDTO));

    }

    @Test
    @DisplayName("US0002-Usuario inexistente")
    void crearReservaConUsuarioInexistenteTest() {
        //arrange

        //expected
        UserDetails userDetailsExpected = User.withUsername("matias@gmail.com")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsExpected, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        when(usuarioRepository.findByMail(anyString())).thenReturn(Optional.empty());

        //act
        //assert
        assertThrows(NotFoundException.class, () -> reservaService.crearReserva(any(ReservaDTO.class)));

    }

    @Test
    @DisplayName("US0002-Metodo de pago inexistente")
    void crearReservaConMetodoDePagoInexistenteTest() {
        //arrange

        //expected
        UserDetails userDetailsExpected = User.withUsername("matias@gmail.com")
                .password("matias")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsExpected, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdVuelo(1L);
        reservaDTO.setAsientos(new ArrayList<>(List.of(new AsientoDTO("1", "1"))));
        reservaDTO.setMetodoDePago(EMetodosDePago.TARJETA_DE_CREDITO);

        when(usuarioRepository.findByMail(anyString())).thenReturn(Optional.of(usuarioMock));
        when(metodoDePagoRepository.findByNombreMetodoDePago(any(EMetodosDePago.class))).thenReturn(Optional.empty());

        //act
        //assert
        assertThrows(NotFoundException.class, () -> reservaService.crearReserva(reservaDTO));
    }

    @Test
    @DisplayName("US0002-Vuelo inexistente")
    void crearReservaConVueloInexistenteTest() {
        //arrange

        //expected
        UserDetails userDetailsExpected = User.withUsername("matias@gmail.com")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsExpected, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ReservaDTO reservaDTO = new ReservaDTO(1L, new ArrayList<>(List.of(new AsientoDTO("1", "2"))), EMetodosDePago.TARJETA_DE_CREDITO);

        when(usuarioRepository.findByMail(anyString())).thenReturn(Optional.of(usuarioMock));
        when(vueloRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(metodoDePagoRepository.findByNombreMetodoDePago(any(EMetodosDePago.class))).thenReturn(Optional.of(metodoDePagoMock));

        //act
        //assert
        assertThrows(NotFoundException.class, () -> reservaService.crearReserva(reservaDTO));
    }


    @Test
    @DisplayName("US0004-Camino Feliz :D")
    public void verReservasDeUnUsuarioTodoOkTest() {
        //arrange
        //expected

        AerolineaDTO aerolineaDTO = new AerolineaDTO("Aerolineas Imaginarias");
        AvionDTO avionDTO = new AvionDTO("avion 1", aerolineaDTO);

        VueloReservaDTO vueloReservaDTO = new VueloReservaDTO(1L, new Date(), new Date(), "Origen A", "Destino A", false, avionDTO, EMetodosDePago.TARJETA_DE_CREDITO.toString());

        List<VueloReservaDTO> vueloReservaDTOS = List.of(vueloReservaDTO);


        HistorialReservasDTO expected = new HistorialReservasDTO("Matias", "Torrez", "matias@gmail.com", "11-2222-3333", vueloReservaDTOS);

        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(this.usuarioMock));

        //act
        HistorialReservasDTO result = reservaService.verReservasDeUnUsuario(anyLong());

        //assert
        assertAll(
                () -> assertEquals(result.getVuelos().get(0).getOrigen(), expected.getVuelos().get(0).getOrigen()),
                () -> assertEquals(result.getVuelos().get(0).getAvion(), expected.getVuelos().get(0).getAvion()),
                () -> assertEquals(result.getVuelos().get(0).getTieneConexion(), expected.getVuelos().get(0).getTieneConexion())
        );
    }

    @Test
    @DisplayName("US0004-Recibe un id de un usuario inexistente")
    public void verReservasDeUsuarioInexistenteTest() {
        //arrange
        //expected
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act
        //assert
        assertThrows(NotFoundException.class, () -> reservaService.verReservasDeUnUsuario(anyLong()));

    }
}