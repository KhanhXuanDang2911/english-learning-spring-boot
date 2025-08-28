package elearningspringboot.service.impl;

import elearningspringboot.entity.WhitelistToken;
import elearningspringboot.enumeration.TokenType;
import elearningspringboot.repository.WhitelistTokenRepository;
import elearningspringboot.service.WhitelistTokenService;
import elearningspringboot.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class WhiteListTokenServiceImpl implements WhitelistTokenService {

    private final WhitelistTokenRepository blacklistTokenRepository;
    private final JwtService jwtService;

    @Override
    public void createToken(String token, TokenType tokenType, String email){
        Date expiredTime = jwtService.extractExpiration(token, tokenType);
        WhitelistToken blacklistToken = WhitelistToken.builder()
                .token(token)
                .email(email)
                .expiredTime(expiredTime)
                .tokenType(tokenType)
                .build();
        blacklistTokenRepository.save(blacklistToken);
    }

    @Override
    public void deleteByToken(String token){
        blacklistTokenRepository.deleteByToken(token);
    }

    public boolean existsByToken(String token){
        return blacklistTokenRepository.existsByToken(token);
    }

}
