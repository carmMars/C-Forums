package me.carmelo.cforums.controllers.auth;

import me.carmelo.cforums.services.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class VerificationTokenController {

    private final VerificationService verificationService;

    @Autowired
    public VerificationTokenController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> registerUser(@RequestParam String token, @RequestParam String email) {
        try {
            boolean result = verificationService.verify(email, token);
            HttpStatus httpStatus = result ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result, httpStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
