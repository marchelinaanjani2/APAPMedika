package apap.tk.profile.restdto.response;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NurseResponseDTO {
   
    private UUID id = UUID.randomUUID();
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean gender;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;
}
