package apap.ti.appointment2206082612.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {  
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173", "https://apap-2121.cs.ui.ac.id", "https://apap-2122.cs.ui.ac.id", "https://apap-2123.cs.ui.ac.id", "https://apap-2124.cs.ui.ac.id", "https://apap-2125.cs.ui.ac.id", "https://apap-2126.cs.ui.ac.id", "https://apap-2127.cs.ui.ac.id", "https://apap-2128.cs.ui.ac.id")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

