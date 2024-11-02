package com.example.mymoo.domain.store.controller;

import com.example.mymoo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

}
