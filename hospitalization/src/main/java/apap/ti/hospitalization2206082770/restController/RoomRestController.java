package apap.ti.hospitalization2206082770.restController;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import apap.ti.hospitalization2206082770.restService.ReservationRestService;
import apap.ti.hospitalization2206082770.restService.RoomRestService;
import apap.ti.hospitalization2206082770.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206082770.restdto.response.RoomResponseDTO;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {
    private final RoomRestService roomRestService;

    public RoomRestController(RoomRestService roomRestService) {
        this.roomRestService = roomRestService;

    }

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<RoomResponseDTO>>> listRoom() {
        List<RoomResponseDTO> listRoom = roomRestService.getAllRoom();

        var baseResponseDTO = new BaseResponseDTO<List<RoomResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listRoom);
        baseResponseDTO.setMessage("List Room berhasil diambil");
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
