package com.nhnacademy.accountapi.adaptor;

import com.nhnacademy.accountapi.dto.AuthUserDto;
import com.nhnacademy.accountapi.dto.UserResponse;

public interface UserAdaptor {

    AuthUserDto getUser(String userId);
}
