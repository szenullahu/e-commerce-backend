package ch.sze.ecommerce.service;

import ch.sze.ecommerce.entity.BasketEntity;
import ch.sze.ecommerce.entity.BasketItemEntity;
import ch.sze.ecommerce.entity.dto.BasketItemResponseDTO;
import ch.sze.ecommerce.entity.dto.BasketResponseDTO;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DTOMapper {

    public BasketResponseDTO toBasketDTO(BasketEntity basket) {
        BasketResponseDTO dto = new BasketResponseDTO();
        dto.setBasketId(basket.getId());
        dto.setTotalPrice(basket.getTotalPrice());

        dto.setItems(basket.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList()));

        dto.setTotalItemsCount(basket.getItems().stream()
                .mapToInt(BasketItemEntity::getQuantity)
                .sum());

        return dto;
    }

    private BasketItemResponseDTO toItemDTO(BasketItemEntity item) {
        BasketItemResponseDTO dto = new BasketItemResponseDTO();
        dto.setItemId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPricePerUnit(item.getPrice());
        dto.setTotalLinePrice(item.getPrice() * item.getQuantity());

        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductImage(item.getProduct().getImage());
        }

        return dto;
    }
}
