package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Like;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.util.KeyValuePair;
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

    public StoreListDTO(List<Store> stores, List<Like> likes, int page, int size) {
        this.totalCount = stores.size();
        this.page = page;
        this.size = size;
        this.stores = stores.stream().map(StoreListElement::StoreListElement).toList();
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
        private int likeCount;
        private int reviewCount;
        private Long usableDonation;
        private double longitude;
        private double latitude;
        private boolean likeable;

        public static StoreListElement StoreListElement(Store store) {
            return StoreListElement.builder()
                    .storeId(store.getId())
                    .name(store.getName())
                    .address(store.getAddress())
                    .likeCount(store.getLikeCount())
                    .usableDonation(store.getUsableDonation())
                    .longitude(store.getLongitude())
                    .latitude(store.getLatitude())
                    .build();
        }

        public void updateLikeable(boolean likeable){
            this.likeable = likeable;
        }

        @Builder
        public StoreListElement(Long storeId, String name, String address, int likeCount, int reviewCount, Long usableDonation, double longitude, double latitude) {
            this.storeId = storeId;
            this.name = name;
            this.address = address;
            this.likeCount = likeCount;
            this.reviewCount = reviewCount;
            this.usableDonation = usableDonation;
            this.longitude = longitude;
            this.latitude = latitude;
            this.likeable = true;
        }
    }
}
