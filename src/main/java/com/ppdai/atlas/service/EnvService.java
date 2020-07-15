package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.EnvDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.manager.EnvManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EnvService {

    @Autowired
    private EnvManager envManager;

    public Optional<EnvDto> getEnvById(Long id) {
        Preconditions.checkNotNull(id, "id can not be null.");
        return envManager.getEnvById(id);
    }

    public List<EnvDto> getAllEnvs() {
        return envManager.getAllEnvs();
    }

    public PageDto<EnvDto> getEnvsByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must bigger or equal than 0. ");
        Preconditions.checkArgument(size > 0, "size must be bigger than 0.");
        return envManager.getEnvsByPage(page, size);
    }

    @Transactional
    public EnvDto updateEnv(EnvDto envDto) {
        Preconditions.checkNotNull(envDto, "envDto can not be null.");
        boolean present = envManager.getEnvById(envDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("envDto for Id=%s not exists", envDto.getId());
        }

        String envName = envDto.getName();

        Optional<EnvDto> envDto2 = envManager.findEnvByNameAndActive(envName, true);
        if (envDto2.isPresent() && !envDto2.get().getId().equals(envDto.getId())) {
            throw AtlasServiceException.newAtlasException("envName=%s already exists", envName);
        }

        return envManager.saveEnv(envDto);
    }

    @Transactional
    public EnvDto addEnv(EnvDto envDto) {
        Preconditions.checkNotNull(envDto, "envDto can not be null.");
        String envName = envDto.getName();
        //envName 不重复
        Optional<EnvDto> envDto2 = envManager.findEnvByNameAndActive(envName, true);
        if (envDto2.isPresent()) {
            throw AtlasServiceException.newAtlasException("envName=%s already exists", envName);
        }

        //将isActive 置为 true
        Optional<EnvDto> envDto3 = envManager.findEnvByNameAndActive(envName, false);
        if (envDto3.isPresent()) {
            EnvDto tempEnv = envDto3.get();
            tempEnv.setActive(true);
            return envManager.saveEnv(tempEnv);
        }
        return envManager.saveEnv(envDto);
    }

    @Transactional
    public void removeEnv(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = envManager.getEnvById(id).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("envDto for Id=%s not exists", id);
        envManager.removeEnvById(id);
    }
}
