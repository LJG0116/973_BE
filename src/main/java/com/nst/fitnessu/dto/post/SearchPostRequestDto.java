package com.nst.fitnessu.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SearchPostRequestDto {
    String area;
    String category;
    String keyword;
    String type;
}
