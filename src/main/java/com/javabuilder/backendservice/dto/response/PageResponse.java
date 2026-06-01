package com.javabuilder.backendservice.dto.response;

import lombok.*;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    @Builder.Default
    private List<T> content = Collections.emptyList();
}
