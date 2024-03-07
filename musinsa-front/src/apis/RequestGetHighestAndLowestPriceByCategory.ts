import { ApiResponse, axiosGetRequestTemplate } from './BaseAxios';

export const HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY = "HIGHEST_AND_LOWEST_PRICE_BY_CATEGORY";

interface PriceAndBrandResponse {
  brand: string;
  price: number;
}

export interface HighestAndLowestPriceByCategory {
  category: string;
  lowest: PriceAndBrandResponse;
  highest: PriceAndBrandResponse;
}

export const getHighestAndLowestPriceByCategory = async (categoryName: string): Promise<ApiResponse<HighestAndLowestPriceByCategory>> => {
  const url = `/price-informations/categories/${encodeURIComponent(categoryName)}/highest-lowest`;
  const data = await axiosGetRequestTemplate<HighestAndLowestPriceByCategory>(url);
  if (data) {
    return {isSuccess: true, response: data};
  }
  else return {isSuccess:false, response: undefined};
}
