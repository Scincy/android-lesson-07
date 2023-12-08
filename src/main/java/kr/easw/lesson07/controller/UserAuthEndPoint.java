package kr.easw.lesson07.controller;

import kr.easw.lesson07.model.dto.UserData;
import kr.easw.lesson07.service.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthEndPoint {
    private final UserDataService userDataService;
    private final BCryptPasswordEncoder encoder;


    // JWT 인증을 위해 사용되는 엔드포인트입니다.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserData data) {
        try {
            // 로그인을 시도합니다.
            return ResponseEntity.ok(userDataService.createTokenWith(data));
        } catch (Exception ex) {
            // 만약 로그인에 실패했다면, 400 Bad Request를 반환합니다.
            return ResponseEntity.badRequest().body("");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserData data) {
        System.out.println(data.getUserId());
        if (!userDataService.isUserExists(data.getUserId())) {
            try {
                String pw = data.getPassword();
                data.setPassword(encoder.encode(pw));
                userDataService.createUser(data);
                return ResponseEntity.ok().build();//성공
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();//이미 가입 되어 있음
    }
}