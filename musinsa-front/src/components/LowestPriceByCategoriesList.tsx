import { LowestPriceByCategoryResponse } from '../pages/PriceInformation';
import StyledUl from './StyledUl';
import StyledLi from './StyledLi';
import StyledTableCell from './StyledTableCell';

type LowestPriceByCategoriesListProps = {
  data: LowestPriceByCategoryResponse
}

const LowestPriceByCategoriesList = ({data}: LowestPriceByCategoriesListProps) => {
  const generateLowestPriceInformationTable = () => {
    return data.lowestPriceInformationResponses.map(({category, brand, price}, i) => {
      return (
        <StyledLi key={i}>
          <StyledTableCell>{category}</StyledTableCell>
          <StyledTableCell>{brand}</StyledTableCell>
          <StyledTableCell>{price}</StyledTableCell>
        </StyledLi>)
    });
  }

  return (
    <>
      <StyledUl>
        {generateLowestPriceInformationTable()}
        <StyledLi>
          <StyledTableCell>총합</StyledTableCell>
          <StyledTableCell>{data.sum}</StyledTableCell>
        </StyledLi>
      </StyledUl>
    </>
  )
};


export default LowestPriceByCategoriesList;
