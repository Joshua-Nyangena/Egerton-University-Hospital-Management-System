package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Config.StaffUserDetails;
import com.eusan.eusanapp.Entity.Role;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Service.AdminService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
public String adminDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    model.addAttribute("staffList", adminService.getAllStaff());
    return "admin_dashboard";
}


    @GetMapping("/staffInterface")
    public String staffdatInterface() {
        return "fragments/staffInterface :: content";
    }

    @GetMapping("/addStaff")
    public String showAddStaffForm(Model model) {
        model.addAttribute("staff", new Staff());
        model.addAttribute("roles", Role.values());
        return "fragments/register_staff :: content";
    }

    @PostMapping("/saveStaff")
public String saveStaff(@Valid @ModelAttribute("staff") Staff staff, 
                        BindingResult bindingResult, 
                        Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("staff", staff);
        model.addAttribute("roles", Role.values());
        return "fragments/register_staff :: content"; 
    }

    adminService.registerStaff(
        staff.getName(),
        staff.getContact(),
        staff.getNationalId(),
        staff.getContractExpiryDate(), 
        staff.getPersonalEmail(), // 🛠 Correct this!
        staff.getRole()
    );
    return "redirect:/admin/staffList";
}

    

    @GetMapping("/staffList")
    public String viewStaffList(@RequestParam(defaultValue = "all") String sortType, Model model) {
        List<Staff> staffList;

        if ("date".equals(sortType)) {
            staffList = adminService.getStaffSortedByDate();
        } else {
            staffList = adminService.getAllStaff();
        }

        model.addAttribute("staffList", staffList);
        return "fragments/staff_details :: content";
    }

    @GetMapping("/searchStaff")
    public String searchStaff(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Staff> staffList;
        if (keyword != null && !keyword.isEmpty()) {
            staffList = adminService.searchStaff(keyword);
        } else {
            staffList = adminService.getAllStaff();
        }
        model.addAttribute("staffList", staffList);
        model.addAttribute("keyword", keyword); 
        return "fragments/staff_details :: content";
    }

    @GetMapping("/editStaff/{id}")
    public String editStaff(@PathVariable Long id, Model model) {
        Staff staff = adminService.getStaffById(id);
        model.addAttribute("staff", staff);
        model.addAttribute("roles", Role.values());
        return "fragments/edit_staff :: content";
    }
   @PostMapping("/updateStaff")
public String updateStaff(@Valid @ModelAttribute("staff") Staff staff, 
                          BindingResult bindingResult, 
                          Model model) {
    
    if (bindingResult.hasErrors()) {
        // Load the admin dashboard with sidebar and pass the edit staff fragment
        model.addAttribute("staff", staff);
        model.addAttribute("roles", Role.values());
        model.addAttribute("contentFragment", "fragments/edit_staff :: content");
        return "admin_dashboard";  // Ensure Thymeleaf includes the edit form inside dashboard
    }

    // If no errors, update and redirect
    adminService.updateStaff(staff);
    return "redirect:/admin/staffList";
}

    


    @GetMapping("/deleteStaff/{id}")
    public String deleteStaff(@PathVariable Long id) {
        adminService.deleteStaff(id);
        return "redirect:/admin/dashboard"; 
    }

    @GetMapping("/resetStaff/{id}")
    public String resetStaff(@PathVariable Long id) {
        adminService.resetStaffDetails(id);
        return "redirect:/admin/dashboard";
    }
}
