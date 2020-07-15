package com.ppdai.atlas.manager;

import com.ppdai.atlas.dao.UserExtRepository;
import com.ppdai.atlas.dao.UserRepository;
import com.ppdai.atlas.dto.UserExtDto;
import com.ppdai.atlas.entity.UserEntity;
import com.ppdai.atlas.entity.UserExtEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserExtManager {

    @Autowired
    private UserExtRepository userExtRepository;

    @Autowired
    private UserRepository userRepository;

    public UserEntity findOneByWorkNumber(String workNumber) {
        List<UserEntity> userEntities = userRepository.findByWorkNumber(workNumber);
        if (userEntities != null && !userEntities.isEmpty()) {
            return userEntities.get(userEntities.size() - 1);
        }

        return null;
    }

    public void removeUserExtByWorkNumber(String userWorkNumber) {

        //删除但不通过调用repository update 方法, 重新 save 该对象, 因为 onetoone
        UserExtEntity userExtEntity = userExtRepository.findUserExtByUserWorkNumber(userWorkNumber);
        if (userExtEntity != null) {
            userExtEntity.setActive(false);
            userExtRepository.save(userExtEntity);
        }

    }

    public UserExtDto findUserExtByWorkNumber(String userWorkNumber) {
        UserExtEntity userExtEntity = userExtRepository.findUserExtByUserWorkNumber(userWorkNumber);
        UserExtDto userExtDto = new UserExtDto();
        if (userExtEntity != null) {
            //手动转化
            userExtDto.setId(userExtEntity.getId());
            userExtDto.setOrgId(userExtEntity.getOrgId());
            userExtDto.setUserWorkNumber(userExtEntity.getUserEntity().getWorkNumber());
        }
        return userExtDto;
    }

    public UserExtDto addUserExt(UserExtDto userExtDto) {
        UserExtEntity userExtEntity = new UserExtEntity();
        //根据 workNumber 获取 UserEntity
        UserEntity userEntity = findOneByWorkNumber(userExtDto.getUserWorkNumber());

        //收到 convert
        userExtEntity.setOrgId(userExtDto.getOrgId());
        userExtEntity.setUserEntity(userEntity);
        userExtEntity = userExtRepository.save(userExtEntity);

        //手动 convert
        userExtDto.setOrgId(userExtEntity.getOrgId());
        userExtDto.setId(userExtEntity.getId());

        return userExtDto;
    }

}
