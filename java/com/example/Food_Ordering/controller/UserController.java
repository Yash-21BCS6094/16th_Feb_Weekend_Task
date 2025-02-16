package com.example.Food_Ordering.controller;

import com.example.Food_Ordering.dto.AuthenticationRequest;
import com.example.Food_Ordering.dto.AuthenticationResponse;
import com.example.Food_Ordering.dto.UserDTO;
import com.example.Food_Ordering.service.AuthenticationService;
import com.example.Food_Ordering.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.rmi.server.LogStream.log;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService customerServices;
    @Autowired
    private AuthenticationService authenticationService;

    // updating users's information
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") UUID userId, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(customerServices.updateUser(userId, userDTO), HttpStatus.ACCEPTED);
    }

    // Get the customer by his Id
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(customerServices.getUserById(id));
    }

    // Delete the customer using id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        customerServices.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Get the customer by his email
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByID(@PathVariable String email) {
        return ResponseEntity.ok(customerServices.getUserByEmail(email));
    }

    // Not getting this
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        return ResponseEntity.ok(customerServices.searchUserByName(name));
    }

    // To get the customer by page and size
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam("page") int page,
                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.of(Optional.ofNullable(customerServices.getAllUser(pageable)));
    }
}
