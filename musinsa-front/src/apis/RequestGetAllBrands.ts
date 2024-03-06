import { axiosGetRequestTemplate } from './BaseAxios';

interface Brand {
  id: bigint;
  name: string;
}


const getAllBrands = async (setBrandsState, setSelectedBrand) => {
  const url = '/brands';
  await axiosGetRequestTemplate<Brand[]>(url, (brands: Brand[]) => {
    setBrandsState(() => brands);
    setSelectedBrand(() => brands[0])
  })
}

export { Brand, getAllBrands };
