package apap.ti.appointment2206082612.restdto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class EndUserResponseDTO {
   
    private UUID id;
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean gender;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;
    private String role;
}

