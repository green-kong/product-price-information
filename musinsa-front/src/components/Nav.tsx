import styled from 'styled-components';
import {Link} from "react-router-dom";

const Nav: React.FC = () => {
  return (
    <NavWrapper>
      <NavList>
        <Link to="/">
          <NavItem>전체 상품</NavItem>
        </Link>
        <Link to="/products">
          <NavItem>데이터 수정</NavItem>
        </Link>
        <Link to="/price-informations">
          <NavItem>가격 정보 조회</NavItem>
        </Link>
      </NavList>
    </NavWrapper>
  );
};

const NavWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 1px solid black;
`;

const NavList = styled.ul`
  width: 700px;
  display: flex;
  justify-content: space-between;
`;

const NavItem = styled.li`
  margin: 0 1px;
  background-color: transparent;
  padding: 12px;
  text-align: center;
  color: black;
  cursor: pointer;

  &:hover {
    background-color: #565656;
    color: white;
  }
`;

export default Nav;
