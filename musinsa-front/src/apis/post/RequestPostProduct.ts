import { ApiResponse, axiosPostRequestTemplate } from '@apis/BaseAxios';

export interface RegisterProductRequest {
  price: number;
  brandId: number;
  categoryId: number;
}

export const requestPostProduct = async (data: RegisterProductRequest): Promise<ApiResponse<number>> => {
  const url = '/products';
  const id = await axiosPostRequestTemplate<RegisterProductRequest>(url, data);
  if (id) {
    return {isSuccess: true, response: id};
  }
  return {isSuccess: false, response: undefined};
};
