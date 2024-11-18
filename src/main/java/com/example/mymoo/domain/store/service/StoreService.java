package com.example.mymoo.domain.store.service;

import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface StoreService {

    void updateStore();
    StoreListDTO getAllStoresByLocation(Double logt, Double lat, int page, int size);
    StoreListDTO getAllStoresByKeyword(String keyword, Pageable pageable);
    StoreListDTO getAllStores(Pageable pageable);
    StoreDetailDTO getStoreById(Long id);
}
