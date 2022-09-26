package com.example.ref.security_jwt.controller;

import com.example.ref.security_jwt.model.User;
import com.example.ref.security_jwt.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
public class SecurityController {
    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    // 이러한 annotation 을 사용하려면 config 설정을 추가해줘야한다.
    @PostMapping("/api/v1/test")
    // 특정 역할을 가지는 사람만 사용할 수 있다.
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    // 위와 비슷한 결과
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    public String test(){
        return null;
    }
    // 메소드를 실행하기 전에 인증 및 인가 체크를 한다.
    // 인가에는 param 으로 받은 값을 사용할 수 있다. #param
    @PreAuthorize("isAuthenticated() and (( #user.username == principal.username ) or hasRole('ROLE_ADMIN'))")
    @PostMapping("/api/v1/test2")
    public String test2(User user){
        return null;
    }

    // 메소드를 실행한 후에 인증 및 인가 체크를 한다.
    // 인가에는 return 값을 사용할 수 있다. returnObject
    @PostAuthorize("isAuthenticated() and (( returnObject.username == principal.username ) or hasRole('ROLE_ADMIN'))")
    @GetMapping("/api/v1/{id}")
    public User test3(@PathVariable("id") int id ){
        return securityService.findById(id);
    }
}
