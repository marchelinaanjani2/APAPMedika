package apap.ti.insurance2206827592.security.service;

import apap.ti.insurance2206827592.restdto.response.BaseResponseDTO;
import apap.ti.insurance2206827592.restdto.response.EndUserResponseDTO;
import apap.ti.insurance2206827592.restdto.response.PatientResponseDTO;
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

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final WebClient webClient;

    public UserDetailsServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    public String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var response = webClient.get().uri(String.format("/api/endUser/detail?id=&username=%s&email=", username)).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<EndUserResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        EndUserResponseDTO responseDTO = new EndUserResponseDTO();
        responseDTO.setRole(response.getData().getRole());
        responseDTO.setUsername(response.getData().getUsername());
        responseDTO.setPassword(hashPassword(response.getData().getPassword()));


        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority(responseDTO.getRole()));
        return new User(responseDTO.getUsername(), responseDTO.getPassword(), grantedAuthoritySet);
    }
}
