package kr.easw.lesson07.model.repository;

import kr.easw.lesson07.model.dto.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findUserDataByuserId(String userId
    );
}