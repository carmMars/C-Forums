package me.carmelo.cforums.services;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import me.carmelo.cforums.helpers.instantiables.TokenGenerator;
import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.models.user.entity.User;
import me.carmelo.cforums.models.user.repository.UserRepository;
import me.carmelo.cforums.models.verification.entity.VerificationToken;
import me.carmelo.cforums.models.verification.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void registerUser(UserDTO userDTO, String ipAddress) throws MessagingException {

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new IllegalStateException("Email already exists");

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            throw new IllegalStateException("Username already exists");

        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .lastIpAddress(ipAddress)
                .build();

        userRepository.save(user);

        // Create verification token
        VerificationToken verificationToken = new VerificationToken(user);
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user.getUsername(), user.getEmail(), verificationToken.getToken());
    }

    public boolean authenticateUser(UserDTO userDTO, String ipAddress) {
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                user.setLastIpAddress(ipAddress);
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    public void setUserEnabled(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
