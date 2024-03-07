import { ApiResponse, axiosGetRequestTemplate } from '@apis/BaseAxios';

export interface Product {
  id: number;
  price: number;
  brand: string;
  category: string;
}

export const getAllProducts = async (): Promise<ApiResponse<Product[]>> => {
  const url = '/products'
  const products = await axiosGetRequestTemplate<Product[]>(url);
  if (products) {
    return {isSuccess: true, response: products};
  }
  return {isSuccess: false, response: undefined};
}
