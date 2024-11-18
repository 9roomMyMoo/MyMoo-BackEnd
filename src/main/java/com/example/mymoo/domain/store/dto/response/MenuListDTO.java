package com.example.mymoo.domain.store.dto.response;

import com.example.mymoo.domain.store.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class MenuListDTO {

    long total_count;
    List<MenuDetailDTO> menus;

    public MenuListDTO(List<Menu> menus) {
        this.total_count = menus.size();
        this.menus = menus.stream().map(MenuDetailDTO::new).toList();
    }

    @Data
    public static class MenuDetailDTO{
        private Long id;
        private String name;
        private String imagePath;
        private String description;
        private int price;

        public MenuDetailDTO(Menu menu) {
            this.id = menu.getId();
            this.name = menu.getName();
            this.imagePath = menu.getImagePath();
            this.description = menu.getDescription();
            this.price = menu.getPrice();
        }
    }
}
