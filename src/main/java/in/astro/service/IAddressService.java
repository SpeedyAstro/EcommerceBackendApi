package in.astro.service;

import in.astro.dto.AddressDto;
import in.astro.entity.Address;

import java.util.List;

public interface IAddressService {

        AddressDto createAddress(AddressDto addressDTO);

        List<AddressDto> getAddresses();

        AddressDto getAddress(Long addressId);

        AddressDto updateAddress(Long addressId, Address address);

        String deleteAddress(Long addressId);
}
