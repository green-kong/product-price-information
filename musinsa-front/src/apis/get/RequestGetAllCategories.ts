import { ApiResponse, axiosGetRequestTemplate } from '../BaseAxios';
import { Brand } from '@apis/get/RequestGetAllBrands';

export interface Category {
  id: number;
  name: string;
}

export const getAllCategories = async (): Promise<ApiResponse<Brand[]>> => {
  const url = '/categories';
  const categories = await axiosGetRequestTemplate<Category[]>(url);
  if (categories) {
    return {isSuccess: true, response: categories}
  }
  return {isSuccess: false, response: undefined}
};
