package in.astro.service.impl;

import in.astro.config.AppConstants;
import in.astro.dto.*;
import in.astro.entity.*;
import in.astro.exceptions.APIException;
import in.astro.exceptions.ResourceNotFoundException;
import in.astro.repository.AddressRepo;
import in.astro.repository.CartRepository;
import in.astro.repository.RoleRepo;
import in.astro.repository.UserRepository;
import in.astro.service.ICartService;
import in.astro.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ICartService cartService;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto registerUser(UserDto userDto) {
        try {
            User user = modelMapper.map(userDto, User.class);

            Cart cart = new Cart();
            user.setCart(cart);

            Role role = roleRepo.findById(AppConstants.USER_ID).get();
            user.getRoles().add(role);

            String country = userDto.getAddress().getCountry();
            String state = userDto.getAddress().getState();
            String city = userDto.getAddress().getCity();
            String pincode = userDto.getAddress().getPincode();
            String street = userDto.getAddress().getStreet();
            String buildingName = userDto.getAddress().getBuildingName();

            Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state,
                    city, pincode, street, buildingName);

            if (address == null) {
                address = new Address(country, state, city, pincode, street, buildingName);

                address = addressRepo.save(address);
            }

            user.setAddresses(List.of(address));
            if (user.getProfilePic() == null || user.getProfilePic().isEmpty()) {
                user.setProfilePic("https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png");
            }else {
                user.setProfilePic(user.getProfilePic());
            }
            User registeredUser = userRepo.save(user);
            cart.setUser(registeredUser);
            cartRepo.save(cart);

            userDto = modelMapper.map(registeredUser, UserDto.class);

            userDto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDto.class));

            return userDto;
        } catch (DataIntegrityViolationException e) {
            throw new APIException("User already exists with emailId: " + userDto.getEmail());
        }
    }


    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<User> pageUsers = userRepo.findAll(pageDetails);

        List<User> users = pageUsers.getContent();

        if (users.isEmpty()) {
            throw new APIException("No User exists !!!");
        }

        List<UserDto> userDTOs = users.stream().map(user -> {
            UserDto dto = modelMapper.map(user, UserDto.class);

            if (!user.getAddresses().isEmpty()) {
                dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDto.class));
            }

            CartDto cart = modelMapper.map(user.getCart(), CartDto.class);

            List<ProductDTO> products = user.getCart().getCartItems().stream()
                    .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            dto.setCart(cart);

            dto.getCart().setProducts(products);

            return dto;

        }).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();

        userResponse.setContent(userDTOs);
        userResponse.setPageNumber(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElements(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());

        return userResponse;
    }


    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        UserDto userDTO = modelMapper.map(user, UserDto.class);

        userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDto.class));

        CartDto cart = modelMapper.map(user.getCart(), CartDto.class);

        List<ProductDTO> products = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        userDTO.setCart(cart);

        userDTO.getCart().setProducts(products);

        return userDTO;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDTO) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        String encodedPass = passwordEncoder.encode(userDTO.getPassword());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMobileNumber(userDTO.getMobileNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodedPass);
        if (userDTO.getProfilePic() != null) {
            user.setProfilePic(userDTO.getProfilePic());
        }

        if (userDTO.getAddress() != null) {
            String country = userDTO.getAddress().getCountry();
            String state = userDTO.getAddress().getState();
            String city = userDTO.getAddress().getCity();
            String pincode = userDTO.getAddress().getPincode();
            String street = userDTO.getAddress().getStreet();
            String buildingName = userDTO.getAddress().getBuildingName();

            Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state,
                    city, pincode, street, buildingName);

            if (address == null) {
                address = new Address(country, state, city, pincode, street, buildingName);

                address = addressRepo.save(address);

                user.setAddresses(List.of(address));
            }
        }

        userDTO = modelMapper.map(user, UserDto.class);

        userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDto.class));

        CartDto cart = modelMapper.map(user.getCart(), CartDto.class);

        List<ProductDTO> products = user.getCart().getCartItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        userDTO.setCart(cart);

        userDTO.getCart().setProducts(products);

        return userDTO;
    }

    @Override
    public String deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        List<CartItem> cartItems = user.getCart().getCartItems();
        Long cartId = user.getCart().getCartId();

        cartItems.forEach(item -> {

            Long productId = item.getProduct().getProductId();

            cartService.deleteProductFromCart(cartId, productId);
        });

        userRepo.delete(user);

        return "User with userId " + userId + " deleted successfully!!!";
    }

    @Override
    public UserDto findByUserEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        UserDto userDTO = modelMapper.map(user, UserDto.class);
        return userDTO;
    }

    @Override
    public String changeRole(Long userId, String role) {
        if (!role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("user")) {
            throw new APIException("Role should be either admin or user");
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
//        remove all roles from user and set new role
        user.getRoles().clear();
        Role newRole = roleRepo.findByRoleName(role.toUpperCase()).orElseThrow(() -> new ResourceNotFoundException("Role", "role", role));
        user.getRoles().add(newRole);
        userRepo.save(user);
        return "Role changed successfully!!!";
    }
}
