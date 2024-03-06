import { axiosGetRequestTemplate } from './BaseAxios';

interface Category {
  id: number;
  name: string;
}

const getAllCategories = async (setCategoriesState, setSelectedCategory) => {
  const url = '/categories';
  await axiosGetRequestTemplate<Category[]>(url, (categories: Category[]) => {
    setCategoriesState(() => categories);
    setSelectedCategory(() => categories[0]);
  })
}

export { Category, getAllCategories };
