package elearningspringboot.controller;

import elearningspringboot.dto.request.SignInRequest;
import elearningspringboot.dto.response.ResponseData;
import elearningspringboot.dto.response.TokenResponse;
import elearningspringboot.service.AuthenticationService;
import elearningspringboot.util.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseData<TokenResponse>> signIn(@Validated @RequestBody SignInRequest request){
        TokenResponse response = authenticationService.signIn(request);
        return ResponseBuilder.withData(HttpStatus.OK, "Sign in successfully", response);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseData<TokenResponse>> refreshToken(@RequestHeader("Y-Token") String refreshToken){
        TokenResponse response = authenticationService.refreshToken(refreshToken);
        return ResponseBuilder.withData(HttpStatus.OK, "Refresh token successfully", response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<Void>> refreshToken(@RequestHeader("X-Token") String accessToken, @RequestHeader("Y-Token") String refreshToken){
        authenticationService.logout(accessToken, refreshToken);
        return ResponseBuilder.noData(HttpStatus.OK, "Logout successfully");
    }
}
