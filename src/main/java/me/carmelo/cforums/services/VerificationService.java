package me.carmelo.cforums.services;

import me.carmelo.cforums.models.user.dto.UserDTO;
import me.carmelo.cforums.models.user.repository.UserRepository;
import me.carmelo.cforums.models.verification.entity.VerificationToken;
import me.carmelo.cforums.models.verification.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public VerificationService( VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public boolean verify(String email, String token) {
        Optional<VerificationToken> tokenOptional = tokenRepository.findByToken(token);
        return tokenOptional.map(verificationToken -> verificationToken.getUser().getEmail().equals(email)).orElse(false);
    }

}
