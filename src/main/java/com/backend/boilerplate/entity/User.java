package com.backend.boilerplate.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author sarvesh
 * @version 0.0.2
 * @since 0.0.1
 */
@Entity
@Table(name = "app_user")
@TypeDef(name = "Status", typeClass = PostgreSQLEnumType.class)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", columnDefinition = "SERIAL")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    @Type(type = "Status")
    @Column(name = "status", columnDefinition = "Status", nullable = false)
    private Status status;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date timestamp;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "f_name", nullable = false)
    private String firstName;

    @Column(name = "l_name", nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserRole> userRoles = new ArrayList<>();

}
