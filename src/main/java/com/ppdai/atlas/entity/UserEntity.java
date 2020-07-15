package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@Table(name = "user_info", schema = "atlas")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "work_number")
    private String workNumber;

    @Column(name = "real_name", nullable = false, length = 45)
    private String realName;

    @Column(name = "user_name", nullable = false, length = 45)
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "source")
    private String source;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ldap_insert_time")
    protected Date ldapInsertTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ldap_update_time")
    protected Date ldapUpdateTime;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    public Map<String, Object> extensions;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_visit_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date lastVisitAt;

}
