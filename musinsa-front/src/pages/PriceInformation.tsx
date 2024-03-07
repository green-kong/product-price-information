import ContentWrapper from "@components/ContentWrapper";
import StyledButton from '@components/StyledButton';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LowestPriceByCategoriesList from '@components/LowestPriceByCategoriesList';
import HighestAndLowestPriceByCategoryList from '@components/HighestAndLowestPriceByCategoryList';
import SpecificBrandLowestPriceByCategoryList from '@components/SpecificBrandLowestPriceByCategoryList';
import { Category, getAllCategories } from '@apis/RequestGetAllCategories';
import { Brand, getAllBrands } from '@apis/RequestGetAllBrands';
import {
  getLowestPriceByCategory,
  LOWEST_PRICE_BY_CATEGORIES,
  LowestPriceByCategoryResponse
} from '@apis/RequestGetLowestPriceByCategory';
import {
  getHighestAndLowestPriceByCategory,
  HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY,
  HighestAndLowestPriceByCategory
} from '@apis/RequestGetHighestAndLowestPriceByCategory';
import {
  getSpecificBrandLowestPriceByCategory,
  SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY,
  SpecificBrandLowestPriceByCategoryResponse
} from '@apis/RequestGetSpecificBrandLowestPriceByCategory';


type PriceInformationResponse =
  LowestPriceByCategoryResponse
  | HighestAndLowestPriceByCategory
  | SpecificBrandLowestPriceByCategoryResponse;

type RequiredStatus =
  "LOWEST_PRICE_BY_CATEGORIES"
  | "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY"
  | "SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY";

const PriceInformation = () => {
  const [status, setStatus] = useState<RequiredStatus>(LOWEST_PRICE_BY_CATEGORIES);

  const [priceInformationResponse, setPriceInformationResponse] = useState<PriceInformationResponse>({
    lowestPriceInformationResponses: [],
    sum: 0
  });

  const [categoriesState, setCategoriesState] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<Category>({id: 0, name: ''});

  const [brandsState, setBrandsState] = useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = useState<Brand>({id: 0, name: ''});

  useEffect(() => {
    (async () => {
      await setLowestPriceByCategory();
      await setCategories();
      await setBrands();
    })();
  }, []);

  const setBrands = async () => {
    const brandsResponse = await getAllBrands();
    if (brandsResponse.isSuccess) {
      const {response: brands} = brandsResponse;
      setBrandsState(() => brands);
      setSelectedBrand(() => brands[0])
    }
  }

  const setCategories = async () => {
    const categoriesResponse = await getAllCategories();
    if (categoriesResponse.isSuccess) {
      const {response: categories} = categoriesResponse;
      setCategoriesState(() => categories);
      setSelectedCategory(() => categories[0])
    }
  }

  const handleCategoryOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: number = Number(target.value);
    const name: string = target.selectedOptions[0].text;
    setSelectedCategory(() => ({id, name}))
  };

  const handleBrandOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: number = Number(target.value);
    const name: string = target.selectedOptions[0].text;
    setSelectedBrand(() => ({id, name}))
  };

  const handleSpecificBrandLowestPriceByCategoryButton = () => {
    return async () => {
      const result = await getSpecificBrandLowestPriceByCategory(selectedBrand.id);
      if (result.isSuccess) {
        setPriceInformationResponse(() => result.response);
        setStatus(() => SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY);
      }
    };
  }

  const setLowestPriceByCategory = async () =>{
    const result = await getLowestPriceByCategory();
    if (result.isSuccess) {
      setPriceInformationResponse(() => result.response);
      setStatus(() => LOWEST_PRICE_BY_CATEGORIES);
    }
  }

  const handleGetHighestAndLowestPriceByCategoryButton = () => {
    return async () => {
      const result = await getHighestAndLowestPriceByCategory(selectedCategory.name);
      if (result.isSuccess) {
        setPriceInformationResponse(() => result.response);
        setStatus(() => HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY);
      }
    };
  }

  return (
    <ContentWrapper>
      <ButtonWrapper>
        <StyledButton onClick={setLowestPriceByCategory}>카테고리 별 최소 가격 정보 조회</StyledButton>
        <ButtonAndSelectOptionWrapper>
          <StyledSelect onChange={handleCategoryOptionChange} value={selectedCategory.id.toString()}>
            {categoriesState.map(({id, name}: Category) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </StyledSelect>
          <StyledButton
            onClick={handleGetHighestAndLowestPriceByCategoryButton()}>
            {selectedCategory.name} 카테고리의 최대 / 최소 가격 조회
          </StyledButton>
        </ButtonAndSelectOptionWrapper>
        <ButtonAndSelectOptionWrapper>
          <StyledSelect onChange={handleBrandOptionChange} value={selectedBrand.id.toString()}>
            {brandsState.map(({id, name}: Brand) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </StyledSelect>
          <StyledButton
            onClick={handleSpecificBrandLowestPriceByCategoryButton()}>{selectedBrand.name} 브랜드의
            카테고리별 최소 가격
            조회</StyledButton>
        </ButtonAndSelectOptionWrapper>
      </ButtonWrapper>

      {
        status == LOWEST_PRICE_BY_CATEGORIES
          ? <LowestPriceByCategoriesList data={priceInformationResponse}></LowestPriceByCategoriesList>
          : (
            status == HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY
              ?
              <HighestAndLowestPriceByCategoryList data={priceInformationResponse}></HighestAndLowestPriceByCategoryList>
              : <SpecificBrandLowestPriceByCategoryList
                data={priceInformationResponse}></SpecificBrandLowestPriceByCategoryList>)
      }
    </ContentWrapper>
  );
}

const ButtonWrapper = styled.div`
  width: 800px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
`

const StyledSelect = styled.select`
  width: 80px;
`

const ButtonAndSelectOptionWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`

export default PriceInformation;
