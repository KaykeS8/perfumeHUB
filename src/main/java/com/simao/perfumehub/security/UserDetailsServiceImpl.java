package com.simao.perfumehub.security;

import com.simao.perfumehub.dtos.user.ChangeUserRoleRequestDto;
import com.simao.perfumehub.dtos.user.UserRoleResponseDto;
import com.simao.perfumehub.entities.User;
import com.simao.perfumehub.entities.enums.Role;
import com.simao.perfumehub.exceptions.ResourceConflictException;
import com.simao.perfumehub.exceptions.ResourceNotFoundException;
import com.simao.perfumehub.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Email: " + email));
    }

    @Transactional
    public UserRoleResponseDto changeUserRole(Long userId, ChangeUserRoleRequestDto dto) {
        log.info("Change user role...");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        User currentUser = getCurrentUser();
        if (userId.equals(currentUser.getId())) {
            throw new ResourceConflictException("You cannot change your own role");
        }

        if (user.getRole() == Role.ADMIN && !Objects.equals(dto.role(), Role.ADMIN.name()) && userRepository.countByRole(Role.ADMIN) <= 1) {
            throw new ResourceConflictException("Cannot remove the role of the last administrator");
        }

        user.setRole(Role.valueOf(dto.role()));

        User userUpdated = userRepository.save(user);

        return new UserRoleResponseDto(
                userUpdated.getId(),
                userUpdated.getRole()
        );
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalArgumentException("No authenticated user found");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
