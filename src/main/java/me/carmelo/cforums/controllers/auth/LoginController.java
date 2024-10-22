package me.carmelo.cforums.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import me.carmelo.cforums.helpers.NetworkUtils;
import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        String ipAddress = NetworkUtils.getClientIpAddr(request);
        boolean isAuthenticated = userService.authenticateUser(userDTO, ipAddress);

        if (isAuthenticated) {
            return new ResponseEntity<>("Login successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }
}
