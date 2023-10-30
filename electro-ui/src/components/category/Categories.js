import React, { useEffect, useCallback, useState } from "react";
import { PulseLoader } from 'react-spinners';

import axiosInstance from '../../axiosInterceptor';
import Category from './Category';

function Categories() {
  const [categories, setCategories] = useState(
    [
      {
        "id": 1,
        "name": "Laptops",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_91902577?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 2,
        "name": "Cameras",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_95596935?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 3,
        "name": "TV",
        "imageUrl":"https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_93658951?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 4,
        "name": "Phones",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_108450077?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 5,
        "name": "Monitors",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_92138501?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 6,
        "name": "Headphones",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_76407836?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 7,
        "name": "Speakers",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_99573973?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      },  {
        "id": 8,
        "name": "Printers",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_88146632?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      },  {
        "id": 9,
        "name": "Gaming",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_120911583?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }, {
        "id": 10,
        "name": "Drones",
        "imageUrl": "https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_100718901?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334"
      }
    ]
);
  const [loading, setLoading] = useState(true);

  const handleError = (error) => {
    console.error('Error fetching categories data:', error);
    setLoading(false);
  };

  const fetchCategories = useCallback(async () => {
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
            <PulseLoader size={15} color="#007bff" loading={loading} />
        </div>
    );
  }

  return (
    <section id="categories-section" className='py-16'>
      <div className='container mx-auto'>
        <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 gap-[40px] max-w-sm max-auto md:max-w-none mx:mx-0'>
          {categories.map(category => (
            <Category category={category} key={category.id}/>
          ))}
        </div>
      </div>
    </section>
  );
}

export default Categories;