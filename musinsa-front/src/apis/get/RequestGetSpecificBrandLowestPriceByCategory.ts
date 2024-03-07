import { ApiResponse, axiosGetRequestTemplate } from '../BaseAxios';

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

export const getSpecificBrandLowestPriceByCategory = async (brandId: number): Promise<ApiResponse<SpecificBrandLowestPriceByCategoryResponse>> => {
  const url = `/price-informations/brands/${brandId}/lowest`
  const data = await axiosGetRequestTemplate<SpecificBrandLowestPriceByCategoryResponse>(url);
  if (data) {
    return {isSuccess: true, response: data};
  }
  return {isSuccess: false, response: undefined};
}
