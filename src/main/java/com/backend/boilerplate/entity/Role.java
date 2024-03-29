package com.backend.boilerplate.entity;

import lombok.*;
import org.hibernate.annotations.Generated;
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
@Table(name = "role")
@TypeDef(name = "Status", typeClass = PostgreSQLEnumType.class)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", columnDefinition = "SERIAL")
    private Long id;

    @Generated(GenerationTime.INSERT)
    @Column(name = "uuid", columnDefinition = "UUID DEFAULT gen_random_uuid()", nullable = false, insertable = false,
        updatable = false, unique = true)
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date timestamp;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RoleFeatures> roleFeatures = new ArrayList<>();

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserRole> userRoles = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }
}
