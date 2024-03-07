import { FormInput, RegisterWrapper, StyledForm, SubmitButton, Tilte } from '@components/RegisterElements';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { Category } from '@apis/RequestGetAllCategories';
import { Brand } from '@apis/RequestGetAllBrands';
import { RegisterProductRequest, requestPostProduct } from '@apis/RequestPostProduct';

interface ProductRegisterFormProps {
  categories: Category[];
  brands: Brand[];
  selectedBrand: Brand;
  selectedCategory: Category;
  setSelectedBrand: React.Dispatch<React.SetStateAction<Brand>>
  setSelectedCategory: React.Dispatch<React.SetStateAction<Category>>
}


const ProductRegisterForm = (props: ProductRegisterFormProps) => {
  const {categories, brands, setSelectedBrand, setSelectedCategory, selectedCategory, selectedBrand} = props;

  const [registerProductRequest, setRegisterProductRequest] = useState<RegisterProductRequest>({
    price: 0,
    brandId: selectedBrand.id,
    categoryId: selectedCategory.id,
  });

  useEffect(() => {
    setRegisterProductRequest({
      price: 0,
      brandId: selectedBrand.id,
      categoryId: selectedCategory.id,
    })
  }, [selectedBrand, selectedCategory])

  const handleBrandOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: number = Number(target.value);
    const name: string = target.selectedOptions[0].text;
    setSelectedBrand(() => ({id, name}))
    setRegisterProductRequest((current) => ({...current, brandId: id}));
  };

  const handleCategoryOptionChange = ({target}: React.ChangeEvent<HTMLSelectElement>) => {
    const id: number = Number(target.value);
    const name: string = target.selectedOptions[0].text;
    setSelectedCategory(() => ({id, name}))
    setRegisterProductRequest((current) => ({...current, categoryId: id}));
  };

  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const price = Number(e.target.value);
    setRegisterProductRequest((current) => ({...current, price}));
  }

  const handleProductRegisterSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const result = await requestPostProduct(registerProductRequest);
    if (result.isSuccess) {
      alert("상품을 등록하였습니다.");
    }
  }

  return (
    <RegisterWrapper>
      <Tilte>상품 등록</Tilte>
      <StyledForm onSubmit={handleProductRegisterSubmit}>
        <ValuesWrapper>
          <StyledSelect onChange={handleBrandOptionChange} value={selectedBrand.id.toString()}>
            {brands.map(({id, name}: Brand) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </StyledSelect>
          <StyledSelect onChange={handleCategoryOptionChange} value={selectedCategory.id.toString()}>
            {categories.map(({id, name}: Category) => {
              return <option value={id.toString()} key={id}>{name}</option>
            })}
          </StyledSelect>
          <FormInput placeholder={'가격'} onChange={handlePriceChange}/>
        </ValuesWrapper>
        <SubmitButton>등록</SubmitButton>
      </StyledForm>
    </RegisterWrapper>
  );
}

const ValuesWrapper = styled.div`
  width: 500px;
  display: flex;
  justify-content: space-between;
`

const StyledSelect = styled.select`
  width: 145px;
  height: 20px;
`

export default ProductRegisterForm;
