package com.teat.teat_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic page response wrapper for paginated API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {

    private List<T> content;
    private PageMetadata metadata;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .metadata(PageMetadata.of(page))
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageMetadata {
        private Integer page;
        private Integer size;
        private Long totalElements;
        private Integer totalPages;
        private Boolean last;

        public static PageMetadata of(Page<?> page) {
            return PageMetadata.builder()
                    .page(page.getNumber())
                    .size(page.getSize())
                    .totalElements(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .last(page.isLast())
                    .build();
        }
    }
}
