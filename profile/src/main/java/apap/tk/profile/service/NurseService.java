package apap.tk.profile.service;


import java.util.List;

import apap.tk.profile.model.Nurse;
import java.util.UUID;

public interface NurseService {
    Nurse createNurse(Nurse nurse);
    List<Nurse> getAllNurse();
    Nurse getNurseById(UUID id);

}
