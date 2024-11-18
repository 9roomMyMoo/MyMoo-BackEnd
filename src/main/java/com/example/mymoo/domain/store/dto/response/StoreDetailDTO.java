package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Store;
import lombok.Data;

@Data
public class StoreDetailDTO {

    private Long id;
    private String name;
    private String address;
    private Integer likeCount;
    private Integer reviewCount;
    private Long allDonation;
    private Long usableDonation;

    public StoreDetailDTO(Store store){
        this.id = store.getId();
        this.name = store.getName();
        this.address = store.getAddress();
        this.likeCount = store.getLikeCount();
        this.reviewCount = store.getReviewCount();
        this.allDonation = store.getAllDonation();
        this.usableDonation = store.getUsableDonation();
    }
}
