import { ApiResponse, axiosPostRequestTemplate } from '@apis/BaseAxios';

export interface RegisterBrandRequest {
  name: string;
}

export const requestPostBrand = async (data: RegisterBrandRequest): Promise<ApiResponse<number>> => {
  const url = '/brands';
  const id = await axiosPostRequestTemplate<RegisterBrandRequest>(url, data);
  if (id) {
    return {isSuccess: true, response: id};
  }
  return {isSuccess: false, response: undefined};
}
