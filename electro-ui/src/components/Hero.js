import React from 'react';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { Link as ScrollLink } from 'react-scroll';

function Hero() {
  const imageUrls = [
    'https://ee.co.uk/medias/iphone-15-pro-max-natural-titanium-desktop-detail-1-WebP-Format-488?context=bWFzdGVyfHJvb3R8Mjc4NTh8aW1hZ2Uvd2VicHxzeXMtbWFzdGVyL3Jvb3QvaDQ1L2gyYi8xMDA3MzgwNDcwMTcyNi9pcGhvbmUtMTUtcHJvLW1heC1uYXR1cmFsLXRpdGFuaXVtLWRlc2t0b3AtZGV0YWlsLTFfV2ViUC1Gb3JtYXQtNDg4fGFkN2Q0YWUwMDQ2NmIxYzg0ZGJjYTY2NTUwM2RjZWI4ZjUxYmJmOTM5NjI4OTEyNTVmY2ZiMDExYTE5NTFmYzg',
    'https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_78976636?x=1800&y=1800&format=jpg&quality=80&sp=yes&strip=yes&ex=1800&ey=1800&align=center&resizesource&unsharp=1.5x1+0.7+0.02',
    'https://assets.mmsrg.com/isr/166325/c1/-/ASSET_MMS_115840538?x=960&y=720&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=960&ey=720&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=960&cdy=720',
    'https://assets.mmsrg.com/isr/166325/c1/-/pixelboxx-mss-81347962?x=480&y=334&format=jpg&quality=80&sp=yes&strip=yes&trim&ex=480&ey=334&align=center&resizesource&unsharp=1.5x1+0.7+0.02&cox=0&coy=0&cdx=480&cdy=334'
  ];

  return (
    <section className="h-screen lg:h-[800px] bg-hero bg-no-repeat bg-cover bg-center py-20 lg:py-32">
      <div className="container mx-auto flex flex-col lg:flex-row justify-between items-center">
        <div className="flex flex-col lg:mr-12">
          <h1 className="text-4xl lg:text-5xl font-semibold mb-4 ">
            Explore a wide selection of electronics.
          </h1>
          <p className="text-base lg:text-lg mb-6">
            Find the latest electronics in one place.
          </p>
          <ScrollLink
            to="categories-section"
            smooth={true}
            duration={500}
            offset={-80}
            className="cursor-pointer self-start inline-block py-3 px-6 bg-red-500 text-white uppercase font-semibold rounded-lg text-lg hover:bg-red-600 transition duration-400"
          >
            Explore Now
          </ScrollLink>
        </div>
        <div className="max-w-[300px] lg:w-full lg:max-w-[400px]">
          <Carousel
            autoPlay
            infiniteLoop
            showThumbs={false}
            showIndicators={false}
            showArrows={false}
            showStatus={false}
          >
            {imageUrls.map((imageUrl, index) => (
              <div key={index} className="image-container">
                <img
                  src={imageUrl}
                  alt={`Slide ${index}`}
                  className="w-full h-[320px] mx-auto mt-4"
                />
              </div>
            ))}
          </Carousel>
        </div>
      </div>
    </section>
  );
}

export default Hero;
