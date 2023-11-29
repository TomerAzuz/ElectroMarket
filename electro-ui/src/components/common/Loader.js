import { MoonLoader } from 'react-spinners';

const Loader = ({ loading }) => {
  return (
    <div className="loading-container flex items-center justify-center h-screen">
        <MoonLoader size={30} color="black" loading={loading} />
    </div>
  );
};

export default Loader;