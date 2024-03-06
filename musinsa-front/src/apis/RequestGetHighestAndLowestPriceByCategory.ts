import { axiosGetRequestTemplate } from './BaseAxios';

const HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY = "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY";

interface PriceAndBrandResponse {
  brand: string;
  price: number;
}

interface HighestAndLowestPriceByCategory {
  category: string;
  lowest: PriceAndBrandResponse;
  highest: PriceAndBrandResponse;
}

const getHighestAndLowestPriceByCategory = async (setPriceInformationResponse, setStatus, selectedCategory) => {
  const url = `/price-informations/categories/${encodeURIComponent(selectedCategory.name)}/highest-lowest`;
  await axiosGetRequestTemplate<HighestAndLowestPriceByCategory>(
    url,
    (highestAndLowestPriceByCategoryResponse: HighestAndLowestPriceByCategory) => {
      setPriceInformationResponse(() => highestAndLowestPriceByCategoryResponse);
      setStatus(() => HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY);
    })
}

export { HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY, HighestAndLowestPriceByCategory, getHighestAndLowestPriceByCategory }
