package com.consultas.proyecto.service;


import com.consultas.proyecto.dto.*;
import com.consultas.proyecto.exception.NotFoundException;
import com.consultas.proyecto.model.*;
import com.consultas.proyecto.repository.MetodoDePagoRepository;
import com.consultas.proyecto.repository.ReservaRepository;
import com.consultas.proyecto.repository.UsuarioRepository;
import com.consultas.proyecto.repository.VueloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private ReservaRepository reservaRepository;
    private MetodoDePagoRepository metodoDePagoRepository;
    private UsuarioRepository usuarioRepository;
    private VueloRepository vueloRepository;

    public ReservaService(ReservaRepository reservaRepository, MetodoDePagoRepository metodoDePagoRepository, UsuarioRepository usuarioRepository, VueloRepository vueloRepository) {
        this.reservaRepository = reservaRepository;
        this.metodoDePagoRepository = metodoDePagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.vueloRepository = vueloRepository;
    }

    @Transactional
    public List<DetalleAsientosPedidosDTO> crearReserva(ReservaDTO reservaDTO) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = this.usuarioRepository.findByMail(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("No se encontro usuario"));
        MetodoDePago metodoDePago = this.metodoDePagoRepository.findByNombreMetodoDePago(reservaDTO.getMetodoDePago()).orElseThrow(() -> new NotFoundException("no existe metodo de pago"));
        Vuelo vuelo = this.vueloRepository.findById(reservaDTO.getIdVuelo()).orElseThrow(() -> new NotFoundException("no se encontro vuelo"));
        List<DetalleAsientosPedidosDTO> detalleAsientosPedidos = new ArrayList<>();
        List<AsientoVuelo> asientosQueFueronReservados = new ArrayList<>();
        this.guardarAsientosCoincidentes(vuelo.getAsientosVuelo(), reservaDTO.getIdVuelo(),reservaDTO.getAsientos(), detalleAsientosPedidos, asientosQueFueronReservados);

        if (!asientosQueFueronReservados.isEmpty()) {
            Double precioTotal = precioTotalDeLosAsientosReservados(asientosQueFueronReservados, metodoDePago.getDescuento());
            //por alguna razon cuando guardo el AsientoVuelo la reserva no se guarda en cada AsientoVuelo
            Reserva reserva = new Reserva(precioTotal, metodoDePago, usuario, asientosQueFueronReservados);
            //por eso debo recorrer cada asientoVuelo que fue reservado y setearle la reserva
            asientosQueFueronReservados.forEach( asientoVuelo -> asientoVuelo.setReserva(reserva));

            reservaRepository.save(reserva);
            return detalleAsientosPedidos;
        } else {
            throw new NotFoundException("No se pudo reservar ningun asiento todos estaban ocupados");
        }

    }

    public HistorialReservasDTO verReservasDeUnUsuario(Long idUsuario){

        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new NotFoundException("No existe usuario con id: "+idUsuario));

        List<Vuelo> vuelos = usuario.getReservas().stream()  // Stream de reservas
                .flatMap(reserva -> reserva.getAsientosVuelos().stream())  // Stream de asientos de avión de las reservas
                .map(AsientoVuelo::getVuelo)  // Mapeo para obtener los vuelos de los asientos de avión
                .distinct()  // Eliminar duplicados de vuelos
                .collect(Collectors.toList());  // Recolectar los vuelos en una lista

        ModelMapper mapper = new ModelMapper();

        List<VueloReservaDTO> vuelosDTO = vuelos.stream().map(vuelo -> {
            VueloReservaDTO vueloReservaDTO = mapper.map(vuelo, VueloReservaDTO.class);
            vuelo.getAsientosVuelo().stream().findFirst().ifPresent(asientoVuelo -> {
                System.out.println(asientoVuelo.getReserva().getMetodoDePago());
                System.out.println(asientoVuelo.getReserva().getMetodoDePago());
                System.out.println(asientoVuelo.getReserva().getMetodoDePago());
                System.out.println(asientoVuelo.getReserva().getMetodoDePago());
                System.out.println(asientoVuelo.getReserva().getMetodoDePago());
                System.out.println(asientoVuelo.getReserva().getMetodoDePago());

                vueloReservaDTO.setAvion(mapper.map(asientoVuelo.getAsiento().getAvion(), AvionDTO.class));
                vueloReservaDTO.setMetodoDePago(asientoVuelo.getReserva().getMetodoDePago().getNombreMetodoDePago().toString());
            });
            return vueloReservaDTO;
        }).toList();

        return new HistorialReservasDTO(usuario.getNombre(),usuario.getApellido(),usuario.getMail(),usuario.getTelefono(),vuelosDTO);


    }


    private Double precioTotalDeLosAsientosReservados(List<AsientoVuelo> asientosQueFueronReservados, Double descuentoPorMetodoDePagoEnPorcentaje){
        return asientosQueFueronReservados.stream().map(asientoVuelo ->  {
            asientoVuelo.setPrecioPagado(realizandoDescuento(asientoVuelo.getPrecio(), descuentoPorMetodoDePagoEnPorcentaje));
            return asientoVuelo;
        }).mapToDouble(AsientoVuelo::getPrecioPagado).sum();
    }

    private Double realizandoDescuento(Double precioOriginal, Double descuentoPorMetodoDePagoEnPorcentaje) {
        return precioOriginal * (1 - (descuentoPorMetodoDePagoEnPorcentaje / 100));
    }

    private void guardarAsientosCoincidentes(List<AsientoVuelo> asientosQueTieneElVuelo, Long idVuelo, List<AsientoDTO> asientosQueSeQuierenReservar, List<DetalleAsientosPedidosDTO> detalleAsientosPedidos, List<AsientoVuelo> asientosQueFueronConfirmados) {
        int contadorAsientosQueTieneElVuelo = 0;
        int asientosEncontrados = 0;
        int contadorAsientosQueSeQuierenReservar = 0;
        while (contadorAsientosQueTieneElVuelo < asientosQueTieneElVuelo.size() && asientosEncontrados < asientosQueSeQuierenReservar.size()) {
            AsientoVuelo asiento = asientosQueTieneElVuelo.get(contadorAsientosQueTieneElVuelo);
            if (idVuelo.equals(asiento.getVuelo().getIdVuelo())) {
                while (contadorAsientosQueSeQuierenReservar < asientosQueSeQuierenReservar.size()) {
                    AsientoDTO asientoDTO = asientosQueSeQuierenReservar.get(contadorAsientosQueSeQuierenReservar);
                    if (verificarSiEsElAsiento(asiento, asientoDTO)) {
                        DetalleAsientosPedidosDTO detalleAsientoPedido = crearYGuardarDetalleDeAsientoPedidos(detalleAsientosPedidos,asiento,asientoDTO);
                        this.cambiarEstadoLibreAFalsoSiElAsientoFueAceptado(asiento, detalleAsientoPedido.getFueAceptado());
                        this.guardarAsientoSiElAsientoFueConfirmado(asientosQueFueronConfirmados, detalleAsientoPedido, asiento);
                        asientosEncontrados++;
                    }
                    contadorAsientosQueSeQuierenReservar++;
                }
                contadorAsientosQueSeQuierenReservar = 0;
            }
            contadorAsientosQueTieneElVuelo++;
        }
    }


    private void cambiarEstadoLibreAFalsoSiElAsientoFueAceptado(AsientoVuelo asientoVuelo,boolean fueAceptado){
        if(fueAceptado){
            asientoVuelo.setEstaLibre(false);
        }
    }

    private void guardarAsientoSiElAsientoFueConfirmado(List<AsientoVuelo> asientosQueFueronConfirmados, DetalleAsientosPedidosDTO detalleAsientoPedido, AsientoVuelo asientoVuelo){
        if (detalleAsientoPedido.getFueAceptado()) {
            asientosQueFueronConfirmados.add(asientoVuelo);
        }
    }

    private DetalleAsientosPedidosDTO crearYGuardarDetalleDeAsientoPedidos(List<DetalleAsientosPedidosDTO> detalleAsientosPedidos, AsientoVuelo asientoVuelo, AsientoDTO asientoDTO){
        DetalleAsientosPedidosDTO detalleAsientoPedido = crearDetalleAsientoPedidoDTO(asientoVuelo, asientoDTO);
        detalleAsientosPedidos.add(detalleAsientoPedido);
        return detalleAsientoPedido;
    }

    private DetalleAsientosPedidosDTO crearDetalleAsientoPedidoDTO(AsientoVuelo asientoVuelo, AsientoDTO asientoDTO) {
        Long vueloId = asientoVuelo.getVuelo().getIdVuelo();
        boolean estaLibre = asientoVuelo.getEstaLibre();
        String mensaje;
        if (estaLibre) {
            mensaje = String.format("El asiento con columna: %s, fila: %s y vuelo: %d fue reservado por usted",
                    asientoDTO.getColumna(), asientoDTO.getFila(), vueloId);
        } else {
            mensaje = String.format("El asiento con columna: %s, fila: %s y vuelo: %d ya fue reservado por alguien más, lo lamento",
                    asientoDTO.getColumna(), asientoDTO.getFila(), vueloId);
        }

        return new DetalleAsientosPedidosDTO(asientoDTO, vueloId, estaLibre, mensaje);
    }

    private boolean verificarSiEsElAsiento(AsientoVuelo asientoVuelo, AsientoDTO asientoDTO) {
        return asientoVuelo.getAsiento().getColumna().equals(asientoDTO.getColumna())
                && asientoVuelo.getAsiento().getFila().equals(asientoDTO.getFila());
    }

}
