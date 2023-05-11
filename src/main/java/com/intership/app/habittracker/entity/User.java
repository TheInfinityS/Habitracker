package com.intership.app.habittracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

@Entity
@Table(name = "usr",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@EqualsAndHashCode(of = {"id"})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Email
    @NotNull
    private String email;

    private Boolean emailVerified;

    @JsonIgnore
    private String password;

    private String icon;

    private String locale;

    private int point;

    private String providerId;

    @JsonIgnore
    private String activationCode;

    private Boolean changablePassword;

}
