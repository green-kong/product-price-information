import StyledUl from './StyledUl';
import StyledLi from './StyledLi';
import StyledTableCell from './StyledTableCell';
import { HighestAndLowestPriceByCategory } from '../pages/PriceInformation';

type HighestAndLowestPriceByCategoryListProps = {
  data: HighestAndLowestPriceByCategory
}

const HighestAndLowestPriceByCategoryList = ({data}: HighestAndLowestPriceByCategoryListProps) => {
  const {category, highest, lowest} = data;

  return (
      <StyledUl>
        <StyledLi>
          <StyledTableCell>{category}</StyledTableCell>
        </StyledLi>
        <StyledLi>
          <StyledTableCell>최대 가격</StyledTableCell>
          <StyledTableCell>{highest.brand}</StyledTableCell>
          <StyledTableCell>{highest.price}원</StyledTableCell>
        </StyledLi>
        <StyledLi>
          <StyledTableCell>최소 가격</StyledTableCell>
          <StyledTableCell>{lowest.brand}</StyledTableCell>
          <StyledTableCell>{lowest.price}원</StyledTableCell>
        </StyledLi>
      </StyledUl>
  )
};


export default HighestAndLowestPriceByCategoryList;
