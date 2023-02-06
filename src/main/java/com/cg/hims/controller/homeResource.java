package com.cg.hims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.hims.Entity.AuthenticationRequestDTO;
import com.cg.hims.Entity.AuthenticationResponseDTO;
import com.cg.hims.service.myUserDetailsService;
import com.cg.hims.utils.jwtUtils;


@RestController
@RequestMapping("/Security")
public class homeResource {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private myUserDetailsService userDetailsService;
	
	@Autowired
	private jwtUtils jwtUtilSer;

	@GetMapping("/Home")
	public String getHomeScreen() {
		return "Welcome to Entry Page";
	}

	@PostMapping("/Authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUserName());

		final String jwt = jwtUtilSer.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
	}
}
