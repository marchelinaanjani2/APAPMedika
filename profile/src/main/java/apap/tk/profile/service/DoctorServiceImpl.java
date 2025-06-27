package apap.tk.profile.service;

import apap.tk.profile.model.Doctor;
import apap.tk.profile.repository.DoctorDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{
    @Autowired
    private DoctorDb doctorDb;

    
    @Autowired
    private EndUserService endUserService;

    private List<Doctor> getAllDoctor() {return doctorDb.findByIsDeletedFalse();}

    @Override
    public Doctor addDoctor(Doctor doctor) {
        doctorDb.save(doctor);

        endUserService.addEndUser(doctor);

        return doctor;
    }

    


    



}
