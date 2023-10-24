import React from "react";
import { Link } from 'react-router-dom';

function Hero() {
  return (
    <section className="h-[800px] bg-hero bg-no-repeat bg-cover bg-black bg-center py-24">
      <div className="container mx-auto flex justify-between items-center">
        <div className="flex flex-col text-white max-w-md">
          <h1 className="text-4xl font-semibold mb-4 text-white">
            Explore Our Latest Deals
          </h1>
          <p className="text-lg mb-6 text-white">
            Discover the best selection of <span className="text-white">electronics</span>.
          </p>
          <Link to={'/'} className="self-start inline-block py-3 px-6 bg-white text-primary uppercase font-semibold rounded-lg text-lg hover:bg-primary-dark transition duration-400">
            Discover More
          </Link>
        </div>
        <div className="hidden lg:block w-[400px] mx-auto transition duration-300">
          <img
            src="https://ee.co.uk/medias/iphone-15-pro-max-natural-titanium-desktop-detail-1-WebP-Format-488?context=bWFzdGVyfHJvb3R8Mjc4NTh8aW1hZ2Uvd2VicHxzeXMtbWFzdGVyL3Jvb3QvaDQ1L2gyYi8xMDA3MzgwNDcwMTcyNi9pcGhvbmUtMTUtcHJvLW1heC1uYXR1cmFsLXRpdGFuaXVtLWRlc2t0b3AtZGV0YWlsLTFfV2ViUC1Gb3JtYXQtNDg4fGFkN2Q0YWUwMDQ2NmIxYzg0ZGJjYTY2NTUwM2RjZWI4ZjUxYmJmOTM5NjI4OTEyNTVmY2ZiMDExYTE5NTFmYzg"
            alt="hero"
            className="w-full rounded-lg"
          />
        </div>
      </div>
    </section>
  );
}


export default Hero;