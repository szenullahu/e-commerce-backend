package ch.sze.ecommerce.controller;

import ch.sze.ecommerce.entity.dto.CreateAdminDTO;
import ch.sze.ecommerce.service.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserEntityService userEntityService;


    public AdminController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody @Valid CreateAdminDTO dto) {
        userEntityService.createAdmin(dto);
        return ResponseEntity.ok("Admin Created");
    }
}
