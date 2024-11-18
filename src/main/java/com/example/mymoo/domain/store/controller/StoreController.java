package com.example.mymoo.domain.store.controller;

import com.example.mymoo.domain.store.dto.response.StoreDetailDTO;
import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import com.example.mymoo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("")
    public ResponseEntity<StoreListDTO> getAllStore(
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "size", required = false) int size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "sortby", required = false) String sortby,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "logt", required = false) Double logt,
            @RequestParam(value = "lat", required = false) Double lat
    ){
        if (logt == null || lat == null) {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(storeService.getAllStoresByLocation(logt, lat, page, size));
        }else if(keyword != null) {
            if (sort.equals("desc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(storeService.getAllStoresByKeyword(keyword, pageable));
            }else if(sort.equals("asc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortby);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(storeService.getAllStoresByKeyword(keyword, pageable));
            }else{
                throw new RuntimeException();
            }
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(storeService.getAllStores(pageable));
        }
    }

    @GetMapping("{storeId}")
    public ResponseEntity<StoreDetailDTO> getStoreById(
            @PathVariable("storeId") Long id
    ){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.get
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreById(id));
    }


    @GetMapping("{storeId}/menus")
    public ResponseEntity

}
