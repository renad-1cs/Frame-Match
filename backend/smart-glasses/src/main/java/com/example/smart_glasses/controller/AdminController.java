package com.example.smart_glasses.controller;

import com.example.smart_glasses.entity.Admin;
import com.example.smart_glasses.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Integer id) {

        Optional<Admin> admin = adminService.getAdminById(id);

        if (admin.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Admin foundAdmin = admin.get();

        return ResponseEntity.ok(Map.of(
                "adminId", foundAdmin.getAdminId(),
                "username", foundAdmin.getUsername(),
                "role", foundAdmin.getRole()
        ));
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@Valid @RequestBody Admin admin) {

        Admin savedAdmin = adminService.saveAdmin(admin);

        return ResponseEntity.ok(Map.of(
                "adminId", savedAdmin.getAdminId(),
                "username", savedAdmin.getUsername(),
                "role", savedAdmin.getRole()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {

        if (adminService.getAdminById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        adminService.deleteAdmin(id);

        return ResponseEntity.noContent().build();
    }
}