package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.support.BaseTest;

class RepositoryCategoryLoaderTest extends BaseTest {

    @Autowired
    RepositoryCategoryLoader repositoryCategoryLoader;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 정보를 불러온다.")
    void loadCategory() {
        //given
        final Category category = categoryRepository.save(Category.createWithoutId("아우터"));

        //when
        final CategoryLoadDto categoryLoadDto = repositoryCategoryLoader.loadCategory(category.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(categoryLoadDto.id()).isEqualTo(category.getId());
            assertThat(categoryLoadDto.category()).isEqualTo(category.getNameValue());
        });
    }

    @Test
    @DisplayName("해당하는 카테고리 없는경우 Optional.empty를 반환한다.")
    void loadCategoryReturnEmpty() {
        //when
        final Optional<CategoryLoadDto> categoryLoadDto = repositoryCategoryLoader.loadCategory(0L);

        //then
        assertThat(categoryLoadDto).isEmpty();
    }

    @Test
    @DisplayName("카테고리의 전체갯수를 반환한다.")
    void countAllCategories() {
        //given
        categoryRepository.save(Category.createWithoutId("아우터"));
        categoryRepository.save(Category.createWithoutId("바지"));
        categoryRepository.save(Category.createWithoutId("상의"));
        categoryRepository.save(Category.createWithoutId("모자"));

        //when
        final int result = repositoryCategoryLoader.countAllCategories();

        //then
        assertThat(result).isEqualTo(4);
    }

    @Test
    @DisplayName("저장된 모든 카테고리를 불러온다.")
    void findAll() {
        //given
        categoryRepository.save(Category.createWithoutId("아우터"));
        categoryRepository.save(Category.createWithoutId("바지"));
        categoryRepository.save(Category.createWithoutId("상의"));
        categoryRepository.save(Category.createWithoutId("모자"));

        //when
        final List<CategoryLoadDto> categoryLoadDtos = repositoryCategoryLoader.loadAllCategories();

        //then
        assertThat(categoryLoadDtos).hasSize(4);
    }
}
