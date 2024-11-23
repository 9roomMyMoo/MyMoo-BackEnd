package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Like;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.util.KeyValuePair;
import com.example.mymoo.domain.store.util.StoreUtil;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreListDTO {
    private long totalCount;
    private int page;
    private int size;
    private List<StoreListElement> stores;

    public StoreListDTO(List<Store> stores, List<Like> likes, int page, int size, double logt, double lat) {
        this.totalCount = stores.size();
        this.page = page;
        this.size = size;
        this.stores = stores.stream().map(store -> StoreListElement.from(store, logt, lat)).toList();
        updateLikeable(likes);
    }

    private void updateLikeable(List<Like> likes){
        for (Like like : likes) {
            Long likedStoreId = like.getStore().getId();
            for (StoreListElement storeListElement : stores) {
                if (storeListElement.getStoreId().equals(likedStoreId)){
                    storeListElement.updateLikeable(false);
                }
            }
        }
    }

    @Data
    private static class StoreListElement{
        private Long storeId;
        private String name;
        private String address;
        private String imagePath;
        private double stars;
        private int likeCount;
        private int reviewCount;
        private Long usableDonation;
        private double longitude;
        private double latitude;
        private boolean likeable;
        private Integer distance;

        public static StoreListElement from(Store store, Double logt, Double lat) {
            return StoreListElement.builder()
                    .storeId(store.getId())
                    .name(store.getName())
                    .address(store.getAddress())
                    .imagePath(store.getImagePath())
                    .stars(store.getStars())
                    .likeCount(store.getLikeCount())
                    .reviewCount(store.getReviewCount())
                    .usableDonation(store.getUsableDonation())
                    .longitude(store.getLongitude())
                    .latitude(store.getLatitude())
                    .distance(StoreUtil.calculateDistance(logt, lat, store.getLongitude(), store.getLatitude()))
                    .build();
        }

        public void updateLikeable(boolean likeable){
            this.likeable = likeable;
        }

        @Builder
        public StoreListElement(
                final Long storeId,
                final String name,
                final String address,
                final double stars,
                final String imagePath,
                final int likeCount,
                final int reviewCount,
                final Long usableDonation,
                final double longitude,
                final double latitude,
                final Integer distance
        ){
            this.storeId = storeId;
            this.name = name;
            this.address = address;
            this.imagePath = imagePath;
            this.stars = stars;
            this.likeCount = likeCount;
            this.reviewCount = reviewCount;
            this.usableDonation = usableDonation;
            this.longitude = longitude;
            this.latitude = latitude;
            this.distance = distance;
            this.likeable = true;
        }
    }
}
