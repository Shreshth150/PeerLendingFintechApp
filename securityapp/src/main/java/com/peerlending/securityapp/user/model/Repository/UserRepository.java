package com.peerlending.securityapp.user.model.Repository;

import com.peerlending.securityapp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , String> {

}
