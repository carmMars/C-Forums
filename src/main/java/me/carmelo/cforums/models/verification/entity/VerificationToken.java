package me.carmelo.cforums.models.verification.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.carmelo.cforums.helpers.instantiables.TokenGenerator;
import me.carmelo.cforums.models.user.entity.User;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private LocalDateTime expiryDate;

    public VerificationToken(User user) {
        this.user = user;
        this.token = new TokenGenerator().generateToken(32);
        this.expiryDate = LocalDateTime.now().plusMinutes(10);
    }

}
