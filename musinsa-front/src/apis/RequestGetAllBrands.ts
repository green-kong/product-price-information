import { ApiResponse, axiosGetRequestTemplate } from './BaseAxios';

export interface Brand {
  id: number;
  name: string;
}

export const getAllBrands = async (): Promise<ApiResponse<Brand[]>> => {
  const url = '/brands';
  const brands = await axiosGetRequestTemplate<Brand[]>(url);
  if (brands) {
    return {isSuccess: true, response: brands}
  }
  return {isSuccess: false, response: undefined}
};
