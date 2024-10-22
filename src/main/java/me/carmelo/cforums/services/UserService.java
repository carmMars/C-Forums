package me.carmelo.cforums.services;

import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.models.user.entity.User;
import me.carmelo.cforums.models.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void registerUser(UserDTO userDTO, String ipAddress) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .lastIpAddress(ipAddress)
                .build();
        userRepository.save(user);
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
