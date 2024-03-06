import { SpecificBrandLowestPriceByCategoryResponse } from '../pages/PriceInformation';
import StyledLi from './StyledLi';
import StyledTableCell from './StyledTableCell';
import StyledUl from './StyledUl';

interface SpecificBrandLowestPriceByCategoryProps {
  data: SpecificBrandLowestPriceByCategoryResponse
}

const SpecificBrandLowestPriceByCategoryList = ({data}: SpecificBrandLowestPriceByCategoryProps) => {
  const generateLowestPriceInformationTable = () => {
    return data.lowestPriceInformationResponses.map(({category, price}, i) => {
      return (
        <StyledLi key={i}>
          <StyledTableCell>{category}</StyledTableCell>
          <StyledTableCell>{price}</StyledTableCell>
        </StyledLi>)
    });
  }

  return (
    <>
      <StyledUl>
        <StyledLi>
          <StyledTableCell>BRAND : {data.brand}</StyledTableCell>
        </StyledLi>
        {generateLowestPriceInformationTable()}
        <StyledLi>
          <StyledTableCell>총합</StyledTableCell>
          <StyledTableCell>{data.sum}</StyledTableCell>
        </StyledLi>
      </StyledUl>
    </>
  )
}

export default SpecificBrandLowestPriceByCategoryList;
