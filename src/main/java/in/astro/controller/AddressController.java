package in.astro.controller;

import in.astro.dto.AddressDto;
import in.astro.entity.Address;
import in.astro.service.IAddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "E-Commerce Application")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDTO) {
        AddressDto savedAddressDTO = addressService.createAddress(addressDTO);

        return new ResponseEntity<AddressDto>(savedAddressDTO, HttpStatus.OK);
    }
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long addressId) {
        AddressDto addressDTO = addressService.getAddress(addressId);

        return new ResponseEntity<AddressDto>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAddresses() {
        List<AddressDto> addressDTOs = addressService.getAddresses();

        return new ResponseEntity<List<AddressDto>>(addressDTOs, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
        AddressDto addressDTO = addressService.updateAddress(addressId, address);

        return new ResponseEntity<AddressDto>(addressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }
}
