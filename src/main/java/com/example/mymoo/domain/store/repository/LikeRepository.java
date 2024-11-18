package com.example.mymoo.domain.store.repository;

import com.example.mymoo.domain.store.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByAccountIdAndStoreId(Long accountId, Long storeId);
    List<Like> findAllByAccountId(Long accountId);
}
