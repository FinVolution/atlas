package com.ppdai.atlas.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "apply", catalog = "")
public class ApplyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "type", nullable = false, length = 128)
    private String type;

    @Basic
    @Column(name = "status", nullable = false, length = 128)
    private String status;

    @Basic
    @Column(name = "request", nullable = true)
    private String request;

    @Basic
    @Column(name = "result", nullable = true)
    private String result;

    @Basic
    @Column(name = "apply_user", nullable = false, length = 64)
    private String applyUser;

    @Basic
    @Column(name = "apply_department", nullable = false, length = 64)
    private String applyDepartment;

    @Basic
    @Column(name = "op_user", nullable = false, length = 64)
    private String opUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "apply_time")
    public Date applyTime;
}
