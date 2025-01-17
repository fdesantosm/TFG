    package org.application.security;

    import org.application.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    @Service
    public class CustomUserDetailService implements UserDetailsService {
        private UserRepository userRepository;

        @Autowired
        public CustomUserDetailService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return userRepository.findByUsername(username).orElseThrow(
                    () -> new UsernameNotFoundException("Nombre de usuario no encontrado"));
        }

    }
