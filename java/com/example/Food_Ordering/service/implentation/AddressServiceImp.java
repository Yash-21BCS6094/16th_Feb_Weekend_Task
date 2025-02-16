package com.example.Food_Ordering.service.implentation;

import com.example.Food_Ordering.dto.AddressDTO;
import com.example.Food_Ordering.entity.Address;
import com.example.Food_Ordering.exceptions.ResourceNotFoundException;
import com.example.Food_Ordering.repository.AddressRepository;
import com.example.Food_Ordering.repository.UserRepository;
import com.example.Food_Ordering.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AddressServiceImp implements AddressService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public AddressDTO updateAddress(UUID addressId, AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO getAddressById(UUID addressId) {
        Address savedAddress = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public void deleteAddress(UUID addressId) {
        if(!addressRepository.existsById(addressId)){
            throw new ResourceNotFoundException("Address not found");
        }
        addressRepository.deleteById(addressId);
    }
}