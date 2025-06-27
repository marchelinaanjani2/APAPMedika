package apap.tk.profile.restservice;


import apap.tk.profile.model.Doctor;

import apap.tk.profile.repository.DoctorDb;

import apap.tk.profile.restdto.response.DoctorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

import java.util.UUID;


@Service
@Transactional
public class DoctorRestServiceImpl implements DoctorRestService{
    @Autowired
    DoctorDb doctorDb;

    @Override
    public List<DoctorResponseDTO> getAllDoctor() throws EntityNotFoundException {
        var listDoctor = doctorDb.findByIsDeletedFalse();

        var listDoctorResponseDTO = new ArrayList<DoctorResponseDTO>();
        listDoctor.forEach(doctor -> {
            var docResponseDTO = doctorToDoctorResponseDTO(doctor);
            listDoctorResponseDTO.add(docResponseDTO);
        });

        return listDoctorResponseDTO;
    }

    private DoctorResponseDTO doctorToDoctorResponseDTO(Doctor doctor) {
        var doctorResponseDTO = new DoctorResponseDTO();

        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setName(doctor.getName());
        doctorResponseDTO.setUsername(doctor.getUsername());
        doctorResponseDTO.setPassword(doctor.getPassword());
        doctorResponseDTO.setEmail(doctor.getEmail());
        doctorResponseDTO.setGender(doctor.isGender());
        doctorResponseDTO.setCreatedAt(doctor.getCreatedAt());
        doctorResponseDTO.setUpdatedAt(doctor.getUpdatedAt());
        doctorResponseDTO.setSpecialist(doctor.getSpecialist());
        doctorResponseDTO.setYearsOfExperience(doctor.getYearsOfExperience());
        doctorResponseDTO.setFee(doctor.getFee());
        doctorResponseDTO.setSchedules(doctor.getSchedules());
        doctorResponseDTO.setAvailableDates(getAvailableDates(doctor.getId()));

        return doctorResponseDTO;
    }

    @Override
    public DoctorResponseDTO getDoctorById(UUID id) {
        // Fetch the doctor by ID
        Doctor doctor = doctorDb.findByIdAndIsDeletedFalse(id);
        if (doctor == null) {
            throw new EntityNotFoundException("Doctor not found");
        }

        // Return the updated doctor as a DTO
        return doctorToDoctorResponseDTO(doctor);
    }

    public List<LocalDate> getAvailableDates(UUID doctorId) {
        try {
            Doctor doctor = doctorDb.findByIdAndIsDeletedFalse(doctorId);
            if (doctor == null) {
                return Collections.emptyList();
            }
            List<Integer> schedules = doctor.getSchedules(); // Hari kerja dokter (1=Senin, ..., 7=Minggu)
    
            List<LocalDate> availableDates = new ArrayList<>();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusWeeks(4);
    
            // Cek jadwal kerja dan apakah sudah ada appointment
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (schedules.contains(date.getDayOfWeek().getValue())) {
                    availableDates.add(date);
                }
            }
    
            return availableDates;
        } catch (Exception e) {
            // Log the exception if necessary
            return Collections.emptyList();  // Return an empty list in case of an error
        }
    }
    

}
