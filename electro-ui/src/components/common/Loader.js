import { MoonLoader } from 'react-spinners';

const Loader = ({ loading }) => {
  return (
    <div className="loading-container flex items-center justify-center h-screen">
        <MoonLoader size={50} color="red" loading={loading} />
    </div>
  );
};

export default Loader;