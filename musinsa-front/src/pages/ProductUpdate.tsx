import ContentWrapper from "../components/ContentWrapper";
import CategoryRegisterForm from '@components/CategoryRegisterForm';
import { useEffect, useState } from 'react';
import { Category, getAllCategories } from '@apis/RequestGetAllCategories';
import { Brand, getAllBrands } from '@apis/RequestGetAllBrands';
import BrandRegisterForm from '@components/BrandRegisterForm';

const ProductUpdate = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<Category>({id: 0, name: ''});

  const [brandsState, setBrandsState] = useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = useState<Brand>({id: 0, name: ''});

  useEffect(() => {
    (async () => {
      await getAllCategories(setCategories, setSelectedCategory);
      await getAllBrands(setBrandsState, setSelectedBrand);
    })()
  }, []);

  return <ContentWrapper>
    <CategoryRegisterForm setCategories={setCategories}></CategoryRegisterForm>
    <BrandRegisterForm setBrands={setBrandsState}></BrandRegisterForm>
  </ContentWrapper>
}

export default ProductUpdate;
