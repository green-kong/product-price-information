import ContentWrapper from "../components/ContentWrapper";
import StyledButton from '../components/StyledButton';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LowestPriceByCategoriesList from '../components/LowestPriceByCategoriesList';
import HighestAndLowestPriceByCategoryList from '../components/HighestAndLowestPriceByCategoryList';

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

type PriceInformationResponse = LowestPriceByCategoryResponse | HighestAndLowestPriceByCategory;

const LOWEST_PRICE_BY_CATEGORIES = "LOWEST_PRICE_BY_CATEGORIES";
const HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY = "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY";
type RequiredStatus = "LOWEST_PRICE_BY_CATEGORIES" | "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY";

const PriceInformation = () => {
  const [status, setStatus] = useState<RequiredStatus>();

  const [priceInformationResponse, setPriceInformationResponse] = useState<PriceInformationResponse>();

  const [categoriesState, setCategoriesState] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<Category>({id: 0n, name: ''});

  useEffect(() => {
    (async () => {
      await getLowestPriceByCategory();
      await getAllCategories();
    })();
  }, []);

  const getAllCategories = async () => {
    const {data: categories}: { data: Category[] } = await axios.get('http://localhost:8080/api/categories');
    setCategoriesState(() => categories)
    setSelectedCategory(() => categories[0])
  }


  const getLowestPriceByCategory = async () => {
    const {data: lowestPriceByCategory}: { data: LowestPriceByCategoryResponse } = await axios.get('http://localhost:8080/api/price-informations/categories/lowest');
    setPriceInformationResponse(() => lowestPriceByCategory);
    setStatus(() => LOWEST_PRICE_BY_CATEGORIES);
  }

  const getHighestAndLowestPriceByCategory = async () => {
    const url = `http://localhost:8080/api/price-informations/categories/${encodeURIComponent(selectedCategory.name)}/highest-lowest`;
    const {data: highestAndLowestPriceByCategoryResponse}: { data: HighestAndLowestPriceByCategory } = await axios.get(url);
    setPriceInformationResponse(() => highestAndLowestPriceByCategoryResponse);
    setStatus(() => HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY);
  }

  const handleCategoryOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: bigint = target.value;
    const name: string = target.selectedOptions[0].text;
    setSelectedCategory(() => ({id, name}))
  };

  return (
    <ContentWrapper>
      <ButtonWrapper>
        <StyledButton onClick={getLowestPriceByCategory}>카테고리 별 최소 가격 정보 조회</StyledButton>
        <div>
          <select onChange={handleCategoryOptionChange} value={selectedCategory.id.toString()}>
            {categoriesState.map(({id, name}: Category) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </select>
          <StyledButton onClick={getHighestAndLowestPriceByCategory}>
            {selectedCategory.name} 카테고리의 최대 / 최소 가격 상품 조회
          </StyledButton>
        </div>
        <StyledButton>버튼3</StyledButton>
      </ButtonWrapper>

      {
        status == LOWEST_PRICE_BY_CATEGORIES
          ? <LowestPriceByCategoriesList data={priceInformationResponse}></LowestPriceByCategoriesList>
          : (
            status == HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY
              ?
              <HighestAndLowestPriceByCategoryList data={priceInformationResponse}></HighestAndLowestPriceByCategoryList>
              : <div></div>)
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

export default PriceInformation;
