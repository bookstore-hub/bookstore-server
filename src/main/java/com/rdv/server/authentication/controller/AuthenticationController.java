package com.rdv.server.authentication.controller;

import com.rdv.server.authentication.service.AuthenticationService;
import com.rdv.server.authentication.to.JwtUserDetails;
import com.rdv.server.authentication.token.JwtTokenRequest;
import com.rdv.server.authentication.token.JwtTokenResponse;
import com.rdv.server.authentication.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@Tag(name = "AuthenticationController", description = "Set of endpoints to handle the JWT Authentication")
public class AuthenticationController {

  private final String tokenHeader;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsService jwtUserDetailsService;
  private final AuthenticationService authenticationService;

  public AuthenticationController(  @Value("${jwt.http.request.header}") String tokenHeader, JwtTokenUtil jwtTokenUtil,
                                    UserDetailsService jwtUserDetailsService, AuthenticationService authenticationService) {
      this.tokenHeader = tokenHeader;
      this.jwtTokenUtil = jwtTokenUtil;
      this.jwtUserDetailsService = jwtUserDetailsService;
      this.authenticationService = authenticationService;
  }


  @PostMapping(value = "/authenticate")
  public JwtUserDetails createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest) {
      boolean authenticate = authenticationService.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
      if(authenticate) {
          final JwtUserDetails userDetails = (JwtUserDetails) jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
          final String token = jwtTokenUtil.generateToken(userDetails);
          userDetails.setToken(token);
          return userDetails;
      } else {
          return null;
      }
  }

  @GetMapping(value = "/refresh")
  public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
      String authToken = request.getHeader(tokenHeader);
      final String token = authToken.substring(7);
      String username = jwtTokenUtil.getUsernameFromToken(token);
      JwtUserDetails user = (JwtUserDetails) jwtUserDetailsService.loadUserByUsername(username);
      if (jwtTokenUtil.canTokenBeRefreshed(token)) {
          String refreshedToken = jwtTokenUtil.refreshToken(token);
          return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
      } else {
          return ResponseEntity.badRequest().body(null);
      }
  }

}