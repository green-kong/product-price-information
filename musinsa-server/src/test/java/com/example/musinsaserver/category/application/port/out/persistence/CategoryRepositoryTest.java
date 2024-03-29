package com.example.musinsaserver.category.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.support.BaseTest;

class CategoryRepositoryTest extends BaseTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리를 저장한다.")
    void save() {
        //given
        final String categoryName = "아우터";
        final Category category = Category.createWithoutId(categoryName);

        //when
        final Category savedCategory = categoryRepository.save(category);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedCategory.getId()).isNotNull();
            assertThat(savedCategory.getNameValue()).isEqualTo(categoryName);
        });
    }

    @Test
    @DisplayName("id가 일치하는 카테고리를 조회한다.")
    void findById() {
        //given
        final String categoryName = "아우터";
        final Category category = Category.createWithoutId(categoryName);
        final Category savedCategory = categoryRepository.save(category);

        //when
        final Category found = categoryRepository.findById(savedCategory.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(savedCategory.getId());
            assertThat(found.getNameValue()).isEqualTo(categoryName);
        });
    }

    @Test
    @DisplayName("id가 일치하는 카테고리가 없는 경우 Optional.empty를 반환한다.")
    void findByIdReturnEmpty() {
        //when
        final Optional<Category> found = categoryRepository.findById(0L);

        //then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("저장된 카테고리의 갯수를 반환한다.")
    void countAll() {
        //given
        categoryRepository.save(Category.createWithoutId("아우터"));
        categoryRepository.save(Category.createWithoutId("상의"));
        categoryRepository.save(Category.createWithoutId("바지"));
        categoryRepository.save(Category.createWithoutId("양말"));
        categoryRepository.save(Category.createWithoutId("스니커즈"));
        categoryRepository.save(Category.createWithoutId("액세서리"));

        //when
        final int countAll = categoryRepository.countAll();

        //then
        assertThat(countAll).isEqualTo(6);
    }

    @Test
    @DisplayName("모든 카테고리를 반환한다.")
    void findAll() {
        //given
        categoryRepository.save(Category.createWithoutId("아우터"));
        categoryRepository.save(Category.createWithoutId("상의"));
        categoryRepository.save(Category.createWithoutId("바지"));
        categoryRepository.save(Category.createWithoutId("양말"));
        categoryRepository.save(Category.createWithoutId("스니커즈"));
        categoryRepository.save(Category.createWithoutId("액세서리"));

        //when
        final List<Category> all = categoryRepository.findAll();

        //then
        assertThat(all).hasSize(6);
    }

    @Test
    @DisplayName("이름으로 카테고리를 조회한다.")
    void findByName() {
        //given
        final String name = "아우터";
        final Category save = categoryRepository.save(Category.createWithoutId(name));

        //when
        final Category found = categoryRepository.findByName(name).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(save.getId());
            assertThat(found.getNameValue()).isEqualTo(name);
        });
    }

    @Test
    @DisplayName("일치하는 이름의 카테고리가 없는 경우 Optional.empty를 반환한다.")
    void findByNameReturnEmpty() {
        //when
        final Optional<Category> empty = categoryRepository.findByName("invalidName");

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
        final List<Category> categories = categoryRepository.findByIds(targetIds);

        //then
        final List<Long> foundIds = categories.stream()
                .map(Category::getId)
                .toList();
        assertThat(categories).hasSize(targetIds.size());
        assertThat(foundIds).containsExactlyInAnyOrderElementsOf(targetIds);
    }
}
