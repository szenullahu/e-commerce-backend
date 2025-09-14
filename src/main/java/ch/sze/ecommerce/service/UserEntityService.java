package ch.sze.ecommerce.service;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.ProfilePicture;
import ch.sze.ecommerce.entity.Role;
import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.entity.dto.AuthResponseDTO;
import ch.sze.ecommerce.entity.dto.CreateAdminDTO;
import ch.sze.ecommerce.entity.dto.LoginDTO;
import ch.sze.ecommerce.entity.dto.RegisterDTO;
import ch.sze.ecommerce.repository.UserEntityRepo;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService {

    private final UserEntityRepo userEntityRepo;

    private final PasswordEncoder encoder;

    private final AuthenticationManager manager;

    private final JWTService jwtService;

    public UserEntityService(UserEntityRepo userEntityRepo, PasswordEncoder encoder, AuthenticationManager manager, JWTService jwtService) {
        this.userEntityRepo = userEntityRepo;
        this.encoder = encoder;
        this.manager = manager;
        this.jwtService = jwtService;
    }

    public UserEntity register(RegisterDTO dto) {
        if (userEntityRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userEntityRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setFirstname(dto.getFirstname());
        user.setSurname(dto.getSurname());
        user.setProfilePicture(dto.getProfilePicture());
        user.setRole(Role.CUSTOMER);
        return userEntityRepo.save(user);
    }

    public AuthResponseDTO login(@Valid LoginDTO dto) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = principal.getUser();
        String token = jwtService.generateToken(principal.getUser());

        return new AuthResponseDTO(token, user.getUsername(), user.getRole().name());
    }

    @PostConstruct
    public void createInitialAdmin() {
        if (!userEntityRepo.existsByRole(Role.ADMIN)) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setFirstname("Admin");
            admin.setSurname("User");
            admin.setRole(Role.ADMIN);
            admin.setProfilePicture(ProfilePicture.AVATAR_1);

            userEntityRepo.save(admin);
        }
    }

    public void createAdmin(@Valid CreateAdminDTO dto) {
        if (userEntityRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userEntityRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }

        UserEntity admin = new UserEntity();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(encoder.encode(dto.getPassword()));
        admin.setFirstname(dto.getFirstname());
        admin.setSurname(dto.getSurname());
        admin.setProfilePicture(dto.getProfilePicture());
        admin.setRole(Role.ADMIN);

        userEntityRepo.save(admin);
    }
}
