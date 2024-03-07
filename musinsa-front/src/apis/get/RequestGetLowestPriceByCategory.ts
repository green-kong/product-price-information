import { ApiResponse, axiosGetRequestTemplate } from '../BaseAxios';

export const LOWEST_PRICE_BY_CATEGORIES = "LOWEST_PRICE_BY_CATEGORIES";

interface LowestPriceInformation {
  category: string;
  brand: string;
  price: number;
}

export interface LowestPriceByCategoryResponse {
  lowestPriceInformationResponses: LowestPriceInformation[];
  sum: number;
}

export const getLowestPriceByCategory = async ():Promise<ApiResponse<LowestPriceByCategoryResponse>> => {
  const url = '/price-informations/categories/lowest';
  const data = await axiosGetRequestTemplate<LowestPriceByCategoryResponse>(url);
  if (data) {
    return{isSuccess:true, response: data};
  }
  return {isSuccess:false, response: undefined};
}
