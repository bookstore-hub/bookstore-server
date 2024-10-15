package com.rdv.server.core.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "verification_token", schema="rdv")
public class VerificationToken {

    @Id
    @GeneratedValue(generator = "verification_token_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "verification_token_sequence", sequenceName = "rdv.verification_token_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiration_date")
    private OffsetDateTime expirationDate;


    public VerificationToken() {
    }
    public VerificationToken(final String token, final User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationDate = calculateExpirationDate();
    }

    /**
     * Returns the id
     *
     * @return Returns the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the token
     *
     * @return Returns the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token
     *
     * @param token The token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the user
     *
     * @return Returns the user
     */

    public User getUser() {
        return user;
    }

    /**
     * Sets the user
     *
     * @param user The user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the expirationDate
     *
     * @return Returns the expirationDate
     */
    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expirationDate
     *
     * @param expirationDate The expirationDate to set
     */
    public void setExpirationDate(OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    private OffsetDateTime calculateExpirationDate() {
        return OffsetDateTime.now().plusDays(1);
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expirationDate = calculateExpirationDate();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getExpirationDate() == null) ? 0 : getExpirationDate().hashCode());
        result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
        result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VerificationToken other = (VerificationToken) obj;
        if (getExpirationDate() == null) {
            if (other.getExpirationDate() != null) {
                return false;
            }
        } else if (!getExpirationDate().equals(other.getExpirationDate())) {
            return false;
        }
        if (getToken() == null) {
            if (other.getToken() != null) {
                return false;
            }
        } else if (!getToken().equals(other.getToken())) {
            return false;
        }
        if (getUser() == null) {
            return other.getUser() == null;
        } else return getUser().equals(other.getUser());
    }

    @Override
    public String toString() {
        return "Token [String=" + token + "]" + "[Expires" + expirationDate + "]";
    }

}