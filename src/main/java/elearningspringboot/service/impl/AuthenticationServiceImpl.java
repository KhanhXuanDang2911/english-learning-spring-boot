package elearningspringboot.service.impl;

import elearningspringboot.dto.request.SignInRequest;
import elearningspringboot.dto.request.UserRequest;
import elearningspringboot.dto.response.SignInResponse;
import elearningspringboot.dto.response.TokenResponse;
import elearningspringboot.entity.User;
import elearningspringboot.entity.WhitelistToken;
import elearningspringboot.enumeration.TokenType;
import elearningspringboot.exception.UnauthorizedException;
import elearningspringboot.service.AuthenticationService;
import elearningspringboot.service.WhitelistTokenService;
import elearningspringboot.service.JwtService;
import elearningspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final WhitelistTokenService whitelistTokenService;



    @Override
    public TokenResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userService.findUserByEmail(request.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        whitelistTokenService.createToken(accessToken, TokenType.ACCESS_TOKEN, request.getEmail());
        whitelistTokenService.createToken(refreshToken, TokenType.REFRESH_TOKEN, request.getEmail());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(SignInResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .avatarUrl(user.getAvatarUrl())
                        .role(user.getRole().getRole())
                        .permissions(user.getRole().getRoleHasPermissions().stream()
                                .map(rhp -> rhp.getPermission().getName())
                                .collect(Collectors.toList()))
                        .build())
                .build();
    }
    @Override
    public TokenResponse refreshToken(String refreshToken){
        if (StringUtils.isBlank(refreshToken))
            throw new UnauthorizedException("Refresh token is not valid");
        String email = jwtService.extractEmail(refreshToken, TokenType.REFRESH_TOKEN);

        UserDetails user = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(refreshToken, user, TokenType.REFRESH_TOKEN) || !whitelistTokenService.existsByToken(refreshToken))
            throw new UnauthorizedException("Refresh token is not valid");
        String newAccessToken = jwtService.generateAccessToken(user);

        whitelistTokenService.createToken(newAccessToken, TokenType.ACCESS_TOKEN, email);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isNotBlank(accessToken)) {
            whitelistTokenService.deleteByToken(accessToken);
        }

        if (StringUtils.isNotBlank(refreshToken)) {
            String email = jwtService.extractEmail(refreshToken, TokenType.REFRESH_TOKEN);
            UserDetails user = userDetailsService.loadUserByUsername(email);

            if (!jwtService.isTokenValid(refreshToken, user, TokenType.REFRESH_TOKEN)) {
                throw new UnauthorizedException("Refresh token is not valid");
            }

            if (!whitelistTokenService.existsByToken(refreshToken)) {
                throw new UnauthorizedException("Refresh token is not whitelisted");
            }

            whitelistTokenService.deleteByToken(refreshToken);
        }
    }

}
