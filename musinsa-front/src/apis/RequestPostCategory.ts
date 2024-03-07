import { axiosPostRequestTemplate } from '@apis/BaseAxios';
import React from 'react';
import { Category } from '@apis/RequestGetAllCategories';

export interface RegisterCategoryRequest {
  name: string;
}

export const requestPostCategory = async (
  data: RegisterCategoryRequest,
  setCategories: React.Dispatch<React.SetStateAction<Category[]>>
) => {
  const url = '/categories';
  await axiosPostRequestTemplate<RegisterCategoryRequest>(
    url,
    data,
    (data: RegisterCategoryRequest, id: number) => {
      setCategories(categories => [...categories, {id, name: data.name}]);
      alert("카테고리를 등록하였습니다.");
    });
};
