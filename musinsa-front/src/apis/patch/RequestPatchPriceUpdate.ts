import { ApiResponse, axiosPatchProductPriceRequestTemplate } from '@apis/BaseAxios';

export interface PriceUpdateRequest {
  price: number;
}

export const updatePrice = async (id: number, price: number): Promise<ApiResponse<undefined>> => {
  const url = `/products/${id}`
  const result = await axiosPatchProductPriceRequestTemplate<PriceUpdateRequest>(url, {price});
  if (result) {
    return {isSuccess: true, response: undefined};
  }
  return {isSuccess: false, response: undefined};
}
