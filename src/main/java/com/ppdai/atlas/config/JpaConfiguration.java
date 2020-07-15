package com.ppdai.atlas.config;


import com.ppdai.atlas.dao.AuditLogRepository;
import com.ppdai.auth.utils.PauthTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories(basePackageClasses = {AuditLogRepository.class})
@EnableJpaAuditing
public class JpaConfiguration {


    @Bean
    AuditorAware<String> auditorProvider(PauthTokenUtil pauthTokenUtil) {
        return new UserAuditorAware(pauthTokenUtil);
    }

}
