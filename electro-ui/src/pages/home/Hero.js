// Hero.jsx

import React from 'react';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { Link as ScrollLink } from 'react-scroll';

function Hero() {
  const imageUrls = [
    'https://electromarket-assets.s3.eu-central-1.amazonaws.com/images/ASSET_MMS_78976636.jpg',
    'https://electromarket-assets.s3.eu-central-1.amazonaws.com/images/ASSET_MMS_115840538.jpg',
    'https://electromarket-assets.s3.eu-central-1.amazonaws.com/images/pixelboxx-mss-81347962.jpg'
  ];

  return (
    <section className="py-64 lg:py-32 h-[600px] lg:h-[800px] bg-hero bg-no-repeat bg-cover bg-center">
        <div className="container mx-auto flex flex-col lg:flex-row justify-between items-center">
          <div className="flex flex-col lg:mr-12">
            <h1 className="text-4xl lg:text-5xl font-extrabold mb-4 leading-tight text-gray-800">
              Discover a World of Electronics
            </h1>
            <p className="text-lg mb-6 text-gray-600">
              Explore the latest and greatest electronics, all in one place.
            </p>
            <ScrollLink
              to="categories-section"
              smooth={true}
              duration={400}
              offset={-80}
              className="cursor-pointer inline-block text-center 
                         py-4 px-8 text-xl bg-red-500 text-white uppercase
                         font-semibold transition duration-300 transform
                          hover:bg-white hover:text-red-500 hover:scale-110 
                          focus:outline-none focus:ring focus:border-red-300"
            >
              Shop Now
            </ScrollLink>
          </div>
          <div className="max-w-[400px] lg:w-full lg:max-w-[400px]">
            <div className="hidden lg:block">
              <Carousel
                autoPlay
                infiniteLoop
                showThumbs={false}
                showIndicators={false}
                showArrows={false}
                showStatus={false}
              >
                {imageUrls.map((imageUrl) => (
                  <div key={imageUrl} className="image-container">
                  <img
                    src={imageUrl}
                    alt={imageUrl}
                    className="w-full h-auto lg:h-[340px] object-contain mx-auto mt-4"
                    loading="lazy"
                  />
                </div>
                ))}
              </Carousel>
            </div>
          </div>
        </div>
      </section>
  );
}

export default Hero;
