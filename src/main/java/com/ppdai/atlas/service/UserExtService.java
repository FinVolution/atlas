package com.ppdai.atlas.service;

import com.ppdai.atlas.dto.UserExtDto;
import com.ppdai.atlas.manager.UserExtManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class UserExtService {

    @Autowired
    private UserExtManager userExtManager;

    @Transactional
    public void removeUserExtByWorkNumber(String userWorkNumber) {

        userExtManager.removeUserExtByWorkNumber(userWorkNumber);
    }

    @Transactional
    public UserExtDto addUserExt(UserExtDto userExtDto) {
        Objects.nonNull(userExtDto);
        return userExtManager.addUserExt(userExtDto);
    }

    public UserExtDto findUserExtByWorkNumber(String userWorkNumber) {

        UserExtDto userExtDto = userExtManager.findUserExtByWorkNumber(userWorkNumber);
        return userExtDto;
    }

}
