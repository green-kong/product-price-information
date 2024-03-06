import { FormInput, RegisterWrapper, StyledForm, SubmitButton, Tilte } from '@components/RegisterElements';
import React, { useState } from 'react';
import { axiosPostRequestTemplate } from '@apis/BaseAxios';

interface RegisterCategoryRequest {
  name: string;
}

const CategoryRegisterForm = () => {
  const [category, setCategory] = useState<string>('');

  const handleCategoryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setCategory(() => value);
  }

  const handleCategoryRegisterSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const url = '/categories';
    const data: RegisterCategoryRequest = {name: category};
    await axiosPostRequestTemplate<RegisterCategoryRequest>(
      url,
      data,
      (data: RegisterCategoryRequest, id: bigint) => {
        // 카테고리 리스트 추가
      })
  }

  return (
    <RegisterWrapper>
      <Tilte>카테고리 등록</Tilte>
      <StyledForm onSubmit={handleCategoryRegisterSubmit}>
        <FormInput placeholder={'카테고리 이름'} onChange={handleCategoryChange}/>
        <SubmitButton>등록</SubmitButton>
      </StyledForm>
    </RegisterWrapper>
  )
};
export default CategoryRegisterForm
