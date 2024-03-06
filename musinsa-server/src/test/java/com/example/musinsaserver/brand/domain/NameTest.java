package com.example.musinsaserver.brand.domain;

import static com.example.musinsaserver.brand.domain.Name.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.musinsaserver.brand.exception.InvalidBrandNameException;

class NameTest {

    @Test
    @DisplayName("Name을 생성한다.")
    void createName() {
        //given
        final String brandName = "A";

        //when
        final Name name = from(brandName);

        //then
        assertThat(name.getName()).isEqualTo(brandName);
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 값으로 Name을 생성하면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", ""})
    void createNameWithInvalidValue(final String invalidValue) {
        //when & then
        assertThatThrownBy(() -> from(invalidValue))
                .isInstanceOf(InvalidBrandNameException.class)
                .hasMessageContaining("브랜드의 이름은 한글자 이상이어야 합니다.");
    }
}
