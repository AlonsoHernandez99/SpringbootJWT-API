package com.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.models.dao.IUsuarioDao;
import com.springboot.app.models.entity.Rol;
import com.springboot.app.models.entity.Usuario;

@Service("jpaUserDetailService")
public class JpaUserDetailService implements UserDetailsService {

	@Autowired
	private IUsuarioDao usuarioDao;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioDao.findByUsername(username);

		if (usuario == null) {
			log.info("El usuario no existe: " + username);
			throw new UsernameNotFoundException("El usuario: " + username + " no existe");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Rol rol : usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(rol.getAuthority()));
		}

		if (authorities.isEmpty()) {
			log.info("El usuario: " + username + " no tiene roles asignados");
			throw new UsernameNotFoundException("El usuario: " + username + " no tiene roles asignados");
		}

		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

}
