import React from "react";
import { Link } from 'react-router-dom';

function Hero() {
    return <section className="h-[800px] bg-hero
    bg-no-repeat bg-cover bg-center py-24">
      <div className="container mx-auto flex justify-around">
        <div className="flex flex-col">
          <h1 className="text-[70px] leading-[1.1]
          font-light mb-4">
            OUR DEALS <br />
            <span className="font-semibold">
              LAPTOPS
            </span>
          </h1>
          <Link to={''} className="self-start uppercase
          font-semibold border-b-2 border-primary ">
            Discover More
          </Link>
        </div>
        <div className="hidden lg:block w-[400px] mx-auto transition duration-300">
          <img src="https://www.apple.com/newsroom/images/2023/09/apple-debuts-iphone-15-and-iphone-15-plus/article/Apple-iPhone-15-lineup-hero-230912_inline.jpg.large.jpg" alt='hero'/>
        </div>
      </div>
    </section>
  }

export default Hero;