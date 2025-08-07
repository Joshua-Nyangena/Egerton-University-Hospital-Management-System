package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Entity.Medication;
import com.eusan.eusanapp.Service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/medication")
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

      // Display the form for adding a new medication
      @GetMapping("/addMedication")
      public String showAddMedicationForm(Model model) {
          model.addAttribute("medication", new Medication());
          return "fragments/add_medication :: content";
      }
  
      // Process adding new medication
      @PostMapping("/saveMedication")
      public String saveMedication(@ModelAttribute Medication medication, Model model) {
          medicationService.saveMedication(medication);
          model.addAttribute("medications", medicationService.getAllMedications());
          return "redirect:/pharmacy/dashboard"; // Returns the fragment
      }
      


    // Add this inside MedicationController
@PostMapping("/recordDelivery")
public String recordDrugDelivery(@RequestParam Long medicationId, @RequestParam int quantityDelivered) {
    medicationService.recordDrugDelivery(medicationId, quantityDelivered);
    return "redirect:pharmacy/dashboard";
}

    // View inventory
    @GetMapping("/inventory")
    public String viewInventory(Model model) {
        List<Medication> medications = medicationService.getAllMedications();
        model.addAttribute("medications", medications);
        return "fragments/medication_inventory :: content"; // Thymeleaf template name
    }

    // Issue medication
    @PostMapping("/issueMedication")
    public String issueMedication(@RequestParam String medicationName) {
        medicationService.issueMedication(medicationName);
        return "redirect:/pharmacy/dashboard";
    }

    // Request restock
    @PostMapping("/requestRestock/{medicationId}")
    public String requestRestock(@PathVariable Long medicationId) {
        medicationService.requestRestock(medicationId);
        return "redirect:/pharmacy/dashboard";
    }
    @GetMapping("/inventory/meds")
    public String viewInventory(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Medication> medications;

        if (keyword != null && !keyword.isEmpty()) {
            medications = medicationService.searchMedications(keyword);
        } else {
            medications = medicationService.getAllMedications();
        }
        
        model.addAttribute("medications", medications);
        model.addAttribute("keyword", keyword);
        return "fragments/medication_inventory :: content"; // Ensure your HTML is named medication_inventory.html
    }
}
