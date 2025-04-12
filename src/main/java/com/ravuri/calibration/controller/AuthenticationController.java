package com.ravuri.calibration.controller;

import com.ravuri.calibration.dto.AuthenticationRequest;
import com.ravuri.calibration.dto.AuthenticationResponse;
import com.ravuri.calibration.security.UserDetailedServiceImpl;
import com.ravuri.calibration.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailedServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * This method is used to authenticate the user and generate a JWT token.
     *
     * @param authenticationRequest The authentication request containing email and password.
     * @param response              The HTTP response object.
     * @return AuthenticationResponse containing the generated JWT token.
     * @throws BadCredentialsException If the credentials are invalid.
     * @throws DisabledException       If the user is disabled.
     * @throws UsernameNotFoundException If the user is not found.
     * @throws IOException             If an I/O error occurs.
     */
    @PostMapping(value = "/authentication", produces = "application/json", consumes = "application/json")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest ,
                                                            HttpServletResponse response
                                                            )
    throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException, Exception {
        LOGGER.info("Authentication request received for employeeID: {}", authenticationRequest.getEmployeeId());
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmployeeId(),
                    authenticationRequest.getPassword()));

        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid username or password");
        }catch (DisabledException e){
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is disabled");
            throw new DisabledException("User is disabled");
        }catch (UsernameNotFoundException e){
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            throw new UsernameNotFoundException("User not found");
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            throw new Exception("Internal server error");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmployeeId());
        final String jwt = jwtUtils.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(jwt, authenticationRequest.getEmployeeId());
    }


}
