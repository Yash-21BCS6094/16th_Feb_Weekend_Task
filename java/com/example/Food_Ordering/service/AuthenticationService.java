package com.example.Food_Ordering.service;

import com.example.Food_Ordering.dto.AuthenticationRequest;
import com.example.Food_Ordering.dto.AuthenticationResponse;
import com.example.Food_Ordering.dto.UserDTO;
import com.example.Food_Ordering.entity.Address;
import com.example.Food_Ordering.entity.Users;
import com.example.Food_Ordering.enums.Role;
import com.example.Food_Ordering.exceptions.ResourceNotFoundException;
import com.example.Food_Ordering.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager manager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // While registering the user
    public AuthenticationResponse register(UserDTO userDTO){
        Users user = new Users();
        user.setAddress(modelMapper.map(userDTO.getAddress(), Address.class));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        if(userDTO.getRole() == "USER"){
            user.setRole(Role.USER);
        }else{
            user.setRole(Role.ADMIN);
        }
        String jwtToken = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateRefresh(new HashMap<>(), user.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        userRepository.save(user);

        // While registering we are building an authentication response and
        // then passing the user jwtToken and refresh token
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(jwtToken);
//        response.setRefreshToken(refreshToken);

        return response;
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ));

        Users user = userRepository.findByUsername(authenticationRequest.getUsername());

        AuthenticationResponse response = new AuthenticationResponse();
        if(authentication.isAuthenticated()){
            response.setMessage("User authenticated");
            response.setToken(jwtService.generateToken(user));
        }else{
            response.setMessage("User not verified");
        }
        return response;
    }

    public AuthenticationResponse refreshToken(AuthenticationRequest authenticationRequest){

        Users user = userRepository.findByUsername(authenticationRequest.getUsername());

        if(user == null){
            throw new ResourceNotFoundException("Cannot find user");
        }

        String newRefreshToken = jwtService.generateToken(user);

        // This refresh token method is used when the token expires user can give a
        // refresh token and generate a new token from the server.
        AuthenticationResponse response = new AuthenticationResponse();
        response.setRefreshToken(newRefreshToken);
        return response;
    }

}