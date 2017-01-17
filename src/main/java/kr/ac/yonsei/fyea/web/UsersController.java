package kr.ac.yonsei.fyea.web;

import kr.ac.yonsei.fyea.security.model.User;
import kr.ac.yonsei.fyea.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log
@RequiredArgsConstructor
@RestController
public class UsersController {

    private final UserRepository userRepository;

    @GetMapping("/user")
    public User getUser(Authentication authentication) {
        User user = userRepository.findOne(authentication.getName());
        return user;
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody User userData, Authentication authentication) {
        User user = userRepository.findOne(authentication.getName());
        user.setPassword(userData.getPassword());
        userRepository.save(user);

        return ResponseEntity.ok(null);
    }
}
