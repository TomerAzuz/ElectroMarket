import React, { useContext, useEffect, useState } from 'react';
import { toast } from 'react-hot-toast';

import Hero from './Hero';
import Testimonials from './Testimonials';
import Categories from '../../components/category/Categories';
import FeaturedProducts from '../../components/product/FeaturedProducts';
import { AuthContext } from '../../contexts/AuthContext';
import { usePage } from '../../contexts/PageContext';

const Home = () => {
  const { user } = useContext(AuthContext);
  const [showMessage, setShowMessage] = useState(true);
  const { page, setPage } = usePage();
 
  useEffect(() => {
    if (showMessage && user) {
      toast.success(`Welcome back, ${user.username}!`);
      setShowMessage(false);
    }
    if (page > 0) {
      setPage(0);
    }
  }, [user, setPage, page, showMessage]);

  return (
    <div className='font-sans'>
      <Hero />
      <FeaturedProducts />
      <div className="container mx-auto px-4 py-8">
        <Categories />
      </div>
      <Testimonials />
    </div>
  );
};

export default Home;
