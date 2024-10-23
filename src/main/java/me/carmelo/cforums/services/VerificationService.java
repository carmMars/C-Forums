package me.carmelo.cforums.services;

import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.models.user.entity.User;
import me.carmelo.cforums.models.user.repository.UserRepository;
import me.carmelo.cforums.models.verification.entity.VerificationToken;
import me.carmelo.cforums.models.verification.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VerificationService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public VerificationService(UserRepository userRepository, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public boolean verify(String email, String token) {
        Optional<VerificationToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            VerificationToken verificationToken = tokenOptional.get();
            User user = verificationToken.getUser();

            // Check if the token is associated with the provided email
            if (user.getEmail().equalsIgnoreCase(email)) {
                // Enable the user
                user.setEnabled(true);
                userRepository.save(user);

                // Delete the token after successful verification
                tokenRepository.delete(verificationToken);

                return true;
            }
        }
        return false;
    }


    /**
     * Scheduled task to purge expired verification tokens.
     * Runs every day at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void purgeExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
