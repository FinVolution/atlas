package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.ZoneRepository;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.ZoneDto;
import com.ppdai.atlas.entity.ZoneEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public List<ZoneDto> getAllZones() {
        List<ZoneEntity> zoneEntities = zoneRepository.findAll();
        return ConvertUtils.convert(zoneEntities, ZoneDto.class);
    }

    public PageDto<ZoneDto> getZoneInPage(Integer page, Integer size) {
        Preconditions.checkArgument(page >= 0, "page must be equal or bigger than 0.");
        Preconditions.checkArgument(size > 0, "size must be bigger than 0");

        PageRequest pageRequest = new PageRequest(page, size);
        Page<ZoneEntity> zoneEntities = zoneRepository.findAll(pageRequest);
        return ConvertUtils.convertPage(zoneEntities, ZoneDto.class);
    }

    public PageDto<ZoneDto> searchZoneByEnvAndName(Integer page, Integer size, String envName, String zoneName) {
        Preconditions.checkArgument(page >= 0, "page must be equal or bigger than 0.");
        Preconditions.checkArgument(size > 0, "size must be bigger than 0");

        PageRequest pageRequest = new PageRequest(page, size);
        Page<ZoneEntity> zoneEntities = zoneRepository.findByEnvAndName(pageRequest, envName, zoneName);
        return ConvertUtils.convertPage(zoneEntities, ZoneDto.class);
    }

    public Optional<ZoneDto> getZoneById(Long id) {
        Preconditions.checkNotNull(id, "zone id can not be null.");
        ZoneEntity zoneEntity = zoneRepository.findOneZoneById(id);
        return Optional.ofNullable(zoneEntity).map(e -> ConvertUtils.convert(e, ZoneDto.class));
    }

    public List<ZoneDto> getZonesByEnv(String env) {
        Preconditions.checkNotNull(env, "env can not be null.");
        List<ZoneEntity> zoneEntities = zoneRepository.findByEnv(env);
        return ConvertUtils.convert(zoneEntities, ZoneDto.class);
    }

    public ZoneDto addOrUpdateZone(ZoneDto zoneDto) {
        Preconditions.checkNotNull(zoneDto, "zoneDto can not be null.");

        ZoneEntity zoneEntity = ConvertUtils.convert(zoneDto, ZoneEntity.class);

        zoneEntity = zoneRepository.save(zoneEntity);
        zoneRepository.flush();

        return ConvertUtils.convert(zoneEntity, zoneDto);
    }

    @Transactional
    public void deleteZone(Long id) {
        Preconditions.checkNotNull(id, "zone id can not be null.");

        zoneRepository.removeZoneById(id);
        zoneRepository.flush();
    }
}
