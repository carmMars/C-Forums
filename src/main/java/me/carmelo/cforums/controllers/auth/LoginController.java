package me.carmelo.cforums.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import me.carmelo.cforums.helpers.utils.NetworkUtils;
import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import me.carmelo.cforums.helpers.instantiables.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoginController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        String ipAddress = NetworkUtils.getClientIpAddr(request);
        boolean isAuthenticated = userService.authenticateUser(userDTO, ipAddress);

        if (isAuthenticated) {
            String token = jwtUtil.generateToken(userDTO.getUsername()); // Generate JWT token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid credentials");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
