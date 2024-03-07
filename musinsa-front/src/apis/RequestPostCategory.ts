import { ApiResponse, axiosPostRequestTemplate } from '@apis/BaseAxios';

export interface RegisterCategoryRequest {
  name: string;
}

export const requestPostCategory = async (data: RegisterCategoryRequest): Promise<ApiResponse<number>> => {
  const url = '/categories';
  const id = await axiosPostRequestTemplate<RegisterCategoryRequest>(url, data);
  if (id) {
    return {isSuccess: true, response: id};
  }
  return {isSuccess: false, response: undefined};
};
