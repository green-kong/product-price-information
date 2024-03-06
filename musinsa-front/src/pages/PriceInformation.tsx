import ContentWrapper from "../components/ContentWrapper";
import StyledButton from '../components/StyledButton';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LowestPriceByCategoriesList from '../components/LowestPriceByCategoriesList';
import HighestAndLowestPriceByCategoryList from '../components/HighestAndLowestPriceByCategoryList';
import SpecificBrandLowestPriceByCategoryList from '../components/SpecificBrandLowestPriceByCategoryList';

interface PriceAndBrandResponse {
  brand: string;
  price: number;
}

export interface HighestAndLowestPriceByCategory {
  category: string;
  lowest: PriceAndBrandResponse;
  highest: PriceAndBrandResponse;
}

interface Category {
  id: bigint;
  name: string;
}

interface LowestPriceInformation {
  category: string;
  brand: string;
  price: number;
}

export interface LowestPriceByCategoryResponse {
  lowestPriceInformationResponses: LowestPriceInformation[];
  sum: number;
}

type PriceInformationResponse =
  LowestPriceByCategoryResponse
  | HighestAndLowestPriceByCategory
  | SpecificBrandLowestPriceByCategoryResponse;

const LOWEST_PRICE_BY_CATEGORIES = "LOWEST_PRICE_BY_CATEGORIES";
const HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY = "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY";
const SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY = "SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY";
type RequiredStatus =
  "LOWEST_PRICE_BY_CATEGORIES"
  | "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY"
  | "SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY";

interface Brand {
  id: bigint;
  name: string;
}

interface CategoryAndPriceResponse {
  category: string;
  price: number;
}

export interface SpecificBrandLowestPriceByCategoryResponse {
  brand: string;
  lowestPriceInformationResponses: CategoryAndPriceResponse[];
  sum: number;
}


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
      await getLowestPriceByCategory();
      await getAllCategories();
      await getAllBrands();
    })();
  }, []);

  const getAllCategories = async () => {
    const url = 'http://localhost:8080/api/categories';
    const {data: categories}: { data: Category[] } = await axios.get(url);
    setCategoriesState(() => categories)
    setSelectedCategory(() => categories[0])
  }


  const getAllBrands = async () => {
    const url = 'http://localhost:8080/api/brands'
    const {data: brands}: { data: Brand[] } = await axios.get(url);
    setBrandsState(() => brands);
    setSelectedBrand(() => brands[0])
  }

  const getLowestPriceByCategory = async () => {
    const url = 'http://localhost:8080/api/price-informations/categories/lowest';
    const {data: lowestPriceByCategory}: { data: LowestPriceByCategoryResponse } = await axios.get(url);
    setPriceInformationResponse(() => lowestPriceByCategory);
    setStatus(() => LOWEST_PRICE_BY_CATEGORIES);
  }

  const getHighestAndLowestPriceByCategory = async () => {
    const url = `http://localhost:8080/api/price-informations/categories/${encodeURIComponent(selectedCategory.name)}/highest-lowest`;
    const {data: highestAndLowestPriceByCategoryResponse}: { data: HighestAndLowestPriceByCategory } = await axios.get(url);
    setPriceInformationResponse(() => highestAndLowestPriceByCategoryResponse);
    setStatus(() => HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY);
  }

  const getSpecificBrandLowestPriceByCategory = async () => {
    const url = `http://localhost:8080/api/price-informations/brands/${selectedBrand.id}/lowest`
    const {data: specificBrandLowestPriceByCategoryResponse}: { data: SpecificBrandLowestPriceByCategoryResponse } = await axios.get(url);
    setPriceInformationResponse(() => specificBrandLowestPriceByCategoryResponse);
    setStatus(() => SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY);
  }

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

  return (
    <ContentWrapper>
      <ButtonWrapper>
        <StyledButton onClick={getLowestPriceByCategory}>카테고리 별 최소 가격 정보 조회</StyledButton>
        <ButtonAndSelectOptionWrapper>
          <StyledSelect onChange={handleCategoryOptionChange} value={selectedCategory.id.toString()}>
            {categoriesState.map(({id, name}: Category) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </StyledSelect>
          <StyledButton onClick={getHighestAndLowestPriceByCategory}>
            {selectedCategory.name} 카테고리의 최대 / 최소 가격 조회
          </StyledButton>
        </ButtonAndSelectOptionWrapper>
        <ButtonAndSelectOptionWrapper>
          <StyledSelect onChange={handleBrandOptionChange} value={selectedBrand.id.toString()}>
            {brandsState.map(({id, name}: Category) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </StyledSelect>
          <StyledButton onClick={getSpecificBrandLowestPriceByCategory}>{selectedBrand.name} 브랜드의 카테고리별 최소 가격
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
