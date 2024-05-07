package in.astro.service.impl;

import in.astro.dto.AddressDto;
import in.astro.entity.Address;
import in.astro.entity.User;
import in.astro.exceptions.APIException;
import in.astro.exceptions.ResourceNotFoundException;
import in.astro.repository.AddressRepo;
import in.astro.repository.UserRepository;
import in.astro.service.IAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService implements IAddressService {
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public AddressDto createAddress(AddressDto addressDTO) {
        String country = addressDTO.getCountry();
        String state = addressDTO.getState();
        String city = addressDTO.getCity();
        String pincode = addressDTO.getPincode();
        String street = addressDTO.getStreet();
        String buildingName = addressDTO.getBuildingName();
        Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country,
                state, city, pincode, street, buildingName);
        if (addressFromDB != null) {
            throw new APIException("Address already exists with addressId: " + addressFromDB.getAddressId());
        }
        Address address = modelMapper.map(addressDTO, Address.class);

        Address savedAddress = addressRepo.save(address);

        return modelMapper.map(savedAddress, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddresses() {
        List<Address> addresses = addressRepo.findAll();
        return addresses.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto getAddress(Long addressId) {
        Address addressDB = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(addressDB, AddressDto.class);
    }

    @Override
    public AddressDto updateAddress(Long addressId, Address address) {
        Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
                address.getCountry(), address.getState(), address.getCity(), address.getPincode(), address.getStreet(),
                address.getBuildingName());
        if (addressFromDB == null) {
            Address addressDB = addressRepo.findById(addressId)
                    .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
            addressDB.setCountry(address.getCountry());
            addressDB.setState(address.getState());
            addressDB.setCity(address.getCity());
            addressDB.setPincode(address.getPincode());
            addressDB.setStreet(address.getStreet());
            addressDB.setBuildingName(address.getBuildingName());
            Address updatedAddress = addressRepo.save(addressDB);
            return modelMapper.map(updatedAddress, AddressDto.class);
        }else{
            List<User> users = userRepo.findByAddress(addressId);
            final Address a = addressFromDB;
            users.forEach(user -> user.getAddresses().add(a));
            deleteAddress(addressId);
            return modelMapper.map(addressFromDB, AddressDto.class);
        }
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressDB = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        userRepo.findByAddress(addressId).forEach(user -> {
            user.getAddresses().remove(addressDB);
            userRepo.save(user);
        });
        addressRepo.deleteById(addressId);
        return "Address deleted successfully with addressId: " + addressId;
    }
}
