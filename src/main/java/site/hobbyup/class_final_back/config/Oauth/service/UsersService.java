package site.hobbyup.class_final_back.config.Oauth.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.hobbyup.class_final_back.config.Oauth.domain.Users;
import site.hobbyup.class_final_back.config.Oauth.domain.UsersRepository;

@Service
public class UsersService {

    private UsersRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;

    public UsersService(UsersRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void join(Users user) {
        if (user.getOauth() != null) {// 카카오 로그인
            String rawPassword = user.getPassword();
            if (!kakaovalidateDuplicateUser(user).isPresent()) {
                // 회원가입
                String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
                user.setPassword(encPassword);

                userRepository.save(user);
            }
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            validateDuplicateUser(user);
            String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encPassword);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public void validateDuplicateUser(Users user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원");
                });
    }

    @Transactional(readOnly = true)
    public Optional<Users> kakaovalidateDuplicateUser(Users user) {
        return userRepository.findByUsername(user.getUsername());
    }

    @Transactional
    public void userModify(Users user) {
        // 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화 시키고, 영속화된 User 오브젝트를 수정
        // select를 해서 user오브젝트를 DB로부터 가져오는 이유는 영속화를 하기 위해서
        // 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날림
        Users persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("회원찾기 실패");
        });

        if (persistance.getOauth() == null || persistance.getOauth().equals("")) {
            String encPassword = bCryptPasswordEncoder.encode(user.getPassword());
            persistance.setPassword(encPassword);
        }

        // 세션 등록
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 회원수정 합수 종료 = 서비스 종료 = 트랜잭션 종료 = commit 자동으로 됨
        // 영속화된 persistance 객체의 변화가 감지되면 더티체킹이 되어 update문을 날림
    }
}