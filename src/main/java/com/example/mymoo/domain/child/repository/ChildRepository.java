package com.example.mymoo.domain.child.repository;

import com.example.mymoo.domain.child.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    boolean existsByAccount_Id(Long accountId);
    Optional<Child> findByAccount_Id(Long accountId);
}
