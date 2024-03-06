import React, { useState } from 'react';
import { Brand } from '@apis/RequestGetAllBrands';
import { FormInput, RegisterWrapper, StyledForm, SubmitButton, Tilte } from '@components/RegisterElements';
import { axiosPostRequestTemplate } from '@apis/BaseAxios';

interface RegisterBrandRequest {
  name: string;
}

interface BrandRegisterFormProps {
  setBrands: React.Dispatch<React.SetStateAction<Brand[]>>
}

const BrandRegisterForm = ({setBrands}: BrandRegisterFormProps) => {
  const [brand, setBrand] = useState<string>('');

  const handleBrandChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setBrand(() => value);
  }

  const handleBrandRegisterSubmit = async (event: React.FormEvent<SubmitEvent>) => {
    event.preventDefault();
    const url = '/brands';
    const data: RegisterBrandRequest = {name: brand};
    await axiosPostRequestTemplate<RegisterBrandRequest>(
      url,
      data,
      (data: RegisterBrandRequest, id: number) => {
        setBrands(brands => [...brands, {id, name: data.name}]);
        alert("브랜드를 등록하였습니다.");
      })
  };

  return (
    <RegisterWrapper>
      <Tilte>브랜드 등록</Tilte>
      <StyledForm onSubmit={handleBrandRegisterSubmit}>
        <FormInput placeholder={'브랜드 이름'} onChange={handleBrandChange}/>
        <SubmitButton>등록</SubmitButton>
      </StyledForm>
    </RegisterWrapper>
  )
};

export default BrandRegisterForm;
