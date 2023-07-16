package com.consultas.proyecto.repository;

import com.consultas.proyecto.enums.EMetodosDePago;
import com.consultas.proyecto.model.MetodoDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetodoDePagoRepository extends JpaRepository<MetodoDePago, Long> {


    Optional<MetodoDePago> findByNombreMetodoDePago(EMetodosDePago nombreMetodoDePago);
}
