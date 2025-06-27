package apap.tk.profile.service;

import apap.tk.profile.model.Nurse;

import apap.tk.profile.repository.NurseDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
public class NurseServiceImpl implements NurseService {

    private final NurseDb nurseDb;

    @Autowired
    private EndUserService endUserService;

    public NurseServiceImpl(NurseDb nurseDb) {
        this.nurseDb = nurseDb;
    }

    @Override
    public Nurse createNurse(Nurse nurse) {  
        nurseDb.save(nurse);
        endUserService.addEndUser(nurse);
        return nurse;
    }

    @Override
    public List<Nurse> getAllNurse() {
        List<Nurse> nurses = nurseDb.findAll();
        System.out.println("Nurses found: " + nurses.size()); // Debug log
        return nurses;
    }


    @Override
    public Nurse getNurseById(UUID id) {
        return nurseDb.findById(id).orElse(null);
    }

    
}
