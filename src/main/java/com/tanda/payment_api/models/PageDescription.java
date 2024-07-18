package com.tanda.payment_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;


@Data
@Builder
@AllArgsConstructor
public class PageDescription {

    private int page;

    private int itemsInPage;

    private long totalItems;

    private int totalPages;

    public PageDescription() {

    }

    public static PageDescription pageDescriptionFromPage(Page<?> page) {
        return PageDescription.builder()
                .itemsInPage(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .page(page.getNumber())
                .build();
    }

}
