package com.sw.cmc.application.port.in.user;

import com.sw.cmc.domain.user.UserDomain;

/**
 * packageName    : com.sw.cmc.application.port.in.user
 * fileName       : UserUseCase
 * author         : SungSuHan
 * date           : 2025-02-27
 * description    :
 */
public interface UserUseCase {
    UserDomain getMyInfo() throws Exception;

    String withdraw(UserDomain userDomain) throws Exception;

    String update(UserDomain userDomain) throws Exception;

    String updatePassword(UserDomain userDomain) throws Exception;
}
