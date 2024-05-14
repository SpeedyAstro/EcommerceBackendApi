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
//                const productCategory = [
//  { id: 1, label: "Airpodes", value: "airpodes" },
//  { id: 2, label: "Camera", value: "camera" },
//  { id: 3, label: "Earphones", value: "earphones" },
//  { id: 4, label: "Mobiles", value: "mobiles" },
//  { id: 5, label: "Mouse", value: "Mouse" },
//  { id: 6, label: "Printers", value: "printers" },
//  { id: 7, label: "Processor", value: "processor" },
//  { id: 8, label: "Refrigerator", value: "refrigerator" },
//  { id: 9, label: "Speakers", value: "speakers" },
//  { id: 10, label: "Trimmers", value: "trimmers" },
//  { id: 11, label: "Televisions", value: "televisions" },
//  { id: 12, label: "Watches", value: "watches" },
//  { id: 13, label: "Others", value: "Others" },
//];
                List<String > categoriesList = List.of("Airpodes", "Camera", "Earphones", "Mobiles", "Mouse", "Printers", "Processor", "Refrigerator", "Speakers", "Trimmers", "Televisions", "Watches", "Others");
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
