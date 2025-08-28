package elearningspringboot.service;

import elearningspringboot.dto.request.SignInRequest;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse signIn(SignInRequest request);
    TokenResponse refreshToken(String refreshToken);
    void logout(String accessToken, String refreshToken);
}
