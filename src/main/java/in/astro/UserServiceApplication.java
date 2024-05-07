package in.astro;

import com.cloudinary.Cloudinary;
import in.astro.config.AppConstants;
import in.astro.entity.Role;
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

@SpringBootApplication
@SecurityScheme(name = "E-Commerce Application", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class UserServiceApplication implements CommandLineRunner{

    @Autowired
    private RoleRepo roleRepo;

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

            savedRoles.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
