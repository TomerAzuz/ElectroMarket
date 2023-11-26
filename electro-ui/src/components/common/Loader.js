import { MoonLoader } from 'react-spinners';

const Loader = ({ loading }) => {
  return (
    <div className="loading-container items-center justify-center">
        <MoonLoader size={25} color="black" loading={loading} />
    </div>
  );
};

export default Loader;