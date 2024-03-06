import styled from "styled-components";
import React from 'react';

interface ContentWrapperProps {
  children: React.ReactNode;
}

const ContentWrapper: React.FC<ContentWrapperProps> = ({children}) => {
  return <Wrapper>{children}</Wrapper>
}

const Wrapper = styled.div`
  width: 100%;
  background-color: tomato;
  margin-top: 30px;
`

export default ContentWrapper;
