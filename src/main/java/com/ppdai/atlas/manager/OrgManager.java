package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.OrgRepository;
import com.ppdai.atlas.dto.OrgDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.UserDto;
import com.ppdai.atlas.dto.query.OrgQuery;
import com.ppdai.atlas.entity.OrgEntity;
import com.ppdai.atlas.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrgManager {

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private UserExtManager userExtManager;

    private OrgDto setUserDtoToOrg(OrgEntity orgEntity) {
        OrgDto orgDto = ConvertUtils.convert(orgEntity, OrgDto.class);
        UserEntity userEntity = userExtManager.findOneByWorkNumber(orgEntity.getUserWorkNumber());
        Optional<UserDto> userDtoOptional = Optional.ofNullable(userEntity).map(item -> ConvertUtils.convert(item, UserDto.class));
        orgDto.setUserDto(userDtoOptional.orElse(new UserDto()));
        return orgDto;
    }

    public Optional<OrgDto> getOrgById(Long id) {
        OrgEntity orgEntity = orgRepository.findOneById(id);
        return Optional.ofNullable(orgEntity).map(m -> setUserDtoToOrg(m));
    }

    @Transactional
    public OrgDto addOrg(OrgDto orgDto) {
        OrgEntity orgEntity = ConvertUtils.convert(orgDto, OrgEntity.class);
        orgEntity = orgRepository.save(orgEntity);
        return setUserDtoToOrg(orgEntity);
    }

    public List<OrgDto> getAllOrgs() {
        List<OrgEntity> orgEntities = orgRepository.findAll();
        //添加parent 组织名
        List<OrgDto>
                orgDtos = orgEntities.stream().map(m -> setUserDtoToOrg(m)).collect(Collectors.toList());
        return orgDtos;
    }

    public PageDto<OrgDto> getOrgByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<OrgEntity> orgEntities = orgRepository.findAll(pageRequest);
        return setUserDtoToOrgDtoPage(orgEntities);
    }

    @Transactional
    public void removeOrgById(Long id) {
        orgRepository.removeOneEntityById(id);
    }

    public PageDto<OrgDto> getOrgByCondition(OrgQuery orgQuery, Pageable pageRequest) {
        OrgQuery query = (orgQuery == null) ? new OrgQuery() : orgQuery;
        Page<OrgEntity> orgEntities = orgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(query.getName()).ifPresent(o -> predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + query.getName() + "%")));
            Optional.ofNullable(query.getUserWorkNumber()).ifPresent(o -> predicates.add(criteriaBuilder.equal(root.get("userWorkNumber").as(String.class), query.getUserWorkNumber())));
            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), true));
            Predicate[] arrayPredicates = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arrayPredicates));
        }, pageRequest);
        return setUserDtoToOrgDtoPage(orgEntities);
    }

    //将 orgEntityPage 转成 orgDtoPageDto
    private PageDto<OrgDto> setUserDtoToOrgDtoPage(Page<OrgEntity> orgEntities) {
        PageDto<OrgDto> orgDtoPageDto = ConvertUtils.convertPage(orgEntities, OrgDto.class);

        //更改 Content，添加userDto
        List<OrgEntity> entitiesContent = orgEntities.getContent();
        List<OrgDto> orgDtos = entitiesContent.stream().map(m -> setUserDtoToOrg(m)).collect(Collectors.toList());
        orgDtoPageDto.setContent(orgDtos);

        return orgDtoPageDto;
    }

}
