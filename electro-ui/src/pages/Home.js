import React from 'react';
import Hero from '../components/Hero';
import Categories from '../components/category/Categories';

const Home = () => {
  return (
    <div>
      <Hero />
      <div className="container mx-auto px-4 py-8">
        <Categories />
      </div>
    </div>
  );
};

export default Home;