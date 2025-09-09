package ch.sze.ecommerce.service;

import ch.sze.ecommerce.config.UserPrincipal;
import ch.sze.ecommerce.entity.UserEntity;
import ch.sze.ecommerce.repository.UserEntityRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserEntityRepo repo;

    public UserDetailService(UserEntityRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = repo.findByUsername(username);

        if (userEntity == null) {

            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("User was found");
        return new UserPrincipal(userEntity);
    }
}
