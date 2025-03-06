package com.arda.googleContacts.contacts.controller;

import java.io.IOException;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.arda.googleContacts.contacts.service.GoogleContactsService;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "http://localhost:8080") // Allow cross-origin requests (adjust as needed)
public class GoogleContactsController {

    private final GoogleContactsService googleContactsService;

    public GoogleContactsController(GoogleContactsService googleContactsService) {
        this.googleContactsService = googleContactsService;
    }

    @GetMapping
    public ResponseEntity<String> getAllContacts(@AuthenticationPrincipal OAuth2User principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(401).body("{\"error\": \"User not authenticated\"}");
            }
            
            String result = googleContactsService.getContacts(principal.getName());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
    @PostMapping
    public String createContact(@AuthenticationPrincipal OAuth2User principal, @RequestBody Map<String, Object> contactData) {
        return googleContactsService.createContact(principal.getName(), contactData);
    }

    
     @PostMapping("/update")
    public String updateContact(
            @RequestParam String resourceName,
            @RequestParam String familyName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber) {

        try {
            googleContactsService.updateContact(resourceName, familyName, email, phoneNumber);
            System.out.println("Contact updated: " + resourceName);
            return "Contact updated successfully";
        } catch (IOException e) {
            return "error";
        }
    }

    @PostMapping("/delete")
    public String deleteContact(@RequestParam String resourceName) {
        try {
            googleContactsService.deleteContact(resourceName);
            System.out.println("Deleted contact: " + resourceName);
            return "Contact deleted successfully";
        } catch (IOException e) {
            return "error";
        }
    }

}
