import { axiosPostRequestTemplate } from '@apis/BaseAxios';
import React from 'react';
import { Brand } from '@apis/RequestGetAllBrands';

export interface RegisterBrandRequest {
  name: string;
}


export const requestPostBrand = async (data: RegisterBrandRequest, setBrands: React.Dispatch<React.SetStateAction<Brand[]>>) => {
  const url = '/brands';
  await axiosPostRequestTemplate<RegisterBrandRequest>(
    url,
    data,
    (data: RegisterBrandRequest, id: number) => {
      setBrands(brands => [...brands, {id, name: data.name}]);
      alert("브랜드를 등록하였습니다.");
    })
}
