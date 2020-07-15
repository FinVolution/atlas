package com.ppdai.atlas.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_ext", schema = "atlas")
public class UserExtEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_work_number", referencedColumnName = "work_number")
    public UserEntity userEntity;

    @Column(name = "orgId", nullable = false)
    private Long orgId;

}
