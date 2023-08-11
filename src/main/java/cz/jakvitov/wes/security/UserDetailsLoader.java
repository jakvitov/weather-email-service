package cz.jakvitov.wes.security;

import cz.jakvitov.wes.persistence.entity.UserEntity;
import cz.jakvitov.wes.persistence.repository.UserRepository;
import cz.jakvitov.wes.persistence.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsLoader implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity> users = this.userRepository.findAllByEmail(username);
        if (users.isEmpty()){
            throw new UsernameNotFoundException(username + " not found.");
        }
        if (users.size() > 1){
            throw new RuntimeException("Multiple users for email found!");
        }
        return new UserDeatilsImpl(users.get(0));
    }
}
