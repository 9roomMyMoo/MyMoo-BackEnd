package com.example.mymoo.domain.store.service.Impl;

import com.example.mymoo.domain.account.entity.Account;
import com.example.mymoo.domain.account.repository.AccountRepository;
import com.example.mymoo.domain.store.dto.response.MenuListDTO;
import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import com.example.mymoo.domain.store.entity.*;
import com.example.mymoo.domain.store.repository.LikeRepository;
import com.example.mymoo.domain.store.repository.StoreRepository;
import com.example.mymoo.domain.store.service.StoreService;
import com.example.mymoo.domain.store.util.StoreUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service @Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final LikeRepository likeRepository;
    private final AccountRepository accountRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    // 위치기반으로 음식점을 조회
    public StoreListDTO getAllStoresByLocation(Double logt, Double lat, int page, int size, Long accountId){
        List<Store> foundStores = storeRepository.findAll();
        Map<Integer, Store> storeMap = new HashMap<>();
        for(Store store : foundStores){
            storeMap.put(StoreUtil.calculateDistance(logt, lat, store.getLongitude(), store.getLatitude()), store);
        }
        List<Integer> storeList = new ArrayList<>(storeMap.keySet());
        storeList.sort(Comparator.naturalOrder());
        List<Store> selectedStores = new ArrayList<>();
        for (int i=page*size ; i<page*size+size ;i++) {
            selectedStores.add(storeMap.get(storeList.get(i)));
        }
        List<Like> likes = likeRepository.findAllByAccount_Id(accountId);
        return new StoreListDTO(selectedStores, likes, page, size, logt, lat);
    }

    //keyword 를 포함하는 음식점명, 주소를 가진 음식점을 조회
    public StoreListDTO getAllStoresByKeyword(String keyword, Pageable pageable, Long accountId, Double logt, Double lat){
        Page<Store> storesFoundByKeyword = storeRepository.findAllByNameContainsOrAddressContains(keyword, keyword, pageable);
        System.out.println(keyword);
        List<Store> selectedStores = storesFoundByKeyword.stream().toList();
        List<Like> likes = likeRepository.findAllByAccount_Id(accountId);
        return new StoreListDTO(selectedStores, likes, pageable.getPageNumber(), pageable.getPageSize(), logt, lat);
    }

    //음식점 id로 음식점을 조회
    public StoreDetailDTO getStoreById(Long storeId, Long accountId){
        Store found = storeRepository.findById(storeId).orElseThrow(RuntimeException::new);
        Optional<Like> foundLike = likeRepository.findByAccount_IdAndStore_Id(accountId,storeId);
        return new StoreDetailDTO(found, foundLike.isEmpty());
    }

    //음식점 id로 메뉴를 조회
    public MenuListDTO getMenusByStoreId(Long id){
        Store found = storeRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Menu> foundMenus = found.getMenus();
        return new MenuListDTO(foundMenus);
    }

    //음식점 좋아요 수정
    public String updateStoreLikeCount(Long storeId, Long accountId){
        Optional<Like> foundLike = likeRepository.findByAccount_IdAndStore_Id(accountId,storeId);
        if (foundLike.isPresent()){
            Like like = foundLike.get();
            Store store = like.getStore();
            store.decrementLikeCount();
            storeRepository.save(store);
            likeRepository.delete(like);
            return "likeCount--";
        }else{
            Store foundStore = storeRepository.findById(storeId).orElseThrow(RuntimeException::new);
            Account foundAccount = accountRepository.findById(accountId).orElseThrow(RuntimeException::new);

            foundStore.incrementLikeCount();

            likeRepository.save(
                    Like.builder()
                    .account(foundAccount)
                    .store(foundStore)
                    .build());
            return "likeCount++";
        }
    }

}
