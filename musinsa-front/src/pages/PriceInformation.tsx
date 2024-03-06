import ContentWrapper from "../components/ContentWrapper";
import StyledButton from '../components/StyledButton';
import axios from 'axios';
import { useEffect, useState } from 'react';
import styled from 'styled-components';
import LowestPriceByCategoriesList from '../components/LowestPriceByCategoriesList';

interface LowestPriceInformation {
  category: string;
  brand: string;
  price: number;
}

export interface LowestPriceByCategoryResponse {
  lowestPriceInformationResponses: LowestPriceInformation[];
  sum: number;
}

type PriceInformationResponse = LowestPriceByCategoryResponse;

const LOWEST_PRICE_BY_CATEGORIES = "LOWEST_PRICE_BY_CATEGORIES";
type RequiredStatus = "LOWEST_PRICE_BY_CATEGORIES" | "A";

const PriceInformation = () => {
  const [status, setStatus] = useState<RequiredStatus>();
  const [priceInformationResponse, setPriceInformationResponse] = useState<PriceInformationResponse>();

  useEffect(() => {
    (async () => {
      await getLowestPriceByCategory();
    })();
  }, []);

  const getLowestPriceByCategory = async () => {
    const {data: lowestPriceByCategory}: { data: LowestPriceByCategoryResponse } = await axios.get('http://localhost:8080/api/price-informations/categories/lowest');
    setPriceInformationResponse(() => lowestPriceByCategory);
    setStatus(LOWEST_PRICE_BY_CATEGORIES);
  }

  return <ContentWrapper>

    <ButtonWrapper>
      <StyledButton onClick={getLowestPriceByCategory}>카테고리 별 최소 가격 정보 보기</StyledButton>
      <StyledButton>버튼2</StyledButton>
      <StyledButton>버튼3</StyledButton>
    </ButtonWrapper>

    {
      status == LOWEST_PRICE_BY_CATEGORIES
        ? <LowestPriceByCategoriesList data={priceInformationResponse}></LowestPriceByCategoriesList>
        : <div></div>
    }
  </ContentWrapper>;
}

const ButtonWrapper = styled.div`
  width: 500px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
`

export default PriceInformation;
