package com.nhnacademy.accountapi.security.details;

import com.nhnacademy.accountapi.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //TODO#4 - 회원 검증을 위해서 username(회원 아이디) 데이터베이스에 조회 합니다.
        //여러분의 쇼핑몰 구조에 맞게 데이터 베이스에서 조회하는 로직을 추가하세요.
        //또는 RestTemplate를 이용해서 shoppingmall-api 서버로 회원정보를 호출해서 PrincipalDetails 객체를 생성할 수도 있습니다.
        
        User user = User.createUser("nhnacademy","엔에이치엔아카데미","nhnacademy12345*");
        return new PrincipalDetails(user);
    }

}
