package me.carmelo.cforums.controllers.auth;


import jakarta.servlet.http.HttpServletRequest;
import me.carmelo.cforums.helpers.utils.NetworkUtils;
import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            userService.registerUser(userDTO, NetworkUtils.getClientIpAddr(request));
            return ResponseEntity.ok("Registration successful. A verification email has been sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed.");
        }
    }
}
