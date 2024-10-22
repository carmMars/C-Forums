package me.carmelo.cforums.services;

import jakarta.transaction.Transactional;
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
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void registerUser(UserDTO userDTO, String ipAddress) {

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
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
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
}
