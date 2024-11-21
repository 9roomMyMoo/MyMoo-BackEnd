package com.example.mymoo.domain.child.controller;


import com.example.mymoo.domain.child.dto.request.ChildReqeustDTO;
import com.example.mymoo.domain.child.dto.response.ChildResponseDTO;
import com.example.mymoo.domain.child.entity.Child;
import com.example.mymoo.domain.child.repository.ChildRepository;
import com.example.mymoo.domain.child.service.ChildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    @Operation(
            summary = "[아동] 아동정보 생성",
            description = "account 가 아동회원일 경우 아동에 대한 정보를 추가하는 api 입니다. account signUp 후에 accountId 값과 cardNumber 를 requestBody 에 보내주시고 cardNumber 는 따로 검증하지 않는 String 으로 보내주시면 됩니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "아동 정보생성 성공"),
            }
    )
    @PostMapping("")
    public ResponseEntity<ChildResponseDTO> createChild(
            @RequestBody ChildReqeustDTO request) {
        Child newChild = childService.createChild(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ChildResponseDTO(HttpStatus.CREATED, newChild, "tb_child row++"));
    }

}
