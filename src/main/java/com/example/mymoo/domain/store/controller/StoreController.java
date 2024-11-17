package com.example.mymoo.domain.store.controller;

import com.example.mymoo.domain.store.dto.response.StoreListDTO;
import com.example.mymoo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("")
    public StoreListDTO getAllStore(
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "size", required = false) int size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "sortby", required = false) String sortby,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "logt", required = false) Double logt,
            @RequestParam(value = "lat", required = false) Double lat
    ){
        if (logt == null || lat == null) {

            return storeService.getAllStoresByLocation(logt, lat, page, size);
        }else if(keyword != null) {
            if (sort.equals("desc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
                return storeService.getAllStoresByKeyword(keyword, pageable);
            }else if(sort.equals("asc")){
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortby);
                return storeService.getAllStoresByKeyword(keyword, pageable);
            }else{
                throw new RuntimeException();
            }
        }else{
            Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, sortby);
            return storeService.getAllStores(pageable);
        }
    }
}
