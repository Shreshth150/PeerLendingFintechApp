package com.peerlender.lendingengine.application;

import com.peerlender.lendingengine.application.service.Impl.TokenValidationService;
import com.peerlender.lendingengine.domain.model.User;
import com.peerlender.lendingengine.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {


    private final TokenValidationService tokenValidationService;
    private final UserRepository userRepository ;

    @Autowired
    public UserController(TokenValidationService tokenValidationService, UserRepository userRepository) {
        this.tokenValidationService = tokenValidationService;
        this.userRepository = userRepository;
    }


    @GetMapping(value = "/users")
    public List<User> findUsers(HttpServletRequest request){
        tokenValidationService.validateTokenAndGetUser(request.getHeader(HttpHeaders.AUTHORIZATION));
        List<User> list = userRepository.findAll();
        return list;
    }
}
