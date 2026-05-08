package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.UserProfile;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserRepository repo;

    // ── Health — tests DB connectivity ────────────────────────────────────
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            long count = repo.count(); // throws if DB is down
            return ResponseEntity.ok("JSON Service OK | DB connected | profiles: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("JSON Service ERROR | DB connection failed: " + e.getMessage());
        }
    }

    // ── CRUD ──────────────────────────────────────────────────────────────
    @PostMapping("/users")
    public ResponseEntity<UserProfile> create(@RequestBody UserProfile user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(user));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Optional<UserProfile> profile = repo.findById(id);
        if (profile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
        return ResponseEntity.ok(profile.get());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserProfile user) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
        user.setId(id);
        return ResponseEntity.ok(repo.save(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Profile deleted");
    }
}