package com.example.Food_Ordering.controller;

import com.example.Food_Ordering.dto.AddressDTO;
import com.example.Food_Ordering.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    @Autowired
    private final AddressService addressService;

    public AddressController(AddressService addressService){
        this.addressService = addressService;
    }

    // updating address
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable UUID addressId,
                                                    @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, addressDTO));
    }

    // Getting an address
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable UUID addressId) {
        return ResponseEntity.ok(addressService.getAddressById(addressId));
    }

    // Delete the address
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

}
