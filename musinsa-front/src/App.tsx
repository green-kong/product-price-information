import Nav from './components/Nav'
import './App.css'
import { BrowserRouter, Route, Routes } from "react-router-dom";
import ProductList from './pages/ProudctList';
import ProductUpdate from './pages/ProductUpdate';
import PriceInformation from './pages/PriceInformation';

function App() {
  return (
    <BrowserRouter>
      <Nav/>
      <Routes>
        <Route path="/" element={<ProductList/>}></Route>
        <Route path="/products" element={<ProductUpdate/>}></Route>
        <Route path="/price-informations" element={<PriceInformation/>}></Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
