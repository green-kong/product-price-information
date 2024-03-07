import ContentWrapper from '../components/ContentWrapper';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { getAllProducts, Product } from '@apis/get/RequestGetAllProducts';
import { requestDeleteProductById } from '@apis/delete/RequestDeleteProductById';
import PriceUpdateForm from '@components/PriceUpdateForm';


const ProductList = () => {

  const [products, setProducts] = useState<Product[]>([]);

  useEffect(() => {
    (async () => {
      await setProductsState();
    })()
  }, [])

  const setProductsState = async () => {
    const result = await getAllProducts();
    if (result.isSuccess) {
      setProducts(result.response);
    }
  }

  const handleDeleteButton = async (event: React.MouseEvent<HTMLButtonElement>) => {
    const id = Number(event.currentTarget.value);
    const result = await requestDeleteProductById(id);
    if (result.isSuccess) {
      await setProductsState();
    }
  }

  const row = products.map(({id, brand, category, price}: Product, index) => {
    return (
      <TableRow key={id} $bgc={index % 2 == 0 ? '#e1e1e1' : '#f1f1f1'}>
        <TableCell>{brand}</TableCell>
        <TableCell>{category}</TableCell>
        <TableCell>
          <PriceUpdateForm id={id} price={price} setProductsState={setProductsState}></PriceUpdateForm>
        </TableCell>
        <TableCell>
          <button value={id} onClick={handleDeleteButton}>삭제</button>
        </TableCell>
      </TableRow>
    )
  });

  return (
    <ContentWrapper>
      <TableOut>
        <div>전체 상품 수 {products.length}</div>
        <TableRow $bgc={'#f1f1f1'}>
          <TableCell>브랜드</TableCell>
          <TableCell>카테고리</TableCell>
          <TableCell>
            가격
          </TableCell>
          <TableCell>
            수정
          </TableCell>
        </TableRow>
        {row}
      </TableOut>
    </ContentWrapper>)
}

const TableOut = styled.ul`
  width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
`

const TableRow = styled.li<{ $bgc?: string }>`
  width: 100%;
  display: flex;
  background-color: ${($bgc) => $bgc};
  border-bottom: 1px solid #b9b9b9;
  height: 30px;
`

const TableCell = styled.div`
  width: 25%;
  padding: 10px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
`

export default ProductList;
