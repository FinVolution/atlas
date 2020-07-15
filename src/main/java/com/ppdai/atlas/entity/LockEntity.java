package com.ppdai.atlas.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "locks", schema = "atlas")
public class LockEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "possessor")
    private String possessor;

}
