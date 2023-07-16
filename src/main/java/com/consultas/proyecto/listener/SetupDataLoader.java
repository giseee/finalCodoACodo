package com.consultas.proyecto.listener;

import com.consultas.proyecto.enums.EMetodosDePago;
import com.consultas.proyecto.model.*;
import com.consultas.proyecto.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private VueloRepository vueloRepository;
	@Autowired
	private RoleRepository perfilRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MetodoDePagoRepository metodoDePagoRepository;

	@Autowired
	private ReservaRepository reservaRepository;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
            return;
		Role perfilAdministrador = crearPerfilSiNoExiste(Role.ROLE_ADMIN);
		Role perfilUsuario = crearPerfilSiNoExiste(Role.ROLE_USER);
		Role perfilAgenteDeVentas = crearPerfilSiNoExiste(Role.ROLE_SALES_AGENT);

		
		Usuario usuarioAdmin = new Usuario();
		usuarioAdmin.setNombre("Juan");
		usuarioAdmin.setApellido("Juanito");
		usuarioAdmin.setTelefono("11-2222-3333-4444");
		usuarioAdmin.setMail("juan@gmail.com");
		usuarioAdmin.setPassword(passwordEncoder.encode("juancito"));
		usuarioAdmin.setActivo(true);
		Set<Role> perfilesAdmin = new HashSet<>();
		perfilesAdmin.add(perfilUsuario);
		perfilesAdmin.add(perfilAgenteDeVentas);
		perfilesAdmin.add(perfilAdministrador);
		usuarioAdmin.setRoles(perfilesAdmin);
		if(!usuarioRepository.findByNombre(usuarioAdmin.getNombre()).isPresent())
			usuarioRepository.save(usuarioAdmin);				
		
		Usuario usuarioComun = new Usuario();
		usuarioComun.setNombre("Fernando");
		usuarioComun.setApellido("Fernandito");
		usuarioComun.setTelefono("22-3333-4444-5555");
		usuarioComun.setMail("fernando@gmail.com");
		usuarioComun.setPassword(passwordEncoder.encode("fer"));
		usuarioComun.setActivo(true);
		Set<Role> perfilesUsuario = new HashSet<>();
		perfilesUsuario.add(perfilUsuario);
		usuarioComun.setRoles(perfilesUsuario);
		if(!usuarioRepository.findByNombre(usuarioComun.getNombre()).isPresent())
			usuarioRepository.save(usuarioComun);

		Usuario usuarioAgenteDeVentas = new Usuario();
		usuarioAgenteDeVentas.setNombre("Matias");
		usuarioAgenteDeVentas.setApellido("Mati");
		usuarioAgenteDeVentas.setTelefono("33-4444-5555-6666");
		usuarioAgenteDeVentas.setMail("matias@gmail.com");
		usuarioAgenteDeVentas.setPassword(passwordEncoder.encode("matias"));
		usuarioAgenteDeVentas.setActivo(true);
		Set<Role> perfilesAgenteDeVentas = new HashSet<>();
		perfilesAgenteDeVentas.add(perfilUsuario);
		perfilesAgenteDeVentas.add(perfilAgenteDeVentas);
		usuarioAgenteDeVentas.setRoles(perfilesAgenteDeVentas);
		if(!usuarioRepository.findByNombre(usuarioAgenteDeVentas.getNombre()).isPresent())
			usuarioRepository.save(usuarioAgenteDeVentas);


		Aerolinea aerolinea1 = new Aerolinea();
		aerolinea1.setNombre("Aerolineas Imaginarias");

		Aerolinea aerolinea2 = new Aerolinea();
		aerolinea2.setNombre("Aerolineas Imaginarias 2");

		Avion avion1 =new Avion("avion 1",aerolinea1);
		Avion avion2 =new Avion("avion 2", aerolinea2);


		List<Asiento> asientosAvion1 = new ArrayList<>();
		asientosAvion1.add(new Asiento("1", "1", true,avion1));
		asientosAvion1.add(new Asiento("2", "1", true,avion1));
		asientosAvion1.add(new Asiento("3", "1", true,avion1));
		asientosAvion1.add(new Asiento("4", "1", true,avion1));
		asientosAvion1.add(new Asiento("5", "1", true,avion1));
		asientosAvion1.add(new Asiento("1", "2", false,avion1));
		asientosAvion1.add(new Asiento("2", "2", false,avion1));
		asientosAvion1.add(new Asiento("3", "2", true,avion1));

		List<Asiento> asientosAvion2 = new ArrayList<>();
		asientosAvion2.add(new Asiento("1", "1", true, avion2));
		asientosAvion2.add(new Asiento("2", "1", true, avion2));
		asientosAvion2.add(new Asiento("3", "1", true, avion2));
		asientosAvion2.add(new Asiento("4", "1", true, avion2));
		asientosAvion2.add(new Asiento("5", "1", true, avion2));
		asientosAvion2.add(new Asiento("1", "2", false, avion2));
		asientosAvion2.add(new Asiento("2", "2", false, avion2));
		asientosAvion2.add(new Asiento("3", "2", true, avion2));

		avion1.setAsientos(asientosAvion1);
		avion2.setAsientos(asientosAvion2);

		Vuelo vuelo1 = new Vuelo(new Date(), new Date(), "Origen A", "Destino A", false);
		Vuelo vuelo2 = new Vuelo(new Date(), new Date(), "Origen B", "Destino B", true );
		Vuelo vuelo3 = new Vuelo(new Date(), new Date(), "Origen C", "Destino C", false);

		List<AsientoVuelo> asientoVuelo1 = new ArrayList<>();
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(0), vuelo1, asientosAvion1.get(0).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(1), vuelo1, asientosAvion1.get(1).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(2), vuelo1, asientosAvion1.get(2).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(3), vuelo1, asientosAvion1.get(3).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(4), vuelo1, asientosAvion1.get(4).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(5), vuelo1, asientosAvion1.get(5).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(6), vuelo1, asientosAvion1.get(6).getEstaApto(),null, 9000D));
		asientoVuelo1.add(new AsientoVuelo(asientosAvion1.get(7), vuelo1, asientosAvion1.get(7).getEstaApto(),null, 9000D));

		List<AsientoVuelo> asientoVuelo2 = new ArrayList<>();
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(0), vuelo2, asientosAvion1.get(0).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(1), vuelo2, asientosAvion1.get(1).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(2), vuelo2, asientosAvion1.get(2).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(3), vuelo2, asientosAvion1.get(3).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(4), vuelo2, asientosAvion1.get(4).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(5), vuelo2, asientosAvion1.get(5).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(6), vuelo2, asientosAvion1.get(6).getEstaApto(),null, 9000D));
		asientoVuelo2.add(new AsientoVuelo(asientosAvion1.get(7), vuelo2, asientosAvion1.get(7).getEstaApto(),null, 9000D));

		List<AsientoVuelo> asientoVuelo3 = new ArrayList<>();
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(0), vuelo3, asientosAvion2.get(0).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(1), vuelo3, asientosAvion2.get(1).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(2), vuelo3, asientosAvion2.get(2).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(3), vuelo3, asientosAvion2.get(3).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(4), vuelo3, asientosAvion2.get(4).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(5), vuelo3, asientosAvion2.get(5).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(6), vuelo3, asientosAvion2.get(6).getEstaApto(),null, 9000D));
		asientoVuelo3.add(new AsientoVuelo(asientosAvion2.get(7), vuelo3, asientosAvion2.get(7).getEstaApto(),null, 9000D));


		vuelo1.setAsientosVuelo(asientoVuelo1);
		vuelo2.setAsientosVuelo(asientoVuelo2);
		vuelo3.setAsientosVuelo(asientoVuelo3);

		vueloRepository.save(vuelo1);
		vueloRepository.save(vuelo2);
		vueloRepository.save(vuelo3);

		metodoDePagoRepository.save(new MetodoDePago(EMetodosDePago.TARJETA_DE_CREDITO, 20D));
		metodoDePagoRepository.save(new MetodoDePago(EMetodosDePago.TRANSFERENCIA_BANCARIA, 10D));

		alreadySetup = true;
	}
	
	  @Transactional
	  Role crearPerfilSiNoExiste(String nombreRol) {
	 
		  Optional<Role> perfilOptional = perfilRepository.findByNombreRol(nombreRol);
	        if (!perfilOptional.isPresent()) {
				Role perfil = new Role();
	            perfil.setNombreRol(nombreRol);
	            perfilRepository.save(perfil);
	            return perfil;
	        }
	        return perfilOptional.get();
	    }

}
