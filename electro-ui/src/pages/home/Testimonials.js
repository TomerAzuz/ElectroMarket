const Testimonials = () => {
  return (
    <section className="container mx-auto px-4 py-12 mb-10">
    <h2 className="text-3xl font-extrabold mb-8 text-center">What Our Customers Say</h2>
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <div className="bg-white p-6 rounded-lg shadow-md">
        <p className="text-gray-800 mb-4">"I couldn't be happier with the products I purchased. They exceeded my expectations, and the customer service was top-notch!"</p>
        <p className="text-gray-600">- Amanda S.</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <p className="text-gray-800 mb-4">"The variety of products is amazing, and the quality is outstanding. I've recommended this store to all my friends!"</p>
        <p className="text-gray-600">- John D.</p>
      </div>
      <div className="bg-white p-6 rounded-lg shadow-md">
        <p className="text-gray-800 mb-4">"Shopping here was a breeze. The website is user-friendly, and the delivery was faster than I expected. Will definitely shop again!"</p>
        <p className="text-gray-600">- Emily H.</p>
      </div>
    </div>
  </section>
  );
};

export default Testimonials;