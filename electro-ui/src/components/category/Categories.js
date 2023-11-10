import React, { useEffect, useCallback, useState } from 'react';
import { PulseLoader } from 'react-spinners';
import axiosInstance from '../../axiosInterceptor';
import Category from './Category';

function Categories() {
  const [categories, setCategories] = useState([
    {
      "name": "Laptops",
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_91902577?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
    }, {
      "name": "Cameras",
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_95596935?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
    }, {
      "name": "TV",
      "imageUrl":"https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_93658951?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
    }, {
      "name": "Phones",
      "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_108450077?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
    }
  ]);
  const [loading, setLoading] = useState(true);

  const handleError = (error) => {
    console.error('Error fetching categories data', error);
    setLoading(false);
  };

  const fetchCategories = useCallback(async() => {
    try {
      const response = await axiosInstance.get('/category');
      setCategories(response.data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCategories();
  }, [fetchCategories]);

  if (loading) {
    return (
      <div className="loading-container">
        <PulseLoader size={15} color="black" loading={loading} />
      </div>
    );
  }

  return (
    <section id="categories-section">
      <div className="container mx-auto">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8 max-w-screen-xl mx-auto">
          {categories.map((category) => (
            <Category key={category.id} category={category} />
          ))}
        </div>
      </div>
    </section>
  );
}

export default Categories;
