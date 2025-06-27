package apap.tk.profile.restcontroller;

import apap.tk.profile.restdto.request.CreateUserRequestDTO;
import apap.tk.profile.restdto.response.BaseResponseDTO;
import apap.tk.profile.restdto.response.EndUserResponseDTO;
import apap.tk.profile.restservice.EndUserRestService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/endUser")
public class EndUserRestController {

    @Autowired
    private EndUserRestService endUserRestService;

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<EndUserResponseDTO>>> listUsers() {
        List<EndUserResponseDTO> listUser = endUserRestService.getAllUser();

        var baseResponseDTO = new BaseResponseDTO<List<EndUserResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listUser);
        baseResponseDTO.setMessage("Successfully retrieved users list");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<EndUserResponseDTO>> detailEndUser(@PathVariable("id") UUID id) {
        var baseResponseDTO = new BaseResponseDTO<EndUserResponseDTO>();
        var endUser = endUserRestService.getEndUserById(id);
        
        if (endUser == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data end user tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDTO);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(endUser);
        baseResponseDTO.setMessage(String.format("EndUser dengan ID %s berhasil ditemukan", id));
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody CreateUserRequestDTO userRequestDTO) {
        var baseResponseDTO = new BaseResponseDTO<EndUserResponseDTO>();
        try {
            EndUserResponseDTO userResponseDTO = endUserRestService.addUser(userRequestDTO);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(userResponseDTO);
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage(String.format("User %s dengan id %s berhasil dibuat!",
                    userResponseDTO.getUsername(),
                    userResponseDTO.getId()));
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }
        catch (EntityExistsException e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage("Gagal membuat User!");
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<BaseResponseDTO<EndUserResponseDTO>> getEndUserByIdOrUsernameOrEmail(
            @RequestParam(value = "id", required = false) UUID id,
            @RequestParam(value = "username", defaultValue = "", required = false) String username,
            @RequestParam(value = "email", defaultValue = "", required = false) String email
    ) {
        var baseResponseDTO = new BaseResponseDTO<EndUserResponseDTO>();
        try {
            var endUser = endUserRestService.getDetailUser(id, username, email);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(endUser);
            baseResponseDTO.setMessage(String.format("EndUser berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }
        catch (ConstraintViolationException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
        catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setMessage(e.getMessage());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/viewall/role")
    public ResponseEntity<BaseResponseDTO<List<EndUserResponseDTO>>> listUsersByRole(@RequestParam("role") String role) {
        var baseResponseDTO = new BaseResponseDTO<List<EndUserResponseDTO>>();

        try {
            List<EndUserResponseDTO> usersByRole = endUserRestService.getUsersByRole(role);

            if (usersByRole.isEmpty()) {
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage(String.format("No end users found with the role '%s'", role));
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(usersByRole);
            baseResponseDTO.setMessage(String.format("Successfully retrieved end users with the role '%s'", role));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("An error occurred while retrieving the data.");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
