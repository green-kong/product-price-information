import React, { useState } from 'react';
import { Brand } from '@apis/get/RequestGetAllBrands';
import { FormInput, RegisterWrapper, StyledForm, SubmitButton, Tilte } from '@components/RegisterElements';
import { RegisterBrandRequest, requestPostBrand } from '@apis/post/RequestPostBrand';


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
    const result = await requestPostBrand(data);
    if (result.isSuccess) {
      setBrands(brands => [...brands, {id: result.response, name: data.name}]);
      alert("브랜드를 등록하였습니다.");
    }
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
