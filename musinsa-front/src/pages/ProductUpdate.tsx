import ContentWrapper from "../components/ContentWrapper";
import CategoryRegisterForm from '@components/CategoryRegisterForm';
import { useEffect, useState } from 'react';
import { Category, getAllCategories } from '@apis/get/RequestGetAllCategories';
import { Brand, getAllBrands } from '@apis/get/RequestGetAllBrands';
import BrandRegisterForm from '@components/BrandRegisterForm';
import ProductRegisterForm from '@components/ProductRegisterForm';

const ProductUpdate = () => {
  const [categoriesState, setCategoriesState] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<Category>({id: 0, name: ''});

  const [brandsState, setBrandsState] = useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = useState<Brand>({id: 0, name: ''});

  useEffect(() => {
    (async () => {
      await setCategories();
      await setBrands();
    })()
  }, []);

  const setCategories = async () => {
    const categoriesResponse = await getAllCategories();
    if (categoriesResponse.isSuccess) {
      const {response: categories} = categoriesResponse;
      setCategoriesState(() => categories);
      setSelectedCategory(() => categories[0])
    }
  }

  const setBrands = async () => {
    const brandsResponse = await getAllBrands();
    if (brandsResponse.isSuccess) {
      const {response: brands} = brandsResponse;
      setBrandsState(() => brands);
      setSelectedBrand(() => brands[0])
    }
  }

  return <ContentWrapper>
    <CategoryRegisterForm setCategories={setCategories}></CategoryRegisterForm>
    <BrandRegisterForm setBrands={setBrandsState}></BrandRegisterForm>
    <ProductRegisterForm
      brands={brandsState}
      categories={categoriesState}
      selectedBrand={selectedBrand}
      selectedCategory={selectedCategory}
      setSelectedBrand={setSelectedBrand}
      setSelectedCategory={setSelectedCategory}
    ></ProductRegisterForm>
  </ContentWrapper>
}

export default ProductUpdate;
