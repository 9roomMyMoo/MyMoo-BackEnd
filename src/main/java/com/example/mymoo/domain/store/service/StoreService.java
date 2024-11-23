package com.example.mymoo.domain.store.service;

import com.example.mymoo.domain.store.dto.response.MenuListDTO;
import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface StoreService {

    StoreListDTO getAllStoresByLocation(Double logt, Double lat, int page, int size, Long accountId);
    StoreListDTO getAllStoresByKeyword(String keyword, Pageable pageable, Long accountId, Double logt, Double lat);
    StoreDetailDTO getStoreById(Long storeid, Long accountId);
    MenuListDTO getMenusByStoreId(Long id);
    String updateStoreLikeCount(Long storeId, Long accountId);
}
