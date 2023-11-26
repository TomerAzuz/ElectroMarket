import Category from './Category';
import useCategories from '../../customHooks/useCategories';
import Loader from '../common/Loader';

function Categories() {
  const { categories, loading } = useCategories();

  return loading ? (
    <Loader loading={loading}/>
    ) : 
    (<section id="categories-section">
      <div className="container mx-auto">
        <div className="grid grid-cols-1 
                      md:grid-cols-2 
                      lg:grid-cols-3 
                      xl:grid-cols-4 
                      gap-4 
                      max-w-screen-xl 
                      mx-auto"
        >
          {categories?.map((category) => (
            <Category key={category.id} category={category} />
          ))}
        </div>
      </div>
    </section>
  );
}

export default Categories;
