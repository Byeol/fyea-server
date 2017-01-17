package kr.ac.yonsei.fyea.service;

import kr.ac.yonsei.fyea.security.UserRepository;
import kr.ac.yonsei.fyea.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final String DEFAULT_USER = "user";
    private static final String DEFAULT_PASSWORD = "password";

    private final UserRepository userRepository;

    public void init() {
        User user = userRepository.findOne(DEFAULT_USER);

        if (user != null) {
            return;
        }

        user = new User(DEFAULT_USER);
        user.setPassword(DEFAULT_PASSWORD);
        userRepository.save(user);
    }
}
