import { FormInput, RegisterWrapper, StyledForm, SubmitButton, Tilte } from '@components/RegisterElements';
import React, { useState } from 'react';
import { Category } from '@apis/RequestGetAllCategories';
import { RegisterCategoryRequest, requestPostCategory } from '@apis/RequestPostCategory';

interface CategoryRegisterFormProps {
  setCategories: React.Dispatch<React.SetStateAction<Category[]>>;
}

const CategoryRegisterForm = ({setCategories}: CategoryRegisterFormProps) => {
  const [category, setCategory] = useState<string>('');

  const handleCategoryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setCategory(() => value);
  }

  const handleCategoryRegisterSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const data: RegisterCategoryRequest = {name: category};
    const result = await requestPostCategory(data);
    if (result.isSuccess) {
      setCategories(categories => [...categories, {id: result.response, name: data.name}]);
      alert("카테고리를 등록하였습니다.");
    }
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
