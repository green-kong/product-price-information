import { ApiResponse, axiosDeleteRequestTemplate } from '@apis/BaseAxios';

export const requestDeleteProductById = async (id: number): Promise<ApiResponse<undefined>> => {
  const url = `/products/${id}`
  const isSuccess = await axiosDeleteRequestTemplate(url);
  if (isSuccess) {
    return {isSuccess: true, response: undefined}
  }
  return {isSuccess: false, response: undefined}
}
