package com.example.mymoo.domain.store.controller;

import com.example.mymoo.domain.store.dto.response.MenuListDTO;
import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import com.example.mymoo.domain.store.dto.response.StoreResponseDTO;
import com.example.mymoo.domain.store.repository.StoreRepository;
import com.example.mymoo.domain.store.service.StoreService;
import com.example.mymoo.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;

    @GetMapping("")
    public ResponseEntity<StoreListDTO> getAllStore(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "asc") String sort,
            @RequestParam(value = "sortby", required = false, defaultValue = "name") String sortby,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "logt", required = false) Double logt,
            @RequestParam(value = "lat", required = false) Double lat
    ){
        Long accountId = userDetails.getAccountId();
        System.out.println(accountId);
        if (logt != null && lat != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(storeService.getAllStoresByLocation(logt, lat, page, size, accountId));
        }else if(keyword != null) {
            System.out.println(keyword);
            if (sort.equals("desc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(storeService.getAllStoresByKeyword(keyword, pageable, accountId));
            }else if(sort.equals("asc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortby);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(storeService.getAllStoresByKeyword(keyword, pageable, accountId));
            }else{
                throw new RuntimeException();
            }
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(storeService.getAllStores(pageable, accountId));
        }
    }

    @GetMapping("{storeId}")
    public ResponseEntity<StoreDetailDTO> getStoreById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("storeId") Long storeId
    ){
        Long accountId = userDetails.getAccountId();
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreById(storeId, accountId));
    }

    @GetMapping("{storeId}/menus")
    public ResponseEntity<MenuListDTO> getMenusByStoreId(
            @PathVariable("storeId") Long id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getMenusByStoreId(id));
    }

    @PatchMapping("{storeId}")
    public ResponseEntity<StoreResponseDTO> updateStoreLikeCount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("storeId") Long id
    ){
        Long accountId = userDetails.getAccountId();
        String result = storeService.updateStoreLikeCount(id, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new StoreResponseDTO(HttpStatus.NO_CONTENT, storeRepository.findById(id).get(), result)
        );
    }
}
