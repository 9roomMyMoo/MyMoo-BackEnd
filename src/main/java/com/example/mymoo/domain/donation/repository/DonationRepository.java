package com.example.mymoo.domain.donation.repository;

import com.example.mymoo.domain.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long> {

}
