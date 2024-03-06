import { axiosGetRequestTemplate } from './BaseAxios';

export const SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY = "SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY";

interface CategoryAndPriceResponse {
  category: string;
  price: number;
}

export interface SpecificBrandLowestPriceByCategoryResponse {
  brand: string;
  lowestPriceInformationResponses: CategoryAndPriceResponse[];
  sum: number;
}

export const getSpecificBrandLowestPriceByCategory = async (selectedBrand, setPriceInformationResponse, setStatus) => {
  const url = `/price-informations/brands/${selectedBrand.id}/lowest`
  await axiosGetRequestTemplate<SpecificBrandLowestPriceByCategoryResponse>(
    url,
    (specificBrandLowestPriceByCategoryResponse: SpecificBrandLowestPriceByCategoryResponse) => {
      setPriceInformationResponse(() => specificBrandLowestPriceByCategoryResponse);
      setStatus(() => SPECIFIC_BRAND_LOWEST_PRICE_BY_CATEGORY);
    })
}
