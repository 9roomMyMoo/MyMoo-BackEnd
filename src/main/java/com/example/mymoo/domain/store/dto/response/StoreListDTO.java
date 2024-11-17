package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.util.KeyValuePair;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreListDTO {
    private long totalCount;
    private List<StoreListElement> stores;

    public StoreListDTO(List<Store> stores) {
        this.totalCount = stores.size();
        this.stores = stores.stream().map(StoreListElement::new).toList();
    }

    @Data
    private class StoreListElement{
        private Long storeId;
        private String name;
        private String address;
        private int distance;
        private int likeCount;
        private int reviewCount;
        private Long usableDonation;
        private double longitude;
        private double latitude;

        public StoreListElement(Store store) {
            this.storeId = store.getId();
            this.name = store.getName();
            this.address = store.getAddress();
            this.likeCount = store.getLikeCount();
            this.reviewCount = store.getReviewCount();
            this.usableDonation = store.getUsableDonation();
            this.longitude = store.getLongitude();
            this.latitude = store.getLatitude();
        }
    }
}
