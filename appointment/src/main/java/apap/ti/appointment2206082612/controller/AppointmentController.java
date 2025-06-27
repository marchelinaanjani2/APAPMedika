package apap.ti.appointment2206082612.controller;

import apap.ti.appointment2206082612.dto.TreatmentDTO;
import apap.ti.appointment2206082612.dto.request.CreateAppointmentRequestDTO;
import apap.ti.appointment2206082612.dto.request.CreatePatientRequestDTO;
import apap.ti.appointment2206082612.dto.request.UpdateAppointmentRequestDTO;
import apap.ti.appointment2206082612.dto.response.PatientResponseDTO;
import apap.ti.appointment2206082612.model.Appointment;
import apap.ti.appointment2206082612.model.Treatment;
import apap.ti.appointment2206082612.dto.response.AppointmentResponseDTO;
import apap.ti.appointment2206082612.service.AppointmentService;
import apap.ti.appointment2206082612.service.TreatmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;

import apap.ti.appointment2206082612.restdto.response.AppointmentResponseRestDTO;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TreatmentService treatmentService;

    /**
     * View All Appointments
     */
    @GetMapping("/appointment/all")
    public String viewAllAppointments(Model model,
                                      @RequestParam(value = "search", required = false) String search,
                                      @RequestParam(value = "status", required = false) Integer status) {
        try {
            List<AppointmentResponseRestDTO> appointments = appointmentService.getAllAppointmentFromRest();

            if (search != null && !search.isEmpty()) {
                appointments = appointments.stream()
                        .filter(app -> app.getDoctorName().toLowerCase().contains(search.toLowerCase())
                                || app.getPatientName().toLowerCase().contains(search.toLowerCase()))
                        .collect(Collectors.toList());
            }

            if (status != null) {
                appointments = appointments.stream()
                        .filter(app -> app.getStatus() == status)
                        .collect(Collectors.toList());
            }

            model.addAttribute("listAppointment", appointments);
            model.addAttribute("statuses", List.of("Created", "Completed", "Cancelled")); // Adjust statuses as needed
            return "viewall-appointment"; // Ensure a Thymeleaf template with this name exists
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }


    // Fix later
    // /**
    //  * View Detail Appointment
    //  */
    // @GetMapping("/appointment/{id}")
    // public String viewDetailAppointment(@PathVariable String id, Model model) {
    //     try {
    //         Appointment appointment = appointmentService.getAppointmentById(id);
    //         if (appointment == null) {
    //             model.addAttribute("errorMessage", "Appointment not found.");
    //             return "error";
    //         }
    //         model.addAttribute("specializationNames", doctorService.getAllSpecializations());
    //         model.addAttribute("appointment", appointment);
    //         return "detail-appointment"; // Ensure a Thymeleaf template with this name exists
    //     } catch (Exception e) {
    //         model.addAttribute("errorMessage", e.getMessage());
    //         return "error";
    //     }
    // }

    // Fix later
    // /**
    //  * Flow 1 - Show Create Appointment Form for Existing Patient
    //  */
    // @GetMapping("/appointment/{nik}/create")
    // public String showCreateAppointmentFormForExistingPatient(@PathVariable("nik") String nik, Model model) {
    //     try {
    //         // Find patient by NIK
    //         Patient patient = patientService.findByNik(nik);
    //         if (patient == null) {
    //             model.addAttribute("errorMessage", "Patient with NIK " + nik + " not found.");
    //             return "error";
    //         }

    //         // Get list of doctors and their schedules
    //         List<Doctor> listDoctor = doctorService.getAllDoctors(Sort.by("name"));
    //         model.addAttribute("createAppointmentRequestDTO", new CreateAppointmentRequestDTO());
    //         model.addAttribute("listDoctor", listDoctor);
    //         model.addAttribute("nik", nik); // Add NIK to model
    //         model.addAttribute("patient", patient); // Add patient data to model

    //         // Display doctor schedules for the next 4 weeks
    //         Map<String, List<LocalDate>> doctorSchedules = new HashMap<>();
    //         for (Doctor doctor : listDoctor) {
    //             List<LocalDate> availableDates = getNextFourWeeksSchedule(doctor.getSchedules());
    //             doctorSchedules.put(doctor.getId(), availableDates);
    //         }
    //         model.addAttribute("doctorSchedules", doctorSchedules);

    //         return "create-appointment"; // Render the create-appointment.html form page
    //     } catch (Exception e) {
    //         model.addAttribute("errorMessage", "Error finding patient: " + e.getMessage());
    //         return "error"; // Render error page if exception occurs
    //     }
    // }

    // @GetMapping("/appointment/{id}/update")
    // public String updateAppointmentForm(@PathVariable String id, Model model) {
    //     // Cari appointment berdasarkan ID
    //     Appointment appointment = appointmentService.getAppointmentById(id);
    //     if (appointment == null) {
    //         model.addAttribute("errorMessage", "Appointment dengan ID " + id + " tidak ditemukan.");
    //         return "error"; // Gunakan error.html
    //     }

    //     // Cek apakah appointment dapat di-update (lebih dari satu hari sebelum jadwal appointment)
    //     LocalDate appointmentDate = appointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    //     if (LocalDate.now().plusDays(1).isAfter(appointmentDate)) {
    //         model.addAttribute("errorMessage", "Appointment tidak dapat diubah karena waktunya kurang dari satu hari.");
    //         return "error"; // Gunakan error.html
    //     }

    //     // Membuat DTO untuk update appointment
    //     UpdateAppointmentRequestDTO updateAppointmentDTO = new UpdateAppointmentRequestDTO();
    //     updateAppointmentDTO.setId(appointment.getId());
    //     updateAppointmentDTO.setDoctorId(appointment.getDoctor().getId());
    //     updateAppointmentDTO.setDate(appointment.getDate());

    //     // Siapkan data untuk dropdown dokter dan schedule
    //     List<Doctor> listDoctor = doctorService.getAllDoctors(Sort.by("name"));
    //     // Display doctor schedules for the next 4 weeks
    //     Map<String, List<LocalDate>> doctorSchedules = new HashMap<>();
    //     for (Doctor doctor : listDoctor) {
    //         List<LocalDate> availableDates = getNextFourWeeksSchedule(doctor.getSchedules());
    //         doctorSchedules.put(doctor.getId(), availableDates);
    //     }
    //     model.addAttribute("doctorSchedules", doctorSchedules);
    //     model.addAttribute("updateAppointmentDTO", updateAppointmentDTO);
    //     model.addAttribute("doctors", listDoctor);

    //     return "form-update-appointment"; // Pastikan template ini ada
    // }


    // @PostMapping("/appointment/update")
    // public String updateAppointmentSubmit(@Valid @ModelAttribute UpdateAppointmentRequestDTO updateAppointmentDTO, 
    //                                     BindingResult bindingResult, 
    //                                     Model model) {
    //     if (bindingResult.hasErrors()) {
    //         model.addAttribute("errors", bindingResult.getAllErrors().stream()
    //             .map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
    //         return "error"; // Gunakan error.html
    //     }

    //     try {
    //         // Cari appointment berdasarkan ID
    //         Appointment existingAppointment = appointmentService.getAppointmentById(updateAppointmentDTO.getId());
    //         if (existingAppointment == null) {
    //             model.addAttribute("errorMessage", "Appointment dengan ID " + updateAppointmentDTO.getId() + " tidak ditemukan.");
    //             return "error"; // Gunakan error.html
    //         }

    //         // Cek apakah appointment dapat di-update
    //         LocalDate appointmentDate = existingAppointment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    //         if (LocalDate.now().plusDays(1).isAfter(appointmentDate)) {
    //             model.addAttribute("errorMessage", "Appointment tidak dapat diubah karena waktunya kurang dari satu hari.");
    //             return "error"; // Gunakan error.html
    //         }

    //         // Update appointment
    //         appointmentService.updateAppointment(updateAppointmentDTO);

    //         model.addAttribute("responseMessage", "Berhasil mengupdate appointment.");
    //         return "success"; // Gunakan success.html
    //     } catch (Exception e) {
    //         model.addAttribute("errorMessage", "Terjadi kesalahan saat mengupdate appointment: " + e.getMessage());
    //         return "error"; // Gunakan error.html
    //     }
    // }

    @GetMapping("/appointment/{id}/note")
    public String showUpdateDiagnosisAndTreatmentForm(@PathVariable("id") String id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            model.addAttribute("errorMessage", "Appointment not found.");
            return "error";
        }
    
        // Fetch all treatments from the database
        List<Treatment> treatmentList = treatmentService.getAllTreatments();
        model.addAttribute("treatmentList", treatmentList);
        model.addAttribute("appointment", appointment);
        return "detail-appointment";  // View for displaying the form
    }
    
    @PostMapping("/appointment/note")
    public String updateDiagnosisAndTreatment(
            @ModelAttribute Appointment updatedAppointment,
            @RequestParam("treatmentIds") List<Long> treatmentIds,
            Model model) {
        
        // Fetch the existing appointment by ID
        Appointment appointment = appointmentService.getAppointmentById(updatedAppointment.getId());
        
        // Check if the appointment exists
        if (appointment == null) {
            model.addAttribute("errorMessage", "Appointment not found.");
            return "error"; // Redirect to error.html
        }

        // Update the diagnosis field
        appointment.setDiagnosis(updatedAppointment.getDiagnosis());

        // Fetch the selected treatments based on the treatmentIds from the form
        List<Treatment> selectedTreatments = treatmentService.getTreatmentsByIds(treatmentIds);
        
        // Set the selected treatments for the appointment
        appointment.setTreatments(selectedTreatments);

        // Recalculate the total fee based on the selected treatments
        long totalFee = selectedTreatments.stream().mapToLong(Treatment::getPrice).sum();
        appointment.setTotalFee(totalFee);

        // // Save the updated appointment back to the database
        // appointmentService.updateAppointment(appointment);

        // Set a success message
        model.addAttribute("responseMessage", "Successfully updated the diagnosis and treatments for appointment " + appointment.getId() + ".");
        
        // Return success page after the update
        return "success"; // Redirect to success.html
    }


    // POST - Menambah row treatment pada appointment
    @PostMapping(value = "/appointment/note", params = {"addRowUpdateAppointment"})
    public String addTreatmentRow(@ModelAttribute Appointment appointment, Model model) {
        if (appointment.getTreatments() == null) {
            appointment.setTreatments(new ArrayList<>());
        }
        
        // Add a new Treatment with default value (e.g., "New Treatment")
        Treatment newTreatment = new Treatment();
        newTreatment.setName("New Treatment"); // You can change this default value if needed
        appointment.getTreatments().add(newTreatment);

        model.addAttribute("appointment", appointment);
        return "detail-appointment"; // Return to the form with the newly added treatment row
    }

    // POST - Menghapus row treatment dari appointment
    @PostMapping(value = "/appointment/note", params = {"deleteRowUpdateAppointment"})
    public String deleteTreatmentRow(@ModelAttribute Appointment appointment, 
                                    @RequestParam("deleteRow") int row, Model model) {
        if (appointment.getTreatments() != null && row >= 0 && row < appointment.getTreatments().size()) {
            appointment.getTreatments().remove(row); // Remove the treatment row based on the index
        }

        model.addAttribute("appointment", appointment);
        return "detail-appointment"; // Return to the form with the updated list of treatments
    }


    @PostMapping("/appointment/{id}/done")
    public String markAsDone(@PathVariable String id, Model model) {
        try {
            // Ubah status appointment menjadi "Done" (1)
            appointmentService.updateAppointmentStatus(id, 1);
            model.addAttribute("responseMessage", "Appointment berhasil ditandai sebagai selesai.");
            return "redirect:/appointment/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/appointment/{id}/cancel")
    public String cancelAppointment(@PathVariable String id, Model model) {
        try {
            // Ubah status appointment menjadi "Cancelled" (2)
            appointmentService.updateAppointmentStatus(id, 2);
            model.addAttribute("responseMessage", "Appointment berhasil dibatalkan.");
            return "redirect:/appointment/" + id;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    /**
     * 8. Soft Delete Appointment - GET (Confirmation Page)
     */
    @GetMapping("/appointment/{id}/delete")
    public String deleteAppointmentForm(@PathVariable String id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            model.addAttribute("errorMessage", "Appointment not found.");
            model.addAttribute("page", "appointment");
            return "error";
        }
        model.addAttribute("appointment", appointment);
        model.addAttribute("page", "appointment");
        return "confirm-delete-appointment"; // Make sure this template exists
    }

    /**
     * 8. Soft Delete Appointment - POST
     */
    @PostMapping("/appointment/delete")
    public String deleteAppointmentSubmit(@RequestParam String id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            model.addAttribute("errorMessage", "Appointment not found.");
            model.addAttribute("page", "appointment");
            return "error";
        }
        try {
            appointmentService.deleteAppointment(id);
            model.addAttribute("responseMessage", 
                String.format("Berhasil menghapus Appointment %s.", appointment.getId()));
            model.addAttribute("page", "appointment");
            return "success"; // Use success.html for confirmation
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Terjadi kesalahan saat menghapus appointment.");
            model.addAttribute("page", "appointment");
            return "error"; // Use error.html for errors
        }
    }

    // Fix later
    // /**
    //  * Flow 2 - Handle Create Appointment Submission for Existing Patient
    //  */
    // @PostMapping("/appointment/{nik}/create")
    // @Transactional
    // public String createAppointmentForExistingPatient(
    //         @PathVariable("nik") String nik,
    //         @Valid @ModelAttribute CreateAppointmentRequestDTO createAppointmentRequestDTO,
    //         BindingResult appointmentBindingResult,
    //         @RequestParam("appointmentDateStr") String appointmentDateStr,
    //         @RequestParam("doctorId") String doctorId,
    //         Model model) {
    //     try {
    //         // Find patient by NIK
    //         Patient patient = patientService.findByNik(nik);
    //         if (patient == null) {
    //             model.addAttribute("errorMessage", "Patient with NIK " + nik + " not found.");
    //             return "error";
    //         }

    //         // Set patientId in request DTO
    //         createAppointmentRequestDTO.setPatientId(patient.getId());

    //         // Convert date from string (format "EEEE, dd MMMM yyyy") to LocalDate
    //         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    //         LocalDate parsedDate = LocalDate.parse(appointmentDateStr, formatter);

    //         // Convert LocalDate to java.util.Date
    //         Date dateToSave = java.sql.Date.valueOf(parsedDate);
    //         createAppointmentRequestDTO.setDate(dateToSave); // Set appointment date

    //         // Get doctor by doctorId
    //         Doctor doctor = doctorService.getDoctorById(doctorId);
    //         if (doctor == null) {
    //             model.addAttribute("errorMessage", "Doctor not found with ID: " + doctorId);
    //             return "error";
    //         }

    //         // Save appointment
    //         AppointmentResponseDTO createdAppointment = appointmentService.addAppointment(createAppointmentRequestDTO, doctor);

    //         // Display success message after appointment is created
    //         model.addAttribute("responseMessage", "Appointment Berhasil Dibuat dengan ID: " + createdAppointment.getId());
    //         return "success";
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         model.addAttribute("errorMessage", e.getMessage());
    //         return "error";
    //     }
    // }

    // @GetMapping("/appointment/create-with-patient")
    // public String showCreateAppointmentWithPatientForm(Model model) {
    //     try {
    //         // Get list of doctors and their schedules
    //         List<Doctor> listDoctor = doctorService.getAllDoctors(Sort.by("name"));
    //         model.addAttribute("createPatientRequestDTO", new CreatePatientRequestDTO());
    //         model.addAttribute("createAppointmentRequestDTO", new CreateAppointmentRequestDTO());
    //         model.addAttribute("listDoctor", listDoctor);

    //         // Display doctor schedules for the next 4 weeks
    //         Map<String, List<LocalDate>> doctorSchedules = new HashMap<>();
    //         for (Doctor doctor : listDoctor) {
    //             List<LocalDate> availableDates = getNextFourWeeksSchedule(doctor.getSchedules());
    //             doctorSchedules.put(doctor.getId(), availableDates);
    //         }
    //         model.addAttribute("doctorSchedules", doctorSchedules);

    //         return "create-appointment-with-patient"; // Template for patient & appointment form
    //     } catch (Exception e) {
    //         model.addAttribute("errorMessage", e.getMessage());
    //         return "error";
    //     }
    // }

    // Fix later

    private List<LocalDate> getNextFourWeeksSchedule(List<Integer> scheduleDays) {
        List<LocalDate> availableDates = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 28; i++) { // Check next 4 weeks (7 days * 4)
            LocalDate date = now.plusDays(i);
            int dayOfWeek = date.getDayOfWeek().getValue(); // Monday = 1, Sunday = 7
            if (scheduleDays.contains(dayOfWeek)) {
                availableDates.add(date);
            }
        }
        return availableDates;
    }

    // Fix later
    // /**
    //  * Flow 2 - Handle Create Appointment for New Patient Submission
    //  */
    // @PostMapping("/appointment/create-with-patient")
    // @Transactional
    // public String createAppointmentWithPatient(
    //         @Valid @ModelAttribute CreatePatientRequestDTO createPatientRequestDTO,
    //         BindingResult patientBindingResult,
    //         @Valid @ModelAttribute CreateAppointmentRequestDTO createAppointmentRequestDTO,
    //         BindingResult appointmentBindingResult,
    //         @RequestParam("appointmentDateStr") String appointmentDateStr,
    //         @RequestParam("doctorId") String doctorId,
    //         Model model) {
    //     try {
    //         // Step 1: Save new patient first
    //         PatientResponseDTO newPatient = patientService.addPatient(createPatientRequestDTO);

    //         // Step 2: Set patientId in appointment after patient is created
    //         createAppointmentRequestDTO.setPatientId(newPatient.getId());

    //         // Step 3: Convert date from string (format "EEEE, dd MMMM yyyy") to LocalDate
    //         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    //         LocalDate parsedDate = LocalDate.parse(appointmentDateStr, formatter);

    //         // Step 4: Convert LocalDate to java.util.Date
    //         Date dateToSave = java.sql.Date.valueOf(parsedDate);

    //         // Step 5: Set appointment date
    //         createAppointmentRequestDTO.setDate(dateToSave);

    //         // Step 6: Set doctorId in appointment request DTO
    //         createAppointmentRequestDTO.setDoctorId(doctorId);

    //         // Step 7: Get doctor by doctorId
    //         Doctor doctor = doctorService.getDoctorById(doctorId);

    //         // Step 8: Save appointment
    //         AppointmentResponseDTO appointment = appointmentService.addAppointment(createAppointmentRequestDTO, doctor);

    //         // Step 9: Display success message
    //         model.addAttribute("responseMessage", "Berhasil membuat appointment " + appointment.getId());
    //         return "success";
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         model.addAttribute("errorMessage", e.getMessage());
    //         return "error";
    //     }
    // }

    // Fix later
    // @GetMapping("/appointment/available-dates")
    // @ResponseBody
    // public List<String> getAvailableDates(@RequestParam("doctorId") String doctorId) {
    //     try {
    //         List<LocalDate> availableDates = doctorService.getAvailableDates(doctorId);
    //         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
    //         return availableDates.stream()
    //                 .map(date -> date.format(formatter))
    //                 .collect(Collectors.toList());
    //     } catch (Exception e) {
    //         return new ArrayList<>();
    //     }
    // }
}
