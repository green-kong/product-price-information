import React, { useState } from 'react';
import { Brand } from '@apis/RequestGetAllBrands';
import { FormInput, RegisterWrapper, StyledForm, SubmitButton, Tilte } from '@components/RegisterElements';
import { RegisterBrandRequest, requestPostBrand } from '@apis/RequestPostBrand';


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
    const data: RegisterBrandRequest = {name: brand};
    await requestPostBrand(data, setBrands);
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
