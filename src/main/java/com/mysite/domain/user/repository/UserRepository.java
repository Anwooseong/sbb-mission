package com.mysite.domain.user.repository;

import com.mysite.domain.user.dto.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
}
