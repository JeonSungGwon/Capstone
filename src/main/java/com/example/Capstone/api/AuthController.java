package com.example.Capstone.api;

import com.example.Capstone.dto.MemberRequestDto;
import com.example.Capstone.dto.MemberResponseDto;
import com.example.Capstone.dto.TokenDto;
import com.example.Capstone.jwt.TokenProvider;
import com.example.Capstone.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@Api(tags = "Auth", description = "인증 관련 api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final TokenProvider tokenProvider;

    @Operation(summary = "회원가입", description = "회원가입 메서드입니다.")
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @Operation(summary = "로그인", description = "로그인 메서드입니다.")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

}
