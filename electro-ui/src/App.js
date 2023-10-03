import './App.css';
import useSWR from 'swr';

const fetcher = (url) => fetch(url).then((res) => res.json());

function App() {
  const apiUrl = 'http://localhost:9001/api/products/1';

  const { data, error } = useSWR(apiUrl, fetcher);

  if (error)  {
    console.error("Error loading data:", error);
    return <div>Error loading data</div>;
  }
  if (!data)  {
    return <div>Loading...</div>
  }
  return (
    <div>
      <h1>Product Details</h1>
      <p>ID: {data.id}</p>
      <p>Name: {data.name}</p>
      <p>Description: {data.description}</p>
      <p>Price: ${data.price}</p>
      <p>Category ID: {data.categoryId}</p>
      <p>Stock: {data.stock}</p>
      <img src={data.imageUrl} alt="Product" />
      <p>Created Date: {data.createdDate}</p>
      <p>Last Modified Date: {data.lastModifiedDate}</p>
      <p>Version: {data.version}</p>
    </div>
  );
}

export default App;
