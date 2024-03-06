import ContentWrapper from "../components/ContentWrapper";
import StyledButton from '../components/StyledButton';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LowestPriceByCategoriesList from '../components/LowestPriceByCategoriesList';
import HighestAndLowestPriceByCategoryList from '../components/HighestAndLowestPriceByCategoryList';
import SpecificBrandLowestPriceByCategoryList from '../components/SpecificBrandLowestPriceByCategoryList';
import { axiosGetRequestTemplate } from '../axios/BaseAxios';
import { Category, getAllCategories } from '../axios/RequestGetAllCategories';
import { Brand, getAllBrands } from '../axios/RequestGetAllBrands';
import {
  getLowestPriceByCategory,
  LOWEST_PRICE_BY_CATEGORIES,
  LowestPriceByCategoryResponse
} from '../axios/RequestGetLowestPriceByCategory';
import {
  getHighestAndLowestPriceByCategory, HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY,
  HighestAndLowestPriceByCategory
} from '../axios/RequestGetHighestAndLowestPriceByCategory';
import {
  getSpecificBrandLowestPriceByCategory,
  SpecificBrandLowestPriceByCategoryResponse
} from '../axios/RequestGetSpecificBrandLowestPriceByCategory';


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
  const [selectedCategory, setSelectedCategory] = useState<Category>({id: 0n, name: ''});

  const [brandsState, setBrandsState] = useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = useState<Brand>({id: 0n, name: ''});

  useEffect(() => {
    (async () => {
      await getLowestPriceByCategory(setPriceInformationResponse, setStatus);
      await getAllCategories(setCategoriesState, setSelectedCategory);
      await getAllBrands(setBrandsState, setSelectedBrand);
    })();
  }, []);

  const handleCategoryOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: bigint = target.value;
    const name: string = target.selectedOptions[0].text;
    setSelectedCategory(() => ({id, name}))
  };

  const handleBrandOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: bigint = target.value;
    const name: string = target.selectedOptions[0].text;
    setSelectedBrand(() => ({id, name}))
  };

  const handleSpecificBrandLowestPriceByCategoryButton = () => {
    return async () => {
      await getSpecificBrandLowestPriceByCategory(selectedBrand, setPriceInformationResponse, setStatus)
    };
  }

  const handleGetLowestPriceByCategoryButton = () => {
    return async () => {
      await getLowestPriceByCategory(setPriceInformationResponse, setStatus)
    };
  }

  const handleGetHighestAndLowestPriceByCategoryButton = () => {
    return async () => {
      await getHighestAndLowestPriceByCategory(setPriceInformationResponse, setStatus, selectedCategory)
    };
  }

  return (
    <ContentWrapper>
      <ButtonWrapper>
        <StyledButton onClick={handleGetLowestPriceByCategoryButton()}>카테고리 별 최소 가격 정보 조회</StyledButton>
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
            {brandsState.map(({id, name}: Category) => {
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