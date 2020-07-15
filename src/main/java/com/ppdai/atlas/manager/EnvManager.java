package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.EnvRepository;
import com.ppdai.atlas.dto.EnvDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.entity.EnvEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class EnvManager {

    @Autowired
    private EnvRepository envRepository;

    public Optional<EnvDto> getEnvById(Long id) {
        EnvEntity envEntity = envRepository.findOneById(id);
        return Optional.ofNullable(envEntity == null ? null : ConvertUtils.convert(envEntity, EnvDto.class));
    }

    public List<EnvDto> getAllEnvs() {
        List<EnvEntity> entities = envRepository.findAll();
        if (!entities.isEmpty()) {
            return ConvertUtils.convert(entities, EnvDto.class);
        }
        return new ArrayList<>();
    }

    public PageDto<EnvDto> getEnvsByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<EnvEntity> envEntities = envRepository.findAll(pageRequest);
        PageDto<EnvDto> envDtoPageDto = ConvertUtils.convertPage(envEntities, EnvDto.class);
        List<EnvDto> envDtos = new ArrayList<>();
        if (!envEntities.getContent().isEmpty()) {
            envDtos = ConvertUtils.convert(envEntities.getContent(), EnvDto.class);
        }
        envDtoPageDto.setContent(envDtos);
        return envDtoPageDto;
    }

    @Transactional
    public void removeEnvById(Long id) {
        envRepository.removeEnvEntity(id);
        envRepository.flush();
    }

    @Transactional
    public EnvDto saveEnv(EnvDto envDto) {
        EnvEntity envEntity = ConvertUtils.convert(envDto, EnvEntity.class);
        envEntity = envRepository.save(envEntity);
        envRepository.flush();
        return ConvertUtils.convert(envEntity, EnvDto.class);
    }

    public Optional<EnvDto> findEnvByNameAndActive(String envName, boolean isActive) {
        EnvEntity envEntity = envRepository.findOneByNameAndActive(envName, isActive);
        return Optional.ofNullable(envEntity == null ? null : ConvertUtils.convert(envEntity, EnvDto.class));
    }

}
