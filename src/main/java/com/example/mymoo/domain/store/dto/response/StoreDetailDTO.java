package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;

@Data
public class StoreDetailDTO {

    private Long id;
    private String name;
    private String address;
    private Double stars;
    private String imagePath;
    private Integer likeCount;
    private Integer reviewCount;
    private Long allDonation;
    private Long usableDonation;
    private boolean likeable;

    public StoreDetailDTO(Store store, boolean likeable){
        this.id = store.getId();
        this.name = store.getName();
        this.stars = store.getStars();
        this.imagePath = store.getImagePath();
        this.address = store.getAddress();
        this.likeCount = store.getLikeCount();
        this.reviewCount = store.getReviewCount();
        this.allDonation = store.getAllDonation();
        this.usableDonation = store.getUsableDonation();
        this.likeable = likeable;
    }
}
