    package org.application.security;

    import org.application.entity.Rol;
    import org.application.entity.UserEntity;
    import org.application.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.User;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import java.util.Collection;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class CustomUserDetailService implements UserDetailsService {
        private UserRepository userRepository;

        @Autowired
        public CustomUserDetailService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Nombre de usuario no encontrado"));
            return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
        }



        private Collection<GrantedAuthority> mapRolesToAuthorities(List<Rol> roles){
            return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
        }
    }
