package com.firstproject.employeebackend.controller;

import com.firstproject.employeebackend.dto.AuthResponseDTO;
import com.firstproject.employeebackend.dto.LoginRequestDTO;
import com.firstproject.employeebackend.dto.RefreshRequestDTO;
import com.firstproject.employeebackend.dto.RegisterRequestDTO;
import com.firstproject.employeebackend.entity.AppUser;
import com.firstproject.employeebackend.entity.RefreshToken;
import com.firstproject.employeebackend.repository.AppUserRepository;
import com.firstproject.employeebackend.repository.RefreshTokenRepository;
import com.firstproject.employeebackend.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshExpiration;


    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder,RefreshTokenRepository refreshTokenRepository,
                          @Value("${jwt.refresh-expiration}")
                          long refreshExpiration) {
        this.jwtService = jwtService;
        this.authenticationManager= authenticationManager;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshExpiration = refreshExpiration;
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestBody RefreshRequestDTO request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        String username = refreshToken.getUsername();

        String newAccessToken =
                jwtService.generateToken(username);

        String newRefreshToken =
                jwtService.generateRefreshToken(username);

        refreshTokenRepository.delete(refreshToken);

        RefreshToken newRefreshTokenEntity = new RefreshToken();

        newRefreshTokenEntity.setToken(newRefreshToken);
        newRefreshTokenEntity.setUsername(username);
        newRefreshTokenEntity.setExpiryDate(
                Instant.now().plusMillis(refreshExpiration)
        );

        refreshTokenRepository.save(newRefreshTokenEntity);

        return new AuthResponseDTO(
                newAccessToken,
                newRefreshToken
        );
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String accessToken =
                jwtService.generateToken(request.getUsername());

        String refreshToken =
                jwtService.generateRefreshToken(request.getUsername());

        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(request.getUsername());
        refreshTokenEntity.setExpiryDate(
               // Instant.now().plusMillis(2592000000L)
                Instant.now().plusMillis(refreshExpiration)

        );

        refreshTokenRepository.save(refreshTokenEntity);

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDTO request) {

        if (appUserRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }

        AppUser user = new AppUser();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        appUserRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/logout")
    public String logout(@RequestBody RefreshRequestDTO request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        refreshTokenRepository.delete(refreshToken);

        return "Logged out successfully";
    }
}