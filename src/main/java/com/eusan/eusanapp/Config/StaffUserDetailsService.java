package com.eusan.eusanapp.Config;

import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Repository.StaffRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffUserDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public StaffUserDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String workId) throws UsernameNotFoundException {
        Optional<Staff> staff = staffRepository.findByWorkId(workId);

        if (staff.isEmpty()) {
            throw new UsernameNotFoundException("Invalid Work ID or Password");
        }

        return new StaffUserDetails(staff.get());
    }
}
