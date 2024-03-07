package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.common.adaptor.out.loader.RepositoryCategoryLoader;
import com.example.musinsaserver.common.application.port.out.loader.dto.CategoryLoadDto;
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

    @Test
    @DisplayName("이름이 일치하는 카테고리를 불러온다.")
    void findByName() {
        //given
        final String name = "아우터";
        final Category saved = categoryRepository.save(Category.createWithoutId(name));

        //when
        final CategoryLoadDto categoryLoadDto = repositoryCategoryLoader.loadCategory(name).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(categoryLoadDto.id()).isEqualTo(saved.getId());
            assertThat(categoryLoadDto.category()).isEqualTo(name);
        });
    }

    @Test
    @DisplayName("이름이 일치하는 카테고리가 없는 경우 empty를 반환한다..")
    void findByNameReturnsEmpty() {
        //when
        final Optional<CategoryLoadDto> empty = repositoryCategoryLoader.loadCategory("invalid");

        //then
        assertThat(empty).isEmpty();
    }

    @Test
    @DisplayName("아이디목록에 포함된 id와 일치하는 모든 카테고리를 조회한다.")
    void findByIds() {
        //given
        final Category outer = categoryRepository.save(Category.createWithoutId("아우터"));
        final Category top = categoryRepository.save(Category.createWithoutId("상의"));
        final Category pants = categoryRepository.save(Category.createWithoutId("바지"));
        final Category socks = categoryRepository.save(Category.createWithoutId("양말"));
        final Category sneakers = categoryRepository.save(Category.createWithoutId("스니커즈"));
        final Category accessories = categoryRepository.save(Category.createWithoutId("액세서리"));
        final List<Long> targetIds = Stream.of(outer, pants, sneakers, accessories)
                .map(Category::getId)
                .toList();

        //when
        final List<CategoryLoadDto> categoryLoadDtos = repositoryCategoryLoader.loadCategoriesByIds(targetIds);

        //then
        final List<Long> foundIds = categoryLoadDtos.stream()
                .map(CategoryLoadDto::id)
                .toList();
        assertThat(categoryLoadDtos).hasSize(targetIds.size());
        assertThat(foundIds).containsExactlyInAnyOrderElementsOf(targetIds);
    }
}
