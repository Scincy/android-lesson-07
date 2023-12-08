package kr.easw.lesson07.service;

import kr.easw.lesson07.model.dto.UserAuthDTO;
import kr.easw.lesson07.model.dto.UserData;
import kr.easw.lesson07.model.repository.UserDataRepository;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDataService {
    private final UserDataRepository repository;

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;

    @PostConstruct
    public void init() {
        createUser(new UserData(0L, "admin", encoder.encode("admin"), true));
        createUser(new UserData(0L, "guest", encoder.encode("guest"), false));
    }

    public boolean isUserExists(String userId) {
        return repository.findUserDataByuserId(userId).isPresent();
    }

    public void createUser(UserData entity) {
        repository.save(entity);
    }

    @Nullable
    public UserAuthDTO createTokenWith(UserData userDataEntity) {
        Optional<UserData> entity = repository.findUserDataByuserId(userDataEntity.getUserId());
        if (entity.isEmpty()) throw new BadCredentialsException("Credentials invalid");

        UserData archivedEntity = entity.get();
        if (encoder.matches(userDataEntity.getPassword(), archivedEntity.getPassword()))
            return new UserAuthDTO(jwtService.generateToken(archivedEntity.getUserId()));
        throw new BadCredentialsException("Credentials invalid");
    }

    public List<String> listUsers() {
        List<UserData> userList = repository.findAll();

        return userList.stream()
                .map(UserData::getUserId)
                .toList();
    }

    public void deleteUsers(String userId) {
        Optional<UserData> entity = repository.findUserDataByuserId(userId);

        if (entity.isEmpty()) throw new BadCredentialsException("Credentials invalid");

        UserData temp = entity.get();
        try {
            repository.delete(temp);
        } catch (Exception ex) {
            System.out.println("Error While Delete Users");
        }
    }
}