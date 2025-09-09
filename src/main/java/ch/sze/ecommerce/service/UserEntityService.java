package ch.sze.ecommerce.service;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.repository.UserEntityRepo;
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

    public UserEntityService(UserEntityRepo userEntityRepo, PasswordEncoder encoder, AuthenticationManager manager) {
        this.userEntityRepo = userEntityRepo;
        this.encoder = encoder;
        this.manager = manager;
    }

    public UserEntity register(UserEntity user) {
        if (userEntityRepo.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userEntityRepo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("E-Mail already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userEntityRepo.save(user);
    }

    public UserEntity login(@Valid UserEntity user) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }
}
