package me.carmelo.cforums.controllers.auth;

import me.carmelo.cforums.models.user.entity.User;
import me.carmelo.cforums.models.user.repository.UserRepository;
import me.carmelo.cforums.services.UserService;
import me.carmelo.cforums.services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class VerificationTokenController {

    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final UserService userService;

    @Autowired
    public VerificationTokenController(UserRepository userRepository, VerificationService verificationService, UserService userService) {
        this.userRepository = userRepository;
        this.verificationService = verificationService;
        this.userService = userService;
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> registerUser(@RequestParam("token") String token, @RequestParam("email") String email) {
        try {
            boolean result = verificationService.verify(email, token);
            HttpStatus httpStatus = result ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

            if (httpStatus == HttpStatus.OK) {
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isEmpty()) return new ResponseEntity<>(false, httpStatus);
                userService.setUserEnabled(userOptional.get().getEmail());
            }

            return new ResponseEntity<>(result, httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
