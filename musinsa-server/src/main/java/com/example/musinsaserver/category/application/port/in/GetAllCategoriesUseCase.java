package com.example.musinsaserver.category.application.port.in;

import java.util.List;

import com.example.musinsaserver.category.application.port.in.dto.CategoryResponse;

public interface GetAllCategoriesUseCase {
    List<CategoryResponse> getAllCategories();

}
