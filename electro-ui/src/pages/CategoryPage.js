import { useParams } from 'react-router-dom';
import ProductList from '../components/product/ProductList';

const CategoryPage = () => {    
    const { categoryId } = useParams();
    const categoryIdInt = parseInt(categoryId, 10);

    return (
        <div className="flex-col items-center h-screen">
            <h1 className="text-4xl text-center mt-36 mb-0">CATEGORY PAGE</h1>
            <ProductList categoryId={categoryIdInt} />
        </div>
    );
} 

export default CategoryPage;
