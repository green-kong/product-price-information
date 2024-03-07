import React, { useState } from 'react';
import styled from 'styled-components';
import { updatePrice } from '@apis/patch/RequestPatchPriceUpdate';

interface PriceUpdateFormProps {
  id: number;
  price: number;
  setProductsState: () => Promise<void>;
}

const PriceUpdateForm = ({id, price, setProductsState}: PriceUpdateFormProps) => {

  const [priceState, setPriceState] = useState<number>(price);

  const handlePriceUpdateButton = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const result = await updatePrice(id, priceState);
    if (result.isSuccess) {
      await setProductsState();
      alert('가격이 수정되었습니다.');
      return;
    }
  };

  const handlePriceInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setPriceState(() => {
      const value = Number(event.target.value);
      if (isNaN(value)) {
        return price;
      }
      return value
    });
  }

  return (
    <PriceUpdateFormElement value={id} onSubmit={handlePriceUpdateButton}>
      <PriceInput value={priceState} onChange={handlePriceInputChange}/>
      <button value={id}>수정</button>
    </PriceUpdateFormElement>
  )
}

const PriceUpdateFormElement = styled.form`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 70%;
`

const PriceInput = styled.input`
  width: 50px;
`

export default PriceUpdateForm
