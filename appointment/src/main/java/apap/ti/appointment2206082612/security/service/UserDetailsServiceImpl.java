package apap.ti.appointment2206082612.security.service;

import apap.ti.appointment2206082612.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206082612.restdto.response.EndUserResponseDTO;
import apap.ti.appointment2206082612.restdto.response.PatientResponseDTO;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WebClient webClient;

    public UserDetailsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }
    
    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Attempting to load user by username: {}", username);

        // Fetching data from web client
        var response = webClient.get()
                .uri(String.format("/api/endUser/detail?id=&username=%s&email=", username))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<EndUserResponseDTO>>() {})
                .block();

        if (response == null) {
            logger.error("Response from API is null");
            return null;
        }

        logger.info("API Response: {}", response);

        // Check the status of the response
        if (response.getStatus() != 200) {
            logger.error("API returned non-200 status: {}", response.getStatus());
            return null;
        }

        EndUserResponseDTO responseDTO = response.getData();
        if (responseDTO == null) {
            logger.error("Response data is null");
            return null;
        }

        logger.info("Response DTO: {}", responseDTO);

        // Hashing the password and preparing the user details
        responseDTO.setPassword(hashPassword(responseDTO.getPassword()));
        logger.info("Hashed password for user: {}", responseDTO.getUsername());

        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority(responseDTO.getRole()));
        logger.info("Assigned role: {}", responseDTO.getRole());

        // Returning UserDetails
        User user = new User(responseDTO.getUsername(), responseDTO.getPassword(), grantedAuthoritySet);
        logger.info("Successfully created UserDetails for username: {}", responseDTO.getUsername());

        return user;
    }
}
