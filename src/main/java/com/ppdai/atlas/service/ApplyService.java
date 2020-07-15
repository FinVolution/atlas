package com.ppdai.atlas.service;

import com.alibaba.fastjson.JSON;
import com.ppdai.atlas.controller.response.MessageType;
import com.ppdai.atlas.dao.ApplyRepository;
import com.ppdai.atlas.dto.*;
import com.ppdai.atlas.entity.ApplyEntity;
import com.ppdai.atlas.entity.converter.EnvUrl;
import com.ppdai.atlas.enums.EnvEnum;
import com.ppdai.atlas.exception.BaseException;
import com.ppdai.atlas.utils.ConvertUtils;
import com.ppdai.atlas.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApplyService {

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private Mapper dozerBeanMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private EnvService envService;

    @Autowired
    private AppService appService;

    @Autowired
    private SpecTypeService specTypeService;

    @Autowired
    private AppQuotaService appQuotaService;

    public ApplyDto getById(long id) {
        ApplyEntity applyEntity = applyRepository.getOne(id);
        return ConvertUtils.convert(applyEntity, ApplyDto.class);
    }

    @Transactional
    public ApplyDto create(ApplyDto applyDto) {
        ApplyEntity applyEntity = dozerBeanMapper.map(applyDto, ApplyEntity.class);
        if (StringUtils.isEmpty(applyEntity.getApplyDepartment())) {
            applyEntity.setApplyDepartment("notexist");
        }

        applyEntity.setApplyTime(new Date());
        applyEntity = applyRepository.save(applyEntity);
        return dozerBeanMapper.map(applyEntity, ApplyDto.class);
    }

    @Transactional
    public ApplyDto update(ApplyDto applyDto) {
        ApplyEntity applyEntity = dozerBeanMapper.map(applyDto, ApplyEntity.class);
        ApplyEntity fromDb = applyRepository.getOne(applyEntity.getId());
        ConvertUtils.convert(applyEntity, fromDb, ConvertUtils.getNullPropertyNames(applyEntity));

        applyEntity = applyRepository.save(fromDb);
        return dozerBeanMapper.map(applyEntity, ApplyDto.class);
    }

    public PageVO<ApplyDto> getByPage(String applyUser, String status, int page, int size) {
        PageVO<ApplyDto> applyPageVO = new PageVO<>();
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "applyTime");

        Page<ApplyEntity> applyPage = applyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            if (applyUser != null && !applyUser.trim().equals("")) {
                list.add(criteriaBuilder.equal(root.get("applyUser").as(String.class), applyUser.trim()));
            }

            if (status != null && !status.trim().equals("")) {
                list.add(criteriaBuilder.equal(root.get("status").as(String.class), status.trim()));
            }

            list.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), Boolean.TRUE));

            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        }, pageable);


        List<ApplyEntity> applyEntitiesList = applyPage.getContent();
        List<ApplyDto> applyList = ConvertUtils.convert(applyEntitiesList, ApplyDto.class, dozerBeanMapper);

        applyPageVO.setContent(applyList);
        applyPageVO.setTotalElements(applyPage.getTotalElements());
        return applyPageVO;
    }

    public void autoCreateApp(ApplyDto applyDto) {
        NewAppApplyDto newAppApplyDto = JSON.parseObject(applyDto.getRequest(), NewAppApplyDto.class);
        AppDtoPlus appDtoPlus = new AppDtoPlus();
        appDtoPlus.setEnableHa(true);
        appDtoPlus.setAppId(newAppApplyDto.getAppId());
        appDtoPlus.setName(newAppApplyDto.getAppName());
        appDtoPlus.setZoneType(newAppApplyDto.getZoneType());

        Optional<UserDto> applyUser = userService.findUserByUserName(applyDto.getApplyUser());
        if (!applyUser.isPresent()) {
            throw BaseException.newException(MessageType.ERROR, "无法找到对应的申请人: %s", applyDto.getApplyUser());
        }

        appDtoPlus.setDevelopers(applyUser.get().getWorkNumber());

        List<OrgDto> orgDtoList = orgService.getAllOrgs();
        for (OrgDto orgDto : orgDtoList) {
            if (orgDto.getName().equals(newAppApplyDto.getDepartment())) {
                appDtoPlus.setOrgId(orgDto.getId());
            }
        }

        List<EnvUrl> envUrlList = new ArrayList<>();

        List<EnvDto> envDtoList = envService.getAllEnvs();
        for (EnvDto envDto : envDtoList) {
            EnvUrl envUrl = new EnvUrl();
            envUrl.setEnvName(envDto.getName());
            envUrl.setUrl(newAppApplyDto.getAppName());
            envUrlList.add(envUrl);

        }

        appDtoPlus.setEnvUrls(envUrlList);

        appService.addApp(appDtoPlus);

        for (EnvDto envDto : envDtoList) {
            if (envDto.getName().equalsIgnoreCase(EnvEnum.PRO.name())) {
                SpecTypeDto specTypeDto = specTypeService.getSpecTypeByName(newAppApplyDto.getInstanceSpec());
                if (specTypeDto == null) {
                    throw BaseException.newException(MessageType.ERROR, "无法找到对应的规格: %s", newAppApplyDto.getInstanceSpec());
                }

                AppQuotaDto appQuotaDto = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(newAppApplyDto.getAppId(),
                        envDto.getName(), newAppApplyDto.getInstanceSpec());
                if (appQuotaDto == null) {
                    appQuotaDto = new AppQuotaDto();
                    appQuotaDto.setAppName(newAppApplyDto.getAppName());
                    appQuotaDto.setEnvId(envDto.getId());
                    appQuotaDto.setSpectypeId(specTypeDto.getId());
                    appQuotaDto.setNumber(Long.valueOf(newAppApplyDto.getInstanceCount()));
                    appQuotaService.addAppQuota(appQuotaDto);
                } else {
                    appQuotaDto.setNumber(Long.valueOf(newAppApplyDto.getInstanceCount()));
                    appQuotaService.updateAppQuota(appQuotaDto);
                }
            }
        }
    }
}
