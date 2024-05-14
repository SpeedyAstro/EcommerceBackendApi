package in.astro;

import com.cloudinary.Cloudinary;
import in.astro.config.AppConstants;
import in.astro.entity.Category;
import in.astro.entity.Role;
import in.astro.repository.CategoryRepository;
import in.astro.repository.RoleRepo;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@SecurityScheme(name = "E-Commerce Application", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class UserServiceApplication implements CommandLineRunner{

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> env = new HashMap<>();
        env.put("cloud_name", "dvqaeehte");
        env.put("api_key", "617982441979326");
        env.put("api_secret", "zh46RJEuNBiVy4by1F6NJpT9D20");
        env.put("secure", "true");
        return new Cloudinary(env);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Role adminRole = new Role();
            adminRole.setRoleId(AppConstants.ADMIN_ID);
            adminRole.setRoleName("ADMIN");

            Role userRole = new Role();
            userRole.setRoleId(AppConstants.USER_ID);
            userRole.setRoleName("USER");

            List<Role> roles = List.of(adminRole, userRole);

            List<Role> savedRoles = roleRepo.saveAll(roles);

            Optional<Category> byId = categoryRepo.findById(1L);
            byId.ifPresent(category -> {
                System.out.println("Category with id 1 is present");
            });
            if (byId.isEmpty()) {
                List<String> categoriesList = List.of("Fashion", "Electronics", "Home Appliances", "Books", "Furniture", "Grocery", "Beauty", "Toys", "Sports", "Automobiles", "Stationary", "Footwear", "Jewellery", "Accessories", "Health", "Fitness", "Bags", "Watches", "Mobiles", "Laptops", "Cameras", "Headphones", "Speakers", "Smart Watches", "Smart Bands", "Smart Glasses", "Smart Home", "Smart Wearables", "Smart Speakers", "Smart Cameras", "Smart Appliances", "Smart Security", "Smart Health", "Smart Fitness", "Smart Beauty", "Smart Toys", "Smart Automobiles", "Smart Stationary", "Smart Footwear", "Smart Jewellery", "Smart Accessories", "Smart Bags", "Smart Watches", "Smart Mobiles", "Smart Laptops", "Smart Cameras", "Smart Headphones", "Smart Speakers", "Smart Wearables", "Smart Glasses", "Smart Home", "Smart Appliances", "Smart Security", "Smart Health", "Smart Fitness", "Smart Beauty", "Smart Toys", "Smart Automobiles", "Smart Stationary", "Smart Footwear", "Smart Jewellery", "Smart Accessories", "Smart Bags", "Smart Watches", "Smart Mobiles", "Smart Laptops", "Smart Cameras", "Smart Headphones", "Smart Speakers", "Smart Wearables", "Smart Glasses", "Smart Home", "Smart Appliances", "Smart Security", "Smart Health", "Smart Fitness", "Smart Beauty", "Smart Toys", "Smart Automobiles", "Smart Stationary", "Smart");
                long i = 1L;
                for (String category : categoriesList) {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(category);
                    newCategory.setCategoryId(i);
                    categoryRepo.save(newCategory);
                    i++;
                }
            }
            savedRoles.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
