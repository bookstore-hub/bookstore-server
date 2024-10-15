package com.rdv.server.account.entity;

import com.rdv.server.core.entity.User;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "password_reset_token", schema="rdv")
public class PasswordResetToken {

    public PasswordResetToken() {
        super();
    }

    @Id
    @GeneratedValue(generator = "password_reset_token_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "password_reset_token_sequence", sequenceName = "rdv.password_reset_token_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiration_date")
    private OffsetDateTime expirationDate;


    public PasswordResetToken(final String token, final User user) {
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

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final OffsetDateTime expirationDate) {
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
        final PasswordResetToken other = (PasswordResetToken) obj;
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
