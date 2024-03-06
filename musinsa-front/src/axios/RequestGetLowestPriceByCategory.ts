import { axiosGetRequestTemplate } from './BaseAxios';

const LOWEST_PRICE_BY_CATEGORIES = "LOWEST_PRICE_BY_CATEGORIES";

interface LowestPriceInformation {
  category: string;
  brand: string;
  price: number;
}

interface LowestPriceByCategoryResponse {
  lowestPriceInformationResponses: LowestPriceInformation[];
  sum: number;
}

const getLowestPriceByCategory = async (setPriceInformationResponse, setStatus) => {
  const url = '/price-informations/categories/lowest';
  await axiosGetRequestTemplate<LowestPriceByCategoryResponse>(
    url,
    (lowestPriceByCategory: LowestPriceByCategoryResponse) => {
      setPriceInformationResponse(() => lowestPriceByCategory);
      setStatus(() => LOWEST_PRICE_BY_CATEGORIES);
    })
}

export { LOWEST_PRICE_BY_CATEGORIES, LowestPriceByCategoryResponse, getLowestPriceByCategory };
